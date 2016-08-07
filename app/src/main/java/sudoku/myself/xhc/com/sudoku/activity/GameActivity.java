package sudoku.myself.xhc.com.sudoku.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

import sudoku.myself.xhc.com.sudoku.R;
import sudoku.myself.xhc.com.sudoku.bean.Record;
import sudoku.myself.xhc.com.sudoku.db.RecordDao;
import sudoku.myself.xhc.com.sudoku.inter.IsWinCallBack;
import sudoku.myself.xhc.com.sudoku.mywidget.SudokuMap;
import sudoku.myself.xhc.com.sudoku.util.Constant;

public class GameActivity extends Activity implements IsWinCallBack {

    private TextView tvLable;
    private SudokuMap map ;
    private int type = 0 ;
    private int level = 0 ;
    private RecordDao dao = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_game);
        findView();
        init();
    }

    private void init(){
        dao = new RecordDao(this);
        map.setIsWinCallBack(this);
        Intent intent = getIntent();
        type = intent.getIntExtra(Constant.type , -1);
        level = intent.getIntExtra(Constant.level , -1);
        if(type == -1 || level == -1){
            finish();
            return ;
        }


        List<Record> listRecord = getHistory();
        if(listRecord != null && listRecord.size() > 0){
           Record record = listRecord.get(0);
            map.setHistory(record);
        }
        else{

            if(type == Constant.normal){
                map.setNormalLevel(level);
            }else if(type == Constant.harder){
                map.setHarderLevel(level);
            }else if(type == Constant.screening){
                //闯关模式
            }
        }

    }

    private  List<Record> getHistory(){
        List<Record> listRecord = dao.getRecordFromTypeAndLevel(type, level);
        return listRecord;
    }


    @Override
    protected void onPause() {
        super.onPause();
        //将数据保存到本地数据库中
        Log.e("xhc" , "保存的数独-> level "+level+" type "+type);

        String json = new Gson().toJson(map.getNodes());
        Record record = new Record();
        record.setLevel(level);
        record.setLevelType(type);
        record.setNodes(json);
        record.setStartTime(new Date().getTime());
        record.setTime(new Date().getTime());
        dao.addOrUpdate(record);
//        List<Record> listRecord = getHistory();
//        for(int i = 0 ;i < listRecord.size() ; ++ i){
//            Record r = listRecord.get(i);
//            Node[][] array =  new Gson().fromJson(r.getNodes() , Node[][].class);
//
//            for(int x = 0 ; x < array.length ; ++ x ){
//                String str ="";
//                for(int y = 0 ; y < array[x].length ; ++ y){
//                    str += array[x][y]+" ";
//                }
//                Log.e("xhc",str);
//            }
//        }
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
