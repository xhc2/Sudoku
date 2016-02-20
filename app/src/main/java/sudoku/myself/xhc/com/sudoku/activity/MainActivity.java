package sudoku.myself.xhc.com.sudoku.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sudoku.myself.xhc.com.sudoku.R;
import sudoku.myself.xhc.com.sudoku.fragment.FightSudokuFragment;
import sudoku.myself.xhc.com.sudoku.fragment.HarderSudokuFragment;
import sudoku.myself.xhc.com.sudoku.fragment.NormalSudokuFragment;
import sudoku.myself.xhc.com.sudoku.mywidget.MyIndicator;

public class MainActivity extends FragmentActivity {
    private MyPageAdapter adapter ;
    List<Fragment> listFragment = new ArrayList<Fragment>();
    String[] strTitle = new String[3];
    private ViewPager pager;
    private MyIndicator indicator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        findViewById();
        init();

    }

    private void findViewById(){
        pager = (ViewPager)findViewById(R.id.mypage);
        indicator = (MyIndicator) findViewById(R.id.myindicator);
    }

    private void init(){
        adapter = new MyPageAdapter(getSupportFragmentManager());
        pager.setOffscreenPageLimit(3);

        strTitle[0] = this.getString(R.string.normal_sudoku);
        strTitle[1] = this.getString(R.string.harder_sudoku);
        strTitle[2] = this.getString(R.string.fight_sudoku);

        NormalSudokuFragment normalSudokuFragment = new NormalSudokuFragment();
        HarderSudokuFragment harderSudokuFragment = new HarderSudokuFragment();
        FightSudokuFragment fightSudokuFragment = new FightSudokuFragment();

        listFragment.add(normalSudokuFragment);
        listFragment.add(harderSudokuFragment);
        listFragment.add(fightSudokuFragment);

        pager.setAdapter(adapter);
        indicator.setViewPager(pager);

    }


    class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            return listFragment.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getCount() {
            return listFragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return strTitle[position];
        }
    }
}
