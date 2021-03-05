package org.geektimes.projects.user.context;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import java.util.NoSuchElementException;

/**
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/5 11:12
 * @since: 1.0.0
 */
public class ComponentContext {

    private Context componentContext;

    private static ServletContext servletContext;

    private static final String COMPONENT_CONTEXT_NAME = ComponentContext.class.getName();

    public void initContext(ServletContext servletContext) throws NamingException {
        ComponentContext.servletContext = servletContext;
        componentContext = (Context) new InitialContext().lookup("java:comp/env");
        servletContext.setAttribute(COMPONENT_CONTEXT_NAME, this);
    }

    public static ComponentContext getInstance() {
        return (ComponentContext) servletContext.getAttribute(COMPONENT_CONTEXT_NAME);
    }

    public <T> T getComponent(String name) {
        try {
            return (T) componentContext.lookup(name);
        } catch (NamingException e) {
            throw new NoSuchElementException(name);
        }
    }

    public void destroy() {
        if (componentContext != null) {
            try {
                componentContext.close();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
