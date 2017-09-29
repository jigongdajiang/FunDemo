package gjg.com.fundemo;

import android.app.Application;

import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import java.util.ArrayList;
import java.util.List;

import gjg.com.fundemo.exceptionhandler.ExceptionHandler;
import gjg.com.fundemo.fix.FixManager;
import gjg.com.fundemo.skin.skinframe.core.SkinManager;

/**
 * @author : gongdaocai
 * @date : 2017/9/15
 * FileName:
 * @description:
 */


public class FunApplication extends Application {
    public static FunApplication application;
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        exceptionCrash();
//        fixDexTest();
//        alifix();
    }

    /**
     * 初始化异常捕获
     */
    private void exceptionCrash() {
        ExceptionHandler.getInstance().init(this,false);
    }
    /**
     * 熟悉dex修复原理用，实际不可用
     */
    private void fixDexTest() {
        //加载之前的已经修复过的
        FixManager fixManager = new FixManager(this);
        try {
            fixManager.loadFixDex();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 初始化ali热修复
     */
    private void alifix() {
        mAppPatchLoadStatusListeners = new ArrayList<>();
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(),0).versionName;
        }catch (Exception e){
            appVersion = "1.0.0";
        }
        String idSecret="24630819-1";
        String appSecret="e7464622d6436f334a164fa463e363d1";
        String rsaSecret="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCVw50eRD+sr7mY48GMmFeDYf0DBbg0Xff02ULopWWdql7JXLmH+ur0x8s2voEIH/ujlHr82kMftRGIo2m2lZEOR/A/pQf8qPPEqRv+Lq5YblFhrouHfz+dIpaSudtmE6fMRMtwaeE5FxvwNhwPE6TU4NadUqeRQovaw53MQ7v3vp4VjdxJY2CiN/9zC6vp1Cb26dnZyAcYy753ZW2Apypw/BpiQmoh5pzHjDxQwqvPj5Jt/dTFFNN6Am4+5/dDAV4b5rbmyfUsnGBFnSXo6Cyuq2MyjfwBDj5I2pHbvtd65agHv/V8nW9fCXg4eGrh36iCky/Aa4U7PX3VeVgKXZlbAgMBAAECggEBAIodP03FVQ+Vc00rZbiaWcKhYGskIC+zJVPZ9VmGBwNzNjzvVW6xGXOquZ7/6xEqJbBC43VCeDrKiqP/SCOJJaAM+eRH9uisizqnVliAhmqpOkYiaS0JTgpE9qXT3yDxKYM5QoCmNH2o8SwDSrcgv5dOzr2Bl2VgAIg8dM8G/FdJOyODXOSDgBFLt3gv06AtCgBnR0DFrPUmyt9nCpiHB/vCK0IJ5LAvWmxDp0Q3oejIwJsA/0h7KOMrH2iPLe7ApJZvtvFPdBW4MdB9N51muDDJLWYi7IfxXQQUwHGH1HLGoghEv/Wgi8t3QW6cJgkiBFait130STDWYy7qx7tFymkCgYEAy7Ig3ZjZz+/MxnUdU/hmsI/ZuE2nZs0475SyoFzRSNO1suGzQ+vzW5QmtbR2BXQ0uNn8TTybwh5kRBa7o/8Lh5JcKXlxTmJrCshivuuQf13s12PX9pKUOcNgPE10Ywk9d3ShZgxke0HCUDbbXIKy7ZC5nKP6Sx/R3DtUL04xaE8CgYEAvDhN8QExAercMX2ulsRAVKRzEK9X2Ql8O11Wq9DFbphxA9+9CABnEorMp1/BTdVtsVpcMEjdKNSioYCu/mdsVXepQViHz1MSVcL36QScB6OYyMdYBBs5kVsRKP/i4ltwxnRZHhgl3ryi9fMcwTnpy83fMO8LuP6mNv0ne1SbrzUCgYEAyg4DHe2OOf6EIWRw2CeyLXg0HucgpA/0Z2sYGYyYqWYgebX67egyuWfnUh0VaWmRFsBcznzNgKxHB2eJcC8BT9T2CMoHhBqKVeLaWMowxrbzxeCrjFJwKDBO2OJ4DflPeJw/sYufbMiZKJqR1F1tgcwq4l+iWzzQHmbXpikpR28CgYBv5/YHRZMStcWnMzd7ckFhUckL8YSEiQww64TdcKoB4GbJS9xDBqJBtv6IwPjYQv4OBVBscNX1Mb9j0HO61Fjn09ZxAKb2kEiFbv8grtnp/M2ZClR9VIZ8sq1YOBN4S6+YfpKBvw+9B2DIZHwDMmCcCNIbcPUCT30ZkphHzn0lxQKBgBApH3SQla7VOrFr394i/AH2iYP4YDJ8WGqNeAevCxUD8c6ZPon4iLBG2g0s5QpRPgfnlzQgI6k/sEK22ZsWU7Or09jNBkMwlgerX1oTex0idRHOFxVHnLmDPwJliSb1th5G/ccYqyDz6siZ/MwTbP0Jz3D8pZQ4YiBvdeCOpKbL";
        SophixManager.getInstance()
                .setContext(this)
                .setAppVersion(appVersion)
                .setEnableDebug(true)//默认为false, 是否调试模式, 调试模式下会输出日志以及不进行补丁签名校验.
                .setAesKey(null)//用户自定义aes秘钥, 会对补丁包采用对称加密。这个参数值必须是16位数字或字母的组合，是和补丁工具设置里面AES Key保持完全一致, 补丁才能正确被解密进而加载。
                .setSecretMetaData(idSecret,appSecret,rsaSecret)//可以不在AndroidManifest设置,放到代码里面进行设置可以自定义混淆代码，更加安全，设置会覆盖AndroidManifest里面的设置，如果对应的值设为null，默认会在使用AndroidManifest里面的。
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(int mode, int code, String info, int handlePatchVersion) {
                        if(mAppPatchLoadStatusListeners != null && mAppPatchLoadStatusListeners.size() > 0){
                            for (AppPatchLoadStatusListener listener : mAppPatchLoadStatusListeners) {
                                listener.onLoad(mode,code,info,handlePatchVersion);
                            }
                        }
                    }
                })//补丁加载状态会回调给该接口,这采用观察者模式，让其在该响应的地方交由实际业务去处理
                //.setUnsupportedModel(null,0)//把不支持的设备加入黑名单，加入后不会进行热修复。modelName为该机型上Build.MODEL,sdkVersionInt就是该机型的Android版本，也就是Build.VERSION.SDK_INT ,0则对应该机型所有安卓版本。
                .initialize();//必要的初始化工作以及如果本地有补丁的话会加载补丁, 但不会自动请求补丁
    }


    private List<AppPatchLoadStatusListener> mAppPatchLoadStatusListeners;

    public void regsterAppPatchLoadStatusListener(AppPatchLoadStatusListener listener){
        if(null != mAppPatchLoadStatusListeners){
            mAppPatchLoadStatusListeners.add(listener);
        }
    }
    public void unRegsterAppPatchLoadStatusListener(AppPatchLoadStatusListener listener){
        if(null != mAppPatchLoadStatusListeners){
            mAppPatchLoadStatusListeners.remove(listener);
        }
    }
    /**
     * 补丁更新的接口回调，每次调用补丁更新queryAndLoadNewPatch() 是会回调，转交该类处理，采用观察者模式支持任意位置处理
     */
    public interface AppPatchLoadStatusListener{
        /**
         *
         * @param mode 补丁模式
         *  0:正常请求模式
         *  1:扫码模式
         *  2:本地补丁模式
         * @param code
         *  code: 0 准备好开始
         *  code: 1 补丁加载成功
         *  code: 2 没有初始化热修复补丁sdk或初始化失败
         *  code: 3 只能在主线程请求查询
         *  code: 4 当前的设备不支持热修复
         *  code: 5 拉取补丁信息详情失败，请查看log信息
         *  code: 6 服务端没有最新可用的补丁
         *  code: 7 目标补丁版本号<=当前版本号，将停止下载补丁
         *  code: 8 补丁下载失败
         *  code: 9 补丁下载成功
         *  code: 10 补丁文件损坏
         *  code: 11 RSASECRET错误，官网中的密钥是否正确请检查
         *  code: 12 当前应用已经存在一个旧补丁, 应用重启尝试加载新补丁
         *  code: 13 补丁加载失败, 导致的原因很多种, 比如UnsatisfiedLinkError等异常, 此时应该严格检查logcat异常日志
         *  code: 14 没有发现可下载的补丁
         *  code: 15 appid 错误
         *  code: 16 APPSECRET错误，官网中的密钥是否正确请检查
         *  code: 17 请求不可用，因为已经欠费
         *  code: 18 一键清除补丁
         *  code: 19 连续两次queryAndLoadNewPatch()方法调用不能短于3s
         *  code: 20 补丁无效，补丁不存在或者目录不存在或者不是一个jar压缩文件
         *  code: 21 debuggable是flase!禁止加载本地安全补丁
         * @param info
         *  code 对应的信息描述
         * @param handlePatchVersion 当前处理的补丁版本号
         *  0:无
         *  1:本地补丁
         *  其它:后台补丁
         *
         *
         *  example:
         *      //补丁加载回调通知
         *      if (code == PatchStatus.CODE_LOAD_SUCCESS) {
         *           //表明补丁加载成功
         *       } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
         *           // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
         *           // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
         *       } else {
         *           // 其它错误信息, 查看PatchStatus类说明
         *       }
         */
        void onLoad(int mode, int code, String info, int handlePatchVersion);
    }
}
