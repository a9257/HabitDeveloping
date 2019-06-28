package com.example.habittest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar5);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    // 跳转至微博个人页
    public void jumpToWeiboProfileInfo(Context context, String uid) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        boolean weiboInstalled = Utils.isSinaWeiboInstalled(context);
        if (weiboInstalled) {
            intent.setData(Uri.parse("sinaweibo://userinfo?uid=" + uid));
        } else {
            intent.setData(Uri.parse("http://weibo.cn/qr/userinfo?uid=" + uid));
        }
        context.startActivity(intent);
    }

    public void weibo(View v) {
        jumpToWeiboProfileInfo(this, "2102377183");
    }

    public void jumptoQQ(View view) {
        String qqnum = "982728182";
        if (checkApkExist(this, "com.tencent.mobileqq")) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + qqnum + "&version=1")));
        } else {
            Toast.makeText(this, "本机未安装QQ应用", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName))
            return false;
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName,
                    PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


}
