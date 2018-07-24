package com.example.michellenguy3n.studybreak.database;

import com.example.michellenguy3n.studybreak.model.Buddy;
import com.example.michellenguy3n.studybreak.model.Course;
import com.example.michellenguy3n.studybreak.model.Gap;
import com.example.michellenguy3n.studybreak.model.Message;
import com.example.michellenguy3n.studybreak.model.User;
import com.example.michellenguy3n.studybreak.model.Users;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * This class provides helper methods to interact with the Firebase database.
 *
 * Created by michellenguy3n on 12/6/16.
 */
public class FirebaseHelper {
    DatabaseReference databaseReference;
    boolean saved;

    public interface OnClassesMatchedListener {
        void onClassesMatched(ArrayList<String> emails);
    }

    public interface OnGapsMatchedListener {
        void onGapsMatched(ArrayList<String> emails);
    }

    public interface OnMessageSentListener {
        void onMessageSent(Message message);
    }

    private OnClassesMatchedListener _onClassesMatchedListener = null;
    private OnGapsMatchedListener _onGapsMatchedListener = null;
    private OnMessageSentListener _onMessageSentListener = null;

    public OnClassesMatchedListener getOnClassesMatchedListener() {
        return _onClassesMatchedListener;
    }

    public OnGapsMatchedListener getOnGapsMatchedListener() {
        return _onGapsMatchedListener;
    }

    public OnMessageSentListener getOnMessageSentListener() {
        return _onMessageSentListener;
    }

    public void setOnClassesMatchedListener(OnClassesMatchedListener onClassesMatchedListener) {
        _onClassesMatchedListener = onClassesMatchedListener;
    }

    public void setOnGapsMatchedListener(OnGapsMatchedListener onGapsMatchedListener) {
        _onGapsMatchedListener = onGapsMatchedListener;
    }

    public void setOnMessageSentListener(OnMessageSentListener onMessageSentListener) {
        _onMessageSentListener = onMessageSentListener;
    }

    public FirebaseHelper(DatabaseReference databaseReference) {
        this.databaseReference = databaseReference;
    }

    public boolean saveToDatabase(User user) {
        if (user == null) {
            saved = false;
        } else {
            try {
                databaseReference.child("users").child(user.getModifiedEmail()).setValue(user);
                saved = true;
            } catch (DatabaseException e) {
                e.printStackTrace();
                saved = false;
            }
        }
        return saved;
    }

    public void getAllUsersFromDatabase() {
        Users.getInstance().clearUsers();
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String firstName = dataSnapshot.child("firstName").getValue().toString();
                String lastName = dataSnapshot.child("lastName").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String classYear = dataSnapshot.child("classYear").getValue().toString();
                String profilePicURL = dataSnapshot.child("profilePicURL").getValue().toString();
                String biography = dataSnapshot.child("biography").getValue().toString();
                ArrayList<Gap> gaps = new ArrayList<Gap>();
                ArrayList<Course> classes = new ArrayList<Course>();
                ArrayList<Buddy> buddies = new ArrayList<Buddy>();
                ArrayList<String> studyPotentialsIveSeen = new ArrayList<String>();
                ArrayList<String> breakPotentialsIveSeen = new ArrayList<String>();
                ArrayList<Message> messages = new ArrayList<Message>();

                if (dataSnapshot.child("gaps").getValue() != null) {
                    long childrenCount = dataSnapshot.child("gaps").getChildrenCount();
                    for (int i = 0; i < childrenCount; i++) {
                        String startTimeHour = dataSnapshot.child("gaps").child("" + i).child("startHour").getValue().toString();
                        String startTimeMinute = dataSnapshot.child("gaps").child("" + i).child("startMinute").getValue().toString();
                        String endTimeHour = dataSnapshot.child("gaps").child("" + i).child("endHour").getValue().toString();
                        String endTimeMinute = dataSnapshot.child("gaps").child("" + i).child("endMinute").getValue().toString();

                        Gap gap = new Gap(Integer.parseInt(startTimeHour), Integer.parseInt(startTimeMinute), Integer.parseInt(endTimeHour), Integer.parseInt(endTimeMinute));
                        gaps.add(gap);
                    }
                }
                if (dataSnapshot.child("classes").getValue() != null) {
                    long childrenCount = dataSnapshot.child("classes").getChildrenCount();

                    for (int i = 0; i < childrenCount; i++) {
                        String subject = dataSnapshot.child("classes").child("" + i).child("subject").getValue().toString();
                        String courseNumber = dataSnapshot.child("classes").child("" + i).child("courseNumber").getValue().toString();
                        String className = dataSnapshot.child("classes").child("" + i).child("className").getValue().toString();
                        String instructor = dataSnapshot.child("classes").child("" + i).child("instructor").getValue().toString();

                        Course course = new Course(subject, courseNumber, className, instructor);
                        classes.add(course);
                    }
                }
                if (dataSnapshot.child("buddies").getValue() != null) {
                    long childrenCount = dataSnapshot.child("buddies").getChildrenCount();

                    for (int i = 0; i < childrenCount; i++) {
                        String buddyEmail = dataSnapshot.child("buddies").child("" + i).child("email").getValue().toString();
                        String type = dataSnapshot.child("buddies").child("" + i).child("type").getValue().toString();
                        String recentMessage = dataSnapshot.child("buddies").child("" + i).child("recentMessage").getValue().toString();

                        Buddy buddy = new Buddy(buddyEmail, type, recentMessage);
                        buddies.add(buddy);
                    }
                }
                if (dataSnapshot.child("studyPotentialsIveSeen").getValue() != null) {
                    long childrenCount = dataSnapshot.child("studyPotentialsIveSeen").getChildrenCount();

                    for (int i = 0; i < childrenCount; i++) {
                        String emailOfUserIveSeen = dataSnapshot.child("studyPotentialsIveSeen").child("" + i).getValue().toString();
                        studyPotentialsIveSeen.add(emailOfUserIveSeen);
                    }
                }
                if (dataSnapshot.child("breakPotentialsIveSeen").getValue() != null) {
                    long childrenCount = dataSnapshot.child("breakPotentialsIveSeen").getChildrenCount();

                    for (int i = 0; i < childrenCount; i++) {
                        String emailOfUserIveSeen = dataSnapshot.child("breakPotentialsIveSeen").child("" + i).getValue().toString();

                        breakPotentialsIveSeen.add(emailOfUserIveSeen);
                    }
                }

                User user = new User(firstName, lastName, email, profilePicURL, classYear, biography, classes, gaps, buddies, studyPotentialsIveSeen, breakPotentialsIveSeen);
                Users.getInstance().addUser(user);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteEntryFromDatabase(String modifiedEmail, String gapOrClass, int index) {
        databaseReference.child("users").child(modifiedEmail).child(gapOrClass).child("" + index).removeValue();
    }

    public void deleteUserFromDatabase(String modifiedEmail) {
        databaseReference.child("users").child(modifiedEmail).removeValue();
    }

    public void getPotentialStudyBuddies(final String email) {
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.child("email").getValue().equals(email)) {
                    if (dataSnapshot.child("classes").getValue() != null) {
                        ArrayList<Course> courses = Users.getInstance().getUserWithEmail(email).getClasses();
                        ArrayList<String> studyPotentialsIveSeen = Users.getInstance().getUserWithEmail(email).getStudyPotentialsIveSeen();
                        long classesCount = dataSnapshot.child("classes").getChildrenCount();
                        if (courses != null) {
                            for (Course course : courses) {
                                for (int i = 0; i < classesCount; i++) {
                                    String subject = dataSnapshot.child("classes").child("" + i).child("subject").getValue().toString();
                                    String classNumber = dataSnapshot.child("classes").child("" + i).child("courseNumber").getValue().toString();

                                    if (subject.equals(course.getSubject()) && classNumber.equals(course.getCourseNumber())) {
                                        ArrayList<String> emailsOfMatchingClasses = new ArrayList<String>();
                                        String email = dataSnapshot.child("email").getValue().toString();
                                        if (!studyPotentialsIveSeen.contains(email)) {
                                            emailsOfMatchingClasses.add(email);
                                        }

                                        if (_onClassesMatchedListener != null) {
                                            _onClassesMatchedListener.onClassesMatched(emailsOfMatchingClasses);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getPotentialBreakBuddies(final String email) {
        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.child("email").getValue().equals(email)) {
                    if (dataSnapshot.child("gaps").getValue() != null) {
                        long gapsCount = dataSnapshot.child("gaps").getChildrenCount();
                        ArrayList<Gap> gaps = Users.getInstance().getUserWithEmail(email).getGaps();
                        ArrayList<String> breakPotentialsIveSeen = Users.getInstance().getUserWithEmail(email).getBreakPotentialsIveSeen();

                        if (gaps != null) {
                            for (Gap gap : gaps) {
                                for (int i = 0; i < gapsCount; i++) {
                                    String startTimeHour = dataSnapshot.child("gaps").child("" + i).child("startHour").getValue().toString();
                                    String startTimeMinute = dataSnapshot.child("gaps").child("" + i).child("startMinute").getValue().toString();

                                    if (startTimeHour.equals("" + gap.getStartHour()) && startTimeMinute.equals("" + gap.getStartMinute())) {
                                        ArrayList<String> emailsOfMatchingGaps = new ArrayList<String>();
                                        String email = dataSnapshot.child("email").getValue().toString();
                                        if (!breakPotentialsIveSeen.contains(email)) {
                                            emailsOfMatchingGaps.add(email);
                                        }
                                        if (_onGapsMatchedListener != null) {
                                            _onGapsMatchedListener.onGapsMatched(emailsOfMatchingGaps);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void saveMessageToDatabase(Message message) {
        DatabaseReference myRef = databaseReference.child("messages").push();
        myRef.setValue(message);
    }

    public void getMessagesFromDatabase(final String currentUserId, final String buddyId) {
        databaseReference.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String fromId = dataSnapshot.child("fromId").getValue().toString();
                String toId = dataSnapshot.child("toId").getValue().toString();
                String messageText = dataSnapshot.child("message").getValue().toString();
                if (fromId.equals(currentUserId) && toId.equals(buddyId)) {
                    Message message = new Message(fromId, toId, messageText);
                    if (_onMessageSentListener != null) {
                        _onMessageSentListener.onMessageSent(message);
                    }
                }
                if (fromId.equals(buddyId) && toId.equals(currentUserId)) {
                    Message message = new Message(fromId, toId, messageText);
                    if (_onMessageSentListener != null) {
                        _onMessageSentListener.onMessageSent(message);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                String fromId = dataSnapshot.child("fromId").getValue().toString();
                String toId = dataSnapshot.child("toId").getValue().toString();
                String messageText = dataSnapshot.child("message").getValue().toString();
                if (fromId.equals(currentUserId) && toId.equals(buddyId)) {
                    Message message = new Message(fromId, toId, messageText);
                    if (_onMessageSentListener != null) {
                        _onMessageSentListener.onMessageSent(message);
                    }
                }
                if (fromId.equals(buddyId) && toId.equals(currentUserId)) {
                    Message message = new Message(fromId, toId, messageText);
                    if (_onMessageSentListener != null) {
                        _onMessageSentListener.onMessageSent(message);
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteOccurencesInDatabase(final String email) {
        databaseReference.child("messages").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String fromId = dataSnapshot.child("fromId").getValue().toString();
                String toId = dataSnapshot.child("toId").getValue().toString();
                if (fromId.equals(email.substring(0, email.length() - 4)) || toId.equals(email.substring(0, email.length() - 4))) {
                    databaseReference.child("messages").child(dataSnapshot.getKey()).removeValue();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        databaseReference.child("users").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.child("buddies").getValue() != null) {
                    long childrenCount = dataSnapshot.child("buddies").getChildrenCount();

                    for (int i = 0; i < childrenCount; i++) {
                        String buddyEmail = dataSnapshot.child("buddies").child("" + i).child("email").getValue().toString();

                        if (buddyEmail.equals(email)) {
                            databaseReference.child("users").child(dataSnapshot.getKey()).child("buddies").child("" + i).removeValue();
                        }
                    }
                }
                if (dataSnapshot.child("studyPotentialsIveSeen").getValue() != null) {
                    long childrenCount = dataSnapshot.child("studyPotentialsIveSeen").getChildrenCount();

                    for (int i = 0; i < childrenCount; i++) {
                        String emailOfUserIveSeen = dataSnapshot.child("studyPotentialsIveSeen").child("" + i).getValue().toString();
                        if (emailOfUserIveSeen.equals(email)) {
                            databaseReference.child("users").child(dataSnapshot.getKey()).child("studyPotentialsIveSeen").child("" + i).removeValue();
                        }
                    }
                }
                if (dataSnapshot.child("breakPotentialsIveSeen").getValue() != null) {
                    long childrenCount = dataSnapshot.child("breakPotentialsIveSeen").getChildrenCount();

                    for (int i = 0; i < childrenCount; i++) {
                        String emailOfUserIveSeen = dataSnapshot.child("breakPotentialsIveSeen").child("" + i).getValue().toString();

                        if (emailOfUserIveSeen.equals(email)) {
                            databaseReference.child("users").child(dataSnapshot.getKey()).child("breakPotentialsIveSeen").child("" + i).removeValue();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
