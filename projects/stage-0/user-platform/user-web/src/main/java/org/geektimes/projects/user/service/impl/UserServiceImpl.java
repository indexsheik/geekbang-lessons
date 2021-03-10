package org.geektimes.projects.user.service.impl;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.UserRepository;
import org.geektimes.projects.user.service.UserService;

import javax.annotation.Resource;

/**
 * 用户服务
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/3 10:17
 * @since: 1.0.0
 */
public class UserServiceImpl implements UserService {

    @Resource(name = "bean/DatabaseUserRepository")
    private UserRepository userRepository;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    public boolean register(User user) {
        return userRepository.save(user);
    }

    /**
     * 登录
     *
     * @param name
     * @param password
     * @return
     */
    @Override
    public User login(String name, String password) {
        return userRepository.getByNameAndPassword(name, password);
    }
}
