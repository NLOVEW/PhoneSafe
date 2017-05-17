package com.example.nhb.nhb_phonesafe.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.nhb.nhb_phonesafe.R;
import com.example.nhb.nhb_phonesafe.utils.StreamUtil;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final int SUCCESS_NUMBER =100;
    private TextView tv_version;
    private PackageManager pm;
    private PackageInfo info;
    //本地应用版本号
    private int mLocalVersionCode;
    //服务器版本名称
    private  String versionName;
    //服务器版本号
    private String versionCode;
    //服务器对新版本的描述
    private String versionDes;
    //服务器对新版本的下载地址
    private String updatePath;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS_NUMBER:{
                    Bundle bundle=msg.getData();
                    versionName=bundle.getString("versionName");
                    versionCode=bundle.getString("versionCode");
                    versionDes=bundle.getString("versionDes");
                    updatePath=bundle.getString("updatePath");
                    checkVersion();
                }break;
            }
        }
    };
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startTime = SystemClock.currentThreadTimeMillis();
        initUI();
        initDate();
        setVersionName();
        getVersionCode();
        getVersion();
    }
    /**
     * 对UI进行初始化操作,找到TextView
     */
    private void initUI(){
        tv_version = (TextView) findViewById(R.id.tv_version);
    }
    /**
     * 获取包管理者
     */
    private void initDate(){
        pm = getPackageManager();
        try {
            info = pm.getPackageInfo(this.getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取版本名称
     * @return 版本名称
     */
    private String getVersionName(){
        String version= info.versionName;
        Log.i("本地版本名称",version);
        return version;
    }

    /**
     * 设置并显示本地应用版本名称
     */
    private void setVersionName(){
        tv_version.setText("版本："+getVersionName());
    }

    /**
     * 获取本地应用版本号
     * @return 版本号
     */
    private void getVersionCode(){
        mLocalVersionCode = info.versionCode;
    }

    /**
     * 获取服务器中的版本号
     * @return服务器中的版本号
     */
    private void getVersion(){
        new Thread(){
            @Override
            public void run() {
                String path="http://10.25.13.155/update/updateInfo.json";
                try {
                    URL url=new URL(path);
                    HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    if(connection.getResponseCode()==200){
                        InputStream is=connection.getInputStream();
                        String info= StreamUtil.getInfo(is);
                        if(info!=null){
                            Message message= Message.obtain();
                            Bundle bundle=new Bundle();
                            JSONObject jsonObject=new JSONObject(info);
                            versionName = jsonObject.getString("versionName");
                            versionCode = jsonObject.getString("versionCode");
                            versionDes = jsonObject.getString("versionDes");
                            updatePath = jsonObject.getString("updatePath");
                            bundle.putString("versionName",versionName);
                            bundle.putString("versionCode",versionCode);
                            bundle.putString("versionDes",versionDes);
                            bundle.putString("updatePath",updatePath);
                            message.setData(bundle);
                            message.what=SUCCESS_NUMBER;
                            //延时跳转
                            long endTime=SystemClock.currentThreadTimeMillis();
                            if((endTime-startTime)<5000){
                                SystemClock.sleep(5000-(endTime-startTime));
                            }
                            mHandler.sendMessage(message);
                        }else{
                            Log.i("联网失败","未获得信息");
                        }
                    }else{
                        Log.i("联网信息","未取得链接");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void checkVersion(){
        if(mLocalVersionCode<Integer.parseInt(versionCode)){
            Log.i("比较成功是否更新","是"+versionCode);
        }else {
            Intent intent=new Intent(this,IndexActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
