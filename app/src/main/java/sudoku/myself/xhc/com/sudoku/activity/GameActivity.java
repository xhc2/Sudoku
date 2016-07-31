package sudoku.myself.xhc.com.sudoku.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import sudoku.myself.xhc.com.sudoku.R;
import sudoku.myself.xhc.com.sudoku.inter.IsWinCallBack;
import sudoku.myself.xhc.com.sudoku.mywidget.SudokuMap;
import sudoku.myself.xhc.com.sudoku.util.Constant;

public class GameActivity extends Activity implements IsWinCallBack {

    private TextView tvLable;
    private SudokuMap map ;
    private int type = 0 ;
    private int level = 0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game);

        findView();
        init();
    }

    private void init(){
        map.setIsWinCallBack(this);
        Intent intent = getIntent();
        type = intent.getIntExtra(Constant.type , -1);
        level = intent.getIntExtra(Constant.level , -1);
        if(type == -1 || level == -1){
            finish();
            return ;
        }
        else if(type == Constant.normal){
            map.setNormalLevel(level);
        }else if(type == Constant.harder){
            map.setHarderLevel(level);
        }else if(type == Constant.screening){
            //闯关模式
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        //将数据保存到本地数据库中
        String json = new Gson().toJson(map.getNodes());


        Log.e("xhc" , "json ->  "+ json);
    }

    private void findView(){
        tvLable = (TextView)findViewById(R.id.tv_label);
        map = (SudokuMap) findViewById(R.id.map);

    }

    @Override
    public void winner() {
        //
        tvLable.setText("颜色数独和数字数独都赢了");
    }
}
