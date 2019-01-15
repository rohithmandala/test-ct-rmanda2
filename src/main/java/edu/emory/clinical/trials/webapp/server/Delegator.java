package edu.emory.clinical.trials.webapp.server;

import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import edu.emory.clinical.trials.webapp.server.entity.UniqueObject;
public class Delegator {

	public static void sendMessage(MimeMessage message) throws Exception {
		// Only actually send the email out if we're in Live App Mode
		if (!Util.isTestMode) {
			Transport.send(message);
		}
	}
	
	/**
	 * @deprecated Use JPA API instead (see LogUtil.persistLog() for an example)
	 * @param em
	 * @param o
	 * @throws Exception
	 */
	@Deprecated
	public static void persistToDatabase(EntityManager em, UniqueObject o) throws Exception {
		if (!Util.isTestMode) {
			saveOrUpdate(em,o);
			//em.persist(o);
		}
	}
	
	public static EntityManagerFactory createEntityManagerFactory(String persistenceUnitName) {
		if (!Util.isTestMode) {
			return Persistence.createEntityManagerFactory(persistenceUnitName);
		}
		else {
			return Persistence.createEntityManagerFactory(persistenceUnitName + "Local");
		}
	}
	
	/**
	 * @param em
	 * @param o
	 * @throws Exception
	 */
	
	private static void saveOrUpdate(EntityManager em, UniqueObject o) throws Exception {
		boolean xAction = em.getTransaction().isActive();
		try {
			if (!xAction) {
				em.getTransaction().begin();
			}
			if (em.find(o.getClass(), o.getPrimaryKey()) != null) {
				em.merge(o);
			} else {
				em.persist(o);
			}
			if (!xAction && em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
		} catch (Exception ex) {
			if (!xAction && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		}
	}
}