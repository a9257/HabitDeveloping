package com.example.habittest;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;


import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import java.util.Calendar;
import java.util.Date;

public class AddHabit extends AppCompatActivity {

    private ImageView imageView;
    private Button[] bt = new Button[4];
    private Button[] bt2 = new Button[3];
    private String[] t = {"任意时间", "晨间习惯", "午间习惯", "晚间习惯"};
    private String[] f = {"每日", "每周", "每月"};

    private String img;
    private String time;
    private String frequency;
    private String strHour = "";
    private String strMin = "";

    //数据库相关变量
    private MySqliteHelper helper;
    private SQLiteDatabase db;
    private DBManager mgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_habit);
        final String[] imgName = {"habit_1", "habit_2", "habit_3", "habit_4", "habit_5"};

        //数据库变量初始化
        helper = DBManager.getIntance(this);
        db = helper.getWritableDatabase();//创建或打开数据库
        mgr = new DBManager(db);

        //返回事件
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bt[0] = (Button) findViewById(R.id.button);
        bt[1] = (Button) findViewById(R.id.morning);
        bt[2] = (Button) findViewById(R.id.noon);
        bt[3] = (Button) findViewById(R.id.button4);

        bt2[0] = (Button) findViewById(R.id.button5);
        bt2[1] = (Button) findViewById(R.id.button6);
        bt2[2] = (Button) findViewById(R.id.button7);


        //设置初始选择任意时间
        selectTime(0);
        //设置初始选择每天
        selectFre(0);

        //选择图标事件
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        imageView = (ImageView) findViewById(R.id.habit_img);

        //设置初始图标
        img = imgName[0];
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.img1:
                        imageView.setImageResource(getResources().getIdentifier(imgName[0], "drawable", getPackageName()));
                        img = imgName[0];
                        break;
                    case R.id.img2:
                        imageView.setImageResource(getResources().getIdentifier(imgName[1], "drawable", getPackageName()));
                        img = imgName[1];
                        break;
                    case R.id.img3:
                        imageView.setImageResource(getResources().getIdentifier(imgName[2], "drawable", getPackageName()));
                        img = imgName[2];
                        break;
                    case R.id.img4:
                        imageView.setImageResource(getResources().getIdentifier(imgName[3], "drawable", getPackageName()));
                        img = imgName[3];
                        break;
                    case R.id.img5:
                        imageView.setImageResource(getResources().getIdentifier(imgName[4], "drawable", getPackageName()));
                        img = imgName[4];
                        break;
                }
            }
        });

        //时间选择器
        final TimePickerView pvTime = new TimePickerBuilder(AddHabit.this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
//                Toast.makeText(AddHabit.this, cal.get(Calendar.HOUR_OF_DAY) + "" + cal.get(Calendar.MINUTE), Toast.LENGTH_SHORT).show();
                EditText et = (EditText) findViewById(R.id.editText3);
                strHour = String.format("%02d", cal.get(Calendar.HOUR_OF_DAY));
                strMin = String.format("%02d", cal.get(Calendar.MINUTE));
                et.setText(strHour + "   :   " + strMin);
            }
        }).setType(new boolean[]{false, false, false, true, true, false})
                .setTitleText("设置提醒时间")
                .isCyclic(true)
                .build();

        findViewById(R.id.editText3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });


    }

    public void selectTime(int k) {
        for (int i = 0; i < 4; i++) {
            if (i == k) {
                bt[i].setTextColor(getResources().getColor(R.color.white));
                bt[i].setBackgroundResource(R.drawable.shape_selected);
            } else {
                bt[i].setTextColor(getResources().getColor(R.color.black));
                bt[i].setBackgroundResource(R.drawable.shape);
            }
        }
        time = t[k];
    }

    public void time1(View view) {
        selectTime(0);
    }

    public void time2(View view) {
        selectTime(1);
    }

    public void time3(View view) {
        selectTime(2);
    }

    public void time4(View view) {
        selectTime(3);
    }

    public void selectFre(int k) {
        for (int i = 0; i < 3; i++) {
            if (i == k) {
                bt2[i].setTextColor(getResources().getColor(R.color.white));
                bt2[i].setBackgroundResource(R.drawable.shape_2_selected);
            } else {
                bt2[i].setTextColor(getResources().getColor(R.color.bg_color));
                bt2[i].setBackgroundResource(R.drawable.shape_2);
            }
        }
        frequency = f[k];
        TextView textView = (TextView) findViewById(R.id.textView5);
        textView.setText(f[k]);
    }

    public void fre1(View view) {
        selectFre(0);
    }

    public void fre2(View view) {
        selectFre(1);
    }

    public void fre3(View view) {
        selectFre(2);
    }

    //获取时间的函数
    //获取当天零点的时间
    public long get_zero_time() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        //当天0点
        long zero = calendar.getTimeInMillis();
        return zero;
    }

    //根据小时、分钟、习惯名称设置提醒
    public void set_date_notice(String habit_name, String htext, int hour_time, int minute_time) {
        long zero = get_zero_time();
        long extra_msec = 1000 * (hour_time * 60 + minute_time) * 60 + zero;
        CalendarReminderUtils.addCalendarEvent(this, habit_name, htext, extra_msec, 0);
    }

    //创建习惯事件
    public void addaHabit(View view) {
        //获取输入框
        EditText etName = (EditText) findViewById(R.id.editText);
        EditText etNum = (EditText) findViewById(R.id.editText2);
//        EditText etHour = (EditText) findViewById(R.id.editText3);
//        EditText etMin = (EditText) findViewById(R.id.editText4);
        EditText etText = (EditText) findViewById(R.id.editText5);
        //获取输入值
        String name = etName.getText().toString();
        String strNum = etNum.getText().toString();
//        String strHour = etHour.getText().toString();
//        String strMin = etMin.getText().toString();
        String htext = etText.getText().toString();

        int num, hour, min;

        if (name.equals("")) {
            Toast.makeText(this, "习惯名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (strNum.equals("")) {
            Toast.makeText(this, "打卡次数不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        num = Integer.parseInt(strNum);
        if (htext.equals("")) {
            htext = "只有千锤百炼，才能成为好钢。";
        }
        if (strHour.equals("") && strMin.equals("")) {
            Date date = new Date();
            Habit habit = new Habit(name, img, num, 0, time, frequency, htext, 0, 0, 0, Utils.date2String(date), 1);
            if (mgr.insertHabitDB(habit)) {
                finish();
                return;
            } else {
                Toast.makeText(this, "习惯名不能重复", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if ((!strHour.equals("")) && (!strMin.equals(""))) {
            hour = Integer.parseInt(strHour);
            min = Integer.parseInt(strMin);
            if (hour < 24 && hour >= 0 && min >= 0 && min < 60) {
                Date date = new Date();
                Habit habit = new Habit(name, img, num, 0, time, frequency, htext, 0, 0, 0, Utils.date2String(date), 1);
                if (mgr.insertHabitDB(habit)) {
                    set_date_notice(name, htext, hour, min);
                    finish();
                    return;
                } else {
                    Toast.makeText(this, "习惯名不能重复", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                Toast.makeText(this, "确保提醒时间设置正确", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, "确保提醒时间设置正确", Toast.LENGTH_SHORT).show();
    }


}
