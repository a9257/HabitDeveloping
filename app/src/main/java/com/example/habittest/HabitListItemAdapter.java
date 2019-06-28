package com.example.habittest;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HabitListItemAdapter extends ArrayAdapter<HabitListItem> {
    private int layoutId;

    public HabitListItemAdapter(Context context, int layoutId, List<HabitListItem> list) {
        super(context, layoutId, list);
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HabitListItem item = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        TextView textName = (TextView) view.findViewById(R.id.text_name);
        TextView textTime = (TextView) view.findViewById(R.id.text_time);
        TextView textDays = (TextView) view.findViewById(R.id.text_days);
        imageView.setImageResource(getContext().getResources().getIdentifier(item.getPicPath(),"drawable",getContext().getApplicationInfo().packageName));
        textName.setText(item.getName());
        textTime.setText(item.getTime());
        textDays.setText(item.getDays());
        return view;
    }
}