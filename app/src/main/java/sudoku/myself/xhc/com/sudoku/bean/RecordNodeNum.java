package sudoku.myself.xhc.com.sudoku.bean;

/**
 * Created by xhc on 2016/2/15.
 * 记录每个格子中尝试过的数字
 */
public class RecordNodeNum {
    private int[] listNum = new int[9];
    //记录数字的是否填满了9个
    private int count = 0 ;




    //添加成功true， false添加失败
    public boolean setRecordNodeNum(  int num){
        if(num < 1 || num > 9) return false;
        if(!isContain(num)){
            count ++;
            this.listNum[num - 1]  = num;
            return true;
        }
        return false;
    }

    public void clear(){
        count = 0 ;
        for(int i = 0 ;i < listNum.length ; ++ i){
            listNum[i] = 0 ;
        }
    }

    //是否满了true 满了，false没有
    public boolean isFull(){
        return count >= 9;
    }

    //是否有这个数字true 有, false 没有
    public boolean isContain(int num){

        return this.listNum[num - 1] == num;
    }


    public void setListNum(int[] listNum) {
        this.listNum = listNum;
    }


    public int[] getListNum() {
        return listNum;
    }
}
