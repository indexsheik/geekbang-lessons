package org.geektimes.projects.user.util;

import org.geektimes.ioc.core.context.ComponentContext;

import java.lang.reflect.Method;

/**
 * JNDI 符号解析
 *
 * @Author: Xiao Xuezhi
 * @Date: 2021/3/10 23:38
 * @Version: 1.0
 */
public class JNDISymbolParserUtils {

    public static Object parse(String str) {
        if (!str.startsWith("@")) {
            return str;
        }
        String classPrefix = str.substring(1);
        if (classPrefix.contains("#")) {
            String[] split = classPrefix.split("#");
            String typeName = split[0];
            String methodName = split[1];

            Object component = ComponentContext.getInstance().getComponent(typeName);
            try {
                Method method = component.getClass().getDeclaredMethod(methodName);
                return method.invoke(component);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // TODO 其他情况暂时忽略
            return ComponentContext.getInstance().getComponent(classPrefix);
        }
    }
}
