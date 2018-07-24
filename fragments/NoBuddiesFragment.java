package com.example.michellenguy3n.studybreak.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;

/**
 * Users who have no buddies see this when attempting to view their messages/matches.
 * 
 * Created by michellenguy3n on 12/10/16.
 */
public class NoBuddiesFragment extends Fragment {
    public static NoBuddiesFragment newInstance() {
        return new NoBuddiesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.WHITE);

        // TextView
        TextView noBuddiesTextView = new TextView(getContext());
        noBuddiesTextView.setText("You currently do not have any buddies.");
        noBuddiesTextView.setTextColor(Color.BLACK);
        noBuddiesTextView.setTextAppearance(android.R.style.TextAppearance_Large);
        noBuddiesTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // ImageView
        ImageView imageView = new ImageView(getContext());
        imageView.setImageBitmap(resize(R.drawable.sad, 0.25f));
        imageView.setBackgroundColor(Color.TRANSPARENT);

        // Layout params
        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams imageViewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(150, 300, 150, 100);
        imageViewLayoutParams.setMargins(200, 0, 200, 0);

        // Add views
        rootLayout.addView(noBuddiesTextView, textViewLayoutParams);
        rootLayout.addView(imageView, imageViewLayoutParams);

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
}
