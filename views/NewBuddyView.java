package com.example.michellenguy3n.studybreak.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * View shown when users match with a new buddy.
 * 
 * Created by michellenguy3n on 12/14/16.
 */
public class NewBuddyView extends LinearLayout {
    public interface OnKeepSearchingClickedListener {
        void onKeepSearchingClicked();
    }

    public interface OnSendMessageClickedListener {
        void onSendMessageClicked();
    }

    OnKeepSearchingClickedListener onKeepSearchingClickedListener = null;
    OnSendMessageClickedListener onSendMessageClickedListener = null;

    public OnKeepSearchingClickedListener getOnKeepSearchingClickedListener() {
        return onKeepSearchingClickedListener;
    }

    public OnSendMessageClickedListener getOnSendMessageClickedListener() {
        return onSendMessageClickedListener;
    }

    public void setOnKeepSearchingClickedListener(OnKeepSearchingClickedListener onKeepSearchingClickedListener) {
        this.onKeepSearchingClickedListener = onKeepSearchingClickedListener;
    }

    public void setOnSendMessageClickedListener(OnSendMessageClickedListener onSendMessageClickedListener) {
        this.onSendMessageClickedListener = onSendMessageClickedListener;
    }

    public NewBuddyView(Context context, final int screenWidth, String typeOfBuddy, String currentUserPicURL, final String newBuddyPicURL) {
        super(context);

        // Create rootView
        setBackgroundColor(Color.argb(240, 64, 64, 64));

        // Create newBuddyLayout and params
        LinearLayout newBuddyLayout = new LinearLayout(context);
        newBuddyLayout.setBackgroundColor(Color.TRANSPARENT);
        newBuddyLayout.setOrientation(LinearLayout.VERTICAL);
        newBuddyLayout.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams buddyLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        buddyLayoutParams.setMargins(100, 150, 100, 150);

        // Create text views
        TextView congratulationsTextView = new TextView(context);
        congratulationsTextView.setTextSize(screenWidth * 0.025f);
        congratulationsTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        Typeface highlandPerk = Typeface.createFromAsset(context.getAssets(), "fonts/highlandperk.ttf");
        congratulationsTextView.setTypeface(highlandPerk, Typeface.BOLD);
        congratulationsTextView.setTextColor(Color.parseColor("#ffffff"));
        congratulationsTextView.setText("Congratulations!");

        TextView newBuddyTextView = new TextView(context);
        newBuddyTextView.setTextSize(screenWidth * 0.015f);
        newBuddyTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        newBuddyTextView.setTextColor(Color.parseColor("#ffffff"));
        newBuddyTextView.setText("You made a new " + typeOfBuddy + " buddy!");

        // Create layout for details
        LinearLayout detailLayout = new LinearLayout(context);

        // Create layouts for halfs of details
        LinearLayout leftHalfLayout = new LinearLayout(context);
        leftHalfLayout.setOrientation(VERTICAL);
        LinearLayout rightHalfLayout = new LinearLayout(context);
        rightHalfLayout.setOrientation(VERTICAL);

        // Create image views
        final ImageView currentUserProfilePic = new ImageView(context);
        if (currentUserPicURL.equals("")) {
            currentUserProfilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f, screenWidth));
        } else {
            if (!currentUserPicURL.startsWith("http")) {
                Uri imageUri = Uri.parse(currentUserPicURL);
                try {
                    InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    if (selectedImage != null) {
                        currentUserProfilePic.setImageBitmap(resize(selectedImage, 0.25f, screenWidth));
                    } else {
                        currentUserProfilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f, screenWidth));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    URL imageURL = new URL(currentUserPicURL);
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
                            currentUserProfilePic.setImageBitmap(resize(bitmap, 0.25f, screenWidth));
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

        final ImageView newBuddyProfilePic = new ImageView(context);
        if (newBuddyPicURL.equals("")) {
            newBuddyProfilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f, screenWidth));

        } else {
            if (!newBuddyPicURL.startsWith("http")) {
                Uri imageUri = Uri.parse(newBuddyPicURL);
                try {
                    InputStream imageStream = getContext().getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    if (selectedImage != null) {
                        newBuddyProfilePic.setImageBitmap(resize(selectedImage, 0.25f, screenWidth));
                    } else {
                        newBuddyProfilePic.setImageBitmap(resize(R.drawable.blankuserprofile, 0.25f, screenWidth));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    URL imageURL = new URL(newBuddyPicURL);
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
                            newBuddyProfilePic.setImageBitmap(resize(bitmap, 0.25f, screenWidth));
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

        // Create down arrow image views
        ImageView downArrow = new ImageView(context);
        downArrow.setImageResource(R.drawable.arrow);
        ImageView downArrow1 = new ImageView(context);
        downArrow1.setImageResource(R.drawable.arrow);
        ImageView sendMessageImageView = new ImageView(context);
        sendMessageImageView.setImageResource(R.drawable.paperplane);
        sendMessageImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendMessageClicked();
            }
        });
        ImageView handshakeImageView = new ImageView(context);
        handshakeImageView.setImageResource(R.drawable.handshake);
        handshakeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onKeepSearchingClicked();
            }
        });

        // Textviews
        TextView searchTextView = new TextView(context);
        searchTextView.setText("Keep searching");
        searchTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        searchTextView.setTextColor(Color.WHITE);
        searchTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onKeepSearchingClicked();
            }
        });//
        TextView messageTextView = new TextView(context);
        messageTextView.setText("Send a message");
        messageTextView.setTextAlignment(TEXT_ALIGNMENT_CENTER);
        messageTextView.setTextColor(Color.WHITE);
        messageTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendMessageClicked();
            }
        });

        // Create params for views in halves
        LinearLayout.LayoutParams downArrowParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3);
        downArrowParam.setMargins(0, 50, 0, 50);

        // Add views to the halves
        leftHalfLayout.addView(currentUserProfilePic, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3));
        leftHalfLayout.addView(downArrow1, downArrowParam);
        leftHalfLayout.addView(handshakeImageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));
        leftHalfLayout.addView(searchTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        rightHalfLayout.addView(newBuddyProfilePic, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 3));
        rightHalfLayout.addView(downArrow, downArrowParam);
        rightHalfLayout.addView(sendMessageImageView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 2));
        rightHalfLayout.addView(messageTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        // Half layout params
        LinearLayout.LayoutParams leftHalfLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        leftHalfLayoutParams.setMargins(0, 0, 25, 0);
        LinearLayout.LayoutParams rightHalfLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        rightHalfLayoutParams.setMargins(25, 0, 0, 0);

        // Add halves to detailLayout
        detailLayout.addView(leftHalfLayout, leftHalfLayoutParams);
        detailLayout.addView(rightHalfLayout, rightHalfLayoutParams);

        // Create param for detail layout
        LinearLayout.LayoutParams detailsLayoutParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        detailsLayoutParam.setMargins(0, 50, 0, 0);

        // Add views to newBuddyLayout
        newBuddyLayout.addView(congratulationsTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newBuddyLayout.addView(newBuddyTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        newBuddyLayout.addView(detailLayout, detailsLayoutParam);

        // Add newBuddy to the rootViewLayout
        addView(newBuddyLayout, buddyLayoutParams);
    }

    private Bitmap resize(int image, float percent, float screenWidth) {
        float picSize = screenWidth * percent;
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), image);
        bmp = Bitmap.createScaledBitmap(bmp, (int) picSize, (int) picSize, true);

        return bmp;
    }

    private Bitmap resize(Bitmap image, float percent, float screenWidth) {
        float picSize = screenWidth * percent;
        image = Bitmap.createScaledBitmap(image, (int) picSize, (int) picSize, true);

        return image;
    }

    public void onKeepSearchingClicked() {
        if (onKeepSearchingClickedListener != null) {
            onKeepSearchingClickedListener.onKeepSearchingClicked();
        }
    }

    public void onSendMessageClicked() {
        if (onSendMessageClickedListener != null) {
            onSendMessageClickedListener.onSendMessageClicked();
        }
    }
}
