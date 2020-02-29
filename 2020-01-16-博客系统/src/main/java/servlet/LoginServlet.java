package servlet;

import dao.UserDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String username = req.getParameter("username");
        if (username == null || username.isEmpty()) {
            System.out.println("用户名不能为空");
            resp.sendRedirect("/login.html");
            return;
        }
        String password = req.getParameter("password");
        if (password == null || password.isEmpty()) {
            System.out.println("密码不能为空");
            resp.sendRedirect("/login.html");
            return;
        }

        User user = null;
        try {
            user = userDao.findUserByUsernameAndPassword(username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(500, e.getMessage());
        }
        if (user == null) {
            System.out.println("没有找到用户");
            resp.sendRedirect("/login.html");
            return;
        }

        // 设置用户 session
        HttpSession session = req.getSession();
        session.setAttribute("user", user);

        // 跳转页面 或者 提示用户注册成功
        resp.sendRedirect("/profile");
    }
}
