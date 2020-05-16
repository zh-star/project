package util;


import org.sqlite.SQLiteConfig;
import org.sqlite.SQLiteDataSource;
import javax.sql.DataSource;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: STAR
 * Date: 2020 -01
 * Time: 15:07
 */
public class DBUtil {

    private static volatile DataSource DATA_SOURCE;
    private DBUtil() {

    }
    private static String getUtil() throws URISyntaxException {
        String dbName = "xiaotianquan.db";
        URL url = DBUtil.class.getClassLoader().getResource(".");
        return "jdbc:sqlite://" +new File(url.toURI()).getParent()
                +File.separator+dbName;
    }

    /**
     * 获取数据库连接池
     * @return
     */
    public static DataSource getDataSource() throws URISyntaxException {
        if(DATA_SOURCE == null) {
            synchronized (DBUtil.class) {
                if(DATA_SOURCE == null) {
                    //mysql日期格式：yyy-MM-dd HH:mm:ss
                    //sqlite日期格式：yyy-MM-dd HH:mm:ss:SSS
                    SQLiteConfig config = new SQLiteConfig();
                    config.setDateStringFormat(Util.DATA_PATTERN);
                    DATA_SOURCE = new SQLiteDataSource(config);
                    ((SQLiteDataSource)DATA_SOURCE).setUrl(getUtil());
                }
            }
        }
        return DATA_SOURCE;
    }
    /**
     * 获取数据库连接
     * 1.使用class.forName("驱动名") 加载驱动 DriverManager.getConnection()
     * 2.DataSource  好处 ：复用
     * @return
     */
    public static Connection getConnection(){
        try {
            return getDataSource().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据连接失败");
        }
        //Connection c = new MysqlDataSource().getConnection();
    }

    public static void close(Connection connection, Statement statement,
                             ResultSet resultSet) {
        try {
            if(connection != null) {
                connection.close();
            }
            if(statement != null) {
                statement.close();
            }
            if(resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("释放数据库资源错误");
        }
    }
    public  static void close(Connection connection,Statement statement) {
        close(connection,statement,null);
    }
    public static void main(String[] args) throws URISyntaxException {
        Connection connection = getConnection();
    }
}
