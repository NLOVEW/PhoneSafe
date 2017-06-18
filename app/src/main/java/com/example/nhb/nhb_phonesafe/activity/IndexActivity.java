package com.example.nhb.nhb_phonesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhb.nhb_phonesafe.R;
import com.example.nhb.nhb_phonesafe.utils.ContantValue;
import com.example.nhb.nhb_phonesafe.utils.Md5Util;
import com.example.nhb.nhb_phonesafe.utils.SpUtil;

public class IndexActivity extends Activity {

    private GridView gv_content;
    private int[] image_id;
    private String[] image_des;
    private EditText et_psd_3;

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
                        passwordDialog();
                    }break;
                    case 6:{

                    }break;
                    case 7:{

                    }break;
                    case 8:{

                    }break;
                    case 9:{
                        Intent intent=new Intent(getApplicationContext(),SettingActivity.class);
                        startActivity(intent);
                    }break;
                }
            }
        });
    }

    /**
     * 手机防盗密码
     */
    private void passwordDialog() {
        String password=SpUtil.getConfigPassword(getApplicationContext(), ContantValue.SAFE_PASSWORD);
        if(TextUtils.isEmpty(password)){
            initDialog();
        }else{
            getPassword(password);
        }
    }

    private void getPassword(final String value) {
        AlertDialog.Builder builder=new AlertDialog.Builder(IndexActivity.this);
        final AlertDialog dialog=builder.create();
        final View view =View.inflate(getApplicationContext(),R.layout.dialog_getpassword,null);
        dialog.setView(view);
        dialog.show();
        Button bt_getpassword = (Button) view.findViewById(R.id.bt_getpassword);
        Button bt_cancelpassword2 = (Button) view.findViewById(R.id.bt_cancelpassword2);
        bt_getpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_psd_3 = (EditText) view.findViewById(R.id.et_psd_3);
                String key=Md5Util.Md5Convert(et_psd_3.getText().toString());
                if(key.equals(value)){
                    Intent intent=new Intent(getApplicationContext(),AntiActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                }else {
                    Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_cancelpassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     *初始化密码
     */
    private void initDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(IndexActivity.this);
        final AlertDialog dialog=builder.create();
        View view =View.inflate(getApplicationContext(),R.layout.dialog_setpassword,null);
        dialog.setView(view);
        dialog.show();
        final EditText et_psd_1= (EditText) view.findViewById(R.id.et_psd_1);
        final EditText et_psd_2= (EditText) view.findViewById(R.id.et_psd_2);
        Button bt_setpassword = (Button) view.findViewById(R.id.bt_setpassword);
        Button bt_cancelpassword = (Button) view.findViewById(R.id.bt_cancelpassword);
        /**
         * 确认按钮监听事件
         */
        bt_setpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String psd_1=et_psd_1.getText().toString();
                String psd_2=et_psd_2.getText().toString();
                if(!TextUtils.isEmpty(psd_1)&&!TextUtils.isEmpty(psd_2)){
                    if(psd_1.equals(psd_2)){
                        String value= Md5Util.Md5Convert(psd_1);
                        SpUtil.setConfigPassword(getApplication(),ContantValue.SAFE_PASSWORD,value);
                        Intent intent=new Intent(getApplicationContext(),AntiActivity.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }else {
                        Toast.makeText(getApplicationContext(),"密码不匹配",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(),"密码不得为空",Toast.LENGTH_LONG).show();
                }
            }
        });
        bt_cancelpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 得到宫格view
     */
    private void initUI() {
        gv_content = (GridView) findViewById(R.id.gv_content);
    }

    /**
     * GridView适配器
     */
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
