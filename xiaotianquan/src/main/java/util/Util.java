package util;




import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: STAR
 * Date: 2020 -01
 * Time: 16:23
 */
public class Util {
    public static final String DATA_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String[] SIZE_NAME = {"B","KB","MB","GB"};
    private static DateFormat DATA_FORMAT = new SimpleDateFormat(DATA_PATTERN);
    /**
     * 文件大小转换为 KB MB 等等单位
     * @param size
     * @return
     */
    public static String parseSize(Long size) {

        int n = 0;
        while(size >= 1024) {
            size = size/1024;
            n++;
        }

        return  size + SIZE_NAME[n];

    }

    /**
     * 日期解析
     * @param lastModified
     * @return
     */
    public static String parseData(Long lastModified) {

        return DATA_FORMAT.format(new Date(lastModified));
    }
}
