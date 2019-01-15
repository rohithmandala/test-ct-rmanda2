package edu.emory.clinical.trials.webapp.server.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ct_study_staging")
public class ClinicalTrialStaging implements Serializable, UniqueObject  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "nct_id")
	private String nctId;
	
	@Column(name = "org_study_id")
	private String orgStudyId;
	
	@Column(name = "secondary_id")
	private String secondaryId;
	
	@Column(name = "nct_alias")
	private String nctAlias;
	
	@Column(name = "brief_title")
	private String briefTitle;
	
	@Column(name = "official_title")
	private String officialTitle;
	
	@Column(name = "lead_sponsor")
	private String leadSponsor;
	
	@Column(name = "collaborator")
	private String collaborator;
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "authority")
	private String authority;
	
	@Column(name = "has_dmc")
	private String hasDmc;
	
	@Column(name = "brief_summary")
	private String briefSummary;
	
	@Column(name = "overall_status")
	private String overallStatus;
	
	@Column(name = "start_date")
	private String startDate;
	
	@Column(name = "end_date")
	private String endDate;
	
	@Column(name = "why_stopped")
	private String whyStopped;
	
	@Column(name = "primary_completion_date")
	private String primaryCompletionDate;
	
	@Column(name = "phase")
	private String phase;
	
	@Column(name = "study_type")
	private String studyType;
	
	@Column(name = "study_design")
	private String studyDesign;
	
	@Column(name = "primary_outcome_measure")
	private String primaryOutcomeMeasure;
	
	@Column(name = "primary_outcome_time_frame")
	private String primaryOutcomeTimeFrame;
	
	@Column(name = "primary_outcome_safety_issue")
	private String primaryOutcomeSafetyIssue;
	
	@Column(name = "primary_outcome_description")
	private String primaryOutcomeDescription;
	
	@Column(name = "secondary_outcome_measure")
	private String secondaryOutcomeMeasure;
	
	@Column(name = "secondary_outcome_time_frame")
	private String secondaryOutcomeTimeFrame;
	
	@Column(name = "secondary_outcome_safety_issue")
	private String secondaryOutcomeSafetyIssue;
	
	@Column(name = "secondary_outcome_description")
	private String secondaryOutcomeDescription;
	
	@Column(name = "number_of_arms")
	private String numberOfArms;
	
	@Column(name = "enrollment_type")
	private String enrollmentType;
	
	@Column(name = "condition")
	private String condition;
	
	@Column(name = "arm_group_label")
	private String armGroupLabel;
	
	@Column(name = "arm_group_type")
	private String armGroupType;
	
	@Column(name = "arm_group_description")
	private String armGroupDescription;
	
	@Column(name = "eligibility_criteria")
	private String eligibilityCriteria;
	
	@Column(name = "gender")
	private String gender;
	
	@Column(name = "minimum_age")
	private String minimumAge;
	
	@Column(name = "maximum_age")
	private String maximumAge;
	
	@Column(name = "healthy_volunteers")
	private String healthyVolunteers;
	
	@Column(name = "verification_date")
	private String verificationDate;
	
	@Column(name = "last_changed_date")
	private String lastChangedDate;
	
	@Column(name = "first_received_date")
	private String firstReceivedDate;
	
	@Column(name = "responsible_party_type")
	private String responsiblePartyType;
	
	@Column(name = "investigator_affiliation")
	private String investigatorAffiliation;
	
	@Column(name = "investigator_full_name")
	private String investigatorFullName;
	
	@Column(name = "investigator_title")
	private String investigatorTitle;
	
	@Column(name = "is_fda_regulated")
	private String isFdaRegulated;
	
	@Column(name = "is_section_801")
	private String isSection801;
	
	@Column(name = "has_expanded_access")
	private String hasExpandedAccess;
	
	@Column(name = "clinical_results")
	private String clinicalResults;
	
	@Column(name = "emory_specific_status")
	private String emorySpecificStatus;
	
	@Column(name = "transfer_date")
	private Date transferDate;
	
	@Column(name = "overall_contact_name")
	private String overallContactName;
	
	@Column(name = "overall_contact_phone")
	private String overallContactPhone;
	
	@Column(name = "overall_contact_email")
	private String overallContactEmail;
	
	@Column(name = "overall_contact_backup_name")
	private String overallContactBackupName;
	
	@Column(name = "overall_contact_backup_phone")
	private String overallContactBackupPhone;
	
	@Column(name = "overall_contact_backup_email")
	private String overallContactBackupEmail;

	public String getPrimaryKey() {
		return nctId;
	}
	
	public void setNctId(String nctId) {
		this.nctId = nctId;
	}

	public void setOrgStudyId(String orgStudyId) {
		this.orgStudyId = orgStudyId;
	}

	public void setSecondaryId(String secondaryId) {
		this.secondaryId = secondaryId;
	}

	public void setNctAlias(String nctAlias) {
		this.nctAlias = nctAlias;
	}

	public void setBriefTitle(String briefTitle) {
		this.briefTitle = briefTitle;
	}

	public void setOfficialTitle(String officialTitle) {
		this.officialTitle = officialTitle;
	}

	public void setLeadSponsor(String leadSponsor) {
		this.leadSponsor = leadSponsor;
	}

	public void setCollaborator(String collaborator) {
		this.collaborator = collaborator;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public void setHasDmc(String hasDmc) {
		this.hasDmc = hasDmc;
	}

	public void setBriefSummary(String briefSummary) {
		this.briefSummary = briefSummary;
	}

	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public void setWhyStopped(String whyStopped) {
		this.whyStopped = whyStopped;
	}

	public void setPrimaryCompletionDate(String primaryCompletionDate) {
		this.primaryCompletionDate = primaryCompletionDate;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public void setStudyType(String studyType) {
		this.studyType = studyType;
	}

	public void setStudyDesign(String studyDesign) {
		this.studyDesign = studyDesign;
	}

	public void setPrimaryOutcomeMeasure(String primaryOutcomeMeasure) {
		this.primaryOutcomeMeasure = primaryOutcomeMeasure;
	}

	public void setPrimaryOutcomeTimeFrame(String primaryOutcomeTimeFrame) {
		this.primaryOutcomeTimeFrame = primaryOutcomeTimeFrame;
	}

	public void setPrimaryOutcomeSafetyIssue(String primaryOutcomeSafetyIssue) {
		this.primaryOutcomeSafetyIssue = primaryOutcomeSafetyIssue;
	}

	public void setPrimaryOutcomeDescription(String primaryOutcomeDescription) {
		this.primaryOutcomeDescription = primaryOutcomeDescription;
	}

	public void setSecondaryOutcomeMeasure(String secondaryOutcomeMeasure) {
		this.secondaryOutcomeMeasure = secondaryOutcomeMeasure;
	}

	public void setSecondaryOutcomeTimeFrame(String secondaryOutcomeTimeFrame) {
		this.secondaryOutcomeTimeFrame = secondaryOutcomeTimeFrame;
	}

	public void setSecondaryOutcomeSafetyIssue(String secondaryOutcomeSafetyIssue) {
		this.secondaryOutcomeSafetyIssue = secondaryOutcomeSafetyIssue;
	}

	public void setSecondaryOutcomeDescription(String secondaryOutcomeDescription) {
		this.secondaryOutcomeDescription = secondaryOutcomeDescription;
	}

	public void setNumberOfArms(String numberOfArms) {
		this.numberOfArms = numberOfArms;
	}

	public void setEnrollmentType(String enrollmentType) {
		this.enrollmentType = enrollmentType;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public void setArmGroupLabel(String armGroupLabel) {
		this.armGroupLabel = armGroupLabel;
	}

	public void setArmGroupType(String armGroupType) {
		this.armGroupType = armGroupType;
	}

	public void setArmGroupDescription(String armGroupDescription) {
		this.armGroupDescription = armGroupDescription;
	}

	public void setEligibilityCriteria(String eligibilityCriteria) {
		this.eligibilityCriteria = eligibilityCriteria;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setMinimumAge(String minimumAge) {
		this.minimumAge = minimumAge;
	}

	public void setMaximumAge(String maximumAge) {
		this.maximumAge = maximumAge;
	}

	public void setHealthyVolunteers(String healthyVolunteers) {
		this.healthyVolunteers = healthyVolunteers;
	}

	public void setVerificationDate(String verificationDate) {
		this.verificationDate = verificationDate;
	}

	public void setLastChangedDate(String lastChangedDate) {
		this.lastChangedDate = lastChangedDate;
	}

	public void setFirstReceivedDate(String firstReceivedDate) {
		this.firstReceivedDate = firstReceivedDate;
	}

	public String getResponsiblePartyType() {
		return responsiblePartyType != null ? responsiblePartyType : "";
	}

	public void setResponsiblePartyType(String responsiblePartyType) {
		this.responsiblePartyType = responsiblePartyType;
	}

	public void setInvestigatorAffiliation(String investigatorAffiliation) {
		this.investigatorAffiliation = investigatorAffiliation;
	}

	public void setInvestigatorFullName(String investigatorFullName) {
		this.investigatorFullName = investigatorFullName;
	}

	public void setInvestigatorTitle(String investigatorTitle) {
		this.investigatorTitle = investigatorTitle;
	}

	public void setIsFdaRegulated(String isFdaRegulated) {
		this.isFdaRegulated = isFdaRegulated;
	}

	public void setIsSection801(String isSection801) {
		this.isSection801 = isSection801;
	}

	public void setHasExpandedAccess(String hasExpandedAccess) {
		this.hasExpandedAccess = hasExpandedAccess;
	}

	public void setClinicalResults(String clinicalResults) {
		this.clinicalResults = clinicalResults;
	}

	public void setEmorySpecificStatus(String emorySpecificStatus) {
		this.emorySpecificStatus = emorySpecificStatus;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public void setOverallContactName(String overallContactName) {
		this.overallContactName = overallContactName;
	}

	public void setOverallContactPhone(String overallContactPhone) {
		this.overallContactPhone = overallContactPhone;
	}

	public void setOverallContactEmail(String overallContactEmail) {
		this.overallContactEmail = overallContactEmail;
	}

	public void setOverallContactBackupName(String overallContactBackupName) {
		this.overallContactBackupName = overallContactBackupName;
	}

	public void setOverallContactBackupPhone(String overallContactBackupPhone) {
		this.overallContactBackupPhone = overallContactBackupPhone;
	}

	public void setOverallContactBackupEmail(String overallContactBackupEmail) {
		this.overallContactBackupEmail = overallContactBackupEmail;
	}

}
