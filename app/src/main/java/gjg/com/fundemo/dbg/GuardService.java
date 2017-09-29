package gjg.com.fundemo.dbg;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.annotation.Nullable;

import gjg.com.fundemo.GuardAIDL;

/**
 * @author : gongdaocai
 * @date : 2017/9/15
 * FileName:
 * @description: 守护服务，在守护进程中
 */


public class GuardService extends Service {
    private IBinder mBinder;
    private ServiceConnection mServiceConnection;
    @Override
    public void onCreate() {
        super.onCreate();
        if(mBinder == null){
            mBinder = new GuardBinder();
        }
        if(mServiceConnection == null){
            mServiceConnection = new GuardServiceConnection();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //绑定守护进程中的Service
        Intent funIntent = new Intent(GuardService.this,FunService.class);
        GuardService.this.bindService(funIntent,mServiceConnection, Context.BIND_IMPORTANT);
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    private class GuardBinder extends GuardAIDL.Stub{

    }
    private class GuardServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //连接
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //断开连接
            //重新启动守护进程中的Service
            Intent fundIntent = new Intent(GuardService.this,FunService.class);
            startService(fundIntent);
            //重新绑定守护进程中Service
            GuardService.this.bindService(fundIntent,mServiceConnection, Context.BIND_IMPORTANT);
        }
    }
}
