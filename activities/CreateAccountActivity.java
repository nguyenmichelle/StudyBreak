package com.example.michellenguy3n.studybreak.activities;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.database.FirebaseHelper;
import com.example.michellenguy3n.studybreak.model.User;
import com.example.michellenguy3n.studybreak.model.Users;
import com.example.michellenguy3n.studybreak.fragments.SignUpSuccessFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Activity for users who are creating a new account.
 * 
 * Created by michellenguy3n on 12/4/16.
 */
public class CreateAccountActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseHelper firebaseHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_account_main);

        firebaseAuth = FirebaseAuth.getInstance();

        // Set up Firebase database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseHelper = new FirebaseHelper(databaseReference);

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(CreateAccountActivity.this, "Please wait while we verify entries..", Toast.LENGTH_LONG).show();
                EditText firstNameEditText = (EditText) findViewById(R.id.first_name_edit_text);
                EditText lastNameEditText = (EditText) findViewById(R.id.last_name_edit_text);
                EditText emailEditText = (EditText) findViewById(R.id.email_edit_text);
                EditText passwordEditText = (EditText) findViewById(R.id.password_edit_text);
                Spinner classYearSpinner = (Spinner) findViewById(R.id.class_year_spinner);

                final String firstName = firstNameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                final String classYear = classYearSpinner.getSelectedItem().toString();

                // Validation check for required fields
                if (firstName.equals("") || lastName.equals("") || email.equals("") || password.equals("") || classYear.equals("Choose a class year")) {
                    Toast.makeText(CreateAccountActivity.this, "Please fill out all the fields!", Toast.LENGTH_LONG).show();
                } else {
                    // Validation check for existing users
                    if (Users.getInstance().checkUserExists(email)) {
                        Toast.makeText(CreateAccountActivity.this, email + " already exists in the system. Did you mean to log in?", Toast.LENGTH_LONG).show();
                    } else {
                        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Validation check for properly formatted fields
                                if (!task.isSuccessful()) {
                                    Toast.makeText(CreateAccountActivity.this, "Make sure email is properly formatted and password has at least 6 characters!", Toast.LENGTH_LONG).show();
                                } else {
                                    // Create a new user and add to the database
                                    final User user = new User(firstName, lastName, email, "", classYear, "", null, null, null, null, null);
                                    firebaseHelper.saveToDatabase(user);
                                    Users.getInstance().addUser(user);
                                    FragmentManager fm = getFragmentManager();
                                    SignUpSuccessFragment signUpSuccessFragment = new SignUpSuccessFragment();
                                    signUpSuccessFragment.show(fm, "SignUpSuccessFragment");
                                }
                            }
                        });
                    }
                }
            }
        });
    }
}
