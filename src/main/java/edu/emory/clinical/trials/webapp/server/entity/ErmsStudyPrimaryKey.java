package edu.emory.clinical.trials.webapp.server.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;


@Embeddable
public class ErmsStudyPrimaryKey implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "study_id")
	private Integer studyId;

	@Column(name = "nct_id")
	private String nctId;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nctId == null) ? 0 : nctId.hashCode());
        result = prime * result + ((studyId == null) ? 0 : studyId.hashCode());
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
        ErmsStudyPrimaryKey other = (ErmsStudyPrimaryKey) obj;
        if (nctId == null) {
            if (other.nctId != null)
                return false;
        } else if (!nctId.equals(other.nctId))
            return false;
        if (studyId == null) {
            if (other.studyId != null)
                return false;
        } else if (!studyId.equals(other.studyId))
            return false;
        return true;
    }
	
	
}

