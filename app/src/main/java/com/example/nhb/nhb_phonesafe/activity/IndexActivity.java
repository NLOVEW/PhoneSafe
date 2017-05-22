package com.example.nhb.nhb_phonesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhb.nhb_phonesafe.R;

public class IndexActivity extends Activity {

    private GridView gv_content;
    private int[] image_id;
    private String[] image_des;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initUI();
        initDate();
    }

    private void initDate() {
        image_id = new int[]{R.mipmap.short_cut_anti_scan,R.mipmap.short_cut_phone_clean,
                R.mipmap.short_cut_program_manager,R.mipmap.short_cut_active_progress,
                R.mipmap.short_cut_flow_traffic,R.mipmap.short_cut_phone_anti,
                R.mipmap.short_cut_block_call, R.mipmap.short_cut_privacy_protection,
                R.mipmap.short_cut_advertise_block,R.mipmap.short_cut_use_tools
        };
        image_des = new String[]{"手机杀毒","缓存清理","软件管理","进程管理","流量管理","防盗设置",
                "通信管理","个人隐私","广告拦截","高级设置"
        };
        gv_content.setAdapter(new GvAdapter());
        gv_content.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:{

                    }break;
                    case 1:{

                    }break;
                    case 3:{

                    }break;
                    case 4:{

                    }break;
                    case 5:{

                    }break;
                    case 6:{

                    }break;
                    case 7:{

                    }break;
                    case 8:{

                    }break;
                    case 9:{
                        Intent intent=new Intent(getApplicationContext(),ToolActivity.class);
                        startActivity(intent);
                    }break;
                }
            }
        });
    }

    /**
     * 得到宫格view
     */
    private void initUI() {
        gv_content = (GridView) findViewById(R.id.gv_content);
    }

    private class GvAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return image_des.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return image_id[position];
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if(convertView==null){
                view=View.inflate(getApplicationContext(),R.layout.gv_item,null);
            }else{
                view=convertView;
            }
            ImageView iv_icon= (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_title= (TextView) view.findViewById(R.id.tv_title);
            iv_icon.setBackgroundResource(image_id[position]);
            tv_title.setText(image_des[position]);
            return view;
        }
    }
}
