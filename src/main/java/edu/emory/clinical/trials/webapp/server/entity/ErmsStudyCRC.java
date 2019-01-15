package edu.emory.clinical.trials.webapp.server.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "erms_study_crc")
public class ErmsStudyCRC implements UniqueObject  {
	
	@Id
	@Column(name="studyrole_id")
	private Integer studyRoleId;
	
	public Integer getPrimaryKey() { return studyRoleId; }
	
	@Column(name="study_id")
	private Integer studyId;
	
	@Column(name="crc_name")
	private String crcName;
	
	@Column(name="crc_phone")
	private String crcPhone;
	
	@Column(name="crc_email")
	private String crcEmail;
	
	@Column(name="transfer_date")
	private Date transferDate;

	public Integer getStudyRoleId() {
		return studyRoleId;
	}

	public Integer getStudyId() {
		return studyId;
	}

	public String getCrcName() {
		return crcName;
	}

	public String getCrcPhone() {
		return crcPhone;
	}

	public String getCrcEmail() {
		return crcEmail;
	}

	public Date getTransferDate() {
		return transferDate;
	}
}