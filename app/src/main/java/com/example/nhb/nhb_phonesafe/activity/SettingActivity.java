package com.example.nhb.nhb_phonesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.nhb.nhb_phonesafe.R;
import com.example.nhb.nhb_phonesafe.utils.SpUtil;
import com.example.nhb.nhb_phonesafe.view.SettingView;

public class SettingActivity extends Activity {

    private SettingView sv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initDate();
    }

    /**
     * 初始化数据
     */
    private void initDate() {
        sv = (SettingView) findViewById(R.id.sv_1);
        if(!sv.isChecked()){
            sv.setChecked(false);
        }
        sv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv.setChecked(!sv.isChecked());
                SpUtil.setConfigBoolean(getApplicationContext(),"updateVersion",!sv.isChecked());
            }
        });
    }
}
