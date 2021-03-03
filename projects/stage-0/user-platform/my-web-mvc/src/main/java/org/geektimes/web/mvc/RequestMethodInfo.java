package org.geektimes.web.mvc;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * 请求方法信息
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/2/28 13:03
 * @since: 1.0.0
 */
public class RequestMethodInfo {

    private String path;

    private Method method;

    private Set<String> supportedHttpMethods;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Set<String> getSupportedHttpMethods() {
        return supportedHttpMethods;
    }

    public void setSupportedHttpMethods(Set<String> supportedHttpMethods) {
        this.supportedHttpMethods = supportedHttpMethods;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public RequestMethodInfo() {

    }

    public RequestMethodInfo(String path, Method method, Set<String> supportedHttpMethods) {
        this.path = path;
        this.method = method;
        this.supportedHttpMethods = supportedHttpMethods;
    }
}
