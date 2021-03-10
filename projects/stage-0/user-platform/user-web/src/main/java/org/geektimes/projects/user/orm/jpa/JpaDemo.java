package org.geektimes.projects.user.orm.jpa;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.geektimes.projects.user.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Jpa Demo
 *
 * @Author: Xiao Xuezhi
 * @Date: 2021/3/9 21:39
 * @Version: 1.0
 */
public class JpaDemo {

    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("emf", getProperties());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        User user = new User();
        user.setName("小马哥2");
        user.setPassword("***6***");
        user.setEmail("mercyblitz@gmail.com");
        user.setPhoneNumber("123456789");
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        entityManager.persist(user);
        transaction.commit();

        System.out.println(entityManager.find(User.class, 1L));
    }

    private static Map<String, Object> getProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
        properties.put("hibernate.id.new_generator_mappings", false);
        properties.put("hibernate.connection.datasource", getDataSource());
        return properties;
    }

    private static DataSource getDataSource() {
        EmbeddedDataSource dataSource = new EmbeddedDataSource();
        dataSource.setDatabaseName("/db/user-platform");
        dataSource.setCreateDatabase("create");
        return dataSource;
    }
}
