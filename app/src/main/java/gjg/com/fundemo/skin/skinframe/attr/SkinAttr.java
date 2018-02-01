package gjg.com.fundemo.skin.skinframe.attr;

import android.view.View;

/**
 * @author : gongdaocai
 * @date : 2017/9/25
 * FileName:
 * @description:
 * 皮肤属性对象
 */


public class SkinAttr {
    private String mResName;//资源文件的名称 如main_bg_color
    private SkinType mSkinType;//皮肤属性类型对象

    public SkinAttr(String resName, SkinType skinType) {
        this.mResName = resName;
        this.mSkinType = skinType;
    }

    public void skin(View view) {
        mSkinType.skin(view,mResName);
    }
}
