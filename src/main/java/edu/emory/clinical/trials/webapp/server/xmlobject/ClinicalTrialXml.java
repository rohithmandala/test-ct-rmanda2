package edu.emory.clinical.trials.webapp.server.xmlobject;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="clinical_study")
@XmlAccessorType(XmlAccessType.FIELD)
public class ClinicalTrialXml {
	
	@XmlElement(name="id_info")
	private IdInfo idInfo;
	
	@XmlElement(name="brief_title")
	private String briefTitle;
	
	@XmlElement(name="official_title")
	private String officialTitle;
	
	@XmlElement(name="sponsors")
	private Sponsors sponsors;
	
	@XmlElement(name="source")
	private String source;
	
	@XmlElement(name="oversight_info")
	private OversightInfo oversightInfo;
	
	@XmlElement(name="brief_summary")
	private TextBlock briefSummary;
	
	@XmlElement(name="overall_status")
	private String overallStatus;
	
	@XmlElement(name="start_date")
	private String startDate;
	
	@XmlElement(name="completion_date")
	private String endDate;
	
	@XmlElement(name="why_stopped")
	private String whyStopped;
	
	@XmlElement(name="completion_date")
	private String completionDate;
	
	@XmlElement(name="primary_completion_date")
	private String primaryCompletionDate;
	
	@XmlElement(name="phase")
	private String phase;
	
	@XmlElement(name="study_type")
	private String studyType;
	
	@XmlElement(name="study_design")
	private String studyDesign;
	
	@XmlElement(name="primary_outcome")
    private Outcome primaryOutcome;

    @XmlElement(name="secondary_outcome")
    private Outcome secondaryOutcome;
	
	@XmlElement(name="number_of_arms")
	private String numberOfArms;
	
	@XmlElement(name="enrollment_type")
	private String enrollmentType;
	
	@XmlElement(name="condition")
	private String condition;
	
	@XmlElement(name="arm_group")
	private ArmGroup armGroup;
	
	@XmlElement(name="eligibility")
	private Eligibility eligibility;
	
	@XmlElement(name="location")
	private List<Location> locations;
	
	@XmlElement(name="intervention")
	private List<Intervention> interventions;
	
	@XmlElement(name="facility")
	private Facility facility;
	
	@XmlElement(name="overall_official")
	private Investigator overallOfficial;
	
	@XmlElement(name="verification_date")
	private String verificationDate;
	
	@XmlElement(name="lastchanged_date")
	private String lastChangedDate;
	
	@XmlElement(name="firstreceived_date")
	private String firstReceivedDate;
	
	@XmlElement(name="responsible_party")
	private ResponsibleParty responsibleParty;
	
	@XmlElement(name="keyword")
	private List<String> keywords;
	
	@XmlElement(name="is_fda_regulated")
	private String isFdaRegulated;
	
	@XmlElement(name="is_section_801")
	private String isSection801;
	
	@XmlElement(name="has_expanded_access")
	private String hasExpandedAccess;
	
	@XmlElement(name="condition_browse")
	private Browse conditionBrowse;
	
	@XmlElement(name="intervention_browse")
	private Browse interventionBrowse;
	
	@XmlElement(name="overall_contact")
	private Contact overallContact;
	
	@XmlElement(name="overall_contact_backup")
	private Contact overallContactBackup;
	
	public ClinicalTrialXml() {
		
	}
	
	public ClinicalTrialXml(ClinicalTrialXml copy) {
		this.idInfo = copy.idInfo;
		this.briefTitle = copy.briefTitle;
		this.officialTitle = copy.officialTitle;
		this.sponsors = copy.sponsors;
		this.source = copy.source;
		this.oversightInfo = copy.oversightInfo;
		this.briefSummary = copy.briefSummary;
		this.overallStatus = copy.overallStatus;
		this.startDate = copy.startDate;
		this.endDate = copy.endDate;
		this.whyStopped = copy.whyStopped;
		this.completionDate = copy.completionDate;
		this.primaryCompletionDate = copy.primaryCompletionDate;
		this.phase = copy.phase;
		this.studyType = copy.studyType;
		this.studyDesign = copy.studyDesign;
		this.primaryOutcome = copy.primaryOutcome;
		this.secondaryOutcome = copy.secondaryOutcome;
		this.numberOfArms = copy.numberOfArms;
		this.enrollmentType = copy.enrollmentType;
		this.condition = copy.condition;
		this.armGroup = copy.armGroup;
		this.eligibility = copy.eligibility;
		this.locations = copy.locations;
		this.interventions = copy.interventions;
		this.facility = copy.facility;
		this.overallOfficial = copy.overallOfficial;
		this.verificationDate = copy.verificationDate;
		this.lastChangedDate = copy.lastChangedDate;
		this.firstReceivedDate = copy.firstReceivedDate;
		this.responsibleParty = copy.responsibleParty;
		this.keywords = copy.keywords;
		this.isFdaRegulated = copy.isFdaRegulated;
		this.isSection801 = copy.isSection801;
		this.hasExpandedAccess = copy.hasExpandedAccess;
		this.conditionBrowse = copy.conditionBrowse;
		this.interventionBrowse = copy.interventionBrowse;
		this.overallContact = copy.overallContact;
		this.overallContactBackup = copy.overallContactBackup;
	}

	public IdInfo getIdInfo() {
		return idInfo;
	}

	public String getBriefTitle() {
		return briefTitle;
	}

	public String getOfficialTitle() {
		return officialTitle;
	}

	public Sponsors getSponsors() {
		return sponsors != null ? sponsors : new Sponsors();
	}

	public String getSource() {
		return source;
	}

	public OversightInfo getOversightInfo() {
		return oversightInfo;
	}
	
	public TextBlock getBriefSummary() {
		return briefSummary != null ? briefSummary : new TextBlock() ;
	}

	public String getOverallStatus() {
		return overallStatus;
	}

	public Investigator getOverallOfficial() {
		return overallOfficial != null ? overallOfficial : new Investigator();
	}

	public String getStartDate() {
		return startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public String getWhyStopped() {
		return whyStopped;
	}

	public String getCompletionDate() {
		return completionDate;
	}

	public String getPrimaryCompletionDate() {
		return primaryCompletionDate;
	}

	public String getPhase() {
		return phase;
	}

	public String getStudyType() {
		return studyType;
	}

	public String getStudyDesign() {
		return studyDesign;
	}

	public String getNumberOfArms() {
		return numberOfArms;
	}

	public String getEnrollmentType() {
		return enrollmentType;
	}
	
	public String getCondition() {
		return condition;
	}

	public ArmGroup getArmGroup() {
		return armGroup != null ? armGroup : new ArmGroup();
	}

	public List<Intervention> getInterventions() {
		return interventions != null ? interventions : new ArrayList<Intervention>();
	}

	public Eligibility getEligibility() {
		return eligibility != null ? eligibility : new Eligibility();
	}

	public List<Location> getLocations() {
		return locations != null ? locations : new ArrayList<Location>();
	}

	public Facility getFacility() {
		return facility != null ? facility : new Facility();
	}

	public String getVerificationDate() {
		return verificationDate;
	}

	public String getLastChangedDate() {
		return lastChangedDate;
	}

	public String getFirstReceivedDate() {
		return firstReceivedDate;
	}

	public ResponsibleParty getResponsibleParty() {
		return responsibleParty != null ? responsibleParty : new ResponsibleParty();
	}

	public List<String> getKeywords() {
		return keywords != null ? keywords : new ArrayList<String>();
	}

	public String getIsFdaRegulated() {
		return isFdaRegulated;
	}

	public String getIsSection801() {
		return isSection801;
	}

	public String getHasExpandedAccess() {
		return hasExpandedAccess;
	}

	public Browse getConditionBrowse() {
		return conditionBrowse != null ? conditionBrowse : new Browse();
	}
	public Browse getInterventionBrowse() {
		return interventionBrowse != null ? interventionBrowse : new Browse();
	}

	public Outcome getPrimaryOutcome() {
		return primaryOutcome != null ? primaryOutcome : new Outcome();
	}

	public Outcome getSecondaryOutcome() {
		return secondaryOutcome != null ? secondaryOutcome : new Outcome();
	}

	public Contact getOverallContact() {
		return overallContact != null ? overallContact : new Contact();
	}

	public Contact getOverallContactBackup() {
		return overallContactBackup != null ? overallContactBackup : new Contact();
	}

	public void setBriefSummary(TextBlock briefSummary) {
		this.briefSummary = briefSummary;
	}

	public void setSecondaryOutcome(Outcome secondaryOutcome) {
		this.secondaryOutcome = secondaryOutcome;
	}

	public void setArmGroup(ArmGroup armGroup) {
		this.armGroup = armGroup;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}

	public void setInterventions(List<Intervention> interventions) {
		this.interventions = interventions;
	}

	public void setEligibility(Eligibility eligibility) {
		this.eligibility = eligibility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

	public void setOverallOfficial(Investigator overallOfficial) {
		this.overallOfficial = overallOfficial;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public void setConditionBrowse(Browse conditionBrowse) {
		this.conditionBrowse = conditionBrowse;
	}

	public void setInterventionBrowse(Browse interventionBrowse) {
		this.interventionBrowse = interventionBrowse;
	}

	public void setOverallContact(Contact overallContact) {
		this.overallContact = overallContact;
	}

	public void setOverallContactBackup(Contact overallContactBackup) {
		this.overallContactBackup = overallContactBackup;
	}

	public void setResponsibleParty(ResponsibleParty responsibleParty) {
		this.responsibleParty = responsibleParty;
	}
}