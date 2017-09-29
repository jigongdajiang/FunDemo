package gjg.com.fundemo.aidltest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import gjg.com.fundemo.UserAidl;

/**
 * @author : gongdaocai
 * @date : 2017/9/12
 * FileName:
 * @description:
 */


public class MessageService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mIbinder;
    }
    private IBinder mIbinder = new UserAidl.Stub() {
        @Override
        public String getUserName() throws RemoteException {
            return "服务端来的名字";
        }

        @Override
        public String getUserPwd() throws RemoteException {
            return "服务端的加密密码";
        }
    };

}
