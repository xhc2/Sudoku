package sudoku.myself.xhc.com.sudoku.bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by xhc on 2016/2/15.
 * 棋盘上的每一个点的数据结构
 * 使用数组避免过多的自动拆装箱操作
 * 其中的boolean类型的flag false都表示没有被挖，true表示被挖掉
 *
 * 为了计算方便color中保存的数值还是十进制中的1-9，在界面上转换的时候猜转到对应的色值
 */
public class Node {

    private boolean numFlag = false;
    private int systemNum = 0;
    private int userNum = 0;
    private int[] candidateNum = new int[9];

    private boolean colorFlag = false;
    private int systemColor = 0;
    private int userColor = 0 ;
    private int[] candidateColor = new int[9];

    //清除一切状态
    public void clear(){
        numFlag = false;
        systemNum = 0 ;
        userNum = 0 ;
        colorFlag = false;
        systemColor = 0 ;
        userColor = 0 ;
        for(int i = 0 ;i < candidateNum.length ; ++ i){
            candidateNum[i] = 0 ;
            candidateColor[i] = 0 ;
        }
    }

    public boolean isNumFlag() {
        return numFlag;
    }

    public int getSystemNum() {
        return systemNum;
    }

    public int getUserNum() {
        return userNum;
    }

    public int[] getCandidateNum() {
        return candidateNum;
    }

    public boolean isColorFlag() {
        return colorFlag;
    }

    public int getSystemColor() {
        return systemColor;
    }

    public int getUserColor() {
        return userColor;
    }

    public int[] getCandidateColor() {
        return candidateColor;
    }

    public void setNumFlag(boolean numFlag) {
        this.numFlag = numFlag;
    }

    public void setSystemNum(int systemNum) {
        this.systemNum = systemNum;
    }

    public void setUserNum(int userNum) {
        this.userNum = userNum;
    }

    public void setCandidateNum(int[] candidateNum) {
        this.candidateNum = candidateNum;
    }

    public void setColorFlag(boolean colorFlag) {
        this.colorFlag = colorFlag;
    }

    public void setSystemColor(int systemColor) {
        this.systemColor = systemColor;
    }

    public void setUserColor(int userColor) {
        this.userColor = userColor;
    }

    public void setCandidateColor(int[] candidateColor) {
        this.candidateColor = candidateColor;
    }

}
