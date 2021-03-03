package org.geektimes.projects.user.repository.impl;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.UserRepository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 内存型用户存储仓库
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/3 10:20
 * @since: 1.0.0
 */
public class InMemoryUserRepository implements UserRepository {

    private Map<Long, User> userTable = new HashMap<>();

    /**
     * 用户名唯一索引
     */
    private Map<String, Long> userNameIndex = new HashMap<>();

    /**
     * 自增主键
     */
    private AtomicLong incrementId = new AtomicLong(1);

    /**
     * 保存
     *
     * @param user
     * @return
     */
    @Override
    public boolean save(User user) {
        long id = incrementId.getAndIncrement();
        user.setId(id);
        if (userNameIndex.containsKey(user.getName())) {
            return false;
        }
        userTable.put(id, user);
        userNameIndex.put(user.getName(), id);
        return true;
    }

    /**
     * 根据 id 删除
     *
     * @param id
     * @return
     */
    @Override
    public boolean deleteById(Long id) {
        User user = userTable.get(id);
        if (user == null) {
            return false;
        }
        userNameIndex.remove(user.getName());
        userTable.remove(id);
        return true;
    }

    /**
     * 更新
     *
     * @param user
     * @return
     */
    @Override
    public boolean update(User user) {
        Long id = user.getId();
        if (!userTable.containsKey(id)) {
            return false;
        }
        User old = userTable.get(id);
        if (!user.getName().equals(old.getName())) {
            userNameIndex.remove(old.getName());
            userNameIndex.put(user.getName(), id);
        }
        userTable.put(id, user);
        return true;
    }

    /**
     * 根据 id 获取
     *
     * @param id
     * @return
     */
    @Override
    public User getById(Long id) {
        return userTable.get(id);
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
        if (!userNameIndex.containsKey(userName)) {
            return null;
        }
        Long id = userNameIndex.get(userName);
        if (!userTable.containsKey(id)) {
            return null;
        }
        User user = userTable.get(id);
        if (user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * 查询所有
     *
     * @return
     */
    @Override
    public Collection<User> getAll() {
        return userTable.values();
    }
}
