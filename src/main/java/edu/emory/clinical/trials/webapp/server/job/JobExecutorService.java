package edu.emory.clinical.trials.webapp.server.job;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.apache.log4j.Logger;

import edu.emory.clinical.trials.webapp.server.ClinicalTrialsDataExtractor;
import edu.emory.clinical.trials.webapp.server.LogUtil;


/**
 * <p>This class registers the data import job (ClinicalTrialsDataExtractor) with the EJB Scheduler Service.</p>
 *
 * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/bnboy.html">Java EE 6 Tutorial : Using the Timer service</a>
 * for increasing your knowledge bits.
 */

@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
@TransactionAttribute(TransactionAttributeType.NEVER)
public class JobExecutorService {

	private Logger logger = Logger.getLogger(JobExecutorService.class.getName());

	@Schedule(second = "0", minute = "00", hour = "1", persistent = false)
	public void execute() {
	    logger.info("in the JobExecutorService");
		try {
            if (new ClinicalTrialsDataExtractor().extract()) {
            	LogUtil.logJobResult("ClinicalTrialsDataExtractJob", true);
            	logger.info("CT Extract Job Completed Successfully.");
            } else {
            	LogUtil.logJobResult("ClinicalTrialsDataExtractJob", false);
            }
        } catch (Exception e) {  
           logger.error("ClinicalTrialsDataExtractJob failure", e);
        }
	}
}
