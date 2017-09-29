package gjg.com.fundemo.fix;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gjg.com.fundemo.R;

/**
 * @author : gongdaocai
 * @date : 2017/9/19
 * FileName:
 * @description: 热修复测试
 */


public class FixTestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix);
    }

    public void testFix(View view) {
        Toast.makeText(this,"这是老代码",Toast.LENGTH_LONG).show();
//        Toast.makeText(this,"这是新代码",Toast.LENGTH_LONG).show();
    }

    public void fix(View view) {
        try {
            File fixDexFile = new File(Environment.getExternalStorageDirectory(),"fix.dex");
            if(fixDexFile.exists()){
                FixManager fixManager = new FixManager(this);
                fixManager.fieDex(fixDexFile.getAbsolutePath());
                Toast.makeText(this,"修复成功",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"补丁文件不存在",Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,"修复失败",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
