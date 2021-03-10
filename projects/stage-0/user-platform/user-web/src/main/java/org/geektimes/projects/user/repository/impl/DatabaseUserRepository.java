package org.geektimes.projects.user.repository.impl;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.UserRepository;
import org.geektimes.projects.user.sql.DBQueryManager;
import org.geektimes.projects.user.sql.DBResultMapManager;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseUserRepository implements UserRepository {

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    @Resource(name = "bean/DBQueryManager")
    private DBQueryManager dbQueryManager;

    /**
     * 保存
     *
     * @param user
     * @return
     */
    @Override
    public boolean save(User user) {
        return dbQueryManager.execute(
                "INSERT INTO users(name,password,email,phoneNumber) VALUES(?,?,?,?) ",
                COMMON_EXCEPTION_HANDLER,
                user.getName(), user.getPassword(),
                user.getEmail(), user.getPhoneNumber());
    }

    /**
     * 根据 id 删除
     *
     * @param userId
     * @return
     */
    @Override
    public boolean deleteById(Long userId) {
        return dbQueryManager.execute(
                "DELETE FROM users WHERE id = ?",
                COMMON_EXCEPTION_HANDLER, userId);
    }

    /**
     * 更新
     *
     * @param user
     * @return
     */
    @Override
    public boolean update(User user) {
        return dbQueryManager.execute(
                "UPDATE users SET name=?,password=?,email=?,phoneNumber=? WHERE id=?",
                COMMON_EXCEPTION_HANDLER,
                user.getName(), user.getPassword(),
                user.getEmail(), user.getPhoneNumber(),
                user.getId());
    }

    /**
     * 根据 id 获取
     *
     * @param userId
     * @return
     */
    @Override
    public User getById(Long userId) {
        return dbQueryManager.executeQuery(
                "SELECT id,name,password,email,phoneNumber FROM users WHERE id = ?",
                resultSet -> {
                    resultSet.next();
                    return DBResultMapManager.map(resultSet, User.class);
                },
                COMMON_EXCEPTION_HANDLER, userId);
    }

    /**
     * 根据用户名和密码获取用户
     *
     * @param userName
     * @param password
     * @return
     */
    @Override
    public User getByNameAndPassword(String userName, String password) {
        return dbQueryManager.executeQuery(
                "SELECT id,name,password,email,phoneNumber FROM users WHERE name = ? AND password = ?",
                resultSet -> {
                    resultSet.next();
                    return DBResultMapManager.map(resultSet, User.class);
                },
                COMMON_EXCEPTION_HANDLER, userName, password);
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public Collection<User> getAll() {
        return dbQueryManager.executeQuery("SELECT id,name,password,email,phoneNumber FROM users",
                resultSet -> {
                    List<User> list = new ArrayList<>();
                    while (resultSet.next()) {
                        list.add(DBResultMapManager.map(resultSet, User.class));
                    }
                    return list;
                }, COMMON_EXCEPTION_HANDLER);
    }


}
