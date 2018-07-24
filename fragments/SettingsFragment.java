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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;

/**
 * This fragment contains the settings for the application.
 * 
 * Created by michellenguy3n on 12/7/16.
 */
public class SettingsFragment extends Fragment {
    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    public interface OnChangePasswordClickedListener {
        void onChangePasswordClicked();
    }

    public interface OnDeleteAccountClickedListener {
        void onDeleteAccountClicked();
    }

    private OnChangePasswordClickedListener _onChangePasswordClickedListener = null;
    private OnDeleteAccountClickedListener _onDeleteAccountClickedListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onChangePasswordClickedListener = (OnChangePasswordClickedListener) context;
            _onDeleteAccountClickedListener = (OnDeleteAccountClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement OnChangePasswordClickedListener and OnDeleteAccountClickedListener!");
        }
    }

    @Override
    public void onDetach() {
        _onChangePasswordClickedListener = null;
        _onDeleteAccountClickedListener = null;
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
        titleTextView.setText("Settings");
        titleTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // Label
        TextView notificationsLabel = new TextView(getContext());
        notificationsLabel.setText("Notifications");
        notificationsLabel.setTextAppearance(android.R.style.TextAppearance_Large);

        // Notifications layout
        LinearLayout notificationsLayout = new LinearLayout(getContext());
        notificationsLayout.setOrientation(LinearLayout.VERTICAL);
        notificationsLayout.setBackgroundColor(Color.WHITE);

        LinearLayout studyBuddyMatchLayout = new LinearLayout(getContext());
        studyBuddyMatchLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView studyBuddyTextView = new TextView(getContext());
        studyBuddyTextView.setText("New Study Buddy Match");
        studyBuddyTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        CheckBox studyBuddyCheckBox = new CheckBox(getContext());
        studyBuddyCheckBox.setChecked(true);
        studyBuddyMatchLayout.addView(studyBuddyTextView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 8));
        studyBuddyMatchLayout.addView(studyBuddyCheckBox, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        LinearLayout breakBuddyMatchLayout = new LinearLayout(getContext());
        breakBuddyMatchLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView breakBuddyTextView = new TextView(getContext());
        breakBuddyTextView.setText("New Break Buddy Match");
        breakBuddyTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        CheckBox breakBuddyCheckBox = new CheckBox(getContext());
        breakBuddyCheckBox.setChecked(true);
        breakBuddyMatchLayout.addView(breakBuddyTextView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 8));
        breakBuddyMatchLayout.addView(breakBuddyCheckBox, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        LinearLayout newMessageLayout = new LinearLayout(getContext());
        newMessageLayout.setOrientation(LinearLayout.HORIZONTAL);
        TextView messagesTextView = new TextView(getContext());
        messagesTextView.setText("Messages");
        messagesTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        CheckBox messagesCheckBox = new CheckBox(getContext());
        messagesCheckBox.setChecked(true);
        newMessageLayout.addView(messagesTextView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 8));
        newMessageLayout.addView(messagesCheckBox, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        notificationsLayout.addView(studyBuddyMatchLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        notificationsLayout.addView(breakBuddyMatchLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        notificationsLayout.addView(newMessageLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        // Buttons
        Button changePasswordButton = new Button(getContext());
        changePasswordButton.setText("Change Password");
        changePasswordButton.setTextAppearance(android.R.style.TextAppearance_Large);
        changePasswordButton.setBackgroundColor(Color.WHITE);
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onChangePasswordClickedListener.onChangePasswordClicked();
            }
        });
        Button deleteAccountButton = new Button(getContext());
        deleteAccountButton.setText("Delete My Account");
        deleteAccountButton.setTextAppearance(android.R.style.TextAppearance_Large);
        deleteAccountButton.setTypeface(null, Typeface.BOLD);
        deleteAccountButton.setTextColor(Color.RED);
        deleteAccountButton.setBackgroundColor(Color.WHITE);
        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onDeleteAccountClickedListener.onDeleteAccountClicked();
            }
        });

        // Layout params
        LinearLayout.LayoutParams labelParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        labelParam.setMargins(70, 50, 0, 0);
        LinearLayout.LayoutParams notificationsLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        notificationsLayoutParam.setMargins(150, 0, 150, 0);
        LinearLayout.LayoutParams buttonParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParam.setMargins(150, 200, 150, 0);

        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(notificationsLabel, labelParam);
        rootLayout.addView(notificationsLayout, notificationsLayoutParam);
        rootLayout.addView(changePasswordButton, buttonParam);
        rootLayout.addView(deleteAccountButton, buttonParam);

        return rootLayout;
    }
}
