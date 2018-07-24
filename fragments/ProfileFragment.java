package com.example.michellenguy3n.studybreak.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This fragment contains the view of the current user's profile details.
 * 
 * Created by michellenguy3n on 12/7/16.
 */
public class ProfileFragment extends Fragment {
    ImageView profilePic;
    TextView nameTextView;
    TextView yearTextView;
    TextView biographyTextView;
    String name;
    String classYear;
    String biography;
    String profilePicString;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        // Get bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            profilePicString = bundle.getString("ProfilePic");
            name = bundle.getString("Name");
            classYear = bundle.getString("ClassYear");
            biography = bundle.getString("Biography");
        }

        // Title
        TextView titleTextView = new TextView(getContext());
        titleTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        titleTextView.setText("Your Profile");
        titleTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // Pic
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        profilePic = new ImageView(getContext());
        if (profilePicString.equals("")) {
            profilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f));
        } else {
            setProfilePic(profilePicString);
        }
        profilePic.setBackgroundResource(R.drawable.border);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        relativeLayout.addView(profilePic, layoutParams);

        // Details
        LinearLayout detailsLayout = new LinearLayout(getContext());
        detailsLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout nameAndYearLayout = new LinearLayout(getContext());
        nameAndYearLayout.setOrientation(LinearLayout.HORIZONTAL);
        nameTextView = new TextView(getContext());
        nameTextView.setText(name + ", ");
        nameTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        yearTextView = new TextView(getContext());
        yearTextView.setText(classYear);
        yearTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        nameAndYearLayout.addView(nameTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nameAndYearLayout.addView(yearTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        biographyTextView = new TextView(getContext());
        if (biography.equals("")) {
            biographyTextView.setText("Biography");
        } else {
            biographyTextView.setText(biography);
        }
        biographyTextView.setTextAppearance(android.R.style.TextAppearance_Medium);

        detailsLayout.addView(nameAndYearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        detailsLayout.addView(biographyTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams detailLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 5);
        detailLayoutParams.setMargins(100, 0, 100, 0);

        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(relativeLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 6));
        rootLayout.addView(detailsLayout, detailLayoutParams);

        return rootLayout;
    }

    public void setProfilePic(String imageSource) {
        if (!imageSource.startsWith("http")) {
            Uri imageUri = Uri.parse(imageSource);
            try {
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

    public void setNameTextView(String name) {
        nameTextView.setText(name);
    }

    public void setYearTextView(String name) {
        yearTextView.setText(name);
    }

    public void setBiographyTextView(String biography) {
        biographyTextView.setText(biography);
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
