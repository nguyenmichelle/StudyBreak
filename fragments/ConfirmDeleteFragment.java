package com.example.michellenguy3n.studybreak.fragments;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.activities.MainActivity;
import com.example.michellenguy3n.studybreak.database.FirebaseHelper;
import com.example.michellenguy3n.studybreak.model.User;
import com.example.michellenguy3n.studybreak.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Confirms with users that they would like to delete their account.
 * 
 * Created by michellenguy3n on 12/14/16.
 */
public class ConfirmDeleteFragment extends DialogFragment {
    public static ConfirmDeleteFragment newInstance(String email) {
        ConfirmDeleteFragment f = new ConfirmDeleteFragment();

        Bundle args = new Bundle();
        args.putString("Email", email);
        f.setArguments(args);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String email = getArguments().getString("Email");

        final View rootView = inflater.inflate(R.layout.fragment_delete, container, false);
        getDialog().setTitle("Delete Account");

        Button cancelButton = (Button) rootView.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        Button deleteButton = (Button) rootView.findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Verifying credentials...", Toast.LENGTH_LONG).show();
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String email = user.getEmail();
                EditText passwordEditText = (EditText) rootView.findViewById(R.id.password_edit_text);
                String password = passwordEditText.getText().toString();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, password);

                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(), "Account successfully deleted.", Toast.LENGTH_LONG).show();
                                                FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseDatabase.getInstance().getReference());
                                                User user = Users.getInstance().getUserWithEmail(email);
                                                firebaseHelper.deleteUserFromDatabase(email.substring(0, email.length() - 4));
                                                Users.getInstance().removeUser(user);
                                                firebaseHelper.deleteOccurencesInDatabase(email);
                                                startActivity(new Intent(getActivity(), MainActivity.class));
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(), "Incorrect password.", Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });

        return rootView;
    }
}
