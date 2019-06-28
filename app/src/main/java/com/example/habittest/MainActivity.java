package com.example.habittest;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> mPermissionList = new ArrayList<>();//权限集合
    String[] permissions = new String[]{Manifest.permission.READ_CALENDAR, Manifest.permission.WRITE_CALENDAR, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private TextView mTextMessage, top_text;
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager;
    private Toolbar top_tools;
    private Button top_button;
    private FrameLayout frameLayout;
    //用于展示listview
    private List<HabitListItem> list = new ArrayList<HabitListItem>();
    private ListView listView;
    private HabitListItemAdapter itemAdapter;

    //数据库相关变量
    private MySqliteHelper helper;
    private SQLiteDatabase db;
    private DBManager mgr;

    //今日图片
    private byte[] image;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager = getSupportFragmentManager();
            transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Fragment1 frag1 = new Fragment1();
                    frag1.setDBManager(mgr);
                    transaction.replace(R.id.content, frag1);
                    top_text.setText(R.string.top_today);
                    top_button.setVisibility(View.VISIBLE);
                    transaction.commit();
                    return true;
                case R.id.navigation_dashboard:
                    Fragment2 frag2 = new Fragment2();
                    frag2.setDBManager(mgr);
                    transaction.replace(R.id.content, frag2);
                    top_text.setText(R.string.top_all);
                    top_button.setVisibility(View.GONE);
                    transaction.commit();
                    return true;
                case R.id.navigation_notifications:
                    Fragment3 frag3 = new Fragment3();
                    frag3.setImage(image);
                    transaction.replace(R.id.content, frag3);
                    top_text.setText(R.string.top_mine);
                    top_button.setVisibility(View.GONE);
                    transaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //关于申请权限的代码
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
            //Toast.makeText(MainActivity.this, "已经授权", Toast.LENGTH_LONG).show();
        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 2);
        }        //申请结束

        //数据库变量初始化
        helper = DBManager.getIntance(this);
        db = helper.getWritableDatabase();//创建或打开数据库
        mgr = new DBManager(db);
        //创建表
        mgr.createTableOrNot();


        // 今日卡片图片爬虫
        new Thread() {
            @Override
            public void run() {
                super.run();

                try {
                    Document document = Jsoup.connect("https://cn.bing.com/HPImageArchive.aspx?idx=0&n=1").get();
                    Elements elements = document.select("images image url");
                    String imageUrl = elements.get(0).text();

                    String path = "https://cn.bing.com/" + imageUrl;
                    URL url = new URL(path);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(1000);
                    con.setReadTimeout(1000);
                    InputStream inStream = con.getInputStream();

                    int len = 0;
                    byte[] buffer = new byte[1024];
                    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                    while ((len = inStream.read(buffer)) != -1) {
                        outStream.write(buffer, 0, len);
                    }
                    inStream.close();
                    image = outStream.toByteArray();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        setDefaultFragment();
        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        top_tools = (Toolbar) findViewById(R.id.toolbar);
        top_text = (TextView) findViewById(R.id.top_text);
        top_button = (Button) findViewById(R.id.top_button);

    }

    // 设置默认进来是tab 显示的页面
    private void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        Fragment1 frag1 = new Fragment1();
        frag1.setDBManager(mgr);
        transaction.replace(R.id.content, frag1);
        transaction.commit();
    }

    public void addHabit(View v) {
        Intent intent = new Intent(MainActivity.this, AddHabit.class);
        startActivity(intent);
    }

}
