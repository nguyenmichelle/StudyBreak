package com.example.michellenguy3n.studybreak.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.activities.MainActivity;

/**
 * This notifies users when their sign up is complete.
 * 
 * Created by michellenguy3n on 12/14/16.
 */
public class SignUpSuccessFragment extends DialogFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_signup_success, container, false);
        getDialog().setTitle("Sign Up Complete");

        Button logInButton = (Button) rootView.findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MainActivity.class));
            }
        });

        return rootView;
    }
}
