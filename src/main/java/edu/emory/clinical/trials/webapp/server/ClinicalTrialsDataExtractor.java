package edu.emory.clinical.trials.webapp.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.SSLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.ws.http.HTTPException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.clinical.trials.webapp.server.entity.ClinicalTrialInterventionStaging;
import edu.emory.clinical.trials.webapp.server.entity.ClinicalTrialKeywordStaging;
import edu.emory.clinical.trials.webapp.server.entity.ClinicalTrialKeywordStagingPrimaryKey;
import edu.emory.clinical.trials.webapp.server.entity.ClinicalTrialLocationStaging;
import edu.emory.clinical.trials.webapp.server.entity.ClinicalTrialMeshTermStaging;
import edu.emory.clinical.trials.webapp.server.entity.ClinicalTrialMeshTermStagingPrimaryKey;
import edu.emory.clinical.trials.webapp.server.entity.ClinicalTrialStaging;
import edu.emory.clinical.trials.webapp.server.entity.JobLock;
import edu.emory.clinical.trials.webapp.server.entity.UniqueObject;
import edu.emory.clinical.trials.webapp.server.rest.RestServerConfigurator;
import edu.emory.clinical.trials.webapp.server.rest.TestConfigurator;
import edu.emory.clinical.trials.webapp.server.xmlobject.ClinicalTrialXml;
import edu.emory.clinical.trials.webapp.server.xmlobject.Intervention;
import edu.emory.clinical.trials.webapp.server.xmlobject.Location;

public class ClinicalTrialsDataExtractor {

	static int locationPrimaryKey = 1;

	static int interventionPrimaryKey;

	private EmailManager emailManager;

	private final EntityManagerFactory emf;

	EntityManager em;

	boolean isLocked = false;

	private String testNctId = "";
	
	private Logger logger = LoggerFactory.getLogger(ClinicalTrialsDataExtractor.class);

	public ClinicalTrialsDataExtractor() {
		if (RestServerConfigurator.getInstance() != null && !Util.isTestMode) {
			emf = RestServerConfigurator.getInstance().getEmf();
		}
		// Using Test
		else {
			emf = TestConfigurator.getInstance().getEmf();
		}
	}
	
	public boolean extract() throws Exception {
		//boolean error = false;

		long start = 0L;
		long elapsed = 0L;
		long now = 0L;
		long items = 0L;
		List<String> nctIds = new LinkedList<String>();

		try {
			nctIds = getNctIds();
			Collections.sort(nctIds);
			if (isDbUnlocked()) {
				start = System.currentTimeMillis();
				if (nctIds != null && nctIds.size() > 0) {
					for (String nctId : nctIds) {
						String urlString = "https://clinicaltrials.gov/show/" + nctId + "?displayxml=true";
						now = System.currentTimeMillis();
						if (!processNctId(nctId, urlString)) {
							logger.error("Error processing " + nctId + " > skipping");
							continue;
						}
						++items;
						long stop = System.currentTimeMillis();
						elapsed += (stop - now);
					}
				}
			} else {
				emailManager.sendExtractFailureEmail(new Exception("ClinicalTrials update failed"));
				return false;
			}

			LogUtil.logJobResult("ClinicalTrialsDataExtractJob", true);

		} finally {
			unlockDb();
		}

		//need to sort out external.properties for smpt server info
		//emailManager.sendExtractSuccessEmail(nctIds.size());

		return true;

	}

	private boolean processNctId(String nctId, String urlString) throws Exception {
		boolean passed = true;
		JAXBContext context = JAXBContext.newInstance(ClinicalTrialXml.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		EmailManager emailManager = null; // new EmailManager();
		EntityManager em = emf.createEntityManager();
		URL url = new URL(urlString);
		URLConnection connection = url.openConnection();
		logger.debug("Processing " + nctId);
		try {
			ClinicalTrialXml clinicalTrialXml = (ClinicalTrialXml) unmarshaller.unmarshal(connection.getInputStream());
			// Get Data from XML and convert it to (Persistable)
			// Java
			// Object
			ClinicalTrialStaging trial = convertXmlToStudyObject(clinicalTrialXml);
			trial.setNctId(nctId);

			// Get Keywords
			List<ClinicalTrialKeywordStaging> keywords = fetchKeywords(nctId, clinicalTrialXml);

			// Get Mesh Terms
			List<ClinicalTrialMeshTermStaging> meshTerms = fetchMeshTerms(nctId, clinicalTrialXml);

			// Get Interventions
			List<ClinicalTrialInterventionStaging> interventions = fetchInterventions(nctId, clinicalTrialXml);

			/*
			 * Get Location Data -- Currently deprecating / removing this data
			 * pull as the location data volume is extremely large and there
			 * isn't much value in caputuring it at this time.
			 */

			// List<ClinicalTrialLocation> locations =
			// fetchLocations(nctId, clinicalTrialXml);
			List<ClinicalTrialLocationStaging> locations = new ArrayList<ClinicalTrialLocationStaging>();

			logger.debug("Persisting: " + nctId);

			persistObjectsToDB(trial, keywords, meshTerms, locations, interventions, em);

		} catch (SSLException e) {
			logger.error("ERROR while trying to extract " + nctId + ": ", e);
			// emailManager.sendExtractFailureEmail(e);
			passed = false;
		} catch (UnknownHostException e) {
			logger.error("ERROR while trying to extract " + nctId + ": ", e);
			// emailManager.sendExtractFailureEmail(e);
			passed = false;
		} catch (IOException e) {
			logger.error("ERROR while trying to extract " + nctId + ": ", e);
			logger.error(nctId + " does not exist in ClinicalTrials.gov");
			ClinicalTrialStaging trial = new ClinicalTrialStaging();
			trial.setNctId(nctId);
			trial.setBriefTitle("404: Does not exist in ClinicalTrials.gov");
			passed = false;
		} catch (HTTPException hex) {
			logger.error("HTTP Error while accessing NCT: " + nctId);
			passed = false;
		} catch (Exception e) {
			logger.error("ERROR while trying to extract " + nctId + ": ", e);
			// emailManager.sendExtractFailureEmail(e);
			passed = false;
		}
		return passed;
	}

	public void persistObjectsToDB(ClinicalTrialStaging trial, List<ClinicalTrialKeywordStaging> keywords,
			List<ClinicalTrialMeshTermStaging> meshTerms, List<ClinicalTrialLocationStaging> locations,
			List<ClinicalTrialInterventionStaging> interventions, EntityManager em) throws Exception {

		persistObject(trial, em);

		try {

			for (ClinicalTrialKeywordStaging keyword : keywords) {
				persistObject(keyword, em);
			}

			for (ClinicalTrialMeshTermStaging meshTerm : meshTerms) {
				persistObject(meshTerm, em);
			}

			for (ClinicalTrialLocationStaging location : locations) {
				persistObject(location, em);
			}

			for (ClinicalTrialInterventionStaging intervention : interventions) {
				persistObject(intervention, em);
			}

		} finally {
			em.close();
		}

	}

	@SuppressWarnings("unchecked")
	public List<String> getNctIds() {
		EntityManager em = emf.createEntityManager();
		List<String> nctIds = null;
		try {
			Query query = em.createQuery("select distinct trim(e.primaryKey.nctId) from ErmsStudy e");
			nctIds = query.getResultList();
			logger.info("# of NCTIDS pulled from ErmsStudy: " + nctIds.size());
		} finally {
			em.close();
		}

		return nctIds;
	}

	private void persistObject(UniqueObject object, EntityManager em) throws Exception {
		try {
			if (!em.getTransaction().isActive()) {
				em.getTransaction().begin();
				Delegator.persistToDatabase(em, object);
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			unlockDb();
			throw e;
		}
	}

	public List<ClinicalTrialKeywordStaging> fetchKeywords(String nctId, ClinicalTrialXml clinicalTrialXml) {
		List<String> keywords = clinicalTrialXml.getKeywords();
		List<ClinicalTrialKeywordStaging> ctKeywords = new ArrayList<ClinicalTrialKeywordStaging>();

		for (String keyword : keywords) {
			ClinicalTrialKeywordStaging ctKeyword = new ClinicalTrialKeywordStaging();
			ClinicalTrialKeywordStagingPrimaryKey kPk = new ClinicalTrialKeywordStagingPrimaryKey();
			kPk.setKeyword(keyword);
			kPk.setNctId(nctId);
			ctKeyword.setPrimaryKey(kPk);
			ctKeyword.setTransferDate(new Date());
			ctKeywords.add(ctKeyword);
		}

		return ctKeywords;
	}

	public List<ClinicalTrialMeshTermStaging> fetchMeshTerms(String nctId, ClinicalTrialXml clinicalTrialXml) {
		List<String> conditionMeshTerms = clinicalTrialXml.getConditionBrowse().getMeshTerms();
		List<String> interventionMeshTerms = clinicalTrialXml.getInterventionBrowse().getMeshTerms();

		List<ClinicalTrialMeshTermStaging> meshTerms = new ArrayList<ClinicalTrialMeshTermStaging>();

		for (String conditionMeshTerm : conditionMeshTerms) {
			ClinicalTrialMeshTermStaging ctMeshTerm = processMeshTerm(conditionMeshTerm, nctId);
			ctMeshTerm.setMeshTermType("C");
			ctMeshTerm.setTransferDate(new Date());
			meshTerms.add(ctMeshTerm);
		}

		for (String interventionMeshTerm : interventionMeshTerms) {
			ClinicalTrialMeshTermStaging ctMeshTerm = processMeshTerm(interventionMeshTerm, nctId);
			ctMeshTerm.setMeshTermType("I");
			ctMeshTerm.setTransferDate(new Date());
			meshTerms.add(ctMeshTerm);
		}

		return meshTerms;
	}

	public List<ClinicalTrialInterventionStaging> fetchInterventions(String nctId, ClinicalTrialXml clinicalTrialXml) {
		List<Intervention> interventions = clinicalTrialXml.getInterventions();
		List<ClinicalTrialInterventionStaging> ctInterventions = new ArrayList<ClinicalTrialInterventionStaging>();

		for (Intervention intervention : interventions) {
			ClinicalTrialInterventionStaging ctIntervention = new ClinicalTrialInterventionStaging();
			ctIntervention.setInterventionId(interventionPrimaryKey++);
			ctIntervention.setInterventionName(intervention.getInterventionName());
			ctIntervention.setNctId(nctId);
			ctIntervention.setInterventionType(intervention.getInterventionType());
			ctIntervention.setOtherName(intervention.getOtherName());
			ctIntervention.setDescription(intervention.getDescription());
			ctIntervention.setTransferDate(new Date());
			ctInterventions.add(ctIntervention);
		}

		return ctInterventions;
	}

	public ClinicalTrialMeshTermStaging processMeshTerm(String meshTerm, String nctId) {
		ClinicalTrialMeshTermStaging ctMeshTerm = new ClinicalTrialMeshTermStaging();
		ClinicalTrialMeshTermStagingPrimaryKey mtPk = new ClinicalTrialMeshTermStagingPrimaryKey();
		mtPk.setMeshTerm(meshTerm);
		mtPk.setNctId(nctId);
		ctMeshTerm.setPrimaryKey(mtPk);
		return ctMeshTerm;
	}

	public ClinicalTrialStaging convertXmlToStudyObject(ClinicalTrialXml clinicalTrialXml) {

		ClinicalTrialStaging trial = new ClinicalTrialStaging();
		trial.setOrgStudyId(clinicalTrialXml.getIdInfo().getOrgStudyId());
		trial.setSecondaryId(clinicalTrialXml.getIdInfo().getSecondaryId());
		trial.setNctAlias(clinicalTrialXml.getIdInfo().getNctAlias());
		trial.setBriefTitle(clinicalTrialXml.getBriefTitle());
		trial.setOfficialTitle(clinicalTrialXml.getOfficialTitle());
		trial.setLeadSponsor(clinicalTrialXml.getSponsors().getLeadSponsor().getAgency());
		trial.setCollaborator(clinicalTrialXml.getSponsors().getCollaborator().getAgency());
		trial.setSource(clinicalTrialXml.getSource());
		
		if (clinicalTrialXml.getOversightInfo() != null) {
		    trial.setAuthority(clinicalTrialXml.getOversightInfo().getAuthority());
	        trial.setHasDmc(clinicalTrialXml.getOversightInfo().getHasDmc());
		}

		if (clinicalTrialXml.getBriefSummary().getTextBlock().length() > 3000) {
			trial.setBriefSummary(clinicalTrialXml.getBriefSummary().getTextBlock().substring(0, 3000) + "...");
		} else {
			trial.setBriefSummary(clinicalTrialXml.getBriefSummary().getTextBlock());
		}
		
		trial.setOverallStatus(clinicalTrialXml.getOverallStatus());
		trial.setStartDate(clinicalTrialXml.getStartDate());
		trial.setEndDate(clinicalTrialXml.getEndDate());
		trial.setWhyStopped(clinicalTrialXml.getWhyStopped());
		trial.setPrimaryCompletionDate(clinicalTrialXml.getPrimaryCompletionDate());
		trial.setPhase(clinicalTrialXml.getPhase());
		trial.setStudyType(clinicalTrialXml.getStudyType());
		trial.setStudyDesign(clinicalTrialXml.getStudyDesign());
		trial.setPrimaryOutcomeMeasure(clinicalTrialXml.getPrimaryOutcome().getMeasure());
		trial.setPrimaryOutcomeTimeFrame(clinicalTrialXml.getPrimaryOutcome().getTimeFrame());
		trial.setPrimaryOutcomeSafetyIssue(clinicalTrialXml.getPrimaryOutcome().getSafetyIssue());
		trial.setPrimaryOutcomeDescription(clinicalTrialXml.getPrimaryOutcome().getDescription());
		trial.setSecondaryOutcomeMeasure(clinicalTrialXml.getSecondaryOutcome().getMeasure());
		trial.setSecondaryOutcomeTimeFrame(clinicalTrialXml.getSecondaryOutcome().getTimeFrame());
		trial.setSecondaryOutcomeSafetyIssue(clinicalTrialXml.getSecondaryOutcome().getSafetyIssue());
		trial.setSecondaryOutcomeDescription(clinicalTrialXml.getSecondaryOutcome().getDescription());
		trial.setNumberOfArms(clinicalTrialXml.getNumberOfArms());
		trial.setEnrollmentType(clinicalTrialXml.getEnrollmentType());
		trial.setCondition(clinicalTrialXml.getCondition());
		trial.setArmGroupLabel(clinicalTrialXml.getArmGroup().getArmGroupLabel());
		trial.setArmGroupType(clinicalTrialXml.getArmGroup().getArmGroupType());
		trial.setArmGroupDescription(clinicalTrialXml.getArmGroup().getDescription());
		
		if (clinicalTrialXml.getEligibility().getCriteria().getTextBlock().length() > 3000) {
			trial.setEligibilityCriteria(
					clinicalTrialXml.getEligibility().getCriteria().getTextBlock().substring(0, 3000) + "...");
		} else {
			trial.setEligibilityCriteria(clinicalTrialXml.getEligibility().getCriteria().getTextBlock());
		}
		
		trial.setGender(clinicalTrialXml.getEligibility().getGender());
		trial.setMinimumAge(clinicalTrialXml.getEligibility().getMinimumAge());
		trial.setMaximumAge(clinicalTrialXml.getEligibility().getMaximumAge());
		trial.setHealthyVolunteers(clinicalTrialXml.getEligibility().getHealthyVolunteers());
		trial.setVerificationDate(clinicalTrialXml.getVerificationDate());
		trial.setLastChangedDate(clinicalTrialXml.getLastChangedDate());
		trial.setFirstReceivedDate(clinicalTrialXml.getFirstReceivedDate());
		trial.setResponsiblePartyType(clinicalTrialXml.getResponsibleParty().getResponsiblePartyType());
		// If the Responsible Party is a PI and is affiliated with Emory, we
		// will persist the information.
		if (clinicalTrialXml.getResponsibleParty().getResponsiblePartyType().equals("Principal Investigator")
				&& clinicalTrialXml.getResponsibleParty().getInvestigatorAffiliation().contains("Emory")) {
			trial.setInvestigatorAffiliation(clinicalTrialXml.getResponsibleParty().getInvestigatorAffiliation());
			trial.setInvestigatorFullName(clinicalTrialXml.getResponsibleParty().getInvestigatorFullName());
			trial.setInvestigatorTitle(clinicalTrialXml.getResponsibleParty().getInvestigatorTitle());
		}
		// Use "Overall Official" (Investigator) fields if the Responsible Party
		// information isn't there.
		else if (clinicalTrialXml.getOverallOfficial().getRole().equals("Principal Investigator")
				&& clinicalTrialXml.getOverallOfficial().getAffiliation().contains("Emory")) {
			trial.setInvestigatorAffiliation(clinicalTrialXml.getOverallOfficial().getAffiliation());
			trial.setInvestigatorFullName(clinicalTrialXml.getOverallOfficial().getFirstName() + " "
					+ clinicalTrialXml.getOverallOfficial().getLastName());
			trial.setInvestigatorTitle(clinicalTrialXml.getOverallOfficial().getRole());
		}
		trial.setIsFdaRegulated(clinicalTrialXml.getIsFdaRegulated());
		trial.setIsSection801(clinicalTrialXml.getIsSection801());
		trial.setHasExpandedAccess(clinicalTrialXml.getHasExpandedAccess());
		trial.setOverallContactName(clinicalTrialXml.getOverallContact().getFullName());
		trial.setOverallContactPhone(clinicalTrialXml.getOverallContact().getPhone());
		trial.setOverallContactEmail(clinicalTrialXml.getOverallContact().getEmail());
		trial.setOverallContactBackupName(clinicalTrialXml.getOverallContactBackup().getFullName());
		trial.setOverallContactBackupPhone(clinicalTrialXml.getOverallContactBackup().getPhone());
		trial.setOverallContactBackupEmail(clinicalTrialXml.getOverallContactBackup().getEmail());
		trial.setTransferDate(new Date());

		for (Location location : clinicalTrialXml.getLocations()) {
			if (location.getFacility().getName().contains("Emory")) {
				trial.setEmorySpecificStatus(location.getStatus());
			}
		}

		return trial;
	}

	
	public void setTestNctId(String testNctId) {
		this.testNctId = testNctId;
	}

	@SuppressWarnings("unchecked")
	public Boolean isDbUnlocked() throws Exception {
		return isDbUnlocked(null);
	}

	@SuppressWarnings("unchecked")
	public Boolean isDbUnlocked(EntityManager _em) throws Exception {
		EntityManager em = (_em == null) ? emf.createEntityManager() : _em;

		Query query = null;
		List<JobLock> locks = null;

		try {
			query = em.createQuery("Select l from JobLock l");
			locks = query.getResultList();
			if (locks.size() == 0) {
				logger.info("No Existing Locks found ... ");
				lockDb();
				logger.info(InetAddress.getLocalHost().getHostName() + " has locked the DB");
			} else {
				logger.info("Extract Job will not run on: " + InetAddress.getLocalHost().getHostName() + ". "
						+ "DB is Locked by " + locks.get(0).getServerName());
			}
		} catch (Exception ex) {
			throw new Exception(ex);
		} finally {
			if (_em == null) {
				em.close();
			}
		}

		// No Locks; Return true and continue to persist
		return locks.size() == 0;

	}

	public void lockDb() throws Exception {
		lockDb(null);
	}

	public void lockDb(EntityManager _em) throws Exception {
		EntityManager em = (_em == null) ? emf.createEntityManager() : _em;
		try {
			JobLock lock = new JobLock();
			em.getTransaction().begin();
			lock.setJobLockId(1);
			lock.setServerName(InetAddress.getLocalHost().getHostName());
			lock.setLockTime(new Date());
			em.persist(lock);
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new Exception(e);
		} finally {
			if (_em == null) {
				em.close();
			}
		}
	}

	public void unlockDb() throws Exception {
		unlockDb(null);
	}

	public void unlockDb(EntityManager _em) throws Exception {
		EntityManager em = (_em == null) ? emf.createEntityManager() : _em;
		try {
			em.getTransaction().begin();
			em.createQuery("DELETE FROM JobLock").executeUpdate();
			em.getTransaction().commit();
		} catch (Exception ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw new Exception(ex);
		} finally {
			if (_em == null) {
				em.close();
			}
		}
		logger.info("Extract is complete. Unlocked DB.");
	}

}
