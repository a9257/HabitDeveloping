package com.example.habittest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Fragment2 extends Fragment {

    private List<HabitListItem> list;
    private ListView listView;
    private HabitListItemAdapter itemAdapter;

    private Habit[]habit;

    //数据库manager
    private DBManager mgr;

    public void setDBManager(DBManager mgr) {
        this.mgr = mgr;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh_list();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_habits, container, false);
        listView = (ListView) view.findViewById(R.id.ListView0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j) {
                Intent intent = new Intent(getActivity(), HabitLog.class);
                intent.putExtra("isFinished",0);
                intent.putExtra("name",habit[i].hname);
                intent.putExtra("days",habit[i].days+"");
                intent.putExtra("curdays",habit[i].curdays+"");
                intent.putExtra("highdays",habit[i].highdays+"");
                intent.putExtra("credate",habit[i].credate);
                startActivity(intent);
            }
        });

        refresh_list();
        return view;
    }

    public void refresh_list() {
        list = new ArrayList<HabitListItem>();
        habit = mgr.getHabit("任意时间", 1);
        for (int i = 0; i < habit.length; i++) {
            HabitListItem t = new HabitListItem(habit[i].hname, habit[i].htext, habit[i].days + "", habit[i].pic);
            list.add(t);
        }
        itemAdapter = new HabitListItemAdapter(getActivity(), R.layout.habit_list_item, list);
        listView.setAdapter(itemAdapter);
    }

}
