package edu.emory.clinical.trials.webapp.server.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "job_lock")
public class JobLock implements UniqueObject  {
	
	@Id
	@Column(name = "job_lock_id")
	private Integer jobLockId;
	
	public Integer getPrimaryKey() {
		return jobLockId;
	}
	
	@Column(name = "server_name")
	private String serverName;
	
	@Column(name="lock_time")
	private Date lockTime;

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setJobLockId(Integer jobLockId) {
		this.jobLockId = jobLockId;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}
}