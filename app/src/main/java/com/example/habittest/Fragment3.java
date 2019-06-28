package com.example.habittest;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class Fragment3 extends Fragment {

    private byte[] image;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.mine, container, false);

        ConstraintLayout c = (ConstraintLayout) v.findViewById(R.id.constraintLayout7);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), TodayCard.class);
                intent.putExtra("image", image);
                startActivity(intent);
            }
        });

        ConstraintLayout c2 = (ConstraintLayout) v.findViewById(R.id.constraintLayout9);
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUs.class);
                startActivity(intent);
            }
        });

        ConstraintLayout c3 = (ConstraintLayout) v.findViewById(R.id.constraintLayout6);
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OverHabit.class);
                startActivity(intent);
            }
        });

        ConstraintLayout c4 = (ConstraintLayout) v.findViewById(R.id.constraintLayout8);
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpLayout.class);
                startActivity(intent);
            }
        });

        return v;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
