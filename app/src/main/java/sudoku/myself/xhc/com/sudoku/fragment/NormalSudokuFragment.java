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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(getActivity(), GameActivity.class));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.normal_sudoku_layout,container,false);

    }

    @Override
    public void onViewCreated(View view,  Bundle savedInstanceState) {


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test:

                break;

        }
    }
}
