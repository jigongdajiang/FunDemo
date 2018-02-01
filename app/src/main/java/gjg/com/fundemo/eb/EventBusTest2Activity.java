package gjg.com.fundemo.eb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.EventBus;

import gjg.com.fundemo.R;

/**
 * @author : gaojigong
 * @date : 2018/2/1
 * @description:
 */


public class EventBusTest2Activity extends AppCompatActivity {
    Button btnTest;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventbus_test2);
        btnTest = (Button) findViewById(R.id.btn_test);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post("我是来自2的信息");
            }
        });
    }
}
