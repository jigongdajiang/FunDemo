package gjg.com.fundemo.skin;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v4.view.LayoutInflaterFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import gjg.com.fundemo.R;
import gjg.com.fundemo.skin.skinframe.config.SkinConfig;
import gjg.com.fundemo.skin.skinframe.core.BaseSkinActivity;
import gjg.com.fundemo.skin.skinframe.core.SkinManager;
import gjg.com.fundemo.skin.skinframe.sp.SkinSpUtil;

/**
 * @author : gongdaocai
 * @date : 2017/9/25
 * FileName:
 * @description:
 */


public class SkinTestActiivty extends BaseSkinActivity {

    private LinearLayout ll_main;
    private Button btn_change;
    private Button btn_reset;
    private ImageView img_ns;
    private TextView tv_des;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        installMyViewFactoryDemo();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_test);
        ll_main = (LinearLayout) findViewById(R.id.ll_main);
        btn_change = (Button) findViewById(R.id.btn_change);
        btn_reset = (Button) findViewById(R.id.btn_reset);
        img_ns = (ImageView) findViewById(R.id.img_ns);
        tv_des = (TextView) findViewById(R.id.tv_des);
    }

    private void installMyViewFactoryDemo() {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        if (layoutInflater.getFactory() == null) {
            //简单测试
            LayoutInflaterCompat.setFactory(layoutInflater, new LayoutInflaterFactory() {
                @Override
                public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                    Log.e("lanjie", "View创建被拦截--->" + name);
                    //简单的将Button按钮换成TextView
                    if (name.equals("Button")) {
                        TextView textView = new TextView(SkinTestActiivty.this);
                        textView.setText("原来的按钮被替换了");
                        return textView;
                    }
                    return null;
                }
            });
        }
    }


    /**
     * 单纯的换肤原理
     */
    public void skinChange(View view) {
        String skinPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "black.skin";
        int result = SkinManager.getInstance().loadSkin(skinPath);
        if (SkinConfig.SKIN_CHANGE_SUCCESS == result) {
            Toast.makeText(this, "换肤成功", Toast.LENGTH_LONG).show();
        }
    }

    public void skinReset(View view) {
        int result = SkinManager.getInstance().restoreSkin();
        if (result == SkinConfig.SKIN_CHANGE_SUCCESS) {
            Toast.makeText(this, "恢复成功", Toast.LENGTH_LONG).show();
        }

    }

    private void changeSkin(Resources resources, String packageName) {
        int colorMainBgId = resources.getIdentifier("skin_main_bg", "color", packageName);
        int colodMainBg = resources.getColor(colorMainBgId);
        ll_main.setBackgroundColor(colodMainBg);

        int colorBtnBgId = resources.getIdentifier("skin_btn_bg", "color", packageName);
        int colorBtnBg = resources.getColor(colorBtnBgId);
        btn_change.setBackgroundColor(colorBtnBg);
        btn_reset.setBackgroundColor(colorBtnBg);

        int colorBtnTextId = resources.getIdentifier("skin_btn_text_color", "color", packageName);
        int colorBtnText = resources.getColor(colorBtnTextId);
        btn_change.setTextColor(colorBtnText);
        btn_reset.setTextColor(colorBtnText);

        int colorTextId = resources.getIdentifier("skin_text_color", "color", packageName);
        int colorText = resources.getColor(colorTextId);
        tv_des.setTextColor(colorText);

        int drawableImgLogoId = resources.getIdentifier("img_logo", "drawable", packageName);
        Drawable drawableImgLogo = resources.getDrawable(drawableImgLogoId);
        img_ns.setImageDrawable(drawableImgLogo);
    }

    public void jump(View view) {
        Intent intent = new Intent(this, SkinTestActiivty.class);
        startActivity(intent);
    }
}
