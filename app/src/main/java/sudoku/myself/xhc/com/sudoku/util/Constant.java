package sudoku.myself.xhc.com.sudoku.util;

/**
 * Created by xhc on 2016/2/15.
 * <p/>
 * 赤色 【RGB】255, 0, 0 【CMYK】 0, 100, 100, 0
 * 橙色 【RGB】 255, 165, 0 【CMYK】0, 35, 100, 0
 * 黄色 【RGB】255, 255, 0 【CMYK】0, 0, 100, 0
 * 绿色  【RGB】0, 255, 0 【CMYK】100, 0, 100, 0
 * 青色  【RGB】0, 127, 255 【CMYK】100, 50, 0, 0
 * 蓝色  【RGB】0, 0, 255 【CMYK】100, 100, 0, 0
 * 紫色  【RGB】139, 0, 255 【CMYK】45, 100, 0, 0
 */
public class Constant {

    public static final int Color[] = new int[]{0xAAFFFFFF, 0xAAFF0000, 0xAAFFA500, 0xAAFFFF00, 0xAA00FF00, 0xAA007FFF, 0xAA0000FF, 0xAA8B00FF, 0xAADA6DDA};

    public static final String type = "type";

    public static final String level = "level";
    //普通模式
    public static final int normal = 1;
    //困难模式
    public static final int harder = 2;
    //闯关模式
    public static final int screening = 3;
//    http://192.168.0.6:8080/SuduKu/d/LoginAction
    public static String BASEURL = "http://192.168.0.6:8080/SuduKu/d/";

}
