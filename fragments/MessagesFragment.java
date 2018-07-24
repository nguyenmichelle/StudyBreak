package com.example.michellenguy3n.studybreak.fragments;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.model.Buddy;
import com.example.michellenguy3n.studybreak.model.Users;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Allows users to view all of their messages and buddies.
 * 
 * Created by michellenguy3n on 12/7/16.
 */
public class MessagesFragment extends Fragment implements ListAdapter, AdapterView.OnItemClickListener {
    public String email;

    public static MessagesFragment newInstance() {
        return new MessagesFragment();
    }

    public interface OnBuddyClickedListener {
        void onBuddyClicked(int i);
    }

    private OnBuddyClickedListener _onBuddyClickedListener = null;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            _onBuddyClickedListener = (OnBuddyClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException("Must implement OnBuddyClickedListener!");
        }
    }

    @Override
    public void onDetach() {
        _onBuddyClickedListener = null;
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);

        // Get bundles
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            email = bundle.getString("Email");
        }

        // Title
        TextView titleTextView = new TextView(getContext());
        titleTextView.setPadding(25, 25, 0, 25);
        titleTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        titleTextView.setText("Messages");
        titleTextView.setBackgroundResource(R.drawable.border);

        ListView buddyList = new ListView(getContext());
        buddyList.setBackgroundColor(Color.WHITE);
        buddyList.setAdapter(this);
        buddyList.setOnItemClickListener(this);

        rootLayout.addView(titleTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayout.addView(buddyList, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return rootLayout;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return Users.getInstance().getUserWithEmail(email).getBuddies().size();
    }

    @Override
    public Object getItem(int i) {
        return Users.getInstance().getUserWithEmail(email).getBuddies().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LinearLayout buddyLayout = new LinearLayout(getContext());
        buddyLayout.setOrientation(LinearLayout.HORIZONTAL);

        Buddy buddy = (Buddy) getItem(i);
        final ImageView buddyProfilePic = new ImageView(getContext());
        String buddyProfilePicURL = Users.getInstance().getUserWithEmail(buddy.getEmail()).getProfilePicURL();
        if (buddyProfilePicURL.equals("")) {
            buddyProfilePic.setImageBitmap(getCircularBitmapWithWhiteBorder(resize(R.drawable.blankuserprofile, 0.1f), 3));
        } else {
            if (buddyProfilePicURL.startsWith("http")) {
                try {
                    URL imageURL = new URL(buddyProfilePicURL);
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
                            buddyProfilePic.setImageBitmap(getCircularBitmapWithWhiteBorder(resize(bitmap, 0.1f), 3));
                        }
                    };

                    // Execute the task
                    changeProfilePicTask.execute(imageURL);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Uri imageUri = Uri.parse(buddyProfilePicURL);
                try {
                    getView();
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    if (selectedImage != null) {
                        buddyProfilePic.setImageBitmap(getCircularBitmapWithWhiteBorder(resize(selectedImage, 0.1f), 3));
                    } else {
                        buddyProfilePic.setImageBitmap(getCircularBitmapWithWhiteBorder(resize(R.drawable.blankuserprofile, 0.1f), 3));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        LinearLayout nameAndMessageLayout = new LinearLayout(getContext());
        nameAndMessageLayout.setOrientation(LinearLayout.VERTICAL);
        TextView nameTextView = new TextView(getContext());
        TextView messageTextView = new TextView(getContext());
        nameTextView.setTypeface(null, Typeface.BOLD);
        nameTextView.setText(Users.getInstance().getUserWithEmail(buddy.getEmail()).getFirstName());
        nameTextView.setTextAppearance(android.R.style.TextAppearance_Medium);
        if (buddy.getRecentMessage().equals("")) {
            messageTextView.setText("Select buddy to start a conversation");
        } else {
            messageTextView.setText(buddy.getRecentMessage());
        }
        messageTextView.setTypeface(null, Typeface.ITALIC);
        messageTextView.setMaxLines(1);
        messageTextView.setEllipsize(TextUtils.TruncateAt.END);
        nameAndMessageLayout.addView(nameTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        nameAndMessageLayout.addView(messageTextView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        LinearLayout buddyIndicatorLayout = new LinearLayout(getContext());
        buddyIndicatorLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.indicatorBlue));
        buddyIndicatorLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView studyBuddyIndicator = new ImageView(getContext());
        studyBuddyIndicator.setScaleType(ImageView.ScaleType.CENTER);
        studyBuddyIndicator.setAdjustViewBounds(true);
        ImageView breakBuddyIndicator = new ImageView(getContext());
        breakBuddyIndicator.setScaleType(ImageView.ScaleType.CENTER);
        breakBuddyIndicator.setAdjustViewBounds(true);
        if (buddy.getType().equals("study")) {
            studyBuddyIndicator.setImageResource(R.drawable.circleds);
            breakBuddyIndicator.setImageResource(R.drawable.breakindicator);
        } else if (buddy.getType().equals("break")) {
            studyBuddyIndicator.setImageResource(R.drawable.studyindicator);
            breakBuddyIndicator.setImageResource(R.drawable.circledb);

        } else if (buddy.getType().equals("both")) {
            studyBuddyIndicator.setImageResource(R.drawable.circleds);
            breakBuddyIndicator.setImageResource(R.drawable.circledb);
        }
        buddyIndicatorLayout.addView(studyBuddyIndicator, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));
        buddyIndicatorLayout.addView(breakBuddyIndicator, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        // Create params
        LinearLayout.LayoutParams buddyProfPicParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 4);
        buddyProfPicParams.setMargins(50, 50, 50, 50);
        LinearLayout.LayoutParams nameAndMessageLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 10);
        nameAndMessageLayoutParams.setMargins(20, 85, 50, 50);

        buddyLayout.addView(buddyProfilePic, buddyProfPicParams);
        buddyLayout.addView(nameAndMessageLayout, nameAndMessageLayoutParams);
        buddyLayout.addView(buddyIndicatorLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));
        return buddyLayout;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        _onBuddyClickedListener.onBuddyClicked(i);
    }

    public static Bitmap getCircularBitmapWithWhiteBorder(Bitmap bitmap,
                                                          int borderWidth) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }

        final int width = bitmap.getWidth() + borderWidth;
        final int height = bitmap.getHeight() + borderWidth;

        Bitmap canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Canvas canvas = new Canvas(canvasBitmap);
        float radius = width > height ? ((float) height) / 2f : ((float) width) / 2f;
        canvas.drawCircle(width / 2, height / 2, radius, paint);
        paint.setShader(null);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }
}
