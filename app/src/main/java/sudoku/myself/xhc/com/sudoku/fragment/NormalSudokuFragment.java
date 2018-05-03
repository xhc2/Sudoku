package sudoku.myself.xhc.com.sudoku.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import sudoku.myself.xhc.com.sudoku.R;
import sudoku.myself.xhc.com.sudoku.activity.GameActivity;
import sudoku.myself.xhc.com.sudoku.network.smack.ConnectXmpp;
import sudoku.myself.xhc.com.sudoku.util.Constant;

/**
 * Created by xhc on 2016/2/13.
 */
public class NormalSudokuFragment extends Fragment implements View.OnClickListener {

    private TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.normal_sudoku_layout, container, false);
    }

    private Button btLevel1, btLevel2, btLevel3, btLevel4, btLevel5;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btLevel1 = (Button) view.findViewById(R.id.bt_level1);
        btLevel2 = (Button) view.findViewById(R.id.bt_level2);
        btLevel3 = (Button) view.findViewById(R.id.bt_level3);
        btLevel4 = (Button) view.findViewById(R.id.bt_level4);
        btLevel5 = (Button) view.findViewById(R.id.bt_level5);
        view.findViewById(R.id.bt_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ConnectXmpp.class);
                intent.putExtra("user", "123");
                intent.putExtra("pwd", "123");
                getActivity().startService(intent);
            }
        });

        view.findViewById(R.id.bt_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectXmpp.sendMessage("大傻逼");
            }
        });

        init();
    }

    private void init() {
        btLevel1.setOnClickListener(this);
        btLevel2.setOnClickListener(this);
        btLevel3.setOnClickListener(this);
        btLevel4.setOnClickListener(this);
        btLevel5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), GameActivity.class);
        intent.putExtra(Constant.type, Constant.normal);
        switch (v.getId()) {
            case R.id.bt_level1:
                intent.putExtra(Constant.level, 1);
                startActivity(intent);
                break;
            case R.id.bt_level2:
                intent.putExtra(Constant.level, 2);
                startActivity(intent);
                break;
            case R.id.bt_level3:
                intent.putExtra(Constant.level, 3);
                startActivity(intent);
                break;
            case R.id.bt_level4:
                intent.putExtra(Constant.level, 4);
                startActivity(intent);
                break;
            case R.id.bt_level5:
                intent.putExtra(Constant.level, 5);
                startActivity(intent);
                break;
        }
    }
}
