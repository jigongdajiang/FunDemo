package gjg.com.fundemo.dbg;

import android.app.ActivityManager;
import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.IntDef;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.util.List;

/**
 * @author : gongdaocai
 * @date : 2017/9/15
 * FileName:
 * @description: 系统定时唤醒进程
 */


@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class GuardJob extends JobService {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //建立JobInfo
        JobInfo.Builder builder = new JobInfo.Builder(1,new ComponentName(this,GuardJob.class));
        builder.setPeriodic(500);//轮训时间
        JobInfo jobInfo = builder.build();
        //创建进度器
        JobScheduler jobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.schedule(jobInfo);
        return START_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        //当前服务是否活着，如果没有或者则启动功能服务
        if(!isServiceWork(GuardJob.this,FunService.class.getName())){
            Log.e("alive","onStartJob");
            startService(new Intent(GuardJob.this,FunService.class));
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    /**
     *
     * @param context
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return
     */
    private boolean isServiceWork(Context context,String serviceName){
        boolean isWork = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfoList = activityManager.getRunningServices(100);
        if(runningServiceInfoList.size() <= 0){
            return false;
        }
        for(int i=0; i<runningServiceInfoList.size(); i++){
            String name = runningServiceInfoList.get(i).service.getClassName().toString();
            if(serviceName.equals(name)){
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
