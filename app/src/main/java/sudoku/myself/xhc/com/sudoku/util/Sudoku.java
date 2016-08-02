package sudoku.myself.xhc.com.sudoku.util;


import android.util.Log;

import sudoku.myself.xhc.com.sudoku.bean.Node;
import sudoku.myself.xhc.com.sudoku.bean.RecordNodeColor;
import sudoku.myself.xhc.com.sudoku.bean.RecordNodeNum;

/**
 * Created by xhc on 2016/2/15.
 * 数独一些操作
 * 1.数独规则
 * 2.生成普通的随机数独
 * 3.生成加强的随机数独
 * 4.生成闯关数独
 * 5.检验数独是否完成
 */
public class Sudoku {


    private static Sudoku sudoku = new Sudoku();

    private Node[][] array = new Node[9][9];
    private RecordNodeNum[][] recordNodeNum = new RecordNodeNum[9][9];
    private RecordNodeColor[][] recordNodeColors = new RecordNodeColor[9][9];
    private int orderNum = 0;
    private boolean clearFlag = false;
    private Sudoku() {

        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[i].length; ++j) {
                array[i][j] = new Node();
                recordNodeNum[i][j] = new RecordNodeNum();
                recordNodeColors[i][j] = new RecordNodeColor();
            }
        }

    }

    public synchronized static Sudoku getInstance() {
        if(sudoku == null){
            sudoku = new Sudoku();
        }

        return sudoku;
    }

    public void setNodes( Node[][] array){
        this.array = array;

            for(int x = 0 ; x < array.length ; ++ x ){
                String str ="";
                for(int y = 0 ; y < array[x].length ; ++ y){
                    str += array[x][y]+" ";
                }
                Log.e("xhc",str);
            }
    }

    /**
     * 获取一个只有数字的数独
     *
     * @return
     */
    private Node[][] getNormalSudoku() {
        randomSetNum();
        for (int i = 0; i < array.length && !clearFlag; ++i) {
            for (int j = 0; j < array[i].length && !clearFlag; ++j) {

                if (array[i][j].isNumFlag()) {
                    int num = getOrderNum();
                    if (recordNodeNum[i][j].setRecordNodeNum(num) && isSystemNumConform(i, j, num)) {
                        array[i][j].setSystemNum(num);
                    } else {
                        if (recordNodeNum[i][j].isFull()) {
                            //满了
                            recordNodeNum[i][j].clear();
                            array[i][j].setSystemNum(0);
                            int[] temp = backtraceNum(i, j, array);
                            i = temp[0];
                            j = temp[1];
                        } else {
                            //没满
                            --j;
                        }
                    }
                }
            }
        }
        resetNumFlag();
        return array;
    }


    /**
     * 判断数字数独是否赢了
     * @return
     */
    public boolean isNumWin(){

        for(int i = 0 ;i < 9 ; ++ i){
            for(int j = 0 ; j < 9 ; ++ j){
                if(array[i][j].isNumFlag() && array[i][j].getUserNum() == 0 )return false;
                if(array[i][j].isNumFlag()){
                    if(isUserNumConform(i,j,array[i][j].getUserNum())){
                        //符合要求
                        continue;
                    }
                    else return false;
                }
            }
        }
        return true;
    }

    public boolean isColorWin(){
        for(int i = 0 ;i < 9 ; ++ i){
            for(int j = 0 ; j < 9 ; ++ j){
                if(array[i][j].isColorFlag() && array[i][j].getUserColor() == 0)return false;
                if(isUserColorConform(i, j, array[i][j].getUserColor()))continue;
                else return false;
            }
        }

        return true;
    }

    private Node[][] getColorSudoku() {
        long startTime = System.currentTimeMillis();
        randomSetColor();
        for (int i = 0; i < array.length && !clearFlag; ++i) {
            for (int j = 0; j < array[i].length && !clearFlag; ++j) {

                if (array[i][j].isColorFlag()) {
                    //被挖掉
                    int num = getOrderNum();
                    if (recordNodeColors[i][j].setRecordColor(num) && isSystemColorConform(i, j, num)) {
                        array[i][j].setSystemColor(num);
                    } else {
                        if (recordNodeColors[i][j].isFull()) {
                            //满了
                            recordNodeColors[i][j].clear();
                            array[i][j].setSystemColor(0);
                            int[] temp = backtraceColor(i, j, array);
                            i = temp[0];
                            j = temp[1];
                        } else {
                            //没满
                            --j;
                        }
                    }
                }
            }
        }
        resetColorFlag();
        Log.e("xhc", "生成颜色数独的时间-> "+(System.currentTimeMillis() - startTime) + "");
        return array;
    }

    public void clear(){
        clearFlag = true;
        orderNum = 0;
        for (int i = 0 ;i < array.length ; ++i ){
            for (int j = 0 ; j < array[i].length ; ++ j){
                array[i][j].clear();
                recordNodeColors[i][j].clear();
                recordNodeNum[i][j].clear();
            }
        }
    }

    private void resetNumFlag() {
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[i].length; ++j) {
                array[i][j].setNumFlag(false);
            }
        }
    }

    private void resetColorFlag() {
        for (int i = 0; i < array.length; ++i) {
            for (int j = 0; j < array[i].length; ++j) {
                array[i][j].setColorFlag(false);
            }
        }
    }

    //组合数独 最好用一个线程来调用 ，并且是不同level
    public synchronized Node[][] getGameCombinationSudoku(int level) {
        clearFlag = false;
        if (level < 1 || level > 5) {
            return null;
        }
        getNormalSudoku();
        getColorSudoku();
        removeColorSudoku(level);
        removeNumSudoku(level);

        return array;
    }

    //最好用一个线程来调用 获取普通数独
    public synchronized Node[][] getGameNormal(int level) {
        clearFlag = false;
        if (level < 1 || level > 5) {
            return null;
        }
        getNormalSudoku();
        removeNumSudoku(level);
        return array;
    }


    /**
     * 根据等级移除一些数字
     * 1等难度: 随机去除40或者45个空格
     * 2等难度：随机去除46—50个空格
     * 3等难度:偶数行不去i，8奇数行不去除(间隔) i、0这个数 去除51-55个空格
     * 4等难度：蛇形去除   55-60个空格
     * 5等难度:从左至右从上至下 60-62个空格
     * <p>
     * 数字颜色同理
     */
    private void removeNumSudoku(int level) {

        switch (level) {
            case 1:
                numLevel_1();
                break;
            case 2:
                numLevel_2();
                break;
            case 3:
                numLevel_3();
                break;
            case 4:
                numLevel_4();
                break;
            case 5:
                numLevel_5();
                break;
        }

    }

    private void removeColorSudoku(int level) {
        switch (level) {
            case 1:
                colorLevel_1();
                break;
            case 2:
                colorLevel_2();
                break;
            case 3:
                colorLevel_3();
                break;
            case 4:
                colorLevel_4();
                break;
            case 5:
                colorLevel_5();
                break;
        }

    }

    private void numLevel_1() {
        for (int i = 0; i < 47; i++) {
            int x = getRandom8();
            int y = getRandom8();
            if (array[x][y].isNumFlag()) {
                 /*避免挖到同一个洞*/
                i--;
                continue;
            }
                /*尝试挖洞*/
            array[x][y].setNumFlag(true);
        }
    }

    private void numLevel_2() {

        for (int i = 0; i < 55; i++) {
            int x = getRandom8();
            int y = getRandom8();
            if (array[x][y].isNumFlag()) {
                 /*避免挖到同一个洞*/
                i--;
                continue;
            }
				/*尝试挖洞*/
            array[x][y].setNumFlag(true);
        }

    }

    private void numLevel_3() {
        for (int i = 0; i < array.length; i++) {
            if (i % 2 == 0) {
					/*偶数行 i 8这个洞不能挖*/
                for (int j = 0; j < 6; j++) {
                    int tempY = getRandom8();
                    if (tempY == 8 || array[i][tempY].isNumFlag()) {
                        j--;
                        continue;
                    }
                    array[i][tempY].setNumFlag(true);
                }
            } else {
                for (int j = 0; j < 6; j++) {
                    int tempY = getRandom8();
                    if (tempY == 0 || array[i][tempY].isNumFlag()) {
                        j--;
                        continue;
                    }
                    array[i][tempY].setNumFlag(true);
                }
            }
        }
			/*剩余4个随机分配*/
        for (int i = 0; i < 4; i++) {
            int tempX = getRandom8();
            int tempY = getRandom8();
            if (array[tempX][tempY].isNumFlag()) {
                i--;
                continue;
            } else {
                array[tempX][tempY].setNumFlag(true);
            }
        }

    }

    private void numLevel_4() {

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < 6; j++) {
                int tempY = getRandom8();
                if (array[i][tempY].isNumFlag()) {
                    j--;
                    continue;
                }
                array[i][tempY].setNumFlag(true);

            }

        }
        for (int j = 0; j < 6; j++) {
            int tempY = getRandom8();
            int tempX = getRandom8();
            if (array[tempX][tempY].isNumFlag()) {
                j--;
                continue;
            }
            array[tempX][tempY].setNumFlag(true);
        }
    }

    private void numLevel_5() {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < 6; j++) {
                int tempY = getRandom8();
                if (array[i][tempY].isNumFlag()) {
                    j--;
                    continue;
                }

                array[i][tempY].setNumFlag(true);

            }

        }
        for (int i = 0; i < 8; i++) {
            int tempY = getRandom8();
            int tempX = getRandom8();
            if (array[tempX][tempY].isNumFlag()) {
                i--;
                continue;
            }
            array[tempX][tempY].setNumFlag(true);
        }
    }


    private void colorLevel_1() {
        for (int i = 0; i < 47; i++) {
            int x = getRandom8();
            int y = getRandom8();
            if (array[x][y].isColorFlag()) {
                 /*避免挖到同一个洞*/
                i--;
                continue;
            }
				/*尝试挖洞*/
            array[x][y].setColorFlag(true);
        }
    }

    private void colorLevel_2() {
        for (int i = 0; i < 55; i++) {
            int x = getRandom8();
            int y = getRandom8();
            if (array[x][y].isColorFlag()) {
                 /*避免挖到同一个洞*/
                i--;
                continue;
            }
				/*尝试挖洞*/
            array[x][y].setColorFlag(true);
        }
    }

    private void colorLevel_3() {
        for (int i = 0; i < array.length; i++) {
            if (i % 2 == 0) {
					/*偶数行 i 8这个洞不能挖*/
                for (int j = 0; j < 6; j++) {
                    int tempY = getRandom8();
                    if (tempY == 8 || array[i][tempY].isColorFlag()) {
                        j--;
                        continue;
                    }
                    array[i][tempY].setColorFlag(true);
                }
            } else {
                for (int j = 0; j < 6; j++) {
                    int tempY = getRandom8();
                    if (tempY == 0 || array[i][tempY].isColorFlag()) {
                        j--;
                        continue;
                    }
                    array[i][tempY].setColorFlag(true);
                }
            }
        }
			/*剩余4个随机分配*/
        for (int i = 0; i < 4; i++) {
            int tempX = getRandom8();
            int tempY = getRandom8();
            if (array[tempX][tempY].isColorFlag()) {
                i--;
                continue;
            } else {
                array[tempX][tempY].setColorFlag(true);
            }
        }
    }

    private void colorLevel_4() {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < 6; j++) {
                int tempY = getRandom8();
                if (array[i][tempY].isColorFlag()) {
                    j--;
                    continue;
                }
                array[i][tempY].setColorFlag(true);

            }

        }
        for (int j = 0; j < 6; j++) {
            int tempY = getRandom8();
            int tempX = getRandom8();
            if (array[tempX][tempY].isColorFlag()) {
                j--;
                continue;
            }
            array[tempX][tempY].setColorFlag(true);
        }
    }

    private void colorLevel_5() {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < 6; j++) {
                int tempY = getRandom8();
                if (array[i][tempY].isColorFlag()) {
                    j--;
                    continue;
                }

                array[i][tempY].setColorFlag(true);

            }

        }
        for (int i = 0; i < 8; i++) {
            int tempY = getRandom8();
            int tempX = getRandom8();
            if (array[tempX][tempY].isColorFlag()) {
                i--;
                continue;
            }
            array[tempX][tempY].setColorFlag(true);
        }
    }


    /**
     * 注意换行的回退
     * 刚回退到的数如果是系统随机的9个数的话就会引起死循环
     *
     * @param i
     * @param j
     * @return
     */
    private int[] back(int i, int j) {
        int temp[] = new int[2];

        if (j == 0) {
            --i;
            j = 8;
        } else {
            j--;
        }

        temp[0] = i;
        temp[1] = j;

        return temp;
    }

    /**
     * @param i     二维数组中的坐标
     * @param j     二维数组中的坐标
     * @param array 二维数组
     * @return 最后还回退一次，是因为在数组中会向下移动一次
     * 在这里没有被挖的数字，就是系统随机扔的9个数字
     */
    private int[] backtraceNum(int i, int j, Node[][] array) {
        if (i == 0 && j == 0) {
            throw new RuntimeException("回溯错误");
        }
        int[] temp = back(i, j);
        while (!array[temp[0]][temp[1]].isNumFlag()) {
            temp = back(temp[0], temp[1]);
        }

        return back(temp[0], temp[1]);
    }

    private int[] backtraceColor(int i, int j, Node[][] array) {
        if (i == 0 && j == 0) {
            throw new RuntimeException("回溯错误");
        }
        int[] temp = back(i, j);
        while (!array[temp[0]][temp[1]].isColorFlag()) {
            temp = back(temp[0], temp[1]);
        }

        return back(temp[0], temp[1]);
    }


    /**
     * 判断数字是否符合这个位置 ， 如果这个位置已经被挖过了，就和用户填的数字判断，如果没有挖过就和系统数字判断
     * fasle 不符合这个位置
     * true 符合这个位置
     * 这个方法是用户填入时用的判断
     */
    public boolean isUserNumConform(int x, int y, int num) {
        if (num < 1 || num > 9) {
            return false;
        }
            /* 同行比较 */
        for (int j = 0; j < array.length; j++) {
            if(j == y)continue;
            if (array[x][j].isNumFlag()) {
                if (array[x][j].getUserNum() == num) {
                    return false;
                }
            } else {
                //没有被挖和系统比较
                if (array[x][j].getSystemNum() == num) {
                    return false;
                }
            }
        }

        /* 同列比较 */
        for (int i = 0; i < array.length; i++) {
            if(x == i)continue;
            if (array[i][y].isNumFlag()) {
                if (array[i][y].getUserNum() == num) {
                    return false;
                }
            } else {
                if (array[i][y].getSystemNum() == num) {
                    return false;
                }
            }
        }

        /* 九宫格比较 先知道在哪个宫格中,不用知道在宫格中的哪个位置。直接在那个宫格中从头开始遍历进行比较 */
        int sqrtLength = (int) Math.sqrt(array.length);
        /* 知道在哪个九宫格中 */
        int whereX = x / sqrtLength;
        int whereY = y / sqrtLength;
        /* 九宫格做成一个二维方式比较 直接定位到九宫格中 */
        for (int i = whereX * sqrtLength; i < whereX * sqrtLength + sqrtLength; i++) {
            for (int j = whereY * sqrtLength; j < whereY * sqrtLength
                    + sqrtLength; j++) {
                if(i == x && j == y) continue;
                if (array[i][j].isNumFlag()) {
                    if (array[i][j].getUserNum() == num) {
                        return false;
                    }
                } else {
                    if (array[i][j].getSystemNum() == num) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 系统生成的时候做的判断
     *
     * @param x   坐标
     * @param y   坐标
     * @param num 填入的数字
     * @return false不可以，反之
     */
    public boolean isSystemNumConform(int x, int y, int num) {
        if (num < 1 || num > 9) {
            return false;
        }
            /* 同行比较 */
        for (int j = 0; j < array.length; j++) {
            if (array[x][j].getSystemNum() == num) {
                return false;
            }
        }

        /* 同列比较 */
        for (int i = 0; i < array.length; i++) {
            if (array[i][y].getSystemNum() == num) {
                return false;
            }
        }

        /* 九宫格比较 先知道在哪个宫格中,不用知道在宫格中的哪个位置。直接在那个宫格中从头开始遍历进行比较 */
        int sqrtLength = (int) Math.sqrt(array.length);
        /* 知道在哪个九宫格中 */
        int whereX = x / sqrtLength;
        int whereY = y / sqrtLength;
        /* 九宫格做成一个二维方式比较 直接定位到九宫格中 */
        for (int i = whereX * sqrtLength; i < whereX * sqrtLength + sqrtLength; i++) {
            for (int j = whereY * sqrtLength; j < whereY * sqrtLength
                    + sqrtLength; j++) {
                if (array[i][j].getSystemNum() == num) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * @param x     在二维数组中的x坐标
     * @param y     在二维数组中的y坐标
     * @param color 色值
     * @return true 符合这个位置, false 不符合这个位置
     * 这函数是用户填入的数字做的判断
     */
    public boolean isUserColorConform(int x, int y, int color) {

          /* 同行比较 */
        for (int j = 0; j < array.length; j++) {
            if(j==y)continue;
            if (array[x][j].isColorFlag()) {
                if (array[x][j].getUserColor() == color) {
                    return false;
                }
            } else {
                //没有被挖和系统比较
                if (array[x][j].getSystemColor() == color) {
                    return false;
                }
            }
        }

         /* 同列比较 */
        for (int i = 0; i < array.length; i++) {
            if(i == x)continue;
            if (array[i][y].isColorFlag()) {
                if (array[i][y].getUserColor() == color) {
                    return false;
                }
            } else {
                if (array[i][y].getSystemColor() == color) {
                    return false;
                }
            }
        }

          /* 九宫格比较 先知道在哪个宫格中,不用知道在宫格中的哪个位置。直接在那个宫格中从头开始遍历进行比较 */
        int sqrtLength = (int) Math.sqrt(array.length);
        /* 知道在哪个九宫格中 */
        int whereX = x / sqrtLength;
        int whereY = y / sqrtLength;
        /* 九宫格做成一个二维方式比较 直接定位到九宫格中 */
        for (int i = whereX * sqrtLength; i < whereX * sqrtLength + sqrtLength; i++) {
            for (int j = whereY * sqrtLength; j < whereY * sqrtLength
                    + sqrtLength; j++) {
                if(i == x && j == y)continue;
                if (array[i][j].isColorFlag()) {
                    if (array[i][j].getUserColor() == color) {
                        return false;
                    }
                } else {
                    if (array[i][j].getSystemColor() == color) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isSystemColorConform(int x, int y, int color) {

          /* 同行比较 */
        for (int j = 0; j < array.length; j++) {
            if (array[x][j].getSystemColor() == color) {
                return false;
            }
        }

         /* 同列比较 */
        for (int i = 0; i < array.length; i++) {
            if (array[i][y].getSystemColor() == color) {
                return false;
            }
        }

          /* 九宫格比较 先知道在哪个宫格中,不用知道在宫格中的哪个位置。直接在那个宫格中从头开始遍历进行比较 */
        int sqrtLength = (int) Math.sqrt(array.length);
        /* 知道在哪个九宫格中 */
        int whereX = x / sqrtLength;
        int whereY = y / sqrtLength;
        /* 九宫格做成一个二维方式比较 直接定位到九宫格中 */
        for (int i = whereX * sqrtLength; i < whereX * sqrtLength + sqrtLength; i++) {
            for (int j = whereY * sqrtLength; j < whereY * sqrtLength
                    + sqrtLength; j++) {
                if (array[i][j].getSystemColor() == color) {
                    return false;
                }
            }
        }
        return true;
    }

    /*随机投放复合规则的九个数字*/
    private void randomSetNum() {
        for (int i = 0; i < 9; i++) {
            int x = getRandom() - 1;
            int y = getRandom() - 1;
            int tempNum = getRandom();
            if (isSystemNumConform(x, y, tempNum)) {
                array[x][y].setSystemNum(tempNum);
            } else i--;
        }
    	/*其他没有添加的数字 置为抹去*/
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (array[i][j].getSystemNum() == 0)
                    array[i][j].setNumFlag(true);
            }
        }
    }

    //随机投放9个颜色
    private void randomSetColor() {
        for (int i = 0; i < 9; i++) {
            int x = getRandom() - 1;
            int y = getRandom() - 1;
            int color = getRandom();
            if (isSystemColorConform(x, y, color)) {
                array[x][y].setSystemColor(color);
            } else i--;
        }
    	/*其他没有添加的数字 置为抹去*/
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (array[i][j].getSystemColor() == 0)
                    array[i][j].setColorFlag(true);
            }
        }
    }

    private int getOrderNum() {
        if (orderNum == 0) {
            orderNum = getRandom();
        }

        orderNum++;
        orderNum %= 10;

        if (orderNum == 0) {
            orderNum = getRandom();
        }
        return orderNum;
    }


    /* 获取1-9的随机数 */
    private int getRandom() {
        int temp = 0;
        while (temp == 0) {
            temp = (int) (Math.random() * 100 % 10);
        }
        return temp;
    }

    /* 获取0-8的随机数 */
    private int getRandom8() {
        return (int) (Math.random() * 100 % 9);
    }
}









