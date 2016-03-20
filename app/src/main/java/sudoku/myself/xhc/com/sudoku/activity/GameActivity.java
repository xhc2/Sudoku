package sudoku.myself.xhc.com.sudoku.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import sudoku.myself.xhc.com.sudoku.R;
import sudoku.myself.xhc.com.sudoku.inter.IsWinCallBack;
import sudoku.myself.xhc.com.sudoku.mywidget.SudokuMap;

public class GameActivity extends Activity implements IsWinCallBack {

    private TextView tvLable;
    private SudokuMap map ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game);
        tvLable = (TextView)findViewById(R.id.tv_label);
        map = (SudokuMap) findViewById(R.id.map);
        map.setIsWinCallBack(this);
    }


    @Override
    public void winner() {
        //
        tvLable.setText("颜色数独和数字数独都赢了");
    }
}
