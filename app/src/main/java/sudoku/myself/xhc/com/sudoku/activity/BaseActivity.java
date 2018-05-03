package sudoku.myself.xhc.com.sudoku.activity;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by xhc on 2016/11/6.
 */
public class BaseActivity extends Activity{



    protected void showToast(String msg){
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(int stringId){
        showToast(getString(stringId));
    }

}
