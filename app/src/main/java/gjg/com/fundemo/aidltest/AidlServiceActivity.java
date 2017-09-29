package gjg.com.fundemo.aidltest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import gjg.com.fundemo.R;

/**
 * @author : gongdaocai
 * @date : 2017/9/12
 * FileName:
 * @description:
 */


public class AidlServiceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_service);
    }

    public void startAidlService(View view) {
        Intent intent = new Intent(this,MessageService.class);
        intent.setAction("com.aidl.service.MessageService");
        startService(new Intent(this,MessageService.class));
    }

    public void stopAidlService(View view) {
        Intent intent = new Intent(this,MessageService.class);
        intent.setAction("com.aidl.service.MessageService");
        stopService(intent);
    }

    public void openClient(View view) {
        startActivity(new Intent(this,AidlClientActivity.class));
    }
}
