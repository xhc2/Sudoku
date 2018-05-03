package sudoku.myself.xhc.com.sudoku.bean;

/**
 * Created by xhc on 2016/11/6.
 * 当http返回的时候用这个
 */
public class ResultBean {

    private String result ;
    private String reason ;

    public String getResult() {
        return result;
    }

    public String getReason() {
        return reason;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "result='" + result + '\'' +
                ", reason='" + reason + '\'' +
                '}';
    }
}
