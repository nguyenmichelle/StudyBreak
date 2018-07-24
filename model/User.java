package com.example.michellenguy3n.studybreak.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class models the User object. A User contains personal details as well as personal schedule details, buddies, and other users that they've seen.
 * 
 * Created by michellenguy3n on 12/6/16.
 */
public class User implements Serializable {
    String firstName;
    String lastName;
    String email;
    String profilePicURL;
    String classYear;
    String biography;
    ArrayList<Course> classes;
    ArrayList<Gap> gaps;
    ArrayList<Buddy> buddies;
    ArrayList<String> studyPotentialsIveSeen;
    ArrayList<String> breakPotentialsIveSeen;

    public User(String _firstName, String _lastName, String _email, String _profilePicURL, String _classYear, String _biography, ArrayList<Course> _classes, ArrayList<Gap> _gaps, ArrayList<Buddy> _buddies, ArrayList<String> _studyPotentialsIveSeen, ArrayList<String> _breakPotentialsIveSeen) {
        firstName = _firstName;
        lastName = _lastName;
        email = _email;
        profilePicURL = _profilePicURL;
        classYear = _classYear;
        biography = _biography;
        classes = _classes;
        gaps = _gaps;
        buddies = _buddies;
        studyPotentialsIveSeen = _studyPotentialsIveSeen;
        breakPotentialsIveSeen = _breakPotentialsIveSeen;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getModifiedEmail() {
        return email.substring(0, email.length() - 4);
    }

    public String getProfilePicURL() {
        return profilePicURL;
    }

    public String getClassYear() {
        return classYear;
    }

    public String getBiography() {
        return biography;
    }

    public ArrayList<Course> getClasses() {
        return classes;
    }

    public ArrayList<Gap> getGaps() {
        return gaps;
    }

    public ArrayList<Buddy> getBuddies() {
        return buddies;
    }

    public ArrayList<String> getStudyPotentialsIveSeen() {
        return studyPotentialsIveSeen;
    }

    public ArrayList<String> getBreakPotentialsIveSeen() {
        return breakPotentialsIveSeen;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfilePicURL(String profilePicURL) {
        this.profilePicURL = profilePicURL;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setClassYear(String classYear) {
        this.classYear = classYear;
    }

    public void setClasses(ArrayList<Course> classes) {
        this.classes = classes;
    }

    public void setGaps(ArrayList<Gap> gaps) {
        this.gaps = gaps;
    }

    public void setBuddies(ArrayList<Buddy> buddies) {
        this.buddies = buddies;
    }

    public void setStudyPotentialsIveSeen(ArrayList<String> studyPotentialsIveSeen) {
        this.studyPotentialsIveSeen = studyPotentialsIveSeen;
    }

    public void setBreakPotentialsIveSeen(ArrayList<String> breakPotentialsIveSeen) {
        this.breakPotentialsIveSeen = breakPotentialsIveSeen;
    }

    public void addClass(Course course) {
        classes.add(course);
    }

    public void addGap(Gap gap) {
        gaps.add(gap);
    }

    public void addBuddy(Buddy buddy) {
        if (containsBuddyWithEmail(buddy.getEmail())) {
            buddies.get(getIndexOfBuddyWithEmail(buddy.getEmail())).setType("both");
        } else {
            buddies.add(buddy);
        }
    }

    public boolean containsBuddyWithEmail(String email) {
        boolean containsBuddy = false;
        for (Buddy buddy : buddies) {
            if (buddy.getEmail().equals(email)) {
                containsBuddy = true;
                break;
            }
        }

        return containsBuddy;
    }

    public int getIndexOfBuddyWithEmail(String email) {
        int index = -1;

        for (Buddy buddy : buddies) {
            if (buddy.getEmail().equals(email)) {
                index = buddies.indexOf(buddy);
                break;
            }
        }
        return index;
    }

    public void addBreakPotentialIveSeen(String email) {
        breakPotentialsIveSeen.add(email);
    }

    public void addStudyPotentialIveSeen(String email) {
        studyPotentialsIveSeen.add(email);
    }

    public ArrayList<Course> getClassesInCommon(User user) {
        ArrayList<Course> coursesInCommon = new ArrayList<Course>();
        for (Course course : this.getClasses()) {
            for (Course compareCourse : user.getClasses()) {
                if (course.checkEquals(compareCourse)) {
                    coursesInCommon.add(course);
                }
            }
        }
        return coursesInCommon;
    }

    public ArrayList<Gap> getGapsInCommon(User user) {
        ArrayList<Gap> gapsInCommon = new ArrayList<Gap>();
        for (Gap gap : this.getGaps()) {
            for (Gap compareGap : user.getGaps()) {
                if (gap.checkEquals(compareGap)) {
                    gapsInCommon.add(gap);
                }
            }
        }
        return gapsInCommon;
    }

    public Buddy getBuddyWithEmail(String email) {
        Buddy buddyWithEmail = null;
        for (Buddy buddy : buddies) {
            if (buddy.getEmail().equals(email)) {
                buddyWithEmail = buddy;
                break;
            }
        }
        return buddyWithEmail;
    }
}
