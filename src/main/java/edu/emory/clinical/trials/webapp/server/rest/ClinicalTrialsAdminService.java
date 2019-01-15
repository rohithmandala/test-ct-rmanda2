package edu.emory.clinical.trials.webapp.server.rest;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.log4j.Logger;

import edu.emory.clinical.trials.webapp.server.Util;
import edu.emory.clinical.trials.webapp.server.entity.JobLock;

/**
 * Returning JSONP via these methods is considered a hack to get around a
 * security holy in the HTTP protocol. This isn't a current threat, but it might
 * be wise to explore other alternatives in the future.
 * 
 * Root resource (exposed at "myresource" path)
 */
@Path("admin")
@SuppressWarnings("unchecked")
public final class ClinicalTrialsAdminService {

	private final EntityManagerFactory emf;

	private static Logger logger = Logger.getLogger(ClinicalTrialsAdminService.class.getName());

	public ClinicalTrialsAdminService() {
		if (RestServerConfigurator.getInstance() != null && !Util.isTestMode) {
			emf = RestServerConfigurator.getInstance().getEmf();
		}
		// Using Test
		else {
			emf = TestConfigurator.getInstance().getEmf();
		}
	}

	@GET
	@Path("/resetjoblock")
	@Produces("text/html")
	public String getResetDbLock() throws Exception {
		StringBuilder output = new StringBuilder();
		output.append("<html class='dark'>\n");
		// output.append("<link rel='stylesheet' type='text/css'
		// href='../../style.css'>");
		output.append("<h1>Resetting Data Update Job Lock\n");
		output.append("<div class='fun.left'>");
		output.append("<br>");

		EntityManager em = emf.createEntityManager();
		int numDbLocks = 0;
		try {
			numDbLocks = getNumDbLocks(em);
			output.append("<br>" + numDbLocks + " DB locks found,<br>");
			output.append("Job Lock Reset<br>");
		} catch (Exception ex) {
			output.append("Could not read DB locks.  Database Read problem: " + ex.getMessage());
		} finally {
			em.close();
			output.append("</html>\n");
		}
		return output.toString();
	}

	private int getNumDbLocks(EntityManager em) throws Exception {
		int result = -1;
		Query query = null;

		query = em.createQuery("Select l from JobLock l");
		List<JobLock> locks = query.getResultList();
		if (locks.size() == 0) {
			result = 0;
		} else {
			result = locks.size();
		}
		return result;
	}

}
