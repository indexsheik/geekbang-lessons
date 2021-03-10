package org.geektimes.projects.user.web.controller;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserService;
import org.geektimes.web.mvc.controller.PageController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

/**
 * User 控制器
 *
 * @author: Xiao Xuezhi
 * @email: index.xiao@foxmail.com
 * @date: 2021/3/1 10:08
 * @since: 1.0.0
 */
@Path("/user")
public class UserController implements PageController {

    @Resource(name = "bean/UserServiceImpl")
    private UserService userService;

    @GET
    @Path("/index")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        return "index.jsp";
    }

    @POST
    @Path("/register")
    public String register(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");

        User user = new User(name, password, email, phoneNumber);
        boolean register;
        try {
            register = userService.register(user);
        } catch (Exception e) {
            request.getSession().setAttribute("error", e.getMessage());
            register = false;
        }

        return register ? "success.jsp" : "error.jsp";
    }

    @GET
    @Path("/login")
    public String signIn(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User login = userService.login(name, password);
        return login != null ? "success.jsp" : "error.jsp";
    }
}
