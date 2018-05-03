package sudoku.myself.xhc.com.sudoku.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.squareup.okhttp.Request;
import com.xhc.logger.L;

import sudoku.myself.xhc.com.sudoku.R;
import sudoku.myself.xhc.com.sudoku.bean.Param;
import sudoku.myself.xhc.com.sudoku.myhttp.OkHttpClientManager;
import sudoku.myself.xhc.com.sudoku.util.Constant;

/**
 * Created by xhc on 2016/11/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{


    private EditText etUserName;
    private EditText etPassword;
    private String REQUESTURL = Constant.BASEURL + "LoginAction";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginactivity_layout);

        findView();
        init();
    }

    private void findView(){
        etUserName = (EditText)findViewById(R.id.et_user);
        etPassword = (EditText)findViewById(R.id.et_pass);
    }

    private void init(){
        findViewById( R.id.bt_login).setOnClickListener(this);
        findViewById( R.id.bt_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_register:
                //注册
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
            case R.id.bt_login:
                //登录
                login();
                break;
        }
    }

    private void login(){
        String userCount = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        if(TextUtils.isEmpty(userCount) || TextUtils.isEmpty(password)){
            showToast(R.string.please_input_user_or_pass);
        }
        else{
            Param pcount = new Param("userName" , userCount);
            Param ppassword = new Param("password" , password);
            L.i(REQUESTURL);
//            OkHttpClientManager.getAsyn("http://www.zhuantilan.com/jiqiao/66645.html", new OkHttpClientManager.ResultCallback<String>()
//            {
//                @Override
//                public void onError(Request request, Exception e)
//                {
//                    e.printStackTrace();
//                }
//
//                @Override
//                public void onResponse(String u)
//                {
//                    L.i(u);
////                    mTv.setText(u);//注意这里是UI线程
//                }
//            });
            OkHttpClientManager.postAsyn(REQUESTURL, new OkHttpClientManager.ResultCallback<String>() {

                @Override
                public void onError(Request request, Exception e) {
                    L.e(e, "onerror");
                }

                @Override
                public void onResponse(String o) {
                    Log.e("xhc", o);
//                    L.i(response.toString());
                }
            }, pcount, ppassword);
        }
    }

}
