package gjg.com.fundemo.dbg;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import gjg.com.fundemo.R;
import gjg.com.fundemo.util.RxManager;
import rx.functions.Action1;

/**
 * @author : gongdaocai
 * @date : 2017/9/19
 * FileName:
 * @description:
 */


public class DbGuardTestActivity extends AppCompatActivity {
    //开关，实际使用时可通过配置指定
    private boolean isOpenGuard = true;
    private RxManager manager;
    private TextView tv_show_guard_info;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_test);
        if(manager == null){
            manager = new RxManager();
        }
        tv_show_guard_info = (TextView) findViewById(R.id.tv_show_guard_info);
        manager.on("guard", new Action1<String>() {
            @Override
            public void call(String o) {
                tv_show_guard_info.setText(o);
            }
        },"");
    }

    public void openGuard(View view) {
        openGuardFun();
    }

    /**
     * 开启双进程守护功能，实际项目中一般在application实现
     */
    private void openGuardFun() {
        if(isOpenGuard){
            startService(new Intent(this,FunService.class));
            startService(new Intent(this,GuardService.class));
            startService(new Intent(this,GuardJob.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        manager.clear();
    }
}
