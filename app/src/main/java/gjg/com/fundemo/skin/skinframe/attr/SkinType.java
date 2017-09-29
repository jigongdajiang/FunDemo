package gjg.com.fundemo.skin.skinframe.attr;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import gjg.com.fundemo.skin.skinframe.core.SkinManager;
import gjg.com.fundemo.skin.skinframe.core.SkinResource;

/**
 * @author : gongdaocai
 * @date : 2017/9/25
 * FileName:
 * @description:
 */


public enum  SkinType {
    TEXTCOLOR("textColor") {
        /**
         * 根据资源名称 如 @color/main_big_color中的main_big_color 解析资源属性进行重新设置
         */
        @Override
        public void skin(View view, String resName) {
            if(view != null && view instanceof TextView){
                SkinResource skinResource = getSkinResource();
                ColorStateList color = skinResource.getColorByName(resName);
                if(null != color){
                    ((TextView)view).setTextColor(color);
                }
            }

        }
    },BACKGROUND("background") {
        @Override
        public void skin(View view, String resName) {
            if(view != null){
                SkinResource skinResource = getSkinResource();
                Drawable drawable = skinResource.getDrawableByName(resName);
                if(null != drawable){
                    view.setBackgroundDrawable(drawable);
                }else{
                    ColorStateList color = skinResource.getColorByName(resName);
                    if(null != color){
                        view.setBackgroundColor(color.getDefaultColor());
                    }
                }
            }
        }
    },SRC("src") {
        @Override
        public void skin(View view, String resName) {
            if(view != null && view instanceof ImageView){
                SkinResource skinResource = getSkinResource();
                Drawable drawable = skinResource.getDrawableByName(resName);
                if(null != drawable ){
                    ((ImageView)view).setImageDrawable(drawable);
                }
            }
        }
    };

    private String mAttrName;
    SkinType(String attrName){
        this.mAttrName = attrName;
    }
    public abstract void skin(View view, String resName);

    public String getAttrName() {
        return mAttrName;
    }

    SkinResource getSkinResource(){
        return SkinManager.getInstance().getSkinResource();
    }
}
