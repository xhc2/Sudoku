package sudoku.myself.xhc.com.sudoku.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import sudoku.myself.xhc.com.sudoku.R;

// 这个游戏只有数独，普通随机版，闯关版，加强版
public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_welcome);


        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        finish();
    }
}
