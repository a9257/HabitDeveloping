package com.example.habittest;


import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HabitLog extends AppCompatActivity {

    GridView grv;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    private String[] WeekDays = {"日", "一", "二", "三", "四", "五", "六"};
    private int[] sign_days;

    private String hname;//习惯名

    //数据库相关变量
    private MySqliteHelper helper;
    private SQLiteDatabase db;
    private DBManager mgr;

    private int isFinished;//习惯是否完成 1:结束 0:未结束

    //分享相关
    private String days1;//总打卡
    private String days2;//当前连续打卡

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_log);

        //数据库变量初始化
        helper = DBManager.getIntance(this);
        db = helper.getWritableDatabase();//创建或打开数据库
        mgr = new DBManager(db);

        Intent intent = getIntent();
        isFinished = intent.getIntExtra("isFinished", 0);

        hname = intent.getStringExtra("name");
        String days = intent.getStringExtra("days");
        String curdays = intent.getStringExtra("curdays");
        String highdays = intent.getStringExtra("highdays");
        String credate = intent.getStringExtra("credate");
        String date = credate.substring(0, 4) + "." + credate.substring(4, 6) + "." + credate.substring(6, 8);

        TextView topName = (TextView) findViewById(R.id.top_habit_name);
        TextView tdays = (TextView) findViewById(R.id.textView11);
        TextView tcurdays = (TextView) findViewById(R.id.textView13);
        TextView thighdays = (TextView) findViewById(R.id.textView15);
        TextView tcredate = (TextView) findViewById(R.id.textView17);
        topName.setText(hname);
        tdays.setText(days + "天");
        tcurdays.setText(curdays + "天");
        thighdays.setText(highdays + "天");
        tcredate.setText(date);
        //分享相关变量
        days1 = days + "天";
        days2 = curdays + "天";

        if (isFinished == 1) {
            //已结束的习惯不可分享
            ImageView share = (ImageView) findViewById(R.id.imageView6);
            share.setVisibility(View.GONE);
            Button bt = (Button) findViewById(R.id.button2);
            bt.setText("激活习惯");
        }

        sign_days = mgr.getDates(hname);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setdate();
    }

    public void setdate() {
        //获取当前日期
        //获取当前是周几
        //获取当前月份天数
        //获取已经打卡的列表
        int[] mongth_day = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        grv = (GridView) findViewById(R.id.grid_date);
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.DAY_OF_MONTH, 1);//设为当前月份第一天
        int y = calendar.get(Calendar.YEAR);//年
        int month = calendar.get(Calendar.MONTH);//获取当前月份(作为下标)
        mongth_day[1] = (y % 4 == 0 && y % 100 != 0 || y % 400 == 0) ? 29 : 28;
        int day_mon = mongth_day[month];//当月天数
        int week_days = calendar1.get(Calendar.DAY_OF_WEEK);//1-7:周日——周一
        int now_day = calendar.get(Calendar.DAY_OF_MONTH);//当前是第几天
        int i, j;//计数
        int emp_num = week_days - 1;//周1——6空1——6，日不空格
        j = 7 + emp_num + day_mon;
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        for (i = 0; i < j; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (i < 7)//展示周日——周一
            {
                map.put("textView_date", "\n" + WeekDays[i]);
                map.put("image_back_date", R.drawable.pure);
                map.put("image_sign_point", R.drawable.pure);
            } else if (i < 7 + emp_num) {
                map.put("textView_date", " ");
                map.put("image_back_date", R.drawable.pure);
                map.put("image_sign_point", R.drawable.pure);
            } else {
                int tempday = i - 6 - emp_num;
                map.put("textView_date", "\n" + tempday);
                if (tempday == now_day) {
                    map.put("image_back_date", R.drawable.shadow_date);
                } else {
                    map.put("image_back_date", R.drawable.pure);
                }
                //需要判断已经做得
                if (judge_int_exist(sign_days, tempday)) {
                    map.put("image_sign_point", R.drawable.sign_point);
                } else {
                    map.put("image_sign_point", R.drawable.pure);
                }
            }

            data_list.add(map);
        }
        //新建适配器
        String[] from = {"textView_date", "image_back_date", "image_sign_point"};//传入数据
        int[] to = {R.id.textView_date, R.id.image_back_date, R.id.image_sign_point};//传出数据
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.date_item, from, to);
        //配置适配器
        grv.setAdapter(sim_adapter);


        // Inflate the layout for this fragment
    }

    //判断数组中是否有某个值
    public boolean judge_int_exist(int[] s, int num) {
        boolean exist = false;
        int l = s.length;
        for (int i = 0; i < l; i++) {
            if (s[i] == num) {
                exist = true;
                break;
            }
        }
        return exist;
    }

    public void finishHabit(View view) {
        if (isFinished == 0) {
            //结束习惯删除日程提醒
            CalendarReminderUtils.deleteCalendarEvent(this, hname);
        }
        mgr.switchHabit(hname, isFinished);
        finish();
    }


    public void share(View view) {
        shareText("习惯分享", "我正在养成习惯：" + hname + "，目前已连续打卡" + days2 + "，总共打卡" + days1 + "。快来一起使用习惯养成App吧！");
    }

    public void shareText(String title, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, title);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享文字"));
    }
}
