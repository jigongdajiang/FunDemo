package gjg.com.fundemo.skin.skinframe.core;

import android.view.View;

import java.util.List;

import gjg.com.fundemo.skin.skinframe.attr.SkinAttr;


/**
 * @author : gongdaocai
 * @date : 2017/9/25
 * FileName:
 * @description:
 */


public class SkinView {
    private View mView;
    private List<SkinAttr> mSkinAttrs;

    public SkinView(View view, List<SkinAttr> skinAttrs) {
        this.mView = view;
        this.mSkinAttrs = skinAttrs;
    }

    public void skin(){
        for (SkinAttr skinAttr : mSkinAttrs) {
            skinAttr.skin(mView);
        }
    }
}
