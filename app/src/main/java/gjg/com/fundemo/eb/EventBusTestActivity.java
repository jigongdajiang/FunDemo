package gjg.com.fundemo.eb;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import gjg.com.fundemo.R;

/**
 * @author : gaojigong
 * @date : 2018/2/1
 * @description:
 */


public class EventBusTestActivity extends AppCompatActivity {
    private TextView tvTest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus_test1);
        tvTest = (TextView) findViewById(R.id.tv_test);
        EventBus.getDefault().register(this);
    }

    /**
     *
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void test(String text){
        tvTest.setText(text);
    }

    public void toTestActivity(View view) {
        startActivity(new Intent(this,EventBusTest2Activity.class));
    }
}
