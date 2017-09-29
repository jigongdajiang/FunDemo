package gjg.com.fundemo.skin.skinframe.core;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.VectorEnabledTintResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

import gjg.com.fundemo.skin.skinframe.attr.SkinAttr;
import gjg.com.fundemo.skin.skinframe.callback.ISkinChangeListener;
import gjg.com.fundemo.skin.skinframe.config.SkinConfig;
import gjg.com.fundemo.skin.skinframe.inflater.SkinAppCompatViewInflater;
import gjg.com.fundemo.skin.skinframe.sp.SkinSpUtil;

/**
 * @author : gongdaocai
 * @date : 2017/9/26
 * FileName:
 * @description:
 */


public class BaseSkinActivity extends AppCompatActivity implements LayoutInflaterFactory, ISkinChangeListener {
    private static final String TAG = "BaseSkinActivity";
    private static final boolean IS_PRE_LOLLIPOP = Build.VERSION.SDK_INT < 21;

    private SkinAppCompatViewInflater mAppCompatViewInflater;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        installMyViewFactory();
        super.onCreate(savedInstanceState);
        //在得到具体控件后，如果有当前皮肤路径且合格
        String currentSkinPath = SkinSpUtil.getInstance().getCurrentSkinPath();
        if (SkinConfig.SKIN_FILE_QUALIFIED == SkinManager.getInstance().autoLoadCheckSkinPath(currentSkinPath)) {
            //调用自定义控件或者特殊的换肤操作
            changeSkin(SkinManager.getInstance().getSkinResource());
        }
    }

    /**
     * 拦截View的创建
     */
    private void installMyViewFactory() {
        if (!SkinManager.getInstance().isInit()) {
            SkinManager.getInstance().init(this);
        }
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (layoutInflater.getFactory() == null) {
            //仿照AppCompatActivity 的方法，这里一旦自己设置了，将不会走AppCompatActivity中的工厂
            LayoutInflaterCompat.setFactory(layoutInflater, this);
        }

    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        // 创建View
        View view = createView(parent, name, context, attrs);
//        Log.e(TAG,"View-->"+view);
        // 解析属性 src background textColor
        // 1. 一个Activity 对应对个SkinView
        List<SkinAttr> skinAttrs = SkinAttrSupport.getSkinAttrs(context, attrs);
        if (null != view) {
            SkinView skinView = new SkinView(view, skinAttrs);
            // 统一交给SkinManager去管理
            manageSkinView(skinView);
            String currentSkinPath = SkinSpUtil.getInstance().getCurrentSkinPath();
            if (SkinConfig.SKIN_FILE_QUALIFIED == SkinManager.getInstance().autoLoadCheckSkinPath(currentSkinPath)) {
                //自动切换皮肤
                skinView.skin();
            }
        }
        return view;
    }

    /**
     * 管理SkinView
     *
     * @param skinView
     */
    private void manageSkinView(SkinView skinView) {
        List<SkinView> skinViews = SkinManager.getInstance().getSkinViews(this);
        if (null == skinViews) {
            skinViews = new ArrayList<>();
            SkinManager.getInstance().register(this, skinViews);
        }
        skinViews.add(skinView);
    }

    /**
     * 仿照系统逻辑，做好兼容
     */
    public View createView(View parent, final String name, @NonNull Context context,
                           @NonNull AttributeSet attrs) {
        if (mAppCompatViewInflater == null) {
            mAppCompatViewInflater = new SkinAppCompatViewInflater();
        }

        boolean inheritContext = false;
        if (IS_PRE_LOLLIPOP) {
            inheritContext = (attrs instanceof XmlPullParser)
                    // If we have a XmlPullParser, we can detect where we are in the layout
                    ? ((XmlPullParser) attrs).getDepth() > 1
                    // Otherwise we have to use the old heuristic
                    : shouldInheritContext((ViewParent) parent);
        }

        return mAppCompatViewInflater.createView(parent, name, context, attrs, inheritContext,
                IS_PRE_LOLLIPOP, /* Only read android:theme pre-L (L+ handles this anyway) */
                true, /* Read read app:theme as a fallback at all times for legacy reasons */
                VectorEnabledTintResources.shouldBeUsed() /* Only tint wrap the context if enabled */
        );
    }

    private boolean shouldInheritContext(ViewParent parent) {
        if (parent == null) {
            // The initial parent is null so just return false
            return false;
        }
        final View windowDecor = getWindow().getDecorView();
        while (true) {
            if (parent == null) {
                // Bingo. We've hit a view which has a null parent before being terminated from
                // the loop. This is (most probably) because it's the root view in an inflation
                // call, therefore we should inherit. This works as the inflated layout is only
                // added to the hierarchy at the end of the inflate() call.
                return true;
            } else if (parent == windowDecor || !(parent instanceof View)
                    || ViewCompat.isAttachedToWindow((View) parent)) {
                // We have either hit the window's decor view, a parent which isn't a View
                // (i.e. ViewRootImpl), or an attached view, so we know that the original parent
                // is currently added to the view hierarchy. This means that it has not be
                // inflated in the current inflate() call and we should not inherit the context.
                return false;
            }
            parent = parent.getParent();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存溢出，必须溢出，子类中不能覆盖
        SkinManager.getInstance().unRegister(this);
    }

    @Override
    public void changeSkin(SkinResource skinResource) {
        //用于未定义的特殊属性的换肤，或者自定义View的换肤，有了资源器就可以得到任何的属性，拓展时集成覆盖即可
        Log.e("changeSkin", "extend change skin");
    }
}
