package com.teambisu.mobilecomputingandsendingofgrades.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * Created by John Manuel on 25/08/2017.
 */

public class Session {

    private SharedPreferences prefs;

    public Session(Context context) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void setId(int id) {
        prefs.edit().putInt("id", id).apply();
    }

    public int getId() {
        int id = prefs.getInt("id", 0);
        return id;
    }

    public void setusername(String usename) {
        prefs.edit().putString("usename", usename).apply();
    }

    public String getusername() {
        String usename = prefs.getString("usename", "");
        return usename;
    }
    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
