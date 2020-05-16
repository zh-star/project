package util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * Description: 汉字转化为拼音
 * User: STAR
 * Date: 2020 -01
 * Time: 10:51
 */
public class Pinyin4jUtil {

    /**
     * 中文字符格式
     */
    private static final String CHINESE_PATTERN = "[\\u4E00-\\u9FA5]";

    //拼音格式化类
    public static final HanyuPinyinOutputFormat FORMAT = new HanyuPinyinOutputFormat();

    /**
     * 输出格式配置
     */
    static {
        //设置拼音为小写(LOWERCASE)
        FORMAT.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        //设置不带音调
        FORMAT.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        //设置带V字符
        FORMAT.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    /**
     * 判断字符串是否含有中文
     * @param str
     * @return
     */
    public static boolean containsChinese(String str) {
        return str.matches(".*" + CHINESE_PATTERN + ".*");
    }
    // 项目用的方法
    public static String[] get(String hanyu)  {
        //需要返回的字符串数组
        String[] array = new String[2];//zhonghuarenmingongheguo,zhrmghg
        // 全拼
        StringBuilder pinyin = new StringBuilder();
        //拼音首字母
        StringBuilder pinyin_first = new StringBuilder();
        for (int i = 0; i < hanyu.length(); i++) {
            try {
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);

                // 中文字符返回的字符串数组，可能为null或长度为0
                // 返回原始的字符
                if(pinyins == null || pinyins.length == 0) {
                    pinyin.append(hanyu.charAt(i));
                    pinyin_first.append(hanyu.charAt(i));
                } else {
                    pinyin.append(pinyins[0]);
                    pinyin_first.append(pinyins[0].charAt(0));
                }
            } catch (Exception e) {
                //出现异常返回原始字符 例如 汉语字符为 0 ,字母
                pinyin.append(hanyu.charAt(i));
                pinyin_first.append(hanyu.charAt(i));
            }
        }
        array[0] = pinyin.toString();
        array[1] = pinyin_first.toString();
        return array;
    }

    /**
     * 返回所有拼音的排列组合  (项目中暂时不用)
     * @param hanyu 字符串
     * @param fullSpell 是否为全拼,如果是全拼就全拼的排列组合,如果不是就首字母的全拼组合
     * @return
     */

    // 先返回一个二维数组,返回字符串所有的拼音再进行排列组合
    private static String[][] get(String hanyu,boolean fullSpell) {
        String[][] array = new String[hanyu.length()][];
        for (int i = 0; i < array.length; i++) {
            try {
                // 先取到一个二维数组
                String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
                if(pinyins == null || pinyins.length == 0) {
                    //将无法转成汉语拼音的字符,转成字符串方人员字符串数组,里面只有一个字符串
                    array[i] = new String[]{String.valueOf(hanyu.charAt(i))};
                } else {
                    // 去重操作
                   // array[i] = unique(pinyins,fullSpell);

                }
            }catch (Exception e) {
                array[i] = new String[] {String.valueOf(hanyu.charAt(i))};
            }
        }
        return array;
    }

    // 排列组合: 两两组和  第一个和第二个合为一个字符串数组,一次往后组合
    /**
     * 字符串数组去重操作(项目中暂时不用)
     * @param pinyins
     * @param fullSpell 是否为全拼,true 为全拼,false 取首字母
     */
    private static void unique(String[] pinyins, boolean fullSpell) {
        Set<String> set = new HashSet<>();
        for (int i = 0; i < pinyins.length; i++) {
            if(fullSpell) {
                set.add(pinyins[0]);
            } else {
                set.add(String.valueOf(pinyins[i].charAt(0)));
            }
        }

    }

    public static String[][] test(String hanyu) throws BadHanyuPinyinOutputFormatCombination {

        String[][] result = new String[hanyu.length()][];
        for (int i = 0; i < hanyu.length(); i++) {
            // 将 汉语字符 转化为拼音，转化 方式按照 之前的FORMAT设置
            result[i] = PinyinHelper.
                    toHanyuPinyinStringArray(hanyu.charAt(i),FORMAT);
        }

        return result;
    }

    public static void main(String[] args) throws BadHanyuPinyinOutputFormatCombination {

        String[] pinyins = get("中华人民共和国");
        System.out.println(Arrays.toString(pinyins));
        for (int i = 0; i < pinyins.length; i++) {

        }
        //String[][] ret =  test("中华人民共和国");
//        String hanyu = "中国";
//        System.out.println(hanyu.charAt(0));
//        String[] pin = PinyinHelper.toHanyuPinyinStringArray('中',FORMAT);
//        System.out.println(Arrays.toString(pin));
    }
}
