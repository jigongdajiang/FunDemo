package gjg.com.fundemo.skin.skinframe.callback;

import gjg.com.fundemo.skin.skinframe.core.SkinResource;

/**
 * @author : gongdaocai
 * @date : 2017/9/26
 * FileName:
 * @description:
 * 换肤拓展回调
 */


public interface ISkinChangeListener {
    /**
     * 用于未定义的特殊属性的换肤，或者自定义View的换肤，有了资源器就获取属性，并改变属性
     * @param skinResource
     */
    void changeSkin(SkinResource skinResource);
}
