package com.example.michellenguy3n.studybreak.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.model.Course;
import com.example.michellenguy3n.studybreak.model.Gap;
import com.example.michellenguy3n.studybreak.model.User;
import com.example.michellenguy3n.studybreak.model.Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Potential buddies are shown to users in this fragment. Users are able to see their name, age, biography, and matched class/gap.
 * 
 * Created by michellenguy3n on 12/7/16.
 */
public class PotentialBuddyFragment extends Fragment {
    public static PotentialBuddyFragment newInstance() {
        return new PotentialBuddyFragment();
    }

    public interface OnYesButtonClickedListener {
        void onYesClicked();
    }

    public interface OnNoButtonClickedListener {
        void onNoClicked();
    }

    private OnYesButtonClickedListener _onYesButtonClickedListener = null;
    private OnNoButtonClickedListener _onNoButtonClickedListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onYesButtonClickedListener = (OnYesButtonClickedListener) context;
            _onNoButtonClickedListener = (OnNoButtonClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement OnYesButtonClickedListener and OnNoButtonClickedListener!");
        }
    }

    @Override
    public void onDetach() {
        _onYesButtonClickedListener = null;
        _onNoButtonClickedListener = null;
        super.onDetach();
    }

    ImageView profilePic;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        // Get bundles
        Bundle bundle = this.getArguments();
        String currentUserId = "";
        String potentialUserId = "";
        boolean onStudy = true;
        if (bundle != null) {
            currentUserId = bundle.getString("CurrentUserID");
            potentialUserId = bundle.getString("PotentialBuddyID");
            onStudy = bundle.getBoolean("OnStudy");
        }
        User currentUser = Users.getInstance().getUserWithEmail(currentUserId);
        User potentialBuddy = Users.getInstance().getUserWithEmail(potentialUserId);

        // Deal with buttons
        RelativeLayout buttonLayout = new RelativeLayout(getContext());
        //buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        ImageButton noButton = new ImageButton(getContext());
        noButton.setBackgroundColor(Color.TRANSPARENT);
        noButton.setImageResource(R.drawable.delete);
        noButton.setScaleType(ImageView.ScaleType.CENTER);
        noButton.setAdjustViewBounds(true);
        RelativeLayout.LayoutParams noButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        noButtonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //noButtonParams.setMargins(50, 0, 475, 0);
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onNoButtonClickedListener.onNoClicked();
            }
        });
        ImageButton yesButton = new ImageButton(getContext());
        yesButton.setBackgroundColor(Color.TRANSPARENT);
        yesButton.setImageResource(R.drawable.checked);
        yesButton.setScaleType(ImageView.ScaleType.CENTER);
        yesButton.setAdjustViewBounds(true);
        RelativeLayout.LayoutParams yesButtonParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        yesButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onYesButtonClickedListener.onYesClicked();
            }
        });
        buttonLayout.addView(noButton, noButtonParams);
        buttonLayout.addView(yesButton, yesButtonParams);
        LinearLayout.LayoutParams buttonsLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2);
        buttonsLayoutParams.setMargins(50, 0, 50, 0);

        // Deal with user profile pic
        profilePic = new ImageView(getContext());
        LinearLayout.LayoutParams profilePicParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 4);
        profilePicParams.setMargins(200, 0, 200, 0);
        if (potentialBuddy.getProfilePicURL().equals("")) {
            profilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f));
        } else {
            setProfilePic(potentialBuddy.getProfilePicURL());
        }

        // Deal with details
        LinearLayout detailLayout = new LinearLayout(getContext());
        detailLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams detailLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 7);
        detailLayoutParams.setMargins(100, 50, 100, 0);
        TextView nameAndYearTextView = new TextView(getContext());
        nameAndYearTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        nameAndYearTextView.setText(potentialBuddy.getFirstName() + ", " + potentialBuddy.getClassYear());
        TextView biographyTextView = new TextView(getContext());
        biographyTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        biographyTextView.setText(potentialBuddy.getBiography());
        TextView blankTextView = new TextView(getContext());
        blankTextView.setText("");
        TextView inCommonLabel = new TextView(getContext());
        inCommonLabel.setTextAppearance(android.R.style.TextAppearance_Medium);
        inCommonLabel.setTypeface(null, Typeface.BOLD);
        if (onStudy) {
            inCommonLabel.setText("Classes in common:");
        } else {
            inCommonLabel.setText("Gaps in common:");
        }

        detailLayout.addView(nameAndYearTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        detailLayout.addView(biographyTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        detailLayout.addView(blankTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        detailLayout.addView(inCommonLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        if (onStudy) {
            for (Course course : currentUser.getClassesInCommon(potentialBuddy)) {
                TextView classTextView = new TextView(getContext());
                classTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
                classTextView.setText(course.getSubject() + " " + course.getCourseNumber() + ", " + course.getClassName());
                detailLayout.addView(classTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        } else {
            for (Gap gap : currentUser.getGapsInCommon(potentialBuddy)) {
                TextView gapTextView = new TextView(getContext());
                gapTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
                gapTextView.setText(gap.getProperlyFormattedGap());
                detailLayout.addView(gapTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            }
        }

        rootLayout.addView(buttonLayout, buttonsLayoutParams);
        rootLayout.addView(profilePic, profilePicParams);
        rootLayout.addView(detailLayout, detailLayoutParams);

        return rootLayout;
    }

    public void setProfilePic(String imageSource) {
        if (!imageSource.startsWith("http")) {
            Uri imageUri = Uri.parse(imageSource);
            try {
                getView();
                InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                if (selectedImage != null) {
                    profilePic.setImageBitmap(resize(selectedImage, 0.25f));
                } else {
                    profilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f));
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                URL imageURL = new URL(imageSource);

                // Create an asynchronous task to change the profile pic
                AsyncTask<URL, Double, Bitmap> changeProfilePicTask = new AsyncTask<URL, Double, Bitmap>() {
                    @Override
                    protected Bitmap doInBackground(URL... urls) {
                        URL url = urls[0];
                        Bitmap bitmap = null;

                        try {
                            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return bitmap;
                    }

                    @Override
                    protected void onPostExecute(Bitmap bitmap) {
                        super.onPostExecute(bitmap);
                        profilePic.setImageBitmap(resize(bitmap, 0.25f));
                    }
                };

                // Execute the task
                changeProfilePicTask.execute(imageURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Bitmap resize(int image, float percent) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        float picSize = height * percent;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), image);
        bmp = Bitmap.createScaledBitmap(bmp, (int) picSize, (int) picSize, true);

        return bmp;
    }

    private Bitmap resize(Bitmap image, float percent) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        float picSize = height * percent;
        image = Bitmap.createScaledBitmap(image, (int) picSize, (int) picSize, true);

        return image;
    }
}
