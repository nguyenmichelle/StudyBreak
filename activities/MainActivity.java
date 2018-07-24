package com.example.michellenguy3n.studybreak.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.database.FirebaseHelper;
import com.example.michellenguy3n.studybreak.model.User;
import com.example.michellenguy3n.studybreak.model.Users;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

import io.fabric.sdk.android.Fabric;

import org.json.JSONObject;

import java.util.Arrays;

/**
 * Users who are not signed into the application are first met by the Home Activity. This allows users to sign in using an existing Study/Break account or with an existing social media account.
 * 
 * 
 * TO MY GRADER:
 * 
 * In order to run this app, your device or emulator MUST have the latest version of Google Play Store installed.
 * 
 * To get google play store: I used Genymotion Emulator and installed the play store by downloading a zip file from
 * http://opengapps.org/ (with platform: x86, android: 6.0, variant: nano) and dragging and dropping the zip file directly
 * to the emulator and then flashing and restarting it.
 * 
 * To test the app, you can either create an account and try to add your own classes and gaps, or if you want to test
 * all the functionality that the application provides, I would recommend signing in with these credentials:
 * 
 * Username: jon@snow.com
 * Password: adminpass
 */
public class MainActivity extends AppCompatActivity implements OnClickListener {
    EditText usernameEditText;
    EditText passwordEditText;
    private FirebaseAuth firebaseAuth;
    FirebaseHelper firebaseHelper;
    public static CallbackManager callbackManager;
    TwitterAuthClient mTwitterAuthClient;
    GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 0;
    boolean facebooklogin = false;
    boolean googlelogin = false;
    boolean twitterlogin = false;
    private static final String TWITTER_KEY = "hgeN455LbdO8U0yquOdist5Af";
    private static final String TWITTER_SECRET = "sXsLUecnq7kQZmsXKpDP6P8OJPNnz9bxX0lq5etXVREpyzYstI";
    private static final int PERMISSION_REQUEST_CODE = 1;

    private class FacebookUserDetails {
        String name;
        String id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.log_in_main);

        // Retrieve views
        TextView titleView = (TextView) findViewById(R.id.title_text_view);
        TextView createNewAccountText = (TextView) findViewById(R.id.new_account_text_view);
        usernameEditText = (EditText) findViewById(R.id.username_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        Button loginButton = (Button) findViewById(R.id.login_button);
        Button facebookButton = (Button) findViewById(R.id.facebook_button);
        Button googleButton = (Button) findViewById(R.id.google_button);
        Button twitterButton = (Button) findViewById(R.id.twitter_button);

        // Modify title font and size
        Typeface metropolis = Typeface.createFromAsset(getAssets(), "fonts/Metropolis.ttf");
        titleView.setTypeface(metropolis);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        float textSize = height * 0.025f;
        titleView.setTextSize(textSize);
        titleView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.lightBlue));

        // Set on click listeners
        createNewAccountText.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        twitterButton.setOnClickListener(this);

        // Setup firebaseHelper
        firebaseHelper = new FirebaseHelper(FirebaseDatabase.getInstance().getReference());
        //firebaseHelper.setOnUserAddedListener(this);

        // Set up user backend
        firebaseHelper.getAllUsersFromDatabase();

        // Setup firebaseAuth
        firebaseAuth = FirebaseAuth.getInstance();

        // Setup Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());

        // Setup Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Setup Twitter
        mTwitterAuthClient = new TwitterAuthClient();

        // Permissions
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkPermission()) {
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button:
                login();
                break;
            case R.id.facebook_button:
                facebookLogin();
                break;
            case R.id.google_button:
                googleLogin();
                break;
            case R.id.twitter_button:
                twitterLogin();
                break;
            case R.id.new_account_text_view:
                startActivity(new Intent(MainActivity.this, CreateAccountActivity.class));
                break;
        }
    }

    public void login() {
        final String email = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.equals("") || password.equals("")) {
            Toast.makeText(MainActivity.this, "Please fill out all the fields!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(MainActivity.this, "Verifying credentials...", Toast.LENGTH_SHORT).show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(MainActivity.this, "Incorrect email/password combination!", Toast.LENGTH_LONG).show();
                    } else {
                        Intent loginIntent = new Intent(MainActivity.this, HomeActivity.class);
                        loginIntent.putExtra("Email", email);
                        loginIntent.putExtra("RegularLogin", true);
                        startActivity(loginIntent);
                    }
                }
            });
        }
    }

    public void facebookLogin() {
        facebooklogin = true;
        twitterlogin = false;
        googlelogin = false;

        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest.newMeRequest(
                                loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject json, GraphResponse response) {
                                        if (response.getError() != null) {
                                        } else {
                                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);

                                            String jsonresult = String.valueOf(json);
                                            Gson gson = new Gson();
                                            FacebookUserDetails userDetails = gson.fromJson(jsonresult, FacebookUserDetails.class);
                                            String name = userDetails.name;
                                            String id = userDetails.id;
                                            String[] userName = name.split("\\s+");
                                            String firstName = userName[0];
                                            String lastName = userName[1];
                                            String email = "" + id + "@facebook.com";
                                            String profilePicURL = "https://graph.facebook.com/" + id + "/picture?type=large";
                                            User user = new User(firstName, lastName, email, profilePicURL, "Freshman", "", null, null, null, null, null);
                                            if (!Users.getInstance().checkUserExists(email)) {
                                                Users.getInstance().addUser(user);
                                                firebaseHelper.saveToDatabase(user);
                                            }

                                            intent.putExtra("Email", email);
                                            intent.putExtra("RegularLogin", false);
                                            startActivity(intent);
                                        }
                                    }

                                }).executeAsync();
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException error) {
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (facebooklogin) {
            // Pass the activity result back to the Facebook SDK
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

        if (googlelogin) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                if (result.isSuccess()) {
                    // Signed in successfully, show authenticated UI.
                    GoogleSignInAccount acct = result.getSignInAccount();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    String name = acct.getDisplayName();
                    String email = acct.getEmail();
                    String profilePicURL = acct.getPhotoUrl().toString();
                    String[] fullName = name.split("\\s+");
                    String firstName = fullName[0];
                    String lastName = fullName[1];
                    User user = new User(firstName, lastName, email, profilePicURL, "Freshman", "", null, null, null, null, null);
                    if (!Users.getInstance().checkUserExists(email)) {
                        Users.getInstance().addUser(user);
                        firebaseHelper.saveToDatabase(user);
                    }

                    intent.putExtra("Email", email);
                    intent.putExtra("RegularLogin", false);
                    startActivity(intent);
                } else {
                }
            }
        }

        if (twitterlogin) {
            mTwitterAuthClient.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void googleLogin() {
        googlelogin = true;
        twitterlogin = false;
        facebooklogin = false;

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void twitterLogin() {
        twitterlogin = true;
        facebooklogin = false;
        googlelogin = false;

        mTwitterAuthClient.authorize(this, new com.twitter.sdk.android.core.Callback<TwitterSession>() {

            @Override
            public void success(Result<TwitterSession> twitterSessionResult) {
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);


                String userId = "" + twitterSessionResult.data.getUserId();
                String username = twitterSessionResult.data.getUserName();
                String email = "" + userId + "@twitter.com";
                String profilePicURL = "https://twitter.com/" + username + "/profile_image?size=original";
                User user = new User(username, username, email, profilePicURL, "Freshman", "", null, null, null, null, null);
                if (!Users.getInstance().checkUserExists(email)) {
                    Users.getInstance().addUser(user);
                    firebaseHelper.saveToDatabase(user);
                }

                intent.putExtra("Email", email);
                intent.putExtra("RegularLogin", false);
                startActivity(intent);
            }

            @Override
            public void failure(TwitterException e) {
                e.printStackTrace();
            }
        });


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(this, "Read External Storage permission allows us to read images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("value", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}
