package edu.emory.clinical.trials.webapp.server.rest;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestConfigurator implements ClinicalTrialsConfigurator {

	private static TestConfigurator instance;

	private EntityManagerFactory emf;

	private TestConfigurator() {
		if (emf == null) {
			emf = Persistence.createEntityManagerFactory("clinicalTrialsEntityManagerLocal");
		}
	}

	public static void init() throws InstantiationException {
		instance = new TestConfigurator();
	}

	public static TestConfigurator getInstance() {
		if (instance != null) {
			return instance;
		}
		else {
			instance = new TestConfigurator();
			return instance;
		}
	}

	public EntityManagerFactory getEmf() {
		return emf;
	}

}
