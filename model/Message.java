package com.example.michellenguy3n.studybreak.model;

/**
 * The class models the Message class. A message is sent from a user with a specified ID to a user with another ID with text in the message.
 * 
 * Created by michellenguy3n on 12/15/16.
 */
public class Message {
    String fromId;
    String toId;
    String message;

    public Message(String _fromId, String _toId, String _message) {
        fromId = _fromId;
        toId = _toId;
        message = _message;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getMessage() {
        return message;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
