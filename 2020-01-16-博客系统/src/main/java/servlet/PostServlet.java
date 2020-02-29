package servlet;

import dao.ArticleDao;
import model.Article;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/post")
public class PostServlet extends HttpServlet {
    private ArticleDao articleDao = new ArticleDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String title = req.getParameter("title");
        String content = req.getParameter("content");
        HttpSession session = req.getSession();
        // 首先得确保当前有用户已经登陆了
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("/login.html");
            return;
        }

        Article article;
        try {
            article = articleDao.publishArticle(user, title, content);
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendError(500, e.getMessage());
            return;
        }

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().println("<h1>发布成功</h1>");
        resp.getWriter().println(article);
    }
}
