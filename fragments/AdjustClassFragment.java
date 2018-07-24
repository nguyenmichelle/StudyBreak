package com.example.michellenguy3n.studybreak.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.activities.MainActivity;
import com.example.michellenguy3n.studybreak.model.Course;

/**
 * Allows users to edit existing classes in their schedule.
 * 
 * Created by michellenguy3n on 12/8/16.
 */
public class AdjustClassFragment extends Fragment {
    EditText subjectEditText;
    EditText classNoEditText;
    EditText classNameEditText;
    EditText instructorEditText;

    public static AdjustClassFragment newInstance() {
        return new AdjustClassFragment();
    }

    public interface OnSaveClickedListener {
        void onSaveClicked(Course course);
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
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.silver));

        // Title
        TextView titleTextView = new TextView(getContext());
        titleTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        titleTextView.setText("Adjust Class");
        titleTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // Edit texts
        subjectEditText = new EditText(getContext());
        subjectEditText.setBackgroundResource(R.drawable.border);
        subjectEditText.setTextAppearance(android.R.style.TextAppearance_Large);
        classNoEditText = new EditText(getContext());
        classNoEditText.setBackgroundResource(R.drawable.border);
        classNoEditText.setTextAppearance(android.R.style.TextAppearance_Large);
        classNameEditText = new EditText(getContext());
        classNameEditText.setBackgroundResource(R.drawable.border);
        classNameEditText.setTextAppearance(android.R.style.TextAppearance_Large);
        instructorEditText = new EditText(getContext());
        instructorEditText.setBackgroundResource(R.drawable.border);
        instructorEditText.setTextAppearance(android.R.style.TextAppearance_Large);

        // Labels
        TextView subjectTextView = new TextView(getContext());
        subjectTextView.setText("Subject (Ex. Biology, Computer Science)");
        subjectTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        TextView classNoTextView = new TextView(getContext());
        classNoTextView.setText("Class Number (Ex. 1010, 3100)");
        classNoTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        TextView classNameTextView = new TextView(getContext());
        classNameTextView.setText("Class Name (Ex. Principles of Biology, Theory of Computation)");
        classNameTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        TextView instructorTextView = new TextView(getContext());
        instructorTextView.setText("Instructor (Last name, First name)");
        instructorTextView.setTextAppearance(android.R.style.TextAppearance_Medium);

        // Layout params
        LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editTextLayoutParams.setMargins(70, 150, 70, 0);
        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(70, 0, 70, 0);

        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(subjectEditText, editTextLayoutParams);
        rootLayout.addView(subjectTextView, textViewLayoutParams);
        rootLayout.addView(classNoEditText, editTextLayoutParams);
        rootLayout.addView(classNoTextView, textViewLayoutParams);
        rootLayout.addView(classNameEditText, editTextLayoutParams);
        rootLayout.addView(classNameTextView, textViewLayoutParams);
        rootLayout.addView(instructorEditText, editTextLayoutParams);
        rootLayout.addView(instructorTextView, textViewLayoutParams);

        return rootLayout;
    }

    public void save() {
        if (subjectEditText.getText().toString().isEmpty() || classNoEditText.getText().toString().isEmpty() || classNameEditText.getText().toString().isEmpty() || instructorEditText.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), "Fill out all the fields!", Toast.LENGTH_SHORT).show();
        } else {
            Course course = new Course(subjectEditText.getText().toString(), classNoEditText.getText().toString(), classNameEditText.getText().toString(), instructorEditText.getText().toString());
            _onSaveClickedListener.onSaveClicked(course);
        }
    }
}
