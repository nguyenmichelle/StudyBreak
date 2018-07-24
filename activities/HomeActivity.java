package com.example.michellenguy3n.studybreak.activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.database.FirebaseHelper;
import com.example.michellenguy3n.studybreak.fragments.ConversationFragment;
import com.example.michellenguy3n.studybreak.model.Buddy;
import com.example.michellenguy3n.studybreak.model.Course;
import com.example.michellenguy3n.studybreak.model.Gap;
import com.example.michellenguy3n.studybreak.model.User;
import com.example.michellenguy3n.studybreak.model.Users;
import com.example.michellenguy3n.studybreak.fragments.AboutFragment;
import com.example.michellenguy3n.studybreak.fragments.AdjustClassFragment;
import com.example.michellenguy3n.studybreak.fragments.AdjustGapTimesFragment;
import com.example.michellenguy3n.studybreak.fragments.ChangePasswordFragment;
import com.example.michellenguy3n.studybreak.fragments.ConfirmDeleteFragment;
import com.example.michellenguy3n.studybreak.fragments.DeleteAccountFragment;
import com.example.michellenguy3n.studybreak.fragments.EditProfileFragment;
import com.example.michellenguy3n.studybreak.fragments.HomeFragment;
import com.example.michellenguy3n.studybreak.fragments.MessagesFragment;
import com.example.michellenguy3n.studybreak.fragments.NoBuddiesFragment;
import com.example.michellenguy3n.studybreak.fragments.NoPotentialBuddiesFragment;
import com.example.michellenguy3n.studybreak.fragments.PotentialBuddyFragment;
import com.example.michellenguy3n.studybreak.fragments.ProfileFragment;
import com.example.michellenguy3n.studybreak.fragments.ScheduleFragment;
import com.example.michellenguy3n.studybreak.fragments.SettingsFragment;
import com.example.michellenguy3n.studybreak.views.Menu;
import com.example.michellenguy3n.studybreak.views.NewBuddyView;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * After users sign in, the Home Activity starts. Users can perform all non-authorization actions on the application through this activity.
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
public class HomeActivity extends AppCompatActivity implements Menu.OnMenuItemClickedListener, EditProfileFragment.OnEditScheduleClickedListener, ScheduleFragment.OnAddClassClickedListener, ScheduleFragment.OnAddGapClickedListener, SettingsFragment.OnChangePasswordClickedListener, SettingsFragment.OnDeleteAccountClickedListener, DeleteAccountFragment.OnCancelClickedListener, DeleteAccountFragment.OnAccountDeletedListener, EditProfileFragment.OnCameraButtonClickedListener, EditProfileFragment.OnSaveClickedListener, ActivityCompat.OnRequestPermissionsResultCallback, MessagesFragment.OnBuddyClickedListener, AdjustClassFragment.OnSaveClickedListener, AdjustGapTimesFragment.OnSaveClickedListener, ScheduleFragment.OnClassDeletedListener, ScheduleFragment.OnGapDeletedListener, FirebaseHelper.OnClassesMatchedListener, FirebaseHelper.OnGapsMatchedListener, PotentialBuddyFragment.OnNoButtonClickedListener, PotentialBuddyFragment.OnYesButtonClickedListener, NewBuddyView.OnSendMessageClickedListener, NewBuddyView.OnKeepSearchingClickedListener {
    // Declaration of variables
    int screenWidth = 0;
    boolean backArrowPresent = false;
    boolean onHome = true;
    boolean inProfile = false;
    boolean onEdit = false;
    boolean onStudy = false;
    boolean onBreak = false;
    boolean onAddGapOrClass = false;
    boolean onEditScheduleScreen = false;
    boolean onChangePasswordOrDeleteAccount = false;
    boolean inConversation = false;
    ImageView studyImageView;
    ImageView homeImageView;
    ImageView breakImageView;
    FrameLayout menuLayout;
    LinearLayout dockLayout;
    LinearLayout rootLayout;
    LinearLayout topPaneLayout;
    FrameLayout masterLayout;
    ImageButton menuButton;
    ImageButton editButton;
    private final int SELECT_PHOTO = 1;
    User currentUser;
    boolean regularLogin;
    FirebaseHelper firebaseHelper;
    ArrayList<String> potentialStudyBuddies;
    ArrayList<String> potentialBreakBuddies;
    RelativeLayout mainLayout;
    User userYoureLookingAt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize firebaseHelper
        firebaseHelper = new FirebaseHelper(FirebaseDatabase.getInstance().getReference());
        firebaseHelper.setOnClassesMatchedListener(this);
        firebaseHelper.setOnGapsMatchedListener(this);

        // Initialize array lists
        potentialStudyBuddies = new ArrayList<String>();
        potentialBreakBuddies = new ArrayList<String>();

        // Get intent that started activity
        Intent intent = getIntent();
        String email = intent.getStringExtra("Email");
        regularLogin = intent.getExtras().getBoolean("RegularLogin");
        currentUser = Users.getInstance().getUserWithEmail(email);

        // Load potential buddies
        firebaseHelper.getPotentialStudyBuddies(currentUser.getEmail());
        firebaseHelper.getPotentialBreakBuddies(currentUser.getEmail());
        userYoureLookingAt = null;

        // Create main layout
        mainLayout = new RelativeLayout(this);

        // Get screen dimensions
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        // Set up menu
        menuLayout = new FrameLayout(this);
        Menu menuList = new Menu(this);
        menuList.setOnMenuItemClickedListener(this);
        menuLayout.addView(menuList);
        menuLayout.setVisibility(View.INVISIBLE);

        // Create root layout
        rootLayout = new LinearLayout(this);
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        // Deal with top pane
        topPaneLayout = new LinearLayout(this);
        topPaneLayout.setOrientation(LinearLayout.HORIZONTAL);
        topPaneLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.lightBlue));
        menuButton = new ImageButton(this);
        menuButton.setScaleType(ImageView.ScaleType.CENTER);
        menuButton.setAdjustViewBounds(true);
        menuButton.setImageResource(R.drawable.menu);
        menuButton.setBackgroundColor(Color.TRANSPARENT);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (backArrowPresent) {
                    if (onAddGapOrClass) {
                        onAddGapOrClass = false;
                        showScheduleScreen(false);
                    } else if (onChangePasswordOrDeleteAccount) {
                        onChangePasswordOrDeleteAccount = false;
                        showSettingsScreen();
                    } else if (onEdit) {
                        onEdit = false;
                        showProfileScreen();
                    } else if (onEditScheduleScreen) {
                        onEditScheduleScreen = false;
                        showEditProfileScreen();
                    } else if (inConversation) {
                        inConversation = false;
                        showMessagesScreen();
                        TextView titleTextView = (TextView) topPaneLayout.getChildAt(1);
                        titleTextView.setTextSize(screenWidth * 0.025f);
                        titleTextView.setGravity(Gravity.CENTER);
                        Typeface metropolis = Typeface.createFromAsset(getAssets(), "fonts/Metropolis.ttf");
                        titleTextView.setTypeface(metropolis, Typeface.BOLD);
                        titleTextView.setTextColor(Color.parseColor("#ffffff"));
                        titleTextView.setText("Study/Break");
                    } else {
                        backArrowPresent = false;
                        inProfile = false;
                        setHighlightedView(homeImageView);
                        rootLayout.addView(dockLayout);
                        showHomeScreen();
                    }
                } else {
                    menuLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Set up title
        TextView titleTextView = new TextView(this);
        titleTextView.setTextSize(screenWidth * 0.025f);
        titleTextView.setGravity(Gravity.CENTER);
        Typeface metropolis = Typeface.createFromAsset(getAssets(), "fonts/Metropolis.ttf");
        titleTextView.setTypeface(metropolis, Typeface.BOLD);
        titleTextView.setTextColor(Color.parseColor("#ffffff"));
        titleTextView.setText("Study/Break");

        // Set up edit button
        editButton = new ImageButton(this);
        editButton.setBackgroundColor(Color.TRANSPARENT);
        editButton.setScaleType(ImageView.ScaleType.CENTER);
        editButton.setAdjustViewBounds(true);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inProfile) {
                    if (onEdit) {
                        onEdit = false;
                        EditProfileFragment editProfileFragment = (EditProfileFragment) getSupportFragmentManager().findFragmentByTag("EditProfileFragment");
                        editProfileFragment.save();
                        showProfileScreen();
                    } else if (onAddGapOrClass) {
                        onAddGapOrClass = false;
                        AdjustGapTimesFragment adjustGapTimesFragment = (AdjustGapTimesFragment) getSupportFragmentManager().findFragmentByTag("AdjustGapTimesFragment");
                        AdjustClassFragment adjustClassFragment = (AdjustClassFragment) getSupportFragmentManager().findFragmentByTag("AdjustClassFragment");
                        if (adjustGapTimesFragment != null) {
                            adjustGapTimesFragment.save();

                        } else if (adjustClassFragment != null) {
                            adjustClassFragment.save();
                        }
                        showScheduleScreen(false);
                    } else if (onEditScheduleScreen) {
                        onEditScheduleScreen = false;
                        showScheduleScreen(true);
                    } else {
                        showEditProfileScreen();
                    }
                }
            }
        });
        topPaneLayout.addView(menuButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));
        topPaneLayout.addView(titleTextView, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6));
        topPaneLayout.addView(editButton, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        // Deal with master view
        masterLayout = new FrameLayout(this);
        masterLayout.setId(10);
        HomeFragment homeFragment = HomeFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("ProfilePic", currentUser.getProfilePicURL());
        homeFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(masterLayout.getId(), homeFragment, "HomeFragment").commit();
        getSupportFragmentManager().executePendingTransactions();

        // Deal with dock
        dockLayout = new LinearLayout(this);
        dockLayout.setOrientation(LinearLayout.HORIZONTAL);
        studyImageView = new ImageView(this);
        studyImageView.setId(20);
        homeImageView = new ImageView(this);
        homeImageView.setId(21);
        breakImageView = new ImageView(this);
        breakImageView.setId(22);
        studyImageView.setImageResource(R.drawable.study);
        homeImageView.setImageResource(R.drawable.home);
        breakImageView.setImageResource(R.drawable.sbreak);
        setHighlightedView(homeImageView);

        studyImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStudyClicked();
            }
        });
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setOnView(1);
                setHighlightedView(homeImageView);
                showHomeScreen();
            }
        });
        breakImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBreakClicked();
            }
        });
        dockLayout.addView(studyImageView, new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        dockLayout.addView(homeImageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        dockLayout.addView(breakImageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        // Add views to root layout
        rootLayout.addView(topPaneLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        rootLayout.addView(masterLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 8));
        rootLayout.addView(dockLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        // Add views to main layout
        mainLayout.addView(rootLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        float listSize = screenWidth * 0.5f;
        mainLayout.addView(menuLayout, new ViewGroup.LayoutParams((int) listSize, ViewGroup.LayoutParams.MATCH_PARENT));

        setContentView(mainLayout);
    }

    /**
     * Sets the highlighted icon in the dock using the specified imageView.
     *
     * @param imageView - view to be highlighted
     */
    public void setHighlightedView(ImageView imageView) {
        if (imageView.getId() == 20) {
            studyImageView.setBackgroundResource(R.drawable.highlightedborder);
            homeImageView.setBackgroundResource(R.drawable.border);
            breakImageView.setBackgroundResource(R.drawable.border);
        } else if (imageView.getId() == 21) {
            studyImageView.setBackgroundResource(R.drawable.border);
            homeImageView.setBackgroundResource(R.drawable.highlightedborder);
            breakImageView.setBackgroundResource(R.drawable.border);
        } else if (imageView.getId() == 22) {
            studyImageView.setBackgroundResource(R.drawable.border);
            homeImageView.setBackgroundResource(R.drawable.border);
            breakImageView.setBackgroundResource(R.drawable.highlightedborder);
        }
    }

    /**
     * Event listener that performs specific actions based on the menu item that was clicked.
     *
     * @param item - menu item that was clicked
     */
    @Override
    public void onItemClicked(int item) {
        menuLayout.setVisibility(View.INVISIBLE);
        switch (item) {
            case 0:
                if (!onHome) {
                    if (!onStudy && !onBreak) {
                        rootLayout.addView(dockLayout);
                    }
                    setOnView(1);
                    backArrowPresent = false;
                    showHomeScreen();
                    setHighlightedView(homeImageView);
                }
                break;
            case 1:
                setBackArrowInUI();
                showProfileScreen();
                break;
            case 2:
                setBackArrowInUI();
                if (currentUser.getBuddies().size() == 0) {
                    NoBuddiesFragment noBuddiesFragment = NoBuddiesFragment.newInstance();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(masterLayout.getId(), noBuddiesFragment, "NoBuddiesFragment").commit();
                } else {
                    showMessagesScreen();
                }
                break;
            case 3:
                setBackArrowInUI();
                showSettingsScreen();
                break;
            case 4:
                setBackArrowInUI();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                AboutFragment aboutFragment = AboutFragment.newInstance();
                transaction.replace(masterLayout.getId(), aboutFragment);
                transaction.commit();
                break;
        }
    }

    public void setBackArrowInUI() {
        onHome = false;
        rootLayout.removeView(dockLayout);
        menuButton.setImageResource(R.drawable.leftarrow);
        backArrowPresent = true;
    }

    /**
     * Sets boolean values of global variables based on which icon on the dock is enabled.
     *
     * @param view - index of icons in the dock
     */
    public void setOnView(int view) {
        if (view == 0) {
            onStudy = true;
            onHome = false;
            onBreak = false;
        } else if (view == 1) {
            onHome = true;
            onStudy = false;
            onBreak = false;
        } else if (view == 2) {
            onBreak = true;
            onHome = false;
            onStudy = false;
        }
    }

    @Override
    public void onEditScheduleClicked() {
        onEditScheduleScreen = true;
        onEdit = false;
        showScheduleScreen(false);
    }

    @Override
    public void onAddClassClicked() {
        editButton.setImageResource(R.drawable.save);
        onAddGapOrClass = true;
        onEditScheduleScreen = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AdjustClassFragment adjustClassFragment = AdjustClassFragment.newInstance();
        transaction.replace(masterLayout.getId(), adjustClassFragment, "AdjustClassFragment");
        transaction.commit();
    }

    @Override
    public void onAddGapClicked() {
        editButton.setImageResource(R.drawable.save);
        onAddGapOrClass = true;
        onEditScheduleScreen = false;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        AdjustGapTimesFragment adjustGapTimesFragment = AdjustGapTimesFragment.newInstance();
        transaction.replace(masterLayout.getId(), adjustGapTimesFragment, "AdjustGapTimesFragment");
        transaction.commit();
    }

    @Override
    public void onChangePasswordClicked() {
        onChangePasswordOrDeleteAccount = true;

        if (regularLogin) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            ChangePasswordFragment changePasswordFragment = ChangePasswordFragment.newInstance();
            transaction.replace(masterLayout.getId(), changePasswordFragment);
            transaction.commit();
        } else {
            Toast.makeText(HomeActivity.this, "This feature is only available if you logged in with an email account!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeleteAccountClicked() {
        onChangePasswordOrDeleteAccount = true;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        DeleteAccountFragment deleteAccountFragment = DeleteAccountFragment.newInstance();
        transaction.replace(masterLayout.getId(), deleteAccountFragment);
        transaction.commit();
    }

    @Override
    public void onCancelClicked() {
        showSettingsScreen();
    }

    @Override
    public void onAccountDeleted() {
        if (regularLogin) {
            FragmentManager fm = getFragmentManager();
            ConfirmDeleteFragment confirmDeleteFragment = ConfirmDeleteFragment.newInstance(currentUser.getEmail());
            confirmDeleteFragment.show(fm, "ConfirmDeleteFragment");
        } else if (!regularLogin) {
            firebaseHelper.deleteUserFromDatabase(currentUser.getModifiedEmail());
            Users.getInstance().removeUser(currentUser);
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
        }
    }

    @Override
    public void onCameraButtonClicked() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    EditProfileFragment editProfileFragment = (EditProfileFragment) getSupportFragmentManager().findFragmentByTag("EditProfileFragment");
                    editProfileFragment.setProfilePic(imageReturnedIntent.getData().toString());
                    currentUser.setProfilePicURL(imageReturnedIntent.getData().toString());
                }
        }
    }

    @Override
    public void onSaveClicked(String name, String classYear, String biography) {
        currentUser.setFirstName(name);
        currentUser.setClassYear(classYear);
        currentUser.setBiography(biography);
        Users.getInstance().replaceUser(currentUser);
        firebaseHelper.saveToDatabase(currentUser);
    }

    @Override
    public void onBuddyClicked(int i) {
        setBackArrowInUI();
        inConversation = true;
        TextView titleTextView = (TextView) topPaneLayout.getChildAt(1);
        titleTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        titleTextView.setTypeface(Typeface.SANS_SERIF);
        titleTextView.setTextColor(Color.WHITE);
        String emailOfBuddy = currentUser.getBuddies().get(i).getEmail();
        titleTextView.setText(Users.getInstance().getUserWithEmail(emailOfBuddy).getFirstName());

        ConversationFragment conversationFragment = ConversationFragment.newInstance(currentUser.getEmail(), emailOfBuddy);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(masterLayout.getId(), conversationFragment, "ConversationFragment").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    @Override
    public void onSaveClicked(Course course) {
        ArrayList<Course> courses = currentUser.getClasses();
        if (courses == null) {
            courses = new ArrayList<Course>();
            courses.add(course);
        } else {
            courses.add(course);
        }
        currentUser.setClasses(courses);
        Users.getInstance().replaceUser(currentUser);
        firebaseHelper.saveToDatabase(currentUser);
    }

    @Override
    public void onSaveClicked(Gap gap) {
        ArrayList<Gap> gaps = currentUser.getGaps();
        if (gaps == null) {
            gaps = new ArrayList<Gap>();
            gaps.add(gap);
        } else {
            gaps.add(gap);
        }
        currentUser.setGaps(gaps);
        Users.getInstance().replaceUser(currentUser);
        firebaseHelper.saveToDatabase(currentUser);
    }

    @Override
    public void onClassDeleted(int index) {
        onEditScheduleScreen = true;
        firebaseHelper.deleteEntryFromDatabase(currentUser.getModifiedEmail(), "classes", index);
        currentUser.getClasses().remove(index);
        currentUser.setClasses(currentUser.getClasses());
        Users.getInstance().replaceUser(currentUser);
        showScheduleScreen(false);
    }

    @Override
    public void onGapDeleted(int index) {
        onEditScheduleScreen = true;
        firebaseHelper.deleteEntryFromDatabase(currentUser.getModifiedEmail(), "gaps", index);
        currentUser.getGaps().remove(index);
        currentUser.setGaps(currentUser.getGaps());
        Users.getInstance().replaceUser(currentUser);
        showScheduleScreen(false);
    }

    @Override
    public void onClassesMatched(ArrayList<String> emails) {
        for (String email : potentialStudyBuddies) {
            for (String email2 : emails) {
                if (email.equals(email2)) {
                    emails.remove(email2);
                }
            }
        }
        for (String email : emails) {
            potentialStudyBuddies.add(email);
        }
    }

    @Override
    public void onGapsMatched(ArrayList<String> emails) {
        for (String email : potentialBreakBuddies) {
            for (String email2 : emails) {
                if (email.equals(email2)) {
                    emails.remove(email2);
                }
            }
        }
        for (String email : emails) {
            potentialBreakBuddies.add(email);
        }
    }

    @Override
    public void onNoClicked() {
        if (onBreak) {
            currentUser.addBreakPotentialIveSeen(potentialBreakBuddies.get(0));
            userYoureLookingAt.addBreakPotentialIveSeen(currentUser.getEmail());
            Users.getInstance().replaceUser(currentUser);
            Users.getInstance().replaceUser(userYoureLookingAt);
            firebaseHelper.saveToDatabase(currentUser);
            firebaseHelper.saveToDatabase(userYoureLookingAt);
            potentialBreakBuddies.remove(0);
            onBreakClicked();
        } else if (onStudy) {
            currentUser.addStudyPotentialIveSeen(potentialStudyBuddies.get(0));
            userYoureLookingAt.addStudyPotentialIveSeen(currentUser.getEmail());
            Users.getInstance().replaceUser(currentUser);
            Users.getInstance().replaceUser(userYoureLookingAt);
            firebaseHelper.saveToDatabase(currentUser);
            firebaseHelper.saveToDatabase(userYoureLookingAt);
            potentialStudyBuddies.remove(0);
            onStudyClicked();
        }
    }

    /**
     * Checks if potential buddy clicked the "yes" icon as well sets the potential user as a Buddy to the current user if so.
     */
    @Override
    public void onYesClicked() {
        if (onBreak) {
            // User clicked "yes" for a break buddy
            showNewBuddyScreen("break", userYoureLookingAt.getProfilePicURL());
            User breakBuddy = Users.getInstance().getUserWithEmail(potentialBreakBuddies.get(0));
            currentUser.addBreakPotentialIveSeen(potentialBreakBuddies.get(0));
            breakBuddy.addBreakPotentialIveSeen(currentUser.getEmail());
            currentUser.addBuddy(new Buddy(potentialBreakBuddies.get(0), "break", ""));
            breakBuddy.addBuddy(new Buddy(currentUser.getEmail(), "break", ""));
            Users.getInstance().replaceUser(currentUser);
            Users.getInstance().replaceUser(breakBuddy);
            firebaseHelper.saveToDatabase(currentUser);
            firebaseHelper.saveToDatabase(breakBuddy);
            potentialBreakBuddies.remove(0);
        } else if (onStudy) {
            // User clicks "yes" for a study buddy
            showNewBuddyScreen("study", userYoureLookingAt.getProfilePicURL());
            User studyBuddy = Users.getInstance().getUserWithEmail(potentialStudyBuddies.get(0));
            currentUser.addStudyPotentialIveSeen(potentialStudyBuddies.get(0));
            studyBuddy.addStudyPotentialIveSeen(currentUser.getEmail());
            currentUser.addBuddy(new Buddy(potentialStudyBuddies.get(0), "study", ""));
            studyBuddy.addBuddy(new Buddy(currentUser.getEmail(), "study", ""));
            Users.getInstance().replaceUser(currentUser);
            Users.getInstance().replaceUser(studyBuddy);
            firebaseHelper.saveToDatabase(currentUser);
            firebaseHelper.saveToDatabase(studyBuddy);
            potentialStudyBuddies.remove(0);
        }
    }

    /**
     * Sets up and displays potential study buddies for the current user to view
     */
    public void onStudyClicked() {
        setOnView(0);
        firebaseHelper.getPotentialStudyBuddies(currentUser.getEmail());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        setHighlightedView(studyImageView);
        if (potentialStudyBuddies.size() == 0) {
            NoPotentialBuddiesFragment noPotentialBuddiesFragment = NoPotentialBuddiesFragment.newInstance();
            transaction.replace(masterLayout.getId(), noPotentialBuddiesFragment);
            transaction.commit();
        } else {
            userYoureLookingAt = Users.getInstance().getUserWithEmail(potentialStudyBuddies.get(0));
            PotentialBuddyFragment potentialBuddyFragment = PotentialBuddyFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("CurrentUserID", currentUser.getEmail());
            bundle.putString("PotentialBuddyID", potentialStudyBuddies.get(0));
            bundle.putBoolean("OnStudy", true);
            potentialBuddyFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(masterLayout.getId(), potentialBuddyFragment, "PotentialBuddyFragment").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    /**
     * Sets up and displays potential break buddies for the current user to view
     */
    public void onBreakClicked() {
        setOnView(2);
        firebaseHelper.getPotentialBreakBuddies(currentUser.getEmail());
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        setHighlightedView(breakImageView);
        if (potentialBreakBuddies.size() == 0) {
            NoPotentialBuddiesFragment noPotentialBuddiesFragment = NoPotentialBuddiesFragment.newInstance();
            transaction.replace(masterLayout.getId(), noPotentialBuddiesFragment);
            transaction.commit();
        } else {
            userYoureLookingAt = Users.getInstance().getUserWithEmail(potentialBreakBuddies.get(0));
            PotentialBuddyFragment potentialBuddyFragment = PotentialBuddyFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("CurrentUserID", currentUser.getEmail());
            bundle.putString("PotentialBuddyID", potentialBreakBuddies.get(0));
            bundle.putBoolean("OnStudy", false);
            potentialBuddyFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(masterLayout.getId(), potentialBuddyFragment, "PotentialBuddyFragment").commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    public void showHomeScreen() {
        onHome = true;
        editButton.setImageResource(android.R.color.transparent);
        menuButton.setImageResource(R.drawable.menu);
        HomeFragment homeFragment = HomeFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("ProfilePic", currentUser.getProfilePicURL());
        homeFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(masterLayout.getId(), homeFragment, "HomeFragment").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void showProfileScreen() {
        inProfile = true;
        editButton.setImageResource(R.drawable.edit);
        ProfileFragment profileFragment = ProfileFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("ProfilePic", currentUser.getProfilePicURL());
        bundle.putString("Name", currentUser.getFirstName());
        bundle.putString("ClassYear", currentUser.getClassYear());
        bundle.putString("Biography", currentUser.getBiography());
        profileFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(masterLayout.getId(), profileFragment, "ProfileFragment").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void showEditProfileScreen() {
        onEdit = true;
        editButton.setImageResource(R.drawable.save);
        EditProfileFragment editProfileFragment = EditProfileFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("ProfilePic", currentUser.getProfilePicURL());
        bundle.putString("Name", currentUser.getFirstName());
        bundle.putString("ClassYear", currentUser.getClassYear());
        bundle.putString("Biography", currentUser.getBiography());
        editProfileFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(masterLayout.getId(), editProfileFragment, "EditProfileFragment").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void showScheduleScreen(boolean edit) {
        onEditScheduleScreen = true;
        editButton.setImageResource(R.drawable.edit);
        ScheduleFragment scheduleFragment = ScheduleFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("Email", currentUser.getEmail());
        bundle.putBoolean("EditMode", edit);
        scheduleFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(masterLayout.getId(), scheduleFragment, "ScheduleFragment").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void showSettingsScreen() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        SettingsFragment settingsFragment = SettingsFragment.newInstance();
        transaction.replace(masterLayout.getId(), settingsFragment);
        transaction.commit();
    }

    public void showMessagesScreen() {
        menuButton.setImageResource(R.drawable.menu);
        backArrowPresent = false;
        MessagesFragment messagesFragment = MessagesFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString("Email", currentUser.getEmail());
        messagesFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(masterLayout.getId(), messagesFragment, "MessagesFragment").commit();
        getSupportFragmentManager().executePendingTransactions();
    }

    public void showNewBuddyScreen(String typeOfBuddy, String profilePicURL) {
        disableViews();

        NewBuddyView newBuddyView = new NewBuddyView(this, screenWidth, typeOfBuddy, currentUser.getProfilePicURL(), profilePicURL);
        newBuddyView.setId(30);
        newBuddyView.setOnKeepSearchingClickedListener(this);
        newBuddyView.setOnSendMessageClickedListener(this);

        // Add newBuddyView to the main screen
        mainLayout.addView(newBuddyView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onKeepSearchingClicked() {
        reenableViews();
        mainLayout.removeView(findViewById(30));
        if (onStudy) {
            onStudyClicked();
        }
        if (onBreak) {
            onBreakClicked();
        }
    }

    @Override
    public void onSendMessageClicked() {
        reenableViews();
        mainLayout.removeView(findViewById(30));
        setBackArrowInUI();
        inConversation = true;
        TextView titleTextView = (TextView) topPaneLayout.getChildAt(1);
        titleTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        titleTextView.setTypeface(Typeface.SANS_SERIF);
        titleTextView.setTextColor(Color.WHITE);
        titleTextView.setText(userYoureLookingAt.getFirstName());
        ConversationFragment conversationFragment = ConversationFragment.newInstance(currentUser.getEmail(), userYoureLookingAt.getEmail());
        getSupportFragmentManager()
                .beginTransaction()
                .replace(masterLayout.getId(), conversationFragment, "ConversationFragment").commit();
        getSupportFragmentManager().executePendingTransactions();

    }

    public void disableViews() {
        for (int i = 0; i < menuLayout.getChildCount(); i++) {
            menuLayout.getChildAt(i).setEnabled(false);
        }
        for (int i = 0; i < dockLayout.getChildCount(); i++) {
            dockLayout.getChildAt(i).setEnabled(false);
        }
        for (int i = 0; i < topPaneLayout.getChildCount(); i++) {
            topPaneLayout.getChildAt(i).setEnabled(false);
        }
        PotentialBuddyFragment potentialBuddyFragment = (PotentialBuddyFragment) getSupportFragmentManager().findFragmentByTag("PotentialBuddyFragment");
        if (potentialBuddyFragment != null) {
            ViewGroup viewGroup = (ViewGroup) potentialBuddyFragment.getView();
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setEnabled(false);
            }
        }
    }

    public void reenableViews() {
        for (int i = 0; i < menuLayout.getChildCount(); i++) {
            menuLayout.getChildAt(i).setEnabled(true);
        }
        for (int i = 0; i < dockLayout.getChildCount(); i++) {
            dockLayout.getChildAt(i).setEnabled(true);
        }
        for (int i = 0; i < topPaneLayout.getChildCount(); i++) {
            topPaneLayout.getChildAt(i).setEnabled(true);
        }
        PotentialBuddyFragment potentialBuddyFragment = (PotentialBuddyFragment) getSupportFragmentManager().findFragmentByTag("PotentialBuddyFragment");
        if (potentialBuddyFragment != null) {
            ViewGroup viewGroup = (ViewGroup) potentialBuddyFragment.getView();
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                viewGroup.getChildAt(i).setEnabled(true);
            }
        }
    }
}
