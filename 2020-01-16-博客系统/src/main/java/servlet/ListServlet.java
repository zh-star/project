package servlet;

import com.alibaba.fastjson.JSON;
import dao.ArticleDao;
import model.Article;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/list")
public class ListServlet extends HttpServlet {
    private ArticleDao articleDao = new ArticleDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        //resp.setContentType("text/json");
        resp.setCharacterEncoding("UTF-8");

        List<Article> articleList;
        try {
            articleList = articleDao.getArticleList();
        } catch (SQLException e) {
            String s = JSON.toJSONString(e.getMessage());
            resp.getWriter().print(s);
            return;
        }

        String s = JSON.toJSONString(articleList);
        System.out.println(s);
        resp.getWriter().print(s);
    }
}
