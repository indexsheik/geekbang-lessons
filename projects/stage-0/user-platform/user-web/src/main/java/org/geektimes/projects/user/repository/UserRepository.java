package org.geektimes.projects.user.repository;

import org.geektimes.projects.user.domain.User;

import java.util.Collection;

/**
 * 用户存储仓库
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/3 10:16
 * @since: 1.0.0
 */
public interface UserRepository {

    /**
     * 保存
     *
     * @param user
     * @return
     */
    boolean save(User user);

    /**
     * 根据 id 删除
     *
     * @param userId
     * @return
     */
    boolean deleteById(Long userId);

    /**
     * 更新
     *
     * @param user
     * @return
     */
    boolean update(User user);

    /**
     * 根据 id 获取
     *
     * @param userId
     * @return
     */
    User getById(Long userId);

    /**
     * 根据用户名和密码获取用户
     *
     * @param userName
     * @param password
     * @return
     */
    User getByNameAndPassword(String userName, String password);

    /**
     * 查询所有
     *
     * @return
     */
    Collection<User> getAll();
}
