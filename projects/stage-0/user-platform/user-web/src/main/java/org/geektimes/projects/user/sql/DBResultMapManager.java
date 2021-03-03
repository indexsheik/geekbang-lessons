package org.geektimes.projects.user.sql;

import org.geektimes.function.ThrowableBiFunction;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * DB 结果集映射
 *
 * @Author: Xiao Xuezhi
 * @Date: 2021/3/3 22:01
 * @Version: 1.0
 */
public class DBResultMapManager {

    public static <T> T map(ResultSet resultSet, Class<T> clazz) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);

        T t = clazz.newInstance();

        for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
            // 属性类
            Class<?> fieldType = propertyDescriptor.getPropertyType();
            // 属性名
            String fieldName = propertyDescriptor.getName();
            // 属性对应列名
            String columnName = mapColumnLabel(fieldName);
            // 对应 ResultSet 的 get 方法
            ThrowableBiFunction<ResultSet, String, ?> function = typeMethodMappings.get(fieldType);
            // 获取数据
            Object execute = function.execute(resultSet, columnName);
            // 写入对象
            Method writeMethod = propertyDescriptor.getWriteMethod();
            writeMethod.invoke(t, execute);
        }

        return t;
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    /**
     * 数据类型与 ResultSet 方法名映射
     */
    static Map<Class<?>, ThrowableBiFunction<ResultSet, String, ?>> typeMethodMappings = new HashMap<>();

    static {
        typeMethodMappings.put(Long.class, ResultSet::getLong);
        typeMethodMappings.put(String.class, ResultSet::getString);
    }
}
