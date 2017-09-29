package gjg.com.fundemo.exceptionhandler;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : gongdaocai
 * @date : 2017/9/18
 * FileName:
 * @description:
 * 异常捕获类
 * 单例
 */


public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "ExceptionHandler";
    private static final String CRASH_DIR_NAME = "crash";
    private static final String SP_CRASH = "sp_crash";
    private static final String CRASH_CACHE_NAME = "crash_cache_name";
    //单利模式
    private static ExceptionHandler instance;
    private boolean isOpen;
    private ExceptionHandler(){

    }
    public static ExceptionHandler getInstance(){
        if(null == instance){
            synchronized (ExceptionHandler.class){
                instance = new ExceptionHandler();
            }
        }
        return instance;
    }
    //上下文对象，这里一定要是Application，否则会内存泄露
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefalutUncaughtExceptionHandler;

    public void init(Context context,boolean open){
        this.mContext = context;
        this.isOpen = open;
        if(isOpen){
            //获取系统的异常捕获拦截器，目的是为了保证在捕获的同时，也保留系统默认的处理方式，
            // 这句代码要在Thread.setDefaultUncaughtExceptionHandler(this);的前面，否则将会导致uncaughtException不断重复被回调
            // 也就是系统的和当前的重复，导致不断重调，最终app会报无响应异常而卡死
            mDefalutUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            //设置当前系统的异常拦截器为此拦截器
            Thread.setDefaultUncaughtExceptionHandler(this);
        }
    }

    /**
     * 在这里拦截信息，缓存到本地
     */
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if(isOpen){
            Log.e(TAG,"被拦截的异常信息"+t.getName()+":\n"+obtainExceptionInfo(e));
            //1.将异常信息保存到本地
            String crashFileName = saveInfoToSD(e);
            //2.将文件信息进行缓存，方便下次启动时上传，或者扫描时上传
            if(!TextUtils.isEmpty(crashFileName)){
                cacheCrashFileName(crashFileName);
            }
            //保留系统的对异常的处理
            mDefalutUncaughtExceptionHandler.uncaughtException(t,e);
        }
    }

    /**
     * 缓存文件名，方便在其它地方获取上传
     * @param crashFileName
     */
    private void cacheCrashFileName(String crashFileName) {
        SharedPreferences sp = mContext.getSharedPreferences(SP_CRASH,Context.MODE_PRIVATE);
        sp.edit().putString(CRASH_CACHE_NAME,crashFileName).apply();
    }

    /**
     * 拼装设备版本等信息和异常信息并写入sd卡
     */
    private String saveInfoToSD(Throwable e) {
        String fileName = null;
        //用于组装缓存信息
        StringBuilder sb = new StringBuilder();
        //设备版本信息
        Map<String,String> devInfos = obtainSimpleInfo(mContext);
        for(Map.Entry<String,String> entry :devInfos.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();
            sb.append(key).append("=").append(value).append("\n");
        }
        //异常信息
        String exInfo = obtainExceptionInfo(e);
        sb.append(exInfo);
        //保存到内部存储，这里不放到sd卡是为了跳过权限，都崩溃了还要啥权限申请
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){//内部存储可用
            //创建文件夹
            File dir = new File(mContext.getFilesDir() + File.separator + CRASH_DIR_NAME + File.separator);
            //先删除之前的异常信息，每一次崩溃只保留一个异常信息
            if(dir.exists()){
                deleteDir(dir);
            }
            //如果不存在重新创建
            if(!dir.exists()){
                dir.mkdir();
            }
            try {
                fileName = dir.getAbsolutePath() + getAssignTime("yyyy_MM_dd_HH_mm") + ".txt";
                FileOutputStream fos = new FileOutputStream(fileName);
                fos.write(sb.toString().getBytes());
                fos.flush();
                fos.close();
            }catch (Exception ex){
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 根据当前时间获取文件名
     */
    private String getAssignTime(String fromat) {
        DateFormat df = new SimpleDateFormat(fromat);
        long currentTime = System.currentTimeMillis();
        return df.format(currentTime);
    }

    /**
     * 清空文件夹
     */
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()){
            String[] children = dir.list();
            //递归子目录
            for(int i=0; i<children.length; i++){
                boolean success = deleteDir(new File(dir,children[i]));
                if(!success){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 得到异常信息
     */
    private String obtainExceptionInfo(Throwable e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        printWriter.close();
        return stringWriter.toString();
    }

    /**
     * 得到一些设备和版本信息
     */
    private Map<String, String> obtainSimpleInfo(Context context) {
        Map<String,String> map = new HashMap<>();
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),PackageManager.GET_ACTIVITIES);
            map.put("versionName",packageInfo.versionName);
            map.put("versionCode", String.valueOf(packageInfo.versionCode));
            map.put("model", Build.MODEL);
            map.put("sdk_int", String.valueOf(Build.VERSION.SDK_INT));
            map.put("product",Build.PRODUCT);
            map.put("moble_info",getMobileInfo());
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取Build的所有属性信息
     * @return
     */
    private String getMobileInfo() {
        StringBuffer sb = new StringBuffer();
        try {
            Field[] fields = Build.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                String value = field.get(null).toString();
                sb.append(name).append("=").append(value).append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
    public File getCrashCacheFile(){
        SharedPreferences sp = mContext.getSharedPreferences(SP_CRASH,Context.MODE_PRIVATE);
        return new File(sp.getString(CRASH_CACHE_NAME,""));
    }
}
