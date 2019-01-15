package edu.emory.clinical.trials.webapp.server.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

public class ClinicalTrialsRestServiceTest extends JerseyTest {
	
	@Test
	public void testTotalWithStatusParam() {
		String total = target("/trials/total").queryParam("status","active").request().get(String.class);
		assertNotNull(total);
	}
	
	@Test
	public void testSearchWithParam() {
		String searchResults = target("/trials/search").queryParam("criteria","cancer").queryParam("status","active").request().get(String.class);
		assertNotNull(searchResults);
	}
	
	@Test
	public void testSearchWithEmptyParam() {
		String searchResults = target("/trials/search").queryParam("criteria","").request().get(String.class);
		assertNotNull(searchResults);
	}
	
	@Test
	public void testQueryWithNCTID() {
		String queryResults = target("/trials/query").queryParam("nctId", "").queryParam("status", "active").request().get(String.class);
		assertNotNull(queryResults);
	}
	
	@Test
	public void testQueryWithCategory() {
		String queryResults = target("/trials/query").queryParam("category", "").request().get(String.class);
		assertNotNull(queryResults);
	}
	
	@Test
	public void testQueryWithCondition() {
		String queryResults = target("/trials/query").queryParam("condition", "").request().get(String.class);
		assertNotNull(queryResults);
	}
	
	@Test
	public void testCategories() {
		String categories = target("/trials/categories").request().get(String.class);
		assertNotNull(categories);
	}
	
	@Test
	public void testDetail() {
		String detail = target("/trials/detail").queryParam("nctId","NCT02438423").queryParam("studyId","7777").request().get(String.class);
		assertNotNull(detail);
	}

	@Test(expected=NullPointerException.class)
	public void testDetailWithNullParams() {
		new ClinicalTrialsRestService().getStudyDetail(null, null);
	}

	@Override
	protected Application configure() {
		return new ResourceConfig(ClinicalTrialsRestService.class);
	}
}