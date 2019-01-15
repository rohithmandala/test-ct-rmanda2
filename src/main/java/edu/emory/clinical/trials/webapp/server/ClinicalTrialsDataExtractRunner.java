package edu.emory.clinical.trials.webapp.server;

import org.jboss.logging.Logger;

public class ClinicalTrialsDataExtractRunner implements Runnable {
	private Logger logger = Logger.getLogger(ClinicalTrialsDataExtractRunner.class.getName());

	public void run() {
		boolean passed=false;
		String message="";
		try {
			passed = new ClinicalTrialsDataExtractor().extract();
		} catch (Exception e) {
		    logger.error("Clinical Trials Update Job Failed", e);
			passed = false;
		} finally {
			if (!passed) {
				logger.error("Clinical Trials Update Job Failed "+message);
			} else {
				logger.info("Clinical Trials Update Job Complete");
			}
		}
	}

}
