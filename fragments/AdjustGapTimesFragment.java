package com.example.michellenguy3n.studybreak.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.model.Gap;

/**
 * Allows users to edit existing gap times in their schedule.
 * 
 * Created by michellenguy3n on 12/8/16.
 */
public class AdjustGapTimesFragment extends Fragment {
    TimePicker beginTimePicker;
    TimePicker endTimePicker;

    public static AdjustGapTimesFragment newInstance() {
        return new AdjustGapTimesFragment();
    }

    public interface OnSaveClickedListener {
        void onSaveClicked(Gap gap);
    }

    private OnSaveClickedListener _onSaveClickedListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onSaveClickedListener = (OnSaveClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement OnSaveClickedListener!");
        }
    }

    @Override
    public void onDetach() {
        _onSaveClickedListener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getContext());
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.silver));

        // Title
        TextView titleTextView = new TextView(getContext());
        titleTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        titleTextView.setText("Adjust Gap Times");
        titleTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // Begin label
        TextView beginLabel = new TextView(getContext());
        beginLabel.setTextAppearance(android.R.style.TextAppearance_Large);
        beginLabel.setText("Begin");
        LinearLayout.LayoutParams labelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        labelParam.setMargins(70, 50, 0, 20);

        // Start time timepicker widget
        LinearLayout.LayoutParams timePickerParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        timePickerParam.setMargins(150, 0, 150, 0);
        beginTimePicker = new TimePicker(getContext());

        // End label
        TextView endLabel = new TextView(getContext());
        endLabel.setTextAppearance(android.R.style.TextAppearance_Large);
        endLabel.setText("End");

        // End time timepicker widget
        endTimePicker = new TimePicker(getContext());

        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(beginLabel, labelParam);
        rootLayout.addView(beginTimePicker, timePickerParam);
        rootLayout.addView(endLabel, labelParam);
        rootLayout.addView(endTimePicker, timePickerParam);

        scrollView.addView(rootLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        return scrollView;
    }

    public void save() {
        int beginTimeHour = beginTimePicker.getHour();
        int beginTimeMinute = beginTimePicker.getMinute();
        int endTimeHour = endTimePicker.getHour();
        int endTimeMinute = endTimePicker.getMinute();
        Gap gap = new Gap(beginTimeHour, beginTimeMinute, endTimeHour, endTimeMinute);
        _onSaveClickedListener.onSaveClicked(gap);
    }
}
