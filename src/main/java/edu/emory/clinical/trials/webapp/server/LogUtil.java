package edu.emory.clinical.trials.webapp.server;

import java.util.Date;

import javax.persistence.EntityManager;

import edu.emory.clinical.trials.webapp.server.entity.JobLog;
import edu.emory.clinical.trials.webapp.server.rest.RestServerConfigurator;

public class LogUtil {

	public static void logJobResult(String jobName, Boolean success) throws Exception {
		JobLog jobLog = new JobLog();
		jobLog.setJobName(jobName);
		jobLog.setJobCompletionDate(new Date());
		if (success) {
			jobLog.setSuccessFlag('Y');
		} else {
			jobLog.setSuccessFlag('N');
		}
		persistLog(jobLog);
	}

	/**
	 * TODO Configurator refactor, a la Glenn
	 * FactoryPattern should be implemented using Dependency Injection (e.g. CDI)
	 * 
	 * @param jobLog
	 * @throws Exception
	 */
	private static void persistLog(JobLog jobLog) throws Exception {
		EntityManager em = RestServerConfigurator.getInstance().getEmf().createEntityManager();
		try {
			em.getTransaction().begin();
			//Delegator.persistToDatabase(em, jobLog);
			em.persist(jobLog);
			em.getTransaction().commit();
		} finally {
			em.close();
		}
	}
}
