package gjg.com.fundemo.skin.skinframe.attr;

import android.view.View;

/**
 * @author : gongdaocai
 * @date : 2017/9/25
 * FileName:
 * @description:
 */


public class SkinAttr {
    private String mResName;//资源文件的名称 如main_bg_color
    private SkinType mSkinType;

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName = resName;
        this.mSkinType = skinType;
    }

    public void skin(View view) {
        mSkinType.skin(view,mResName);
    }
}
