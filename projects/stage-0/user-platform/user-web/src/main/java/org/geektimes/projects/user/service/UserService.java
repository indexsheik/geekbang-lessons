package org.geektimes.projects.user.service;

import org.geektimes.projects.user.domain.User;

/**
 * 用户服务
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/3 10:16
 * @since: 1.0.0
 */
public interface UserService {

    /**
     * 注册
     *
     * @param user
     * @return
     */
    boolean register(User user);

    /**
     * 登录
     *
     * @param name
     * @param password
     * @return
     */
    User login(String name, String password);
}
