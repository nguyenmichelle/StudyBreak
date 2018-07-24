package com.example.michellenguy3n.studybreak.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
 * The main fragment users see when signing into the application.
 * 
 * Created by michellenguy3n on 12/5/16.
 */
public class HomeFragment extends Fragment {
    TextView locationLabel;
    TextView currentLocation;
    ImageView profilePic;
    String profilePicString;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {

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
        }

        locationLabel = new TextView(getContext());
        locationLabel.setText("Your current location:");
        locationLabel.setTextAppearance(android.R.style.TextAppearance_Medium);
        locationLabel.setPadding(20, 20, 20, 20);
        currentLocation = new TextView(getContext());
        currentLocation.setText("University of Utah");
        currentLocation.setTextAppearance(android.R.style.TextAppearance_Large);
        currentLocation.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        profilePic = new ImageView(getContext());
        profilePic.setBackgroundResource(R.drawable.border);
        if (profilePicString.equals("")) {
            profilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f));
        } else {
            setProfilePic(profilePicString);
        }
        profilePic.setBackgroundResource(R.drawable.border);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        relativeLayout.addView(profilePic, layoutParams);

        rootLayout.addView(locationLabel, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(currentLocation, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(relativeLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return rootLayout;
    }

    public TextView getLocationLabel() {
        return locationLabel;
    }

    public TextView getCurrentLocation() {
        return currentLocation;
    }

    public ImageView getProfilePic() {
        return profilePic;
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
