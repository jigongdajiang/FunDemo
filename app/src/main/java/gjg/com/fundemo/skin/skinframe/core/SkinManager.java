package gjg.com.fundemo.skin.skinframe.core;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gjg.com.fundemo.skin.skinframe.callback.ISkinChangeListener;
import gjg.com.fundemo.skin.skinframe.config.SkinConfig;
import gjg.com.fundemo.skin.skinframe.sp.SkinSpUtil;

/**
 * @author : gongdaocai
 * @date : 2017/9/25
 * FileName:
 * @description: 换肤管理类
 */


public class SkinManager {
    private static SkinManager mInstance;
    private SkinResource mSkinResource;
    private Context mContext;
    private static boolean mHasInit = false;

    private Map<ISkinChangeListener,List<SkinView>> mSkinViewMap = new HashMap<>();
    static {
        mInstance = new SkinManager();
    }
    public static SkinManager getInstance() {
        return mInstance;
    }
    public void init(Context context){
        //防止内存泄露，上下文使用ApplicationContext
        this.mContext = context.getApplicationContext();
        //初始化缓存工具
        SkinSpUtil.getInstance().init(context);
        String skinPath = SkinSpUtil.getInstance().getCurrentSkinPath();
        if(SkinConfig.SKIN_FILE_QUALIFIED == autoLoadCheckSkinPath(skinPath)){
            mSkinResource = new SkinResource(mContext,skinPath);
        }
        mHasInit = true;
    }

    public boolean isInit() {
        return mHasInit;
    }

    /**
     * 只有手动才会调用
     * 加载皮肤并换肤
     */
    public int loadSkin(String skinPath) {
        int checkResult = SkinManager.getInstance().manualLoadCheckSkinPath(skinPath);
        if(SkinConfig.SKIN_FILE_QUALIFIED == checkResult){
            changeSkin(skinPath);
            //保存当前皮肤路径
            saveCurrentSkinPath(skinPath);
            return SkinConfig.SKIN_CHANGE_SUCCESS;
        }
        return checkResult;
    }

    private void changeSkin(String skinPath) {
        mSkinResource = new SkinResource(mContext,skinPath);
        //改变皮肤，将已经添加的SkinView调动起来
        for (ISkinChangeListener skinChangeListener : mSkinViewMap.keySet()) {
            List<SkinView> skinViews = mSkinViewMap.get(skinChangeListener);
            for (SkinView skinView : skinViews) {
                skinView.skin();
            }
            skinChangeListener.changeSkin(mSkinResource);
        }
    }

    /**
     * 重置为app默认皮肤
     * @return
     */
    public int restoreSkin(){
        String appSkinPath = mContext.getPackageResourcePath();
        String currentSkinPath = SkinSpUtil.getInstance().getCurrentSkinPath();
        if(!TextUtils.isEmpty(currentSkinPath)){
            changeSkin(appSkinPath);
            SkinSpUtil.getInstance().removeCurrentSkinPath();
            return SkinConfig.SKIN_CHANGE_SUCCESS;
        }
        return SkinConfig.SKIN_CHANGE_UN_NEED_RESTORE;

    }
    private void saveCurrentSkinPath(String skinPath) {
        SkinSpUtil.getInstance().saveSkinPath(skinPath);
    }

    /**
     * 自动加载皮肤时的验证
     */
    public int autoLoadCheckSkinPath(String skinPath){
        if(TextUtils.isEmpty(skinPath)){
            //文件是为空
            return SkinConfig.SKIN_FILE_NOT_EXIST;
        }
        File file = new File(skinPath);
        if(!file.exists()){
            //文件是不存在
            SkinSpUtil.getInstance().removeCurrentSkinPath();
            return SkinConfig.SKIN_FILE_NOT_EXIST;
        }
        //获取皮肤apk的包名
        String packageName = mContext
                .getPackageManager()
                .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                .packageName;
        if(TextUtils.isEmpty(packageName)){
            //不是合法apk皮肤包
            file.delete();
            SkinSpUtil.getInstance().removeCurrentSkinPath();
            return SkinConfig.SKIN_FILE_ERROR;
        }
        if(!signatureVerify(skinPath)){
            //签名校验失败
            file.delete();
            SkinSpUtil.getInstance().removeCurrentSkinPath();
            return SkinConfig.SKIN_FILE_SIGNATURE_VERIFY_FAILURE;
        }
        return SkinConfig.SKIN_FILE_QUALIFIED;
    }

    public int manualLoadCheckSkinPath(String skinPath){
        String currentPath = SkinSpUtil.getInstance().getCurrentSkinPath();
        if(!TextUtils.isEmpty(currentPath) && currentPath.equals(skinPath)){
            //重复加载了
            return SkinConfig.SKIN_FILE_SAME_CURRENT;
        }
        return autoLoadCheckSkinPath(skinPath);
    }

    /**
     * 进行签名校验
     * @param skinPath
     * @return
     */
    private boolean signatureVerify(String skinPath) {
        return true;
    }




    /**
     * 得到当前已添加的要换肤的空间几何
     * @param activity
     * @return
     */
    public List<SkinView> getSkinViews(Activity activity) {
        return mSkinViewMap.get(activity);
    }

    /**
     * 拦截时注入要换肤的空间
     * @param skinChangeListener
     * @param skinViews
     */
    public void register(ISkinChangeListener skinChangeListener, List<SkinView> skinViews) {
        mSkinViewMap.put(skinChangeListener,skinViews);
    }

    public void unRegister(ISkinChangeListener skinChangeListener){
        mSkinViewMap.remove(skinChangeListener);
    }

    /**
     * 获取当前的资源管理
     * @return
     */
    public SkinResource getSkinResource() {
        return mSkinResource;
    }
}
