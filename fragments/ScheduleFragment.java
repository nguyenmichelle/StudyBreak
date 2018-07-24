package com.example.michellenguy3n.studybreak.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.model.Course;
import com.example.michellenguy3n.studybreak.model.Gap;
import com.example.michellenguy3n.studybreak.model.User;
import com.example.michellenguy3n.studybreak.model.Users;

/**
 * This allows users to view their currently uploaded schedule and edit or add courses.
 * 
 * Created by michellenguy3n on 12/8/16.
 */
public class ScheduleFragment extends Fragment {
    public static ScheduleFragment newInstance() {
        return new ScheduleFragment();
    }

    public interface OnAddClassClickedListener {
        void onAddClassClicked();
    }

    public interface OnAddGapClickedListener {
        void onAddGapClicked();
    }

    public interface OnClassDeletedListener {
        void onClassDeleted(int index);
    }

    public interface OnGapDeletedListener {
        void onGapDeleted(int index);
    }

    private OnAddClassClickedListener _onAddClassClickedListener = null;
    private OnAddGapClickedListener _onAddGapClickedListener = null;
    private OnGapDeletedListener _onGapDeletedListener = null;
    private OnClassDeletedListener _onClassDeletedListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onAddClassClickedListener = (ScheduleFragment.OnAddClassClickedListener) context;
            _onAddGapClickedListener = (ScheduleFragment.OnAddGapClickedListener) context;
            _onGapDeletedListener = (OnGapDeletedListener) context;
            _onClassDeletedListener = (OnClassDeletedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement OnAddClassClickedListener and OnAddGapClickedListener and OnClassDeletedListener and OnGapDeletedListener!");
        }
    }

    @Override
    public void onDetach() {
        _onAddClassClickedListener = null;
        _onAddGapClickedListener = null;
        _onGapDeletedListener = null;
        _onClassDeletedListener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Get bundle
        Bundle bundle = this.getArguments();
        String email = "";
        boolean onEdit = false;
        if (bundle != null) {
            email = bundle.getString("Email");
            onEdit = bundle.getBoolean("EditMode");
        }
        final User user = Users.getInstance().getUserWithEmail(email);

        if (onEdit) {
            Toast.makeText(getContext(), "Click on the class/gap you would like to delete!", Toast.LENGTH_LONG).show();
        }

        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.silver));

        // Title
        TextView titleTextView = new TextView(getContext());
        titleTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        titleTextView.setText("My Schedule");
        titleTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // Classes label
        TextView classesLabel = new TextView(getContext());
        classesLabel.setTextAppearance(android.R.style.TextAppearance_Large);
        classesLabel.setText("Classes");
        LinearLayout.LayoutParams classesLabelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        classesLabelParam.setMargins(70, 50, 0, 20);

        // Classes layout
        LinearLayout classesLayout = new LinearLayout(getContext());
        classesLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams classesLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        classesLayoutParam.setMargins(150, 0, 150, 0);
        classesLayout.setBackgroundResource(R.drawable.border);
        if (user.getClasses() != null) {
            for (final Course course : user.getClasses()) {
                LinearLayout courseAndInstructorLinearLayout = new LinearLayout(getContext());
                courseAndInstructorLinearLayout.setBackgroundResource(R.drawable.border);
                courseAndInstructorLinearLayout.setOrientation(LinearLayout.VERTICAL);
                if (onEdit) {
                    courseAndInstructorLinearLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            _onClassDeletedListener.onClassDeleted(user.getClasses().indexOf(course));
                        }
                    });
                }

                TextView courseTextView = new TextView(getContext());
                courseTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
                courseTextView.setText(course.getSubject() + " " + course.getCourseNumber() + ", " + course.getClassName());
                courseTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                if (onEdit) {
                    courseTextView.setTextColor(Color.RED);
                }

                TextView instructorTextView = new TextView(getContext());
                instructorTextView.setText("Instructor: " + course.getInstructor());
                instructorTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                if (onEdit) {
                    courseTextView.setTextColor(Color.RED);
                }

                courseAndInstructorLinearLayout.addView(courseTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                courseAndInstructorLinearLayout.addView(instructorTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                classesLayout.addView(courseAndInstructorLinearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        // addClasses Button
        Button addClassesButton = new Button(getContext());
        addClassesButton.setText("Add Classes");
        addClassesButton.setTextAppearance(android.R.style.TextAppearance_Large);
        addClassesButton.setTypeface(null, Typeface.BOLD);
        addClassesButton.setBackgroundColor(Color.WHITE);
        addClassesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onAddClassClickedListener.onAddClassClicked();
            }
        });

        // Gaps label
        TextView gapsLabel = new TextView(getContext());
        gapsLabel.setTextAppearance(android.R.style.TextAppearance_Large);
        gapsLabel.setText("Gaps");

        // Gaps layout
        LinearLayout gapsLayout = new LinearLayout(getContext());
        gapsLayout.setOrientation(LinearLayout.VERTICAL);
        gapsLayout.setBackgroundResource(R.drawable.border);
        if (user.getGaps() != null) {
            for (final Gap gap : user.getGaps()) {
                TextView gapTextView = new TextView(getContext());
                gapTextView.setTextAppearance(android.R.style.TextAppearance_Large);
                gapTextView.setBackgroundResource(R.drawable.border);
                gapTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                if (onEdit) {
                    gapTextView.setTextColor(Color.RED);
                    gapTextView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            _onGapDeletedListener.onGapDeleted(user.getGaps().indexOf(gap));
                        }
                    });
                }
                gapTextView.setText(gap.getProperlyFormattedGap());
                gapsLayout.addView(gapTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        // addGaps Button
        Button addGapsButton = new Button(getContext());
        addGapsButton.setText("Add Gaps");
        addGapsButton.setTextAppearance(android.R.style.TextAppearance_Large);
        addGapsButton.setTypeface(null, Typeface.BOLD);
        addGapsButton.setBackgroundColor(Color.WHITE);
        addGapsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onAddGapClickedListener.onAddGapClicked();
            }
        });

        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(classesLabel, classesLabelParam);
        rootLayout.addView(classesLayout, classesLayoutParam);
        rootLayout.addView(addClassesButton, classesLayoutParam);
        rootLayout.addView(gapsLabel, classesLabelParam);
        rootLayout.addView(gapsLayout, classesLayoutParam);
        rootLayout.addView(addGapsButton, classesLayoutParam);

        return rootLayout;
    }
}
