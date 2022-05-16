package com.nhom26.cuoikynhom26.Activities.account;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.nhom26.cuoikynhom26.model.User;

/**
 * Created by hoangg on 13/05/2022.
 */

public class UserControl {
    public static void saveObjectToSharedPreference(Context context, String preferenceFileName, String serializedObjectKey, Object object) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        final Gson gson = new Gson();
        String serializedObject = gson.toJson(object);
        sharedPreferencesEditor.putString(serializedObjectKey, serializedObject);
        sharedPreferencesEditor.commit();
    }
    public static <GenericClass> GenericClass getSavedObjectFromPreference(Context context, String preferenceFileName, String preferenceKey, Class<GenericClass> classType) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(preferenceFileName, 0);
        if (sharedPreferences.contains(preferenceKey)) {
            final Gson gson = new Gson();
            return gson.fromJson(sharedPreferences.getString(preferenceKey, ""), classType);
        }
        return null;
    }
    public static User getLastUser(Context context){
        return getSavedObjectFromPreference(context, "lastUser", "user", User.class);
    }


}
