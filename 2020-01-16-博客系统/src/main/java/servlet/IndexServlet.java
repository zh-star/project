package servlet;

import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/profile")
public class IndexServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter writer = resp.getWriter();

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            writer.println("<h1>用户未登录</h1>");
        } else {
            writer.printf("<h1>%s</h1>%n", user.getNickname());
            writer.printf("<h2>%d</h2>%n", user.getId());
            writer.printf("<h3>%s</h3>%n", user.getUsername());
        }
    }
}
