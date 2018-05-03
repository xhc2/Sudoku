package sudoku.myself.xhc.com.sudoku;

import android.app.Application;

import com.xhc.logger.L;


/**
 * Created by xhc on 2016/2/16.
 * 真机上测试的时候添加到清单文件中。
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        CrashWoodpecker.fly().to(this);
        L.init("xhc",BuildConfig.DEBUG);
    }
}
