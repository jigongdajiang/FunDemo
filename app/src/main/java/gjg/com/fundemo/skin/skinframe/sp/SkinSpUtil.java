package gjg.com.fundemo.skin.skinframe.sp;

import android.content.Context;
import android.content.SharedPreferences;

import gjg.com.fundemo.skin.skinframe.config.SkinConfig;

/**
 * @author : gongdaocai
 * @date : 2017/9/26
 * FileName:
 * @description:
 */


public class SkinSpUtil {
    private static SkinSpUtil mInstance;
    private Context mContext;
    private SharedPreferences mSp;
    private SharedPreferences.Editor mEditor;
    private SkinSpUtil(){

    }
    public static SkinSpUtil getInstance(){
        if(null == mInstance){
            synchronized (SkinSpUtil.class){
                if(null == mInstance){
                    mInstance = new SkinSpUtil();
                }
            }
        }
        return mInstance;
    }
    public void init(Context context){
        mContext = context.getApplicationContext();
    }

    /**
     * 保存当前皮肤路径
     * @param skinPath
     */
    public void saveSkinPath(String skinPath){
        SharedPreferences sharedPreferences = mContext.getApplicationContext().getSharedPreferences(SkinConfig.SP_SKIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(SkinConfig.SP_SKIN_CURRENT_PATH,skinPath);
        editor.apply();
    }

    /**
     * 获取当前皮肤路径
     * @return
     */
    public String getCurrentSkinPath(){
        SharedPreferences sharedPreferences = mContext.getApplicationContext().getSharedPreferences(SkinConfig.SP_SKIN,Context.MODE_PRIVATE);
        return sharedPreferences.getString(SkinConfig.SP_SKIN_CURRENT_PATH,"");
    }

    /**
     * 移除当前皮肤路径
     */
    public void removeCurrentSkinPath(){
        SharedPreferences sharedPreferences = mContext.getApplicationContext().getSharedPreferences(SkinConfig.SP_SKIN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(SkinConfig.SP_SKIN_CURRENT_PATH);
        editor.apply();
    }

}
