package sudoku.myself.xhc.com.sudoku.bean;

/**
 * Created by xhc on 2016/2/15.
 * 回溯生成颜色数独的时候用来记录使用的
 *
 */
public class RecordNodeColor {
    private int count = 0 ;
    private int [] listColors = new int [9];


    public void clear(){
        count = 0 ;
        for(int i = 0 ;i < listColors.length ; ++ i){
            listColors[i] = 0 ;
        }
    }
    public boolean setRecordColor(int color){
        if(color < 1 || color > 9) return false;
        if(!isContain(color)){
            listColors[color - 1] = color;
            count ++;
            return true;
        }
        return false;
    }
    //是否满了true 满了，false没有
    public boolean isFull(){
        return count >= 9;
    }
    public boolean isContain(int color){
        return listColors[color - 1] == color;
    }

    public int getCount() {
        return count;
    }

    public int[] getColor() {
        return listColors;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setColor(int[] colors) {
        this.listColors = colors;
    }
}
