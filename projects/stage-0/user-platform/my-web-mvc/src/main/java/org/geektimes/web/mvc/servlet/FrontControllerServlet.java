package org.geektimes.web.mvc.servlet;

import org.apache.commons.lang.StringUtils;
import org.geektimes.web.mvc.RequestMethodInfo;
import org.geektimes.web.mvc.controller.Controller;
import org.geektimes.web.mvc.controller.PageController;
import org.geektimes.web.mvc.controller.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 前端总控制器
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/2/28 11:37
 * @since: 1.0.0
 */
public class FrontControllerServlet extends HttpServlet {

    private Map<String, Controller> controllerMap = new HashMap<>();

    private Map<String, RequestMethodInfo> requestMethodInfoMap = new HashMap<>();

    @Override
    public void init(ServletConfig config) throws ServletException {
        ServletContext servletContext = config.getServletContext();
        servletContext.log("FrontControllerServlet init starting.");
        ServiceLoader<Controller> serviceLoader = ServiceLoader.load(Controller.class);
        for (Controller controller : serviceLoader) {
            String requestUrl = "";
            // 获取 Controller RequestMapping
            Path pathFormClass = controller.getClass().getAnnotation(Path.class);
            if (pathFormClass != null) {
                requestUrl = pathFormClass.value();
            }
            requestUrl = resolveUrl(requestUrl);
            Method[] declaredMethods = controller.getClass().getDeclaredMethods();
            for (Method method : declaredMethods) {
                // 获取方法支持的 HttpMethod
                Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                if (supportedHttpMethods.isEmpty()) {
                    servletContext.log(String.format("Controller-[%s], Method-[%s] have no HttpMethod.",
                            controller.getClass(), method.getName()));
                    continue;
                }
                String subUrl = "";
                // 获取 Method RequestMapping
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (pathFromMethod != null) {
                    subUrl = pathFromMethod.value();
                    if (StringUtils.isNotBlank(subUrl)) {
                        subUrl = resolveUrl(subUrl);
                    }
                }
                String methodRequestPath = requestUrl + subUrl;
                requestMethodInfoMap.put(methodRequestPath,
                        new RequestMethodInfo(methodRequestPath, method, supportedHttpMethods));
                controllerMap.put(methodRequestPath, controller);
                servletContext.log(String.format("[%s] has been loaded.",
                        methodRequestPath));
            }
        }
    }

    /**
     * 获取方法支持的 HttpMethod
     *
     * @param method
     * @return
     */
    private Set<String> findSupportedHttpMethods(Method method) {
        Set<String> supportedHttpMethods = new HashSet<>();
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            HttpMethod httpMethod = annotation.annotationType().getAnnotation(HttpMethod.class);
            if (httpMethod != null) {
                supportedHttpMethods.add(httpMethod.value());
            }
        }
        return supportedHttpMethods;
    }

    /**
     * 处理 url
     *
     * @param requestUrl
     * @return
     */
    private String resolveUrl(String requestUrl) {
        if (!requestUrl.startsWith("/")) {
            requestUrl = "/" + requestUrl;
        }
        return requestUrl;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext servletContext = request.getServletContext();

        String requestURI = request.getRequestURI();
        servletContext.log(String.format("request url: %s", requestURI));
        String httpMethod = request.getMethod();

        Controller controller = controllerMap.get(requestURI);
        if (controller == null) {
            servletContext.log(String.format("No handler found for %s %s", httpMethod, requestURI));
            // HTTP 404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } else {
            RequestMethodInfo requestMethodInfo = requestMethodInfoMap.get(requestURI);

            try {
                if (!requestMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                    servletContext.log(String.format("Not support method for %s %s", httpMethod, requestURI));
                    // HTTP 405 方法不支持
                    response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                    return;
                }

                if (controller instanceof RestController) {
                    // TODO
                } else if (controller instanceof PageController) {
                    Method method = requestMethodInfo.getMethod();
                    Object result;
                    try {
                        result = method.invoke(controller, request, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        return;
                    }
                    String viewPath = resolveUrl(String.valueOf(result));

                    RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                    requestDispatcher.forward(request, response);
                    return;
                } else {
                    // TODO
                }
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof IOException) {
                    throw (IOException) throwable.getCause();
                } else {
                    throw new ServletException(throwable.getCause());
                }
            }

        }
    }
}
