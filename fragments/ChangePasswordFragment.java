package com.example.michellenguy3n.studybreak.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellenguy3n.studybreak.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Allows users to change their current password.
 * 
 * Created by michellenguy3n on 12/9/16.
 */
public class ChangePasswordFragment extends Fragment {

    EditText currentPasswordEditText;
    EditText newPasswordEditText;
    EditText confirmNewPasswordEditText;

    public static ChangePasswordFragment newInstance() {
        return new ChangePasswordFragment();
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
        titleTextView.setText("Change Password");
        titleTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // Password edit texts
        currentPasswordEditText = new EditText(getContext());
        currentPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        currentPasswordEditText.setTextAppearance(android.R.style.TextAppearance_Large);
        currentPasswordEditText.setBackgroundResource(R.drawable.border);
        newPasswordEditText = new EditText(getContext());
        newPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        newPasswordEditText.setTextAppearance(android.R.style.TextAppearance_Large);
        newPasswordEditText.setBackgroundResource(R.drawable.border);
        confirmNewPasswordEditText = new EditText(getContext());
        confirmNewPasswordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
        confirmNewPasswordEditText.setTextAppearance(android.R.style.TextAppearance_Large);
        confirmNewPasswordEditText.setBackgroundResource(R.drawable.border);

        // Labels
        final TextView currentPasswordTextView = new TextView(getContext());
        currentPasswordTextView.setText("Current Password");
        currentPasswordTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        final TextView newPasswordTextView = new TextView(getContext());
        newPasswordTextView.setText("New Password");
        newPasswordTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        final TextView confirmNewPasswordTextView = new TextView(getContext());
        confirmNewPasswordTextView.setText("Confirm New Password");
        confirmNewPasswordTextView.setTextAppearance(android.R.style.TextAppearance_Medium);

        // Button
        Button saveButton = new Button(getContext());
        saveButton.setText("Save");
        saveButton.setBackgroundColor(Color.WHITE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Verifying... please wait.", Toast.LENGTH_LONG).show();

                final String currentPasswordText = currentPasswordEditText.getText().toString();
                final String newPasswordText = newPasswordEditText.getText().toString();
                final String confirmPasswordText = confirmNewPasswordEditText.getText().toString();

                if (currentPasswordText.isEmpty() || newPasswordText.isEmpty() || confirmPasswordText.isEmpty()) {
                    Toast.makeText(getActivity(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                } else {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String email = user.getEmail();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(email, currentPasswordText);

                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (newPasswordText.equals(confirmPasswordText)) {
                                    user.updatePassword(newPasswordText).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getActivity(), "User password successfully updated.", Toast.LENGTH_LONG).show();
                                                currentPasswordEditText.getText().clear();
                                                newPasswordEditText.getText().clear();
                                                confirmNewPasswordEditText.getText().clear();
                                            } else {
                                                Toast.makeText(getActivity(), "Password Change Unsuccessful. \nMake sure your new password is at least 6 characters.", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "Password change unsuccessful! \nNew password doesn't match.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Password change unsuccessful! \nThe current password is incorrect.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });

        // Layout params
        LinearLayout.LayoutParams editTextLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editTextLayoutParams.setMargins(150, 200, 150, 0);
        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(150, 0, 150, 0);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(150, 150, 150, 0);

        // Add views
        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(currentPasswordEditText, editTextLayoutParams);
        rootLayout.addView(currentPasswordTextView, textViewLayoutParams);
        rootLayout.addView(newPasswordEditText, editTextLayoutParams);
        rootLayout.addView(newPasswordTextView, textViewLayoutParams);
        rootLayout.addView(confirmNewPasswordEditText, editTextLayoutParams);
        rootLayout.addView(confirmNewPasswordTextView, textViewLayoutParams);
        rootLayout.addView(saveButton, buttonParams);

        return rootLayout;
    }
}
