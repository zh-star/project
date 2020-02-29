package dao;

import model.Article;
import model.User;
import util.DB;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ArticleDao {
    public Article publishArticle(User user, String title, String content) throws SQLException {
        Date now = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sql = "INSERT INTO articles (author_id, title, content, published_at) VALUES (?, ?, ?, ?)";

        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setInt(1, user.getId());
                statement.setString(2, title);
                statement.setString(3, content);
                statement.setString(4, dateFormat.format(now));

                statement.executeUpdate();
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    int id = rs.getInt(1);

                    return new Article(id, user, title, content, now);
                }
            }
        }
    }

    public static void main(String[] args) throws SQLException {
        ArticleDao articleDao = new ArticleDao();
        articleDao.publishArticle(null, null, null);
    }

    public Article getArticleById(int id) throws SQLException {
        String sql = "SELECT articles.id, articles.title, articles.content, users.id, users.nickname, users.username FROM articles, users WHERE articles.author_id = users.id AND articles.id = ?";
        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    int articleId = rs.getInt(1);
                    String title = rs.getString(2);
                    String content = rs.getString(3);
                    int userId = rs.getInt(4);
                    String nickname = rs.getString(5);
                    String username = rs.getString(6);

                    User author = new User(userId, username, nickname);
                    Article article = new Article(articleId, author, title, content, null);
                    return article;
                }
            }
        }
    }

    public List<Article> getArticleList() throws SQLException {
        List<Article> articleList = new ArrayList<>();
        try (Connection connection = DB.getConnection()) {
            String sql = "SELECT " +
                    "a.id, a.title, a.content, " +
                    "u.id, u.username, u.nickname " +
                    "FROM articles a, users u " +
                    "WHERE a.author_id = u.id " +
                    "ORDER BY a.published_at DESC";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int articleId = rs.getInt(1);
                        String title = rs.getString(2);
                        String content = rs.getString(3);
                        int userId = rs.getInt(4);
                        String username = rs.getString(5);
                        String nickname = rs.getString(6);

                        User author = new User(userId, username, nickname);
                        Article article = new Article(articleId, author, title, content, null);

                        articleList.add(article);
                    }
                }
            }
        }

        return articleList;
    }
}
