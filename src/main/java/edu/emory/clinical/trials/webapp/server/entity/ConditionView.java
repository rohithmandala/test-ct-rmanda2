package edu.emory.clinical.trials.webapp.server.entity;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name = "conditions_category_view")
public class ConditionView implements UniqueObject  {
	
	@EmbeddedId
	private ConditionViewPrimaryKey primaryKey = new ConditionViewPrimaryKey();

	public ConditionViewPrimaryKey getPrimaryKey() {
		return primaryKey;
	}
	
	@Column(name="category_term")
	private String categoryTerm;
	
	@ManyToOne
	@JoinColumn(name = "category_id", insertable = false, updatable = false)
	private TrialCategory category;

	public String getCategoryTerm() {
		return categoryTerm;
	}

}
