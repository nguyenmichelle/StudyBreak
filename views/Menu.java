package com.example.michellenguy3n.studybreak.views;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * View for the navigation menu. Contains event listeners.
 * 
 * Created by michellenguy3n on 12/6/16.
 */
public class Menu extends ListView implements ListAdapter, AdapterView.OnItemClickListener {
    String[] menuItems = {"Home", "Your Profile", "Messages", "Settings", "About"};

    public interface OnMenuItemClickedListener {
        void onItemClicked(int item);
    }

    OnMenuItemClickedListener onMenuItemClickedListener = null;

    public OnMenuItemClickedListener getOnMenuItemClickedListener() {
        return onMenuItemClickedListener;
    }

    public void setOnMenuItemClickedListener(OnMenuItemClickedListener _onMenuItemClickedListener) {
        onMenuItemClickedListener = _onMenuItemClickedListener;
    }

    public Menu(Context context) {
        super(context);

        setOnItemClickListener(this);
        setAdapter(this);
        setBackgroundColor(Color.argb(220, 191, 191, 191));
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
        return menuItems.length;
    }

    @Override
    public Object getItem(int i) {
        return menuItems[i];
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
        TextView textView = new TextView(getContext());
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        float textSize = height * 0.015f;
        textView.setTextSize(textSize);
        textView.setText(menuItems[i]);
        return textView;
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


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (onMenuItemClickedListener != null) {
            onMenuItemClickedListener.onItemClicked(i);
        }
    }
}
