package com.example.michellenguy3n.studybreak.model;

import java.io.Serializable;

/**
 * This class models the Buddy object. A Buddy is a user who has matched with the current user.
 * 
 * Created by michellenguy3n on 12/7/16.
 */
public class Buddy implements Serializable {
    String type;
    String email;
    String recentMessage;

    public Buddy(String _email, String _type, String _recentMessage) {
        email = _email;
        type = _type;
        recentMessage = _recentMessage;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }
}
