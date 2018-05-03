package sudoku.myself.xhc.com.sudoku.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import sudoku.myself.xhc.com.sudoku.R;
import sudoku.myself.xhc.com.sudoku.bean.Node;
import sudoku.myself.xhc.com.sudoku.bean.Record;
import sudoku.myself.xhc.com.sudoku.db.RecordDao;
import sudoku.myself.xhc.com.sudoku.inter.IsWinCallBack;
import sudoku.myself.xhc.com.sudoku.mywidget.SudokuMap;
import sudoku.myself.xhc.com.sudoku.util.Constant;
import sudoku.myself.xhc.com.sudoku.util.Sudoku;

/**
 * 时间计时是用的sdf，用的24.小时。如果超过了24小时。
 *
 */

public class GameActivity extends Activity implements IsWinCallBack {

    private TextView tvLable;
    private SudokuMap map ;
    private int type = 0 ;
    private int level = 0 ;
    private RecordDao dao = null;
    private long time = 0 ;
    private long startTime = 0;
    private Timer timer = new Timer();
    private final int TIMECHANGE = 1;
    private String timeStr ;
    private SimpleDateFormat sdf2 = new SimpleDateFormat("mm:ss");

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TIMECHANGE:
                    tvLable.setText(timeStr);
                    break;
            }
        }
    };

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
            time = record.getTime();
            startTime = record.getStartTime();

            map.setHistory(record);
        }
        else{

            if(type == Constant.normal){
                setNormalLevel(level);
            }else if(type == Constant.harder){
                setHarderLevel(level);
            }else if(type == Constant.screening){
                //闯关模式
            }
            //记录开始时间
            startTime = System.currentTimeMillis();
        }
        timer.schedule(timerTask,1000 ,1000);
    }

    private  List<Record> getHistory(){
        List<Record> listRecord = dao.getRecordFromTypeAndLevel(type, level);
        return listRecord;
    }

    public void setHarderLevel(int level){
        Sudoku sudoku = Sudoku.getInstance();
        sudoku.clear();
        Node[][] nodes = sudoku.getGameCombinationSudoku(level);
        map.setNodeArray(nodes , Constant.harder );
    }



    public void setNormalLevel(int level){
        Sudoku sudoku  = Sudoku.getInstance();
        sudoku.clear();
        Node[][] nodes = sudoku.getGameNormal(level);
        map.setNodeArray(nodes , Constant.normal);
    }
    @Override
    protected void onPause() {
        super.onPause();
        //将数据保存到本地数据库中

        String json = new Gson().toJson(map.getNodes());
        Record record = new Record();
        record.setLevel(level);
        record.setLevelType(type);
        record.setNodes(json);
        record.setStartTime(startTime);
        record.setTime(time);
        dao.addOrUpdate(record);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    private TimerTask timerTask = new TimerTask() {
        @Override
        public void run() {
            //保存的是ms
            time += 1000;
            int h = (int)(time / (60 *60 *1000));
            Log.e("xhc","time -》 "+time+" "+h);
            if(h < 10){
                timeStr = "0"+h+":"+sdf2.format((time + 5 * 60 * 60 *1000 ));
            }
            else{
                timeStr = h+ ":"+sdf2.format((time + 5 * 60 * 60 *1000));
            }
            handler.sendEmptyMessage(TIMECHANGE);
        }
    };

    private void findView(){
        tvLable = (TextView)findViewById(R.id.tv_label);
        map = (SudokuMap) findViewById(R.id.map);

    }

    @Override
    public void winner() {

        tvLable.setText("颜色数独和数字数独都赢了");
    }
}
