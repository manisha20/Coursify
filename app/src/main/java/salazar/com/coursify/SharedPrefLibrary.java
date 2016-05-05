package salazar.com.coursify;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Salazar on 27-04-2016.
 */
public class SharedPrefLibrary {
    static String sharedPrefName="Pref";
    public static void saveData(Context context, UserProfile profile) {

        SharedPreferences sp =context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("firstName", profile.firstName);
        editor.putString("lastName", profile.lastName);
        editor.putString("fileForProPic", profile.fileForProPic);
        editor.putString("flag", profile.flag.toString());

        editor.commit();



    }
    public static void saveFriendList(Context context, String jsonListOfFriends) {

        SharedPreferences sp =context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("friendList", jsonListOfFriends);


        editor.commit();



    }

    public static String loadProPicFileName(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(sharedPrefName,
                        Context.MODE_PRIVATE);
        String fileForProPic = sp.getString("fileForProPic", "0");

        return fileForProPic;
    }
    public static String loadFriendList(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(sharedPrefName,
                        Context.MODE_PRIVATE);
        String fileForProPic = sp.getString("friendList", "0");

        return fileForProPic;
    }

    public static String loadFirstName(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(sharedPrefName,
                        Context.MODE_PRIVATE);
        String name=sp.getString("firstName","abc");
        return name;
    }
    public static String loadLastName(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(sharedPrefName,
                        Context.MODE_PRIVATE);
        String name=sp.getString("lastName","abc");
        return name;
    }
    public static String loadFlag(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(sharedPrefName,
                        Context.MODE_PRIVATE);
        String name=sp.getString("flag","false");
        return name;
    }

    public static String loadName(Context context) {
        SharedPreferences sp =
                context.getSharedPreferences(sharedPrefName,
                        Context.MODE_PRIVATE);
        String fname=sp.getString("firstName", "abc");

        String lname=sp.getString("lastName", "abc");
        return fname+" "+lname;
    }

}
