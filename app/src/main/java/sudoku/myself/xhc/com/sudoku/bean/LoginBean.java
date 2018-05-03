package sudoku.myself.xhc.com.sudoku.bean;

/**
 * Created by xhc on 2016/11/6.
 */
public class LoginBean extends ResultBean{
    private String userName ;
    private String password;


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
