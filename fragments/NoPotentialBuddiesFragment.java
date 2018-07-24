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
 * If there are no potential buddies for the user to match with (no one with similar classes/gaps that haven't been seen before), users will see this fragment.
 *
 * Created by michellenguy3n on 12/11/16.
 */
public class NoPotentialBuddiesFragment extends Fragment {
    public static NoPotentialBuddiesFragment newInstance() {
        return new NoPotentialBuddiesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout rootLayout = new LinearLayout(getContext());
        rootLayout.setOrientation(LinearLayout.VERTICAL);
        rootLayout.setBackgroundColor(Color.WHITE);

        // TextView
        TextView noBuddiesTextView = new TextView(getContext());
        noBuddiesTextView.setText("There are no potential buddies in the area. \n\nPlease check back soon!");
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
        imageViewLayoutParams.setMargins(200, 250, 200, 100);
        textViewLayoutParams.setMargins(150, 0, 150, 0);

        // Add views
        rootLayout.addView(imageView, imageViewLayoutParams);
        rootLayout.addView(noBuddiesTextView, textViewLayoutParams);

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
