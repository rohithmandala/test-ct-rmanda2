package edu.emory.clinical.trials.webapp.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ClinicalTrialKeywordStagingPrimaryKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "nct_id")
	private String nctId;

	@Column(name = "keyword")
	private String keyword;

	public String getNctId() {
		return nctId;
	}

	public void setNctId(String nctId) {
		this.nctId = nctId;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((keyword == null) ? 0 : keyword.hashCode());
        result = prime * result + ((nctId == null) ? 0 : nctId.hashCode());
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
        ClinicalTrialKeywordStagingPrimaryKey other = (ClinicalTrialKeywordStagingPrimaryKey) obj;
        if (keyword == null) {
            if (other.keyword != null)
                return false;
        } else if (!keyword.equals(other.keyword))
            return false;
        if (nctId == null) {
            if (other.nctId != null)
                return false;
        } else if (!nctId.equals(other.nctId))
            return false;
        return true;
    }
	
}

