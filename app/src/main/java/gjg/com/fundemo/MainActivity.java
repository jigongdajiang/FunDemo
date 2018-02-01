package gjg.com.fundemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import gjg.com.fundemo.aidltest.AidlServiceActivity;
import gjg.com.fundemo.alifix.AliFixTestActivity;
import gjg.com.fundemo.dbg.DbGuardTestActivity;
import gjg.com.fundemo.eb.EventBusTestActivity;
import gjg.com.fundemo.exceptionhandler.ExceptionTextActivity;
import gjg.com.fundemo.fix.FixTestActivity;
import gjg.com.fundemo.skin.SkinTestActiivty;

public class MainActivity extends AppCompatActivity {

    private ListView list;
    private List<Bean> datas = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);list = (ListView) findViewById(R.id.list);
        datas.add(new Bean("简单的AIDL", AidlServiceActivity.class));
        datas.add(new Bean("双进程守护", DbGuardTestActivity.class));
        datas.add(new Bean("异常捕获", ExceptionTextActivity.class));
        datas.add(new Bean("热修复测试", FixTestActivity.class));
        datas.add(new Bean("阿里热修复", AliFixTestActivity.class));
        datas.add(new Bean("换肤框架", SkinTestActiivty.class));
        datas.add(new Bean("EventBus示例及手写", EventBusTestActivity.class));


        ItemAdapter adapter = new ItemAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this,datas.get(position).target));
            }
        });
    }
    private class ItemAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
            TextView tv = (TextView) view.findViewById(R.id.tv);
            tv.setText(datas.get(position).des);
            return view;
        }
    }
    private class Bean{
        public String des;
        public Class target;

        public Bean(String des, Class target) {
            this.des = des;
            this.target = target;
        }
    }
}
