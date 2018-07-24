package com.example.michellenguy3n.studybreak.model;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class models the Users object. Users is a collection of the User class.
 *
 * Created by michellenguy3n on 12/8/16.
 */
public class Users implements Serializable
{
    static Users _Instance = null;
    ArrayList<User> _users = new ArrayList<User>();

    public static Users getInstance() {
        if (_Instance == null) {
            _Instance = new Users();
        }
        return _Instance;
    }

    public int getUserCount()
    {
        return _users.size();
    }

    public void addUser(User user)
    {
        _users.add(user);
    }

    public boolean checkUserExists(String email)
    {
        boolean exists = false;

        if (getUserCount() != 0)
        {
            for (User user : _users)
            {
                if (user.getEmail().equals(email))
                {
                    exists = true;
                    break;
                } else {
                    exists = false;
                }
            }
        }
        else
        {
            exists = false;
        }

        return exists;
    }

    public User getUserWithEmail(String email)
    {
        User user = null;

        for (User userInDB : _users)
        {
            if (userInDB.getEmail().equals(email))
            {
                user = userInDB;
                break;
            }
            else
            {
                user = null;
            }
        }

        return  user;
    }

    public ArrayList<User> getUsers ()
    {
        return _users;
    }

    public ArrayList<String> getUsersIds ()
    {
        ArrayList<String> allUserEmails = new ArrayList<String>();
        for (User user : _users)
        {
            allUserEmails.add(user.getModifiedEmail());
        }
        return allUserEmails;
    }

    public void clearUsers ()
    {
        _users.clear();
    }

    public User getUser (int index)
    {
        return _users.get(index);
    }

    public void addUsers(ArrayList<User> users)
    {
        _users.addAll(users);
    }

    public boolean containsUser(User user)
    {
        return _users.contains(user);
    }

    public void replaceUser(User user)
    {
        _users.remove(getUserWithEmail(user.getEmail()));
        _users.add(user);
    }

    public void removeUser(User user)
    {
        _users.remove(user);
    }
}
