<%@ page import="model.Article" %>
<%@ page import="dao.ArticleDao" %>
<%@ page import="java.sql.SQLException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    private ArticleDao articleDao = new ArticleDao();
%>

<%
    String idStr = request.getParameter("id");
    if (idStr == null || idStr.isEmpty()) {
        response.sendError(400, "必须带有文章 id");
        return;
    }

    int id;
    try {
        id = Integer.parseInt(idStr);
    } catch (NumberFormatException e) {
        response.sendError(400, "文章 id 必须是数字");
        return;
    }

    Article article;
    try {
        article = articleDao.getArticleById(id);
    } catch (SQLException e) {
        e.printStackTrace();
        response.sendError(500, e.getMessage());
        return;
    }

    if (article == null) {
        response.sendError(404, "没有这篇文章");
        return;
    }
%>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= article.getTitle() %></title>
</head>
<body>
<h1><%= article.getTitle() %></h1>
<p><%= article.getAuthor().getNickname() %></p>
<p><%= article.getContent() %></p>
</body>
</html>
