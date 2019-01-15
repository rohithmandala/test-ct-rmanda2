package edu.emory.clinical.trials.webapp.server.util;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Helps with debugging.  Can be used to turn on different aspects of hibernate logging. See the link below for more categories and descriptions
 *
 * http://docs.jboss.org/hibernate/orm/4.3/topical/html/logging/Logging.html
 *
 */
public final class HibernateLogUtil {
	private static final String CATEGORY_ALL = "org.hibernate";
    private static final String CATEGORY_TRANSACTION = "org.hibernate.engine.transaction";
	private static final String CATEGORY_SQL = "org.hibernate.SQL";
	private static final String CATEGORY_JDBC = "org.hibernate.resource.jdbc";

	public static void turnOnAll() {
        Logger.getLogger(CATEGORY_ALL).setLevel(Level.DEBUG);
    }

    public static void turnOffAll() {
        Logger.getLogger(CATEGORY_ALL).setLevel(Level.INFO);
    }

    public static void turnOnSqlLogging() {
        Logger.getLogger(CATEGORY_SQL).setLevel(Level.DEBUG);
    }

    public static void turnOffSqlLogging() {
        LogManager.getLogger(CATEGORY_SQL).setLevel(Level.INFO);
    }

    public static void turnOnJdbcLogging() {
        LogManager.getLogger(CATEGORY_JDBC).setLevel(Level.DEBUG);
    }

    public static void turnOffJdbcLogging() {
        LogManager.getLogger(CATEGORY_JDBC).setLevel(Level.INFO);
    }

    public static void turnOnTransactionLogging() {
        LogManager.getLogger(CATEGORY_TRANSACTION).setLevel(Level.DEBUG);
    }

    public static void turnOffTransactionLogging() {
        LogManager.getLogger(CATEGORY_TRANSACTION).setLevel(Level.INFO);
    }
}
