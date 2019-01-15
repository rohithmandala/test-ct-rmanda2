package edu.emory.clinical.trials.test.util;


import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.emory.clinical.trials.webapp.server.entity.JobLog;
import edu.emory.clinical.trials.webapp.server.rest.TestConfigurator;
import edu.emory.clinical.trials.webapp.server.util.HibernateLogUtil;

/**
 * Use this class to test ad hoc JPA code, which can be from another class on the server.
 * This just lets us bypass running the code on the Application Server and can speed
 * up the coding/debugging process.  It's kind of like a unit test, but doesn't run regularly
 * and does not need to be maintained.
 *
 * NOTE: Make sure to set the database credentials in the test/persistence.xml
 */
public class QueryPlayground {
	private static Logger LOG =LoggerFactory.getLogger(QueryPlayground.class);

	public static void main(String[] args) throws Exception {
		// Boiler plate code for bootstrapping the Entity Manager
		TestConfigurator.init();
		EntityManagerFactory emf = TestConfigurator.getInstance().getEmf();
		EntityManager em = emf.createEntityManager();
		LOG.info("Entity Manager Initialized");
		HibernateLogUtil.turnOnAll();
		createJobWithoutDelegatorSaveOrUpdate(emf, em);
		LOG.info("All Done");
	}

    private static void createJobWithoutDelegatorSaveOrUpdate(EntityManagerFactory emf, EntityManager em) {
        try {
            em.getTransaction().begin();
            JobLog jobLog = new JobLog();

            jobLog.setJobName("Blah Blah");
            jobLog.setJobCompletionDate(new Date());
            jobLog.setCauseOfFailure("bad times");
            jobLog.setSuccessFlag('Y');

            em.persist(jobLog);
            em.getTransaction().commit();

        } finally {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            if (em.isOpen()) {
                em.close();
            }
            emf.close();
        }
    }
}
