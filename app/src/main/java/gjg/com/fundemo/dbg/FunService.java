package gjg.com.fundemo.dbg;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import gjg.com.fundemo.GuardAIDL;
import gjg.com.fundemo.util.RxManager;

/**
 * @author : gongdaocai
 * @date : 2017/9/15
 * FileName:
 * @description:  功能服务
 */


public class FunService extends Service {
    private IBinder mBinder;
    private ServiceConnection mServiceConnection;
    private RxManager manager;
    @Override
    public void onCreate() {
        super.onCreate();
        if(mBinder == null){
            mBinder = new FunBinder();
        }
        if(mServiceConnection == null){
            mServiceConnection = new FunServiceConnection();
        }
        if(manager == null){
            manager = new RxManager();
        }

        new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        //一直活着
                        Thread.sleep(3000);
                        Log.e("alive","funService is Running");
                        manager.post("guard","guard"+System.currentTimeMillis());
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //绑定守护进程中的Service
        Intent guradIntent = new Intent(FunService.this,GuardService.class);
        FunService.this.bindService(guradIntent,mServiceConnection, Context.BIND_IMPORTANT);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private class FunBinder extends GuardAIDL.Stub{

    }
    private class FunServiceConnection implements ServiceConnection{

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            //重新启动守护进程中的Service
            Intent guradIntent = new Intent(FunService.this,GuardService.class);
            startService(guradIntent);
            //重新绑定守护进程中Service
            FunService.this.bindService(guradIntent,mServiceConnection, Context.BIND_IMPORTANT);
        }
    }
}
