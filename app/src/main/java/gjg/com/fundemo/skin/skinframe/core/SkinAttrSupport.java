package gjg.com.fundemo.skin.skinframe.core;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import gjg.com.fundemo.skin.skinframe.attr.SkinAttr;
import gjg.com.fundemo.skin.skinframe.attr.SkinType;

/**
 * @author : gongdaocai
 * @date : 2017/9/25
 * FileName:
 * @description: 皮肤属性解析的支持类
 */


public class SkinAttrSupport {
    private static final String TAG = "SkinAttrSupport";

    /**
     * 解析定义的属性
     * @param context
     * @param attrs
     * @return
     */
    public static List<SkinAttr> getSkinAttrs(Context context, AttributeSet attrs) {
        List<SkinAttr> skinAttrs = new ArrayList<>();
        //获取所有属性的个数
        int attrCount = attrs.getAttributeCount();
        for(int index = 0; index < attrCount; index++){
            String attrName = attrs.getAttributeName(index);
            String attrValue = attrs.getAttributeValue(index);
//            Log.e(TAG,"attrName-->"+attrName + ";  attrValue-->"+attrValue);
            //根据属性名称，得到类型
            SkinType skinType  = getSkinType(attrName);
            //根据资源值，或者资源名称
            String resName = getResName(context,attrValue);
            if(null != skinType && !TextUtils.isEmpty(resName)){
//                Log.e(TAG,"skinType-->"+skinType.toString()+";  resName-->"+resName);
                SkinAttr skinAttr = new SkinAttr(resName,skinType);
                skinAttrs.add(skinAttr);
            }
        }
        return skinAttrs;
    }

    private static String getResName(Context context, String attrValue) {
        //属性值以@开头的是通过引用方式，有些直接"#FFFFFF"的将不解析，因为换肤是根据加载相同资源名称的皮肤包实现的
        if(!TextUtils.isEmpty(attrValue) && attrValue.startsWith("@")){
            String resIdStr = attrValue.substring(1);
            //转化为id
            int resId = Integer.parseInt(resIdStr);
            //根据资源id获取资源名称
            String resName = context.getResources().getResourceEntryName(resId);
            return resName;
        }
        return null;
    }

    private static SkinType getSkinType(String attrName) {
        //遍历枚举集合
        SkinType[] skinTypes = SkinType.values();
        for (SkinType skinType : skinTypes) {
            //只关注和皮肤有关的属性
            if(skinType.getAttrName().equals(attrName)){
                return skinType;
            }
        }
        return null;
    }
}
