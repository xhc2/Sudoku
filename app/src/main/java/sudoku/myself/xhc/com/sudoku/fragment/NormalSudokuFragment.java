package sudoku.myself.xhc.com.sudoku.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import sudoku.myself.xhc.com.sudoku.R;
import sudoku.myself.xhc.com.sudoku.activity.GameActivity;
import sudoku.myself.xhc.com.sudoku.bean.Node;
import sudoku.myself.xhc.com.sudoku.util.Sudoku;

/**
 * Created by xhc on 2016/2/13.
 */
public class NormalSudokuFragment extends Fragment implements View.OnClickListener{

    private TextView tv ;
    String str ="";

    String str2 = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Node[][] nodeArray = Sudoku.getInstance().getGameCombinationSudoku(5);
        Sudoku sudoku = Sudoku.getInstance();
        Log.e("xhc",sudoku +"  1");

        new Thread(new Runnable() {
            @Override
            public void run() {
                Sudoku sudoku = Sudoku.getInstance();
                Log.e("xhc",sudoku.toString() +"  2");
            }
        }).start();

        for(int i = 0 ;i < nodeArray.length ; ++ i){
            for(int j = 0 ;j < nodeArray[i].length ; ++ j){

                if(nodeArray[i][j].isNumFlag()){
                    str2 += "0";
                    str2 +=" ";
                }
                else{
                    str2 += nodeArray[i][j].getSystemNum();
                    str2 +=" ";
                }

                if(nodeArray[i][j].isColorFlag()){
                    str += "0";
                    str +=" ";
                }
                else{
                    str += nodeArray[i][j].getSystemColor();
                    str +=" ";
                }



            }
            str += "\n";
            str2 += "\n";

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.normal_sudoku_layout,container,false);

    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {

        tv = (TextView)view.findViewById(R.id.test);
        str += "\n";str += "\n";str += "\n";
        str += str2;
        tv.setText(str);
        tv.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test:
                startActivity(new Intent(getActivity(), GameActivity.class));
                break;

        }
    }
}
