package edu.emory.clinical.trials.webapp.server.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ct_location_info")
public class ClinicalTrialLocationStaging implements UniqueObject  {
	
	@Id
	@Column(name="location_info_id")
	private Integer id;
	
	public Integer getPrimaryKey() {
		return id;
	}
	
	@Column(name="nct_id")
	private String nctId;
	
	@Column(name="city")
	private String city;
	
	@Column(name="state")
	private String state;
	
	@Column(name="country")
	private String country;
	
	@Column(name="status")
	private String status;
	
}
