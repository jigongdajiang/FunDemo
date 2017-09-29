package gjg.com.fundemo.skin.skinframe.core;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import java.lang.reflect.Method;

/**
 * @author : gongdaocai
 * @date : 2017/9/25
 * FileName:
 * @description: 管理资源文件
 */


public class SkinResource {
    //整个资源通过这个对象获取
    private Resources mResources;
    private String mPackageName;

    /**
     * 根据皮肤资源路径创建对应的Resources
     */
    public SkinResource(Context context, String skinPath) {
        try{
            Resources surperResource = context.getResources();
            Configuration configuration = new Configuration();
            DisplayMetrics displayMetrics = surperResource.getDisplayMetrics();
            AssetManager assetManager = AssetManager.class.newInstance();
            //添加本地下载好的皮肤
            Method methodAddAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath",String.class);
            methodAddAssetPath.setAccessible(true);
            methodAddAssetPath.invoke(assetManager,skinPath);
            mResources = new Resources(assetManager,displayMetrics,configuration);
            //获取皮肤apk的包名
            mPackageName = context
                    .getPackageManager()
                    .getPackageArchiveInfo(skinPath, PackageManager.GET_ACTIVITIES)
                    .packageName;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Drawable getDrawableByName(String resName){
        try {
            int drawableResId = mResources.getIdentifier(resName,"drawable",mPackageName);
            return mResources.getDrawable(drawableResId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }


    }
    public ColorStateList getColorByName(String resName){
        try {
            int colorResId = mResources.getIdentifier(resName,"color",mPackageName);
            return mResources.getColorStateList(colorResId);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
