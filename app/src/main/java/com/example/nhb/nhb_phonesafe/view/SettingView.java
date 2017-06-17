package com.example.nhb.nhb_phonesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.nhb.nhb_phonesafe.R;
import com.example.nhb.nhb_phonesafe.utils.SpUtil;

public class SettingView extends RelativeLayout {

    private CheckBox checkBox;
    private TextView tv;

    public SettingView(Context context) {
        this(context,null);
    }

    public SettingView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.view_setting,this);
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        checkBox = (CheckBox) this.findViewById(R.id.cb_version);
        tv = (TextView) this.findViewById(R.id.tv_setting);
        tv.setText(attrs.getAttributeValue("http://schemas.android.com/apk/res-auto","desText"));
    }

    public boolean isChecked(){
        return SpUtil.getConfigBoolean(getContext(),"updateVersion");
    }

    public void setChecked(boolean key){
        checkBox.setChecked(key);
    }
}
