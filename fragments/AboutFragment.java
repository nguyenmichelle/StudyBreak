package com.example.michellenguy3n.studybreak.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;

/**
 * Allows users to view details about the application.
 * 
 * Created by michellenguy3n on 12/6/16.
 */
public class AboutFragment extends Fragment {
    public static AboutFragment newInstance() {
        return new AboutFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RelativeLayout rootLayout = new RelativeLayout(getContext());
        TextView aboutTextView = new TextView(getContext());
        aboutTextView.setText("About Study/Break");
        aboutTextView.setBackgroundColor(Color.WHITE);
        TextView aboutBody = new TextView(getContext());
        aboutBody.setTextAppearance(android.R.style.TextAppearance_Medium);
        aboutBody.setText("Study/Break was created in 2016 by a college student/app developer, Michelle Nguyen, who was influenced by modern-day dating applications, yet saw a need for more platonic applications in the market. Nguyen decided to produce an application with more than just the idea of making random friends by adding the capability to match with other college peers with similar schedules, and as a result, Study/Break was born. Future developments hope to include the possibility to befriend people with similar schedules who are not from the same campus, yet are in similar classes at other universities/colleges.");
        aboutBody.setBackgroundResource(R.drawable.border);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
        layoutParams.setMargins(100, 0, 100, 0);
        aboutBody.setLayoutParams(layoutParams);
        rootLayout.addView(aboutTextView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(aboutBody);
        return rootLayout;
    }
}
