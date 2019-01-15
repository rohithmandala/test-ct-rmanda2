package edu.emory.clinical.trials.webapp.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "study_status_filter")
public class StatusFilter implements Serializable, UniqueObject {
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private StatusFilterPrimaryKey primaryKey = new StatusFilterPrimaryKey();
	
	@Column(name="status_display")
	private String statusDisplay;

	public StatusFilter() {
		super();
	}

	public StatusFilter(String system, String code) {
		super();
		primaryKey= new StatusFilterPrimaryKey(system,code);
	}

	public StatusFilterPrimaryKey getPrimaryKey() {
		return primaryKey;
	}

	public void setStatusSystem(String system) {
		primaryKey.setStatusSystem(system);
	}
	
	public String getStatusSystem() {
		return primaryKey.getStatusSystem();
	}

	public void setStatusCode(String code) {
		primaryKey.setStatusCode(code);
	}
	
	public String getStatusCode() {
		return primaryKey.getStatusCode();
	}
	
	public String getStatusDisplay() {
		return statusDisplay;
	}

	public void setStatusDisplay(String statusDisplay) {
		this.statusDisplay = statusDisplay;
	}
	
	public boolean canDisplay (String statusSystem, String statusCode) {
		if (statusSystem.equals(getStatusSystem()) &&
				getStatusSystem().equals("ERMS")) {
			if (statusCode.equals(getStatusCode()) &&
					statusDisplay.toUpperCase().trim().equals("Y")) {
				return true;
			}
		}
		if (statusSystem.equals(getStatusSystem()) &&
				getStatusSystem().equals("CT")) {
			if (statusCode.equals(getStatusCode()) &&
					statusDisplay.toUpperCase().trim().equals("Y")) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "StatusFilter [primaryKey=" + primaryKey + ", statusDisplay=" + statusDisplay + "]";
	}
	
	
}
