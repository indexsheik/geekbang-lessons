package org.geektimes.projects.user.web.listener;

import org.geektimes.projects.user.sql.DBInitialization;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class DBConnectionInitializerListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(DBConnectionInitializerListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.log(Level.INFO, "ready to init db");
        try {
            DBInitialization.init();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
