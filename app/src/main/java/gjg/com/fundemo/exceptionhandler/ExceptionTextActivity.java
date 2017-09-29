package gjg.com.fundemo.exceptionhandler;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import gjg.com.fundemo.R;

/**
 * @author : gongdaocai
 * @date : 2017/9/19
 * FileName:
 * @description:
 */


public class ExceptionTextActivity extends AppCompatActivity {
    private TextView tv_crash_file_name;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exception_test);
        tv_crash_file_name = (TextView) findViewById(R.id.tv_crash_file_name);
        final File file = ExceptionHandler.getInstance().getCrashCacheFile();
        if(null != file){
            tv_crash_file_name.setText(file.getAbsolutePath());
        }
        tv_crash_file_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    tv_crash_file_name.setText(getString(new FileInputStream(file)));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private  String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "gbk");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public void textException(View view) {
        int a = 2/0;
        Toast.makeText(this,"测试a="+a,Toast.LENGTH_LONG).show();
    }
}
