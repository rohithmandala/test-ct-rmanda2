package edu.emory.clinical.trials.webapp.server.rest;

import javax.persistence.EntityManagerFactory;

import edu.emory.clinical.trials.webapp.server.Delegator;

public final class RestServerConfigurator implements ClinicalTrialsConfigurator {
	private static RestServerConfigurator instance;

	private EntityManagerFactory emf;

	private RestServerConfigurator() throws InstantiationException {
		emf = Delegator.createEntityManagerFactory("clinicalTrialsEntityManager");
	}

	public static void init() throws InstantiationException {
		instance = new RestServerConfigurator();
	}

	public static RestServerConfigurator getInstance() {
		return instance;
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

}