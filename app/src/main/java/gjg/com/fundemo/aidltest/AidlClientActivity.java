package gjg.com.fundemo.aidltest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import gjg.com.fundemo.R;
import gjg.com.fundemo.UserAidl;

/**
 * @author : gongdaocai
 * @date : 2017/9/12
 * FileName:
 * @description:
 */


public class AidlClientActivity extends AppCompatActivity {
    private TextView tv_user_name;
    private TextView tv_user_pwd;
    private ServiceConnection mServiceConnection;
    private UserAidl mUserAidl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client);
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);
        tv_user_pwd = (TextView) findViewById(R.id.tv_user_pwd);
    }

    public void getAidlData(View view) {
        try {
            tv_user_name.setText(mUserAidl.getUserName());
            tv_user_pwd.setText(mUserAidl.getUserPwd());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void unBindAidlService(View view) {
        unbindService(mServiceConnection);
    }

    public void bindAidlService(View view) {
        Intent intent = new Intent(this,MessageService.class);
        intent.setAction("com.aidl.service.MessageService");
        mServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mUserAidl = UserAidl.Stub.asInterface(service);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(intent,mServiceConnection,BIND_AUTO_CREATE);
    }

}
