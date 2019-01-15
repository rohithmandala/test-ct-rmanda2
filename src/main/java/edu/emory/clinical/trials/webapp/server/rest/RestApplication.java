package edu.emory.clinical.trials.webapp.server.rest;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RestApplication extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApplication.class);
    
    public RestApplication() throws InstantiationException {
        RestServerConfigurator.init();
        LOGGER.info("ClinicalTrials API initialized");
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(ClinicalTrialsRestService.class);
        classes.add(SmokeTest.class);
        return classes;
    }
}
