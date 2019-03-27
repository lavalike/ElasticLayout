package com.wangzhen.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * ListActivity
 * Created by wangzhen on 2019/3/27.
 */
public class ListActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("ListView示例");
        listView = findViewById(R.id.list);

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        listView.setAdapter(new ListAdapter(list));
    }

    class ListAdapter extends BaseAdapter {
        List<Integer> datas;

        public ListAdapter(List<Integer> datas) {
            this.datas = datas;
        }

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
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ListActivity.this).inflate(R.layout.layout_item_list, parent, false);
                holder = new ViewHolder();
                holder.tv = convertView.findViewById(R.id.tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tv.setText(String.valueOf(datas.get(position)));
            return convertView;
        }

        class ViewHolder {
            TextView tv;
        }
    }
}
