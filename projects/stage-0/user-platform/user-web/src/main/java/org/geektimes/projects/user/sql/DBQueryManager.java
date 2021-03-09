package org.geektimes.projects.user.sql;

import org.apache.commons.lang.ClassUtils;
import org.geektimes.function.ThrowableFunction;
import org.geektimes.projects.user.context.ComponentContext;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * DB 执行语句
 *
 * @Author: Xiao Xuezhi
 * @Date: 2021/3/3 21:25
 * @Version: 1.0
 */
public class DBQueryManager {

    private Connection connection;

    public DBQueryManager() {
        DBConnectionManager dbConnectionManager = ComponentContext.getInstance()
                .getComponent("bean/DBConnectionManager");
        connection = dbConnectionManager.getConnection();
//        connection = DBConnectionManager.getInstance().getConnection();
    }

    public boolean execute(String sql, Consumer<Throwable> consumer, Object... args) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class<?> argType = arg.getClass();
                // 获取原生类型
                Class wrapperType = ClassUtils.wrapperToPrimitive(argType);

                if (wrapperType == null) {
                    wrapperType = argType;
                }

                // 获取 PreparedStatement Set 参数方法
                String methodName = preparedStatementMethodMappings.get(argType);
                // 设置参数
                Method method = PreparedStatement.class.getMethod(methodName, int.class, wrapperType);
                method.invoke(preparedStatement, i + 1, arg);
            }
            preparedStatement.execute();
            return true;
        } catch (Throwable e) {
            consumer.accept(e);
        }
        return false;
    }

    public <R> R executeQuery(String sql, ThrowableFunction<ResultSet, R> function,
                              Consumer<Throwable> consumer, Object... args) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class<?> argType = arg.getClass();
                // 获取原生类型
                Class wrapperType = ClassUtils.wrapperToPrimitive(argType);

                if (wrapperType == null) {
                    wrapperType = argType;
                }

                // 获取 PreparedStatement Set 参数方法
                String methodName = preparedStatementMethodMappings.get(argType);
                // 设置参数
                Method method = PreparedStatement.class.getMethod(methodName, int.class, wrapperType);
                method.invoke(preparedStatement, i + 1, arg);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            return function.apply(resultSet);
        } catch (Throwable e) {
            consumer.accept(e);
        }
        return null;
    }

    private static Map<Class<?>, String> preparedStatementMethodMappings = new HashMap<>();

    static {
        preparedStatementMethodMappings.put(Long.class, "setLong");
        preparedStatementMethodMappings.put(String.class, "setString");
    }
}
