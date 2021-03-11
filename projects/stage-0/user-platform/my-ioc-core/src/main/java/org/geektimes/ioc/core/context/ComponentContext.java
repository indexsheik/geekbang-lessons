package org.geektimes.ioc.core.context;

import org.geektimes.ioc.core.function.ThrowableFunction;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.naming.*;
import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * -> DefaultListableBeanFactory
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/5 11:12
 * @since: 1.0.0
 */
public class ComponentContext {

    private Context envContext;

    private static ServletContext servletContext;

    private ClassLoader classLoader;

    private static final String COMPONENT_CONTEXT_NAME = ComponentContext.class.getName();

    private static final String COMPONENT_ENV_CONTEXT_NAME = "java:comp/env";

    private static Logger logger = Logger.getLogger(ComponentContext.class.getName());

    private Map<String, Object> singletonMap = new HashMap<>();

    public void initContext(ServletContext servletContext) {
        ComponentContext.servletContext = servletContext;
        servletContext.setAttribute(COMPONENT_CONTEXT_NAME, this);
        this.classLoader = servletContext.getClassLoader();
        initEnvContext();
        instantiateComponents();
        initializeComponents();
    }

    public static ComponentContext getInstance() {
        return (ComponentContext) servletContext.getAttribute(COMPONENT_CONTEXT_NAME);
    }

    /**
     * 通过名称获取 Component
     *
     * @param name
     * @param <T>
     * @return
     */
    public <T> T getComponent(String name) {
        try {
            return (T) envContext.lookup(name);
        } catch (NamingException e) {
            throw new NoSuchElementException(name);
        }
    }

    /**
     * 通过类型获取 Component
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> List<T> getComponent(Class<T> clazz) {
        return singletonMap.values().stream()
                .filter(obj -> clazz.isAssignableFrom(obj.getClass()))
                .map(obj -> (T) obj)
                .collect(Collectors.toList());
    }

    protected <T> T lookupComponent(String name) {
        return executeInContext(envContext -> (T) envContext.lookup(name));
    }

    /**
     * 初始化 Env 上下文
     */
    private void initEnvContext() {
        if (envContext != null) {
            return;
        }
        Context context = null;
        try {
            context = new InitialContext();
            envContext = (Context) context.lookup(COMPONENT_ENV_CONTEXT_NAME);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        } finally {
            close(context);
        }
    }

    /**
     * 预实例化 Component
     */
    private void instantiateComponents() {
        List<String> componentNameList = listAllComponentName();
        componentNameList.forEach(name -> singletonMap.put(name, lookupComponent(name)));
    }

    private List<String> listAllComponentName() {
        return listComponentName("/");
    }

    private List<String> listComponentName(String name) {
        return executeInContext(context -> {
            NamingEnumeration<NameClassPair> e = executeInContext(context,
                    cxt -> cxt.list(name), true);

            if (e == null) {
                return Collections.emptyList();
            }

            List<String> fullNames = new LinkedList<>();
            while (e.hasMoreElements()) {
                NameClassPair next = e.next();
                String className = next.getClassName();
                Class<?> targetClass = classLoader.loadClass(className);
                if (Context.class.isAssignableFrom(targetClass)) {
                    fullNames.addAll(listComponentName(next.getName()));
                } else {
                    String fullName = name.startsWith("/") ?
                            next.getName() : name + "/" + next.getName();
                    fullNames.add(fullName);
                }
            }
            return fullNames;
        });
    }

    /**
     * 初始化 Component
     */
    private void initializeComponents() {
        singletonMap.values().forEach(component -> {
            Class componentClass = component.getClass();
            // 注入
            injectComponent(component, componentClass);
            // 初始化
            initComponent(component, componentClass);
        });
    }

    private void injectComponent(Object component, Class componentClass) {
        Field[] fields = componentClass.getDeclaredFields();
        Stream.of(fields).filter(field -> {
            int modifiers = field.getModifiers();
            return !Modifier.isStatic(modifiers) &&
                    field.isAnnotationPresent(Resource.class);
        }).forEach(field -> {
            Resource annotation = field.getAnnotation(Resource.class);
            String componentName = annotation.name();
            Object obj = singletonMap.get(componentName);
            field.setAccessible(true);
            try {
                field.set(component, obj);
            } catch (IllegalAccessException e) {
            }
        });
    }

    private void initComponent(Object component, Class componentClass) {
        Stream.of(componentClass.getMethods()).filter(method -> {
            int modifiers = method.getModifiers();
            return !Modifier.isStatic(modifiers) && method.isAnnotationPresent(PostConstruct.class);
        }).forEach(method -> {
            try {
                method.invoke(component);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    protected <R> R executeInContext(ThrowableFunction<Context, R> function) {
        return executeInContext(function, false);
    }

    protected <R> R executeInContext(ThrowableFunction<Context, R> function,
                                     boolean ignoredThrowable) {
        return executeInContext(this.envContext, function, ignoredThrowable);
    }

    private <R> R executeInContext(Context context, ThrowableFunction<Context, R> function,
                                   boolean ignoredThrowable) {
        R result = null;
        try {
            result = ThrowableFunction.execute(context, function);
        } catch (Throwable e) {
            if (ignoredThrowable) {
                logger.warning(e.getMessage());
            } else {
                throw e;
            }
        }
        return result;
    }

    /**
     * 销毁 Env 上下文
     */
    public void destroy() {
        close(envContext);
    }

    /**
     * 关闭上下文
     *
     * @param context
     */
    private void close(Context context) {
        if (context != null) {
            try {
                context.close();
            } catch (NamingException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
