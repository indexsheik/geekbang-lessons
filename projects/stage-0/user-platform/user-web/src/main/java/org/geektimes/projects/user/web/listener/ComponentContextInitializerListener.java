package org.geektimes.projects.user.web.listener;

import org.geektimes.projects.user.context.ComponentContext;

import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 组件上下文初始化监听器
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/5 11:05
 * @since: 1.0.0
 */
public class ComponentContextInitializerListener implements ServletContextListener {

    private static Logger logger = Logger.getLogger(ComponentContextInitializerListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        try {
            ComponentContext componentContext = new ComponentContext();
            componentContext.initContext(servletContext);
        } catch (NamingException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ComponentContext instance = ComponentContext.getInstance();
        instance.destroy();
    }
}
