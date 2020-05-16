package dao;

import app.FileMeta;
import util.DBUtil;
import util.Pinyin4jUtil;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: STAR
 * Date: 2020 -01
 * Time: 9:21
 */
public class FileOperatorDAO {

    public static List<FileMeta> query(String dir_path) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<FileMeta> metas = new ArrayList<>();
        try {
            connection = DBUtil.getConnection();
            String sql = "select name,path,size,Last_modified,is_directory " +
                    "from file_meta where path=?";

            statement = connection.prepareStatement(sql);
            statement.setString(1,dir_path);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {

                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                Long size = resultSet.getLong("size");
                Long Last_modified = resultSet.getLong("Last_modified");
                Boolean is_directory = resultSet.getBoolean("is_directory");

                FileMeta meta =  new FileMeta(name,path,size,Last_modified,is_directory);
                metas.add(meta);
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return metas;
}

    public static void insert(FileMeta localMeta) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            try {
                //1.获取链接
                connection = DBUtil.getConnection();
                String sql = "insert into file_meta" +
                        "(name,path,size,last_modified," +
                        "pinyin,pinyin_fist,is_directory)" +
                        " values (?,?,?,?,?,?,?)";
                //2.获取操作命令对象
                statement = connection.prepareStatement(sql);
                // 填充占位符
                statement.setString(1,localMeta.getName());
                statement.setString(2,localMeta.getPath());
                statement.setLong(3,localMeta.getSize());
                statement.setTimestamp(4,new Timestamp(localMeta.getLastModified()));
                String pinyin = null;
                String pinyin_fist = null;
                //包含中文字符时需要保存全拼和拼音首字母
                if(Pinyin4jUtil.containsChinese(localMeta.getName())) {
                    String[] pinyins = Pinyin4jUtil.get(localMeta.getName());
                    pinyin = pinyins[0];
                    pinyin_fist = pinyins[1];

                }
                statement.setString(5,pinyin);
                statement.setString(6,pinyin_fist);
                statement.setBoolean(7,localMeta.getDirectory());

                System.out.println("inset :" + localMeta.getPath() + File.separator + localMeta.getName());
                //3.执行sql语句
                statement.executeUpdate();
            } finally {
                DBUtil.close(connection,statement);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void delete(FileMeta meta) {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //1.获取链接
            connection = DBUtil.getConnection();
            connection.setAutoCommit(false);
            String sql = "delete from file_meta where name=?" +
                    " and path=? and is_directory =?";
            //2.获取操作命令对象
            statement = connection.prepareStatement(sql);
            // 填充占位符
            statement.setString(1,meta.getName());
            statement.setString(2,meta.getPath());
            statement.setBoolean(3,meta.getDirectory());
            System.out.println("delete :" + meta.getPath() + File.separator + meta.getName());

            statement.executeUpdate();
            //删除子文件夹
            if(meta.getDirectory()) {
                sql = "delete from file_meta where path=? or path like ?";
                statement = connection.prepareStatement(sql);
                String path = meta.getPath() + File.separator+meta.getName();
                statement.setString(1,path);
                statement.setString(2,
                        meta.getPath() + File.separator + meta.getPath()+"%");//File.separator文件分割符  "%" 模糊匹配
                statement.executeUpdate();
            }
            connection.commit();


        } catch (SQLException e) {
            e.printStackTrace();
            if(connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        } finally {
            DBUtil.close(connection,statement);
        }
    }

    public static void main(String[] args){
        delete(new FileMeta("记录","C:\\",0L,0L,true));
    }

    public static List<FileMeta> search(String dir, String text) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<FileMeta> metas = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();
            boolean empty = dir == null || dir.trim().length() == 0;
            String sql = "select name,path,size,Last_modified,is_directory " +
                    "from file_meta where name like ? and pinyin like ?" +
                    " or pinyin_fist like ?"
                    + (empty ? "" : "and (path = ? or path like ?)");

            statement = connection.prepareStatement(sql);
            statement.setString(1,"%" + text + "%");
            statement.setString(2,"%" + text + "%");
            statement.setString(3,"%" + text + "%");
            if(!empty) {
                statement.setString(4,dir);
                statement.setString(5,dir+File.separator+"%");
            }
            System.out.println("search:" + dir +","+"text=" + text);
            resultSet = statement.executeQuery();
            while(resultSet.next()) {
                String name = resultSet.getString("name");
                String path = resultSet.getString("path");
                Long size = resultSet.getLong("size");
                Long Last_modified = resultSet.getLong("Last_modified");
                Boolean is_directory = resultSet.getBoolean("is_directory");

                FileMeta meta =  new FileMeta(name,path,size,Last_modified,is_directory);
                System.out.println("search: " + name + "," + path);
                metas.add(meta);
            }

        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return metas;
    }
}
