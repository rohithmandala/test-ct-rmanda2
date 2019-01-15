package edu.emory.clinical.trials.webapp.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class StatusFilterPrimaryKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "status_system")
	private String statusSystem;

	@Column(name = "status_code")
	private String statusCode;

	public StatusFilterPrimaryKey() {
		super();
	}

	public StatusFilterPrimaryKey(String statusSystem, String statusCode) {
		super();
		this.statusSystem = statusSystem;
		this.statusCode = statusCode;
	}

	public void setStatusSystem(String system) {
		this.statusSystem=system;
	}
	
	public String getStatusSystem() {
		return statusSystem;
	}

	public void setStatusCode(String code) {
		this.statusCode=code;
	}
	
	public String getStatusCode() {
		return statusCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((statusCode == null) ? 0 : statusCode.hashCode());
		result = prime * result + ((statusSystem == null) ? 0 : statusSystem.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StatusFilterPrimaryKey other = (StatusFilterPrimaryKey) obj;
		if (statusCode == null) {
			if (other.statusCode != null)
				return false;
		} else if (!statusCode.equals(other.statusCode))
			return false;
		if (statusSystem == null) {
			if (other.statusSystem != null)
				return false;
		} else if (!statusSystem.equals(other.statusSystem))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "StatusFilterPrimaryKey [statusSystem=" + statusSystem + ", statusCode=" + statusCode + "]";
	}
	
}
