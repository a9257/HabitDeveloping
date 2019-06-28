package com.example.habittest;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class OverHabit extends AppCompatActivity {

    private List<HabitListItem> list;
    private ListView listView;
    private HabitListItemAdapter itemAdapter;

    private Habit[] habit;

    //数据库相关变量
    private MySqliteHelper helper;
    private SQLiteDatabase db;
    private DBManager mgr;

    @Override
    public void onResume() {
        super.onResume();
        refresh_list();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_over_habit);

        //数据库变量初始化
        helper = DBManager.getIntance(this);
        db = helper.getWritableDatabase();//创建或打开数据库
        mgr = new DBManager(db);

        listView = (ListView) findViewById(R.id.dynamic);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar6);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(OverHabit.this, HabitLog.class);
                intent.putExtra("isFinished", 1);
                intent.putExtra("name", habit[i].hname);
                intent.putExtra("days", habit[i].days + "");
                intent.putExtra("curdays", habit[i].curdays + "");
                intent.putExtra("highdays", habit[i].highdays + "");
                intent.putExtra("credate", habit[i].credate);
                startActivity(intent);
            }
        });
        refresh_list();
    }

    public void refresh_list() {
        list = new ArrayList<HabitListItem>();
        habit = mgr.getHabit("任意时间", 0);
        for (int i = 0; i < habit.length; i++) {
            HabitListItem t = new HabitListItem(habit[i].hname, habit[i].htext, habit[i].days + "", habit[i].pic);
            list.add(t);
        }
        itemAdapter = new HabitListItemAdapter(this, R.layout.habit_list_item, list);
        listView.setAdapter(itemAdapter);
    }
}
