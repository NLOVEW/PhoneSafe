package com.example.nhb.nhb_phonesafe.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.example.nhb.nhb_phonesafe.R;
import com.example.nhb.nhb_phonesafe.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final int SUCCESS_NUMBER =100;
    private static final int FAILCONN =104;
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
                    checkVersion();
                }break;
            }
        }
    };
    private long startTime;
    private Message message;

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
                            JSONObject jsonObject=new JSONObject(info);
                            versionName = jsonObject.getString("versionName");
                            versionCode = jsonObject.getString("versionCode");
                            versionDes = jsonObject.getString("versionDes");
                            updatePath = jsonObject.getString("updatePath");
                            message = Message.obtain();
                            message.what=SUCCESS_NUMBER;
                            mHandler.sendMessage(message);
                            //延时跳转
                            long endTime=SystemClock.currentThreadTimeMillis();
                            if((endTime-startTime)<4000){
                                SystemClock.sleep(4000-(endTime-startTime));
                            }
                        }
                    }
                }catch (Exception e) {
                    Intent intent=new Intent(getApplicationContext(),IndexActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }
    private void checkVersion(){
        if(mLocalVersionCode<Integer.parseInt(versionCode)){
            alert();
        }else {
            Intent intent=new Intent(this,IndexActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 弹出对话框提示更新
     */
    private void alert() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("版本更新");
        builder.setIcon(R.mipmap.new_version_icon);
        builder.setMessage(versionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //更新下载新的apk
                download();
            }
        });
        builder.setNegativeButton("下次提醒", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(getApplicationContext(),IndexActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.show();
    }

    private void download() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            final NotificationManager notification= (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
            final Notification.Builder notiBuilder=new Notification.Builder(getApplicationContext());
            notiBuilder.setSmallIcon(R.mipmap.launch);
            notiBuilder.setTicker("正在更新版本");
            notiBuilder.setContentTitle("更新中");
            String path=updatePath;
            System.out.println("下载地址"+path);
            String savePath= Environment.getExternalStorageDirectory().getPath()+"/app-release.apk";
            System.out.println(savePath);
            HttpUtils http=new HttpUtils();
            http.download(path, savePath, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    installApk(responseInfo.result);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                }

                @Override
                public void onStart() {
                    notiBuilder.setProgress(100,0,false);
                    notification.notify(1,notiBuilder.build());
                }

                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    notiBuilder.setProgress((int) total, (int) current,false);
                    notification.notify(1,notiBuilder.build());
                }
            });
        }
    }

    /**
     * 根据系统意图安装apk
     * @param file
     */
    private void installApk(File file) {
        Intent intent=new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivityForResult(intent,3);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Intent intent=new Intent(getApplicationContext(),IndexActivity.class);
        startActivity(intent);
        finish();
    }
}
