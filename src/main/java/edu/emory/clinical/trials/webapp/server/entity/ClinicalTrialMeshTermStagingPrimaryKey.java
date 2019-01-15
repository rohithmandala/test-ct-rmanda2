package edu.emory.clinical.trials.webapp.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class ClinicalTrialMeshTermStagingPrimaryKey implements Serializable {

	@Column(name = "nct_id")
	private String nctId;

	@Column(name = "term")
	private String meshTerm;

	public void setNctId(String nctId) {
		this.nctId = nctId;
	}

	public void setMeshTerm(String meshTerm) {
		this.meshTerm = meshTerm;
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((meshTerm == null) ? 0 : meshTerm.hashCode());
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
        ClinicalTrialMeshTermStagingPrimaryKey other = (ClinicalTrialMeshTermStagingPrimaryKey) obj;
        if (meshTerm == null) {
            if (other.meshTerm != null)
                return false;
        } else if (!meshTerm.equals(other.meshTerm))
            return false;
        if (nctId == null) {
            if (other.nctId != null)
                return false;
        } else if (!nctId.equals(other.nctId))
            return false;
        return true;
    }
}

