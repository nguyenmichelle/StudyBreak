package com.example.michellenguy3n.studybreak.fragments;

import android.content.Context;
import android.graphics.Color;
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
import com.example.michellenguy3n.studybreak.activities.MainActivity;

/**
 * Allows users to delete their account.
 * 
 * Created by michellenguy3n on 12/9/16.
 */
public class DeleteAccountFragment extends Fragment {
    public static DeleteAccountFragment newInstance() {
        return new DeleteAccountFragment();
    }

    public interface OnCancelClickedListener {
        void onCancelClicked();
    }

    public interface OnAccountDeletedListener {
        void onAccountDeleted();
    }

    private OnCancelClickedListener _onCancelClickedListener = null;
    private OnAccountDeletedListener _onAccountDeletedListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onCancelClickedListener = (OnCancelClickedListener) context;
            _onAccountDeletedListener = (OnAccountDeletedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement OnCancelClickedListener and OnAccountDeletedListener!");
        }
    }

    @Override
    public void onDetach() {
        _onCancelClickedListener = null;
        _onAccountDeletedListener = null;
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
        titleTextView.setText("Delete Account");
        titleTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // TextView
        TextView deleteAccountTextView = new TextView(getContext());
        deleteAccountTextView.setText("Are you sure you want to delete your Study/Break account?\nPlease note: All information from this account will be lost forever!");
        deleteAccountTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.silver));
        deleteAccountTextView.setBackgroundResource(R.drawable.border);
        deleteAccountTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        deleteAccountTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Buttons
        LinearLayout buttonsLayout = new LinearLayout(getContext());
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        Button cancelButton = new Button(getContext());
        cancelButton.setText("Cancel");
        cancelButton.setPadding(20, 20, 20, 20);
        cancelButton.setBackgroundResource(R.drawable.border);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onCancelClickedListener.onCancelClicked();
            }
        });
        Button deleteButton = new Button(getContext());
        deleteButton.setText("Delete");
        deleteButton.setTextColor(Color.RED);
        deleteButton.setPadding(20, 20, 20, 20);
        deleteButton.setBackgroundResource(R.drawable.border);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onAccountDeletedListener.onAccountDeleted();
            }
        });
        buttonsLayout.addView(cancelButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        buttonsLayout.addView(deleteButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        // Layout params
        LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewParams.setMargins(150, 350, 150, 300);
        LinearLayout.LayoutParams buttonsLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonsLayoutParams.setMargins(150, 0, 150, 0);

        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(deleteAccountTextView, textViewParams);
        rootLayout.addView(buttonsLayout, buttonsLayoutParams);

        return rootLayout;
    }
}
