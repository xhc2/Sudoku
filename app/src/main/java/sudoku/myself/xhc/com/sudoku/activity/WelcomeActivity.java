package sudoku.myself.xhc.com.sudoku.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import sudoku.myself.xhc.com.sudoku.R;

// 这个游戏只有数独，普通随机版，闯关版，加强版
public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


        startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
        finish();
    }
}
