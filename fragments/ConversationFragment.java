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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michellenguy3n.studybreak.R;
import com.example.michellenguy3n.studybreak.database.FirebaseHelper;
import com.example.michellenguy3n.studybreak.model.Buddy;
import com.example.michellenguy3n.studybreak.model.Message;
import com.example.michellenguy3n.studybreak.model.User;
import com.example.michellenguy3n.studybreak.model.Users;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Allows users to view a specified conversation in their inbox.
 * 
 * Created by michellenguy3n on 12/14/16.
 */
public class ConversationFragment extends Fragment implements ListAdapter, FirebaseHelper.OnMessageSentListener {
    String currentUserEmail = "";
    String buddyEmail = "";
    Bitmap buddyProfilePic;
    ListView convoListView;
    final FirebaseHelper firebaseHelper = new FirebaseHelper(FirebaseDatabase.getInstance().getReference());
    ArrayList<Message> messages = new ArrayList<Message>();
    private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>();

    public static ConversationFragment newInstance(String currentUserEmail, String buddyEmail) {
        ConversationFragment conversationFragment = new ConversationFragment();

        Bundle args = new Bundle();
        args.putString("CurrentEmail", currentUserEmail);
        args.putString("BuddyEmail", buddyEmail);
        conversationFragment.setArguments(args);

        return conversationFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentUserEmail = getArguments().getString("CurrentEmail");
        buddyEmail = getArguments().getString("BuddyEmail");
        firebaseHelper.getMessagesFromDatabase(getModifiedEmail(currentUserEmail), getModifiedEmail(buddyEmail));
        firebaseHelper.setOnMessageSentListener(this);

        View conversationView = inflater.inflate(R.layout.fragment_conversation, container, false);
        convoListView = (ListView) conversationView.findViewById(R.id.convo_list_view);
        convoListView.setBackgroundColor(Color.WHITE);
        convoListView.setAdapter(this);
        Button sendButton = (Button) conversationView.findViewById(R.id.send_button);
        final EditText messageEditText = (EditText) conversationView.findViewById(R.id.message_box_edit_text);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageToSend = messageEditText.getText().toString();
                Message message = new Message(getModifiedEmail(currentUserEmail), getModifiedEmail(buddyEmail), messageToSend);
                firebaseHelper.saveMessageToDatabase(message);
                messageEditText.getText().clear();
            }
        });

        return conversationView;
    }

    public String getModifiedEmail(String email) {
        return email.substring(0, email.length() - 4);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        observers.add(dataSetObserver);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int i) {
        return messages.get(i);
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
        RelativeLayout rootLayout = new RelativeLayout(getContext());
        rootLayout.setPadding(10, 10, 10, 20);

        Message message = (Message) getItem(i);
        Display display = ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int maxWidth = width / 2;

        String buddyProfilePicURL = Users.getInstance().getUserWithEmail(buddyEmail).getProfilePicURL();

        TextView messageTextView = new TextView(getContext());
        messageTextView.setText(message.getMessage());
        messageTextView.setId(60);
        messageTextView.setMaxWidth(maxWidth);
        messageTextView.setTextSize(16.0f);
        if (message.getFromId().equals(getModifiedEmail(currentUserEmail))) {
            messageTextView.setTextColor(Color.WHITE);
            messageTextView.setBackgroundResource(R.drawable.outgoingpatch);
            RelativeLayout.LayoutParams outgoingLayoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            outgoingLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            rootLayout.addView(messageTextView, outgoingLayoutParams);
        } else {
            messageTextView.setBackgroundResource(R.drawable.incomingpatch);
            final ImageView buddyPic = new ImageView(getContext());
            buddyPic.setId(61);
            buddyPic.setScaleType(ImageView.ScaleType.FIT_XY);
            buddyPic.setAdjustViewBounds(true);
            AsyncTask<String, Double, Void> getProfilePicTask = new AsyncTask<String, Double, Void>() {
                @Override
                protected Void doInBackground(String... strings) {
                    String string = strings[0];
                    getProfilePicBitmap(string);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    buddyPic.setImageBitmap(buddyProfilePic);
                }
            };
            getProfilePicTask.execute(buddyProfilePicURL);

            RelativeLayout.LayoutParams textViewParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textViewParams.addRule(RelativeLayout.RIGHT_OF, buddyPic.getId());
            RelativeLayout.LayoutParams buddyPicParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            buddyPicParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            rootLayout.addView(buddyPic, buddyPicParams);
            rootLayout.addView(messageTextView, textViewParams);
        }

        return rootLayout;
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
        return messages.isEmpty();
    }

    public void notifyDataSetChanged() {
        for (DataSetObserver observer : observers) {
            observer.onChanged();
        }
    }

    @Override
    public void onMessageSent(Message message) {
        messages.add(message);
        notifyDataSetChanged();
        Buddy buddy = Users.getInstance().getUserWithEmail(currentUserEmail).getBuddyWithEmail(buddyEmail);
        Buddy buddy1 = Users.getInstance().getUserWithEmail(buddyEmail).getBuddyWithEmail(currentUserEmail);
        buddy.setRecentMessage(message.getMessage());
        buddy1.setRecentMessage(message.getMessage());
        User currentUser = Users.getInstance().getUserWithEmail(currentUserEmail);
        User currentBuddy = Users.getInstance().getUserWithEmail(buddyEmail);
        firebaseHelper.saveToDatabase(currentUser);
        firebaseHelper.saveToDatabase(currentBuddy);
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
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(borderWidth);
        canvas.drawCircle(width / 2, height / 2, radius - borderWidth / 2, paint);
        return canvasBitmap;
    }

    public void getProfilePicBitmap(String imageSource) {
        if (imageSource.equals("")) {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                    R.drawable.blankuserprofile);
            buddyProfilePic = (getCircularBitmapWithWhiteBorder(resize(bitmap, 0.05f), 2));
        } else {
            if (imageSource.startsWith("http")) {
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
                            buddyProfilePic = (getCircularBitmapWithWhiteBorder(resize(bitmap, 0.05f), 2));
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
                Uri imageUri = Uri.parse(imageSource);
                try {
                    getView();
                    InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    if (selectedImage != null) {
                        buddyProfilePic = selectedImage;
                    } else {
                        Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),
                                R.drawable.blankuserprofile);
                        buddyProfilePic = (getCircularBitmapWithWhiteBorder(resize(bitmap, 0.05f), 2));
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
