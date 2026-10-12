package com.example.barayuga;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {

    private static final String PREF_NAME = "canopy_user_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_CURRENT_USER_EMAIL = "current_user_email";

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static boolean registerUser(Context context, String name, String email, String password) {
        if (email == null || password == null || name == null) return false;
        String cleanEmail = email.trim().toLowerCase();
        SharedPreferences prefs = getPrefs(context);

        String existingPassKey = "user_pass_" + cleanEmail;
        if (prefs.contains(existingPassKey)) {
            return false; // User already registered
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(existingPassKey, password);
        editor.putString("user_name_" + cleanEmail, name.trim());
        editor.apply();
        return true;
    }

    public static boolean loginUser(Context context, String email, String password) {
        if (email == null || password == null) return false;
        String cleanEmail = email.trim().toLowerCase();
        SharedPreferences prefs = getPrefs(context);

        String storedPass = prefs.getString("user_pass_" + cleanEmail, null);
        if (storedPass != null && storedPass.equals(password)) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(KEY_IS_LOGGED_IN, true);
            editor.putString(KEY_CURRENT_USER_EMAIL, cleanEmail);
            editor.apply();
            return true;
        }
        return false;
    }

    public static boolean isLoggedIn(Context context) {
        return getPrefs(context).getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public static String getCurrentUserEmail(Context context) {
        return getPrefs(context).getString(KEY_CURRENT_USER_EMAIL, "");
    }

    public static String getCurrentUserName(Context context) {
        String email = getCurrentUserEmail(context);
        if (email.isEmpty()) return "Friend";
        return getPrefs(context).getString("user_name_" + email, "Friend");
    }

    public static void logout(Context context) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.remove(KEY_CURRENT_USER_EMAIL);
        editor.apply();
    }
}