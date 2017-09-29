package gjg.com.fundemo.alifix;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.taobao.sophix.SophixManager;

import gjg.com.fundemo.FunApplication;
import gjg.com.fundemo.R;

/**
 * @author : gongdaocai
 * @date : 2017/9/20
 * FileName:
 * @description:
 */


public class AliFixTestActivity extends AppCompatActivity implements FunApplication.AppPatchLoadStatusListener{
    private TextView tv_console;
    private ImageView iv_test_res;
    private LinearLayout ll_add;
    private MapView mMapView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alifix_test);
        tv_console = (TextView) findViewById(R.id.tv_console);
        iv_test_res = (ImageView) findViewById(R.id.iv_test_res);
        ll_add = (LinearLayout) findViewById(R.id.ll_add);
        mMapView = (MapView) findViewById(R.id.mapview);
        mMapView.onCreate(savedInstanceState);
//        SophixManager.getInstance().queryAndLoadNewPatch();
//        FunApplication.application.regsterAppPatchLoadStatusListener(this);
    }

    public void budinggengxin(View view) {
        //向服务器请求补丁
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    public void ziyuangengxin(View view) {
        //更新之前只有Toast，更新后会显示一个新添加的图片
//        showToast("更新前，我就Toast一下");
        iv_test_res.setImageResource(R.drawable.gbx);
    }

    public void daimagengxin(View view) {
        //更新前
        showToast("换图片了，还能无限添加美女");
//        startActivity(new Intent(this,TestNewActivity.class));
        View v = LayoutInflater.from(this).inflate(R.layout.activity_test_new,ll_add,false);
        ll_add.addView(v);
    }
    private void addFun(){
        updateConsole("新添加的代码执行了");
    }
    public void sogengxin(View view) {
        showToast("更新前，我就Toast一下");
    }
    public void clearPatch(View view) {
        SophixManager.getInstance().cleanPatches();
    }
    public void clearConsole(View view) {
        mStatusStr = "";
        if (tv_console != null) {
            tv_console.setText(mStatusStr);
        }
    }
    @Override
    public void onLoad(int mode, int code, String info, int handlePatchVersion) {
        final String msg = new StringBuilder().append("Mode:").append(mode).append("\n")
                .append(" Code:").append(code).append("\n")
                .append(" Info:").append(info).append("\n")
                .append(" HandlePatchVersion:").append(handlePatchVersion).append("\n")
                .toString();
        if(!TextUtils.isEmpty(msg)){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateConsole(msg);
                }
            });
        }
    }
    private String mStatusStr;
    private void updateConsole(String content) {
        mStatusStr += content + "\n";
        if (tv_console != null) {
            tv_console.setText(mStatusStr);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        FunApplication.application.unRegsterAppPatchLoadStatusListener(this);
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    private void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }

}
