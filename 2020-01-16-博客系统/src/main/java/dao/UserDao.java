package dao;

import model.User;
import util.DB;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDao {
    private static final String secret = "只有你自己知道";

    public User registerUser(String username, String nickname, String password) throws SQLException {
        password = encrypted(password);

        String sql = "INSERT INTO users (username, nickname, password) VALUES (?, ?, ?)";
        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, username);
                statement.setString(2, nickname);
                statement.setString(3, password);
                statement.executeUpdate();

                try (ResultSet rs = statement.getGeneratedKeys()) {
                    rs.next();
                    int id = rs.getInt(1);

                    return new User(id, username, nickname);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                return null;
            }

            throw e;
        }
    }

    public User findUserByUsernameAndPassword(String username, String password) throws SQLException {
        password = encrypted(password);

        String sql = "SELECT id, nickname FROM users WHERE username = ? AND password = ?";
        try (Connection connection = DB.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, username);
                statement.setString(2, password);
                try (ResultSet rs = statement.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    int id = rs.getInt(1);
                    String nickname = rs.getString(2);
                    return new User(id, username, nickname);
                }
            }
        }
    }

    private String encrypted(String password) {
        password = password + secret;
        // 做 SHA-256(一种Hash)
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] input = password.getBytes("UTF-8");
            byte[] output = messageDigest.digest(input);
            StringBuilder sb = new StringBuilder();
            for (byte b : output) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | IOException e) {
            // 理论上永远不应该抛出
            e.printStackTrace();
            return "";
        }
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao();
        String output = userDao.encrypted("你好世界Hx%4$@_");
        System.out.println(output.length());
        System.out.println(output);
    }
}
