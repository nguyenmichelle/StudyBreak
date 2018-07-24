package com.example.michellenguy3n.studybreak.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Allows users to edit their current profile details.
 * 
 * Created by michellenguy3n on 12/7/16.
 */
public class EditProfileFragment extends Fragment {
    ImageView profilePic;
    EditText nameEditText;
    Spinner classYearSpinner;
    EditText biographyEditText;
    String name;
    String classYear;
    String biography;
    String profilePicString;

    public static EditProfileFragment newInstance() {
        return new EditProfileFragment();
    }

    public EditProfileFragment() {

    }

    public interface OnEditScheduleClickedListener {
        void onEditScheduleClicked();
    }

    public interface OnCameraButtonClickedListener {
        void onCameraButtonClicked();
    }

    public interface OnSaveClickedListener {
        void onSaveClicked(String name, String classYear, String biography);
    }

    private OnEditScheduleClickedListener _onEditScheduleClickedListener = null;
    private OnCameraButtonClickedListener _onCameraButtonClickedListener = null;
    private OnSaveClickedListener _onSaveClickedListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onEditScheduleClickedListener = (EditProfileFragment.OnEditScheduleClickedListener) context;
            _onCameraButtonClickedListener = (OnCameraButtonClickedListener) context;
            _onSaveClickedListener = (OnSaveClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement OnEditScheduleClickedListener and OnCameraButtonClickedListener and OnSaveClickedListener!");
        }
    }

    @Override
    public void onDetach() {
        _onEditScheduleClickedListener = null;
        _onCameraButtonClickedListener = null;
        _onSaveClickedListener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ScrollView scrollView = new ScrollView(getContext());
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

        // Setup data for spinners
        ArrayList<String> yearArray = new ArrayList<String>();
        yearArray.add("Freshman");
        yearArray.add("Sophomore");
        yearArray.add("Junior");
        yearArray.add("Senior");
        ArrayList<String> schoolArray = new ArrayList<String>();
        schoolArray.add("University of Utah");

        // Title
        TextView titleTextView = new TextView(getContext());
        titleTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        titleTextView.setText("Edit Profile");
        titleTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        titleTextView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));

        // Pic
        RelativeLayout relativeLayout = new RelativeLayout(getContext());
        profilePic = new ImageView(getContext());
        profilePic.setId(30);
        if (profilePicString.equals("")) {
            profilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f));
        } else {
            setProfilePic(profilePicString);
        }
        profilePic.setBackgroundResource(R.drawable.border);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        relativeLayout.addView(profilePic, layoutParams);

        // Camera button
        ImageButton cameraButton = new ImageButton(getContext());
        cameraButton.setImageBitmap(resize(R.drawable.camera, 0.05f));
        cameraButton.setBackgroundColor(Color.WHITE);
        cameraButton.bringToFront();
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onCameraButtonClickedListener.onCameraButtonClicked();
            }
        });
        RelativeLayout.LayoutParams camLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        camLayoutParams.addRule(RelativeLayout.ALIGN_RIGHT, profilePic.getId());
        camLayoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, profilePic.getId());
        relativeLayout.addView(cameraButton, camLayoutParams);

        // Details
        LinearLayout detailsLayout = new LinearLayout(getContext());
        detailsLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout nameAndYearLayout = new LinearLayout(getContext());
        nameAndYearLayout.setOrientation(LinearLayout.HORIZONTAL);
        nameEditText = new EditText(getContext());
        nameEditText.setText(name);
        nameEditText.setBackgroundResource(R.drawable.border);
        nameEditText.setSelectAllOnFocus(true);
        nameEditText.setTextAppearance(android.R.style.TextAppearance_Large);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                name = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        classYearSpinner = new Spinner(getContext());
        classYearSpinner.setBackgroundResource(R.drawable.border);
        ArrayAdapter<String> classYearSpinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, yearArray);
        classYearSpinner.setAdapter(classYearSpinnerArrayAdapter);
        classYearSpinner.setSelection(returnSelection(classYear));
        classYearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                classYear = returnYear(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        nameAndYearLayout.addView(nameEditText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        nameAndYearLayout.addView(classYearSpinner, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        biographyEditText = new EditText(getContext());
        if (biography.equals("")) {
            biographyEditText.setText("Biography");
        } else {
            biographyEditText.setText(biography);
        }
        biographyEditText.setBackgroundResource(R.drawable.border);
        biographyEditText.setSelectAllOnFocus(true);
        biographyEditText.setTextAppearance(android.R.style.TextAppearance_Small);
        biographyEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                biography = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Spinner schoolSpinner = new Spinner(getContext());
        ArrayAdapter<String> schoolSpinnerArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, schoolArray);
        schoolSpinner.setAdapter(schoolSpinnerArrayAdapter);
        schoolSpinner.setBackgroundResource(R.drawable.border);

        TextView editScheduleTextView = new TextView(getContext());
        editScheduleTextView.setText("Edit Schedule");
        editScheduleTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        LinearLayout.LayoutParams editScheduleLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editScheduleLayoutParams.setMargins(0, 40, 0, 0);
        editScheduleTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        editScheduleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _onEditScheduleClickedListener.onEditScheduleClicked();
            }
        });

        detailsLayout.addView(nameAndYearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        detailsLayout.addView(biographyEditText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        detailsLayout.addView(schoolSpinner, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        detailsLayout.addView(editScheduleTextView, editScheduleLayoutParams);
        LinearLayout.LayoutParams detailLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 5);
        detailLayoutParams.setMargins(100, 0, 100, 0);

        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(relativeLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 6));
        rootLayout.addView(detailsLayout, detailLayoutParams);

        return rootLayout;
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

    public void save() {
        _onSaveClickedListener.onSaveClicked(name, classYear, biography);
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

    private int returnSelection(String classYear) {
        int selection = 0;

        switch (classYear) {
            case "Freshman":
                selection = 0;
                break;
            case "Sophomore":
                selection = 1;
                break;
            case "Junior":
                selection = 2;
                break;
            case "Senior":
                selection = 3;
                break;
        }

        return selection;
    }

    private String returnYear(int classYear) {
        String year = "";

        switch (classYear) {
            case 0:
                year = "Freshman";
                break;
            case 1:
                year = "Sophomore";
                break;
            case 2:
                year = "Junior";
                break;
            case 3:
                year = "Senior";
                break;
        }

        return year;
    }
}
