package org.geektimes.ioc.core.listener;

import org.geektimes.ioc.core.context.ComponentContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 组件上下文初始化监听器
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/5 11:05
 * @since: 1.0.0
 */
public class ComponentContextInitializerListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ComponentContext componentContext = new ComponentContext();
        componentContext.initContext(servletContext);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ComponentContext instance = ComponentContext.getInstance();
        instance.destroy();
    }
}
