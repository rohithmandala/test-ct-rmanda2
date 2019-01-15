package edu.emory.clinical.trials.webapp.server.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.JSONP;

import edu.emory.clinical.trials.webapp.server.ClinicalTrialsDataExtractRunner;
import edu.emory.clinical.trials.webapp.server.Util;
import edu.emory.clinical.trials.webapp.server.entity.ClinicalTrialIntervention;
import edu.emory.clinical.trials.webapp.server.entity.ErmsStudyCRC;
import edu.emory.clinical.trials.webapp.server.entity.SearchResultView;
import edu.emory.clinical.trials.webapp.server.entity.StatusFilter;
import edu.emory.clinical.trials.webapp.server.entity.TrialCategory;
import edu.emory.clinical.trials.webapp.server.entity.TrialDetail;
import edu.emory.clinical.trials.webapp.server.util.HibernateLogUtil;

/**
 * Returning JSONP via these methods is considered a hack to get around a
 * security holy in the HTTP protocol. This isn't a current threat, but it might
 * be wise to explore other alternatives in the future.
 * 
 * Root resource (exposed at "myresource" path)
 */
@Path("trials")
public final class ClinicalTrialsRestService {

	private final EntityManagerFactory emf;

	private static Logger logger = Logger.getLogger(ClinicalTrialsRestService.class.getName());

	public ClinicalTrialsRestService() {
		if (RestServerConfigurator.getInstance() != null && !Util.isTestMode) {
			emf = RestServerConfigurator.getInstance().getEmf();
		}
		// Using Test
		else {
			emf = TestConfigurator.getInstance().getEmf();
		}
	}

	@GET
	@JSONP(callback = "searchResults")
	@Path("/search")
	@Produces("application/javascript")
	public List<SearchResultView> getResults(@QueryParam("criteria") String criteria,
			@QueryParam("status") String status) throws Exception {

		logger.info("GET request for path: /api/trials/search");

		String statusClause = "";

		if (status != null) {
			status = status.toLowerCase();
			statusClause = " AND lower(S.STUDY_STATUS) = :status ";
		}

		String nativeQuery = "SELECT DISTINCT S.* " + "FROM SEARCH_RESULTS_VIEW S " + "LEFT OUTER JOIN CT_KEYWORD K "
				+ "ON K.NCT_ID = S.NCT_ID " + "LEFT OUTER JOIN CT_MESH_TERM M " + "ON M.NCT_ID = S.NCT_ID "
				+ "WHERE (lower(S.BRIEF_SUMMARY) LIKE :searchCriteria " + "OR lower(S.BRIEF_TITLE) LIKE :searchCriteria "
				+ "OR lower(S.CONDITION) LIKE :searchCriteria " + "OR lower(S.DEPT_DIV) LIKE :searchCriteria "
				+ "OR lower(S.DEPT_DIV_ID) LIKE :searchCriteria " + "OR lower(S.ERMS_STUDY_ID) LIKE :searchCriteria "
				+ "OR lower(S.NCT_ID) LIKE :searchCriteria " + "OR lower(S.PI_NAME) LIKE :searchCriteria "
				+ "OR lower(S.PRIMARY_CATEGORY) LIKE :searchCriteria "
				+ "OR lower(S.SECONDARY_CATEGORY) LIKE :searchCriteria "
				+ "OR lower(S.STUDY_STATUS) LIKE :searchCriteria " + "OR lower(K.KEYWORD) LIKE :searchCriteria "
				+ "OR lower(M.TERM) LIKE :searchCriteria ) " + statusClause
				+ "ORDER BY S.STUDY_STATUS_SORT_ORDER,S.NCT_ID desc";

		EntityManager em = emf.createEntityManager();

		if (criteria != null) {
			criteria = criteria.toLowerCase();
			if (criteria.contains("alzheimer's") || criteria.contains("alzeheimer's") || criteria.contains("alzeheimers")) {
				criteria = "alzheimer";
			} 
			else if (criteria.contains("'s")) {
				criteria = criteria.replace("'s", "");
			}
		}

		try {

			Query query = em.createNativeQuery(nativeQuery, SearchResultView.class);
			if (status != null) {
				query.setParameter("status", status);
			}
			
			logger.info(" search criteria going in: " + String.format("%%%s%%", criteria));
			query.setParameter("searchCriteria", String.format("%%%s%%", criteria));
			
			HibernateLogUtil.turnOnSqlLogging();
			List<SearchResultView> searchResults = (List<SearchResultView>) query.getResultList();
			HibernateLogUtil.turnOffSqlLogging();
			
			logger.info("Search Criteria: " + criteria + " -- " + searchResults.size() + " Results Returned.");

			return searchResults;

		}

		catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}

		finally {
			em.close();
		}

	}

	@GET
	@Path("/query")
	@JSONP(callback = "searchResults")
	@Produces("application/javascript")
	public List<SearchResultView> getNct(@QueryParam("callback") String callback, @QueryParam("nctId") String nctId,
			@QueryParam("category") String category, @QueryParam("condition") String condition,
			@QueryParam("status") String status) throws Exception {

		logger.info("GET request for path: /api/trials/query");

		EntityManager em = emf.createEntityManager();

		try {

			List<String> filters = new ArrayList<String>();

			if (nctId != null) {
				filters.add("lower(s.primaryKey.nctId) = :nctId");
			}

			if (category != null) {
				filters.add(
						"(lower(s.primaryCategory) like :primaryCategory or lower(s.secondaryCategory) like :secondaryCategory)");
			}

			if (condition != null) {
				filters.add("lower(condition) like :condition");
			}

			if (status != null) {
				status = status.toLowerCase();
				filters.add("lower(s.studyStatus) like :status");
			}

			String whereClause = "";
			if (!filters.isEmpty()) {
				whereClause = " where " + StringUtils.join(filters, " and ");
			} else {
				throw new IllegalArgumentException();
			}

			Query query = em.createQuery("select s from SearchResultView s " + whereClause);

			if (nctId != null) {
				query.setParameter("nctId", nctId.toLowerCase());
			}

			if (category != null) {
				category = category.toLowerCase();
				query.setParameter("primaryCategory", "%" + category + "%");
				query.setParameter("secondaryCategory", "%" + category + "%");
			}

			if (condition != null) {
				query.setParameter("condition", "%" + condition.toLowerCase() + "%");
			}

			if (status != null) {
				query.setParameter("status", "%" + status.toLowerCase() + "%");
			}

			List<SearchResultView> searchResults = query.getResultList();

			return searchResults;
		} catch (IllegalArgumentException e) {
			logger.error(e);
			logger.error(
					"/query endpoint requires one (or more) of the 4 parameters (case sensetive): nctId,category,condition,status");
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		} finally {
			em.close();
		}
	}

	@GET
	@Path("/total")
	@JSONP(callback = "callback")
	@Produces("application/javascript")
	public String getTotal(@QueryParam("callback") String callback, @QueryParam("status") String status)
			throws Exception {

		logger.info("GET request for path: /api/trials/total");

		EntityManager em = emf.createEntityManager();

		try {

			String whereClause = "";

			if (status != null) {
				status = status.toLowerCase();
				whereClause = "where lower(s.studyStatus) = :status";
			}

			Query query = em.createQuery("select count(s.primaryKey.nctId) from SearchResultView s " + whereClause);

			if (status != null) {
				status = status.toLowerCase();
				query.setParameter("status", status);
			}

			Long count = (Long) query.getSingleResult();

			return "{\"total\":\"" + count + "\"}";
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		} finally {
			em.close();
		}
	}

	@GET
	@Path("/categories")
	@JSONP(callback = "categories")
	@Produces("application/javascript")
	public List<TrialCategory> getCategories(@QueryParam("callback") String callback) throws Exception {

		logger.info("GET request for path: /api/trials/categories");

		EntityManager em = emf.createEntityManager();

		try {
			return em.createQuery("select c from TrialCategory c ", TrialCategory.class).getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		} finally {
			em.close();
		}
	}

	@GET
	@Path("/detail")
	@JSONP
	@Produces("application/javascript")
	public TrialDetail getStudyDetail(@QueryParam("nctId") String nctId, @QueryParam("studyId") Integer studyId) {
		nctId = Objects.requireNonNull(nctId);
		studyId = Objects.requireNonNull(studyId);
		logger.info("GET request for path: /api/trials/detail");

		EntityManager em = emf.createEntityManager();

		try {

			Query query = em.createQuery(
					"select distinct s from SearchResultView s where lower(s.primaryKey.nctId) = :nctId and s.primaryKey.studyId = :studyId");

			nctId = nctId.toLowerCase();
			query.setParameter("nctId", nctId);
			query.setParameter("studyId", studyId);

			SearchResultView searchResult = (SearchResultView) query.getSingleResult();

			TrialDetail detailResult = new TrialDetail(searchResult);

			query = em.createQuery("select distinct i from ClinicalTrialIntervention i where lower(i.nctId) = :nctId");

			nctId = nctId.toLowerCase();
			query.setParameter("nctId", nctId);

			List<ClinicalTrialIntervention> interventions = query.getResultList();

			if (interventions != null) {
				detailResult.setInterventions(interventions);
			}

			query = em.createQuery("select distinct crc from ErmsStudyCRC crc where crc.studyId = :studyId");
			query.setParameter("studyId", studyId);


			List<ErmsStudyCRC> clinicalResearchCoordinators = query.getResultList();

			if (clinicalResearchCoordinators != null) {
				detailResult.setClinicalResearchCoordinators(clinicalResearchCoordinators);
			}

			return detailResult;
		} catch (NoResultException e) {
			logger.error(e);
			logger.error("Either the nctId or the studyId (or the passed in combination) does not exist in the database.");
			return null;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		} finally {
			em.close();
		}

	}

	@GET
	@Path("/test")
	@Produces("text/html")
	public String getTest() {
		logger.info("GET request for path: /api/trials/text");
		return "<html class='dark'>" + "<h1>" + "<center>"
				+ "<link rel='stylesheet' type='text/css' href='../../style.css'>" + "<center>" + "<div class='fun'>"
				+ "<span>" + "<center>This is the ClinicalTrials Test Page...or is it?</center>" + "</span>" + "</div>"
				+ "</html>";
	}

	@GET
	@Path("/help")
	@Produces("text/html")
	public String getHelp() {
		return "<html class='dark'>"
				// + "<center>"
				// + "<link rel='stylesheet' type='text/css'
				// href='../../style.css'>"
				+ "<h1>Commands accepted by this service:<br>"
				// + "<center>"
				+ "<div class='fun.left'>" + "<table>" + "<tr>" + "<th>URL" + "</th>" + "<th>Parameters" + "</th>"
				+ "</tr>" + " <tr>" + " <td>/api/trials/test" + " </td>" + " <td>Prints alive page" + " </td>"
				+ " </tr>" + " <tr>" + " <td>/api/trials/search" + " </td>"
				+ " <td>(criteria,&nbsp;nctId,&nbspcategory,&nbsp;condition,&nbsp;status)" + " </td>" + " </tr>"
				+ " <tr>" + " <td>/api/trials/total" + " </td>" + " <td>(status)" + " </td>" + " </tr>" + " <tr>"
				+ " <td>/api/trials/categories" + " </td>" + " <td>" + " </td>" + " </tr>" + " <tr>"
				+ " <td>/api/trials/detail" + " </td>" + " <td>(nctId,&nbsp;studyId)" + " </td>" + " </tr>" + " <tr>"
				+ " <td>/api/trials/help" + " </td>" + " <td>Shows help page (this one)" + " </td>" + " </tr>"
				+ " <td>/api/trials/statusfilters" + " </td>"
				+ " <td>Shows all status codes and their current display statii" + " </td>" + " </tr>"
				+ " <td>/api/trials/editStatusFilter/{system}/{code}/{display} <<PUT>>" + " </td>"
				+ " <td>Allows user to change display setting for a status code originating on a given system (CT or ERMS)"
				+ " </td>" + " </tr>"

				+ "</table>" + "</div>" + "</html>";
	}

	@GET
	@Path("/statusfilters")
	@Produces("text/html")
	public String getResults() throws Exception {

		logger.info("GET request for path: /api/trials/statusfilters");

		String statusClause = "";
		StringBuilder output = new StringBuilder();

		output.append("<html class='dark'>\n");
		// output.append("<link rel='stylesheet' type='text/css'
		// href='../../style.css'>");
		output.append("<h1>Current Status Filter Settings\n");
		output.append("<div class='fun.left'>");
		output.append("<table>\n");

		String nativeQuery = "SELECT S.* " + "FROM STUDY_STATUS_FILTER S "
				+ "ORDER BY S.STATUS_SYSTEM, S.STATUS_CODE ASC";

		EntityManager em = emf.createEntityManager();

		output.append("<tr>\n");
		output.append("<th>System\n");
		output.append("</th>\n");
		output.append("<th>Status Code\n");
		output.append("</th>\n");
		output.append("<th>Display?\n");
		output.append("</th>\n");
		output.append("</tr>\n");

		try {
			em.getTransaction().begin();
			List<StatusFilter> searchResults = (List<StatusFilter>) em
					.createNativeQuery(nativeQuery, edu.emory.clinical.trials.webapp.server.entity.StatusFilter.class)
					.getResultList();
			for (StatusFilter sf : searchResults) {
				output.append("<tr>\n");
				output.append("<td>" + sf.getStatusSystem() + "\n");
				output.append("</td>\n");
				output.append("<td>" + sf.getStatusCode() + "\n");
				output.append("</td>\n");
				output.append("<td>" + sf.getStatusDisplay() + "\n");
				output.append("</td>\n");
				output.append("</tr>\n");
			}
			em.getTransaction().commit();
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		}

		finally {
			em.close();
		}

		output.append("</table>\n");
		output.append("</div");
		output.append("</html>\n");
		return output.toString();
	}

	@PUT
	@Path("/editStatusFilter/{system}/{code}/{display}")
	// @Produces(MediaType.APPLICATION_JSON)
	@Produces("text/html")
	public String editStatusFilter(@PathParam("system") String system, @PathParam("code") String code,
			@PathParam("display") String display) throws Exception {

		logger.info("PUT for path: /api/trials/editStatusFilter/" + system + "/" + code + "/" + display);

		EntityManager em = emf.createEntityManager();
		String message = null;
		String queryStr = "select s from StatusFilter s where s.primaryKey.statusSystem=:statusSystem "
				+ "and s.primaryKey.statusCode=:statusCode";
		StatusFilter searchResult = null;
		try {
			Query q = em.createQuery(queryStr, StatusFilter.class);
			q.setParameter("statusSystem", system);
			q.setParameter("statusCode", code);
			searchResult = (StatusFilter) q.getSingleResult();
			if (searchResult != null) {
				if (display != null && display.trim().length() > 0) {
					if (display.trim().toUpperCase().startsWith("Y")) {
						searchResult.setStatusDisplay("Y");
					} else {
						searchResult.setStatusDisplay("N");
					}
				}
				em.getTransaction().begin();
				message = "\nSetting display flag to " + searchResult.getStatusDisplay();
				em.merge(searchResult);
				em.flush();
				em.getTransaction().commit();
			}
		} catch (NoResultException e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			message = "Either the system or the code (or the passed in combination) does not exist in the database.";
			logger.error(e);
			logger.error(message);
			throw new Exception(message);
		} catch (IllegalArgumentException e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			message = "System and code parameters are required for the API /editStatusFilter endpoint";
			logger.error(e);
			logger.error(message);
			throw new Exception(message);
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			logger.error(e.getMessage(), e);
			throw new Exception(e);
		} finally {
			em.close();
			if (message == null) {
				message = "ok";
			}
		}
		return message;
	}

	@GET
	@Path("/ctupdate")
	@Produces("text/html")
	public String getCtUpdate() throws Exception {

		logger.info("GET request for path: /api/trials/ctupdate");

		String statusClause = "";
		StringBuilder output = new StringBuilder();

		output.append("<html class='dark'>\n");
		// output.append("<link rel='stylesheet' type='text/css'
		// href='../../style.css'>");
		output.append("Starting CT Update");
		output.append("<br><div class='fun.left'>");
		ExecutorService es = Executors.newSingleThreadExecutor();
		Runnable updateTask = new ClinicalTrialsDataExtractRunner();
		es.submit(updateTask);
		output.append("<br>Started at "+new Date()+"</div>");
		output.append("</html>\n");
		return output.toString();
	}

}
