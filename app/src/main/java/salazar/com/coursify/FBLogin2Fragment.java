package salazar.com.coursify;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphRequestAsyncTask;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;


/**
 * A placeholder fragment containing a simple view.
 */
public class FBLogin2Fragment extends Fragment {
    private CallbackManager callbackManager;
    public static Profile  profile;
    String userFacebookId;
    UserProfile userProfile;

    public FBLogin2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AppEventsLogger.activateApp(getActivity());
        callbackManager=CallbackManager.Factory.create();


        return inflater.inflate(R.layout.fragment_fblogin2, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton=(LoginButton)view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager,callback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    FacebookCallback<LoginResult> callback=new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken=loginResult.getAccessToken();
            profile=Profile.getCurrentProfile();
            userProfile=new UserProfile();
            String firstName=profile.getFirstName();
            String lastName=profile.getLastName();
            System.out.println(firstName+" "+lastName);
            userProfile.setFirstName(firstName);
            userProfile.setLastName(lastName);


            Uri uri=profile.getProfilePictureUri(76, 76);
            userFacebookId=profile.getId();
            new AsyncTask<Void, Void, Bitmap>()
            {
                @Override
                protected Bitmap doInBackground(Void... params)
                {
                    // safety check
                    if (userFacebookId == null)
                        return null;

                    String url = String.format(
                            "https://graph.facebook.com/%s/picture",
                            userFacebookId);

                    // you'll need to wrap the two method calls
                    // which follow in try-catch-finally blocks
                    // and remember to close your input stream

                    InputStream inputStream = null;
                    try {
                        inputStream = new URL(url).openStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap)
                {
                    File folder = new File(Environment.getExternalStorageDirectory() + "/Coursify");
                    boolean success = true;
                    if (!folder.exists()) {
                        success = folder.mkdir();
                    }
                    if (success) {
                        Log.v("create new Dir", "working!!!");
                    } else {
                        Log.v("create new Dir", "NOT working!!!");
                    }
                    File f;
                    f = new File(Environment.getExternalStorageDirectory() + "/Coursify/" + "proPic.png");
                    try {
                        f.createNewFile();
                    } catch (IOException e) {
                        Log.v("create new file", "not working!!!");
                    }
                    if (f.exists() && !f.isDirectory()) {
                        System.out.println("Path= " + f.getAbsolutePath());
                        userProfile.fileForProPic=f.getAbsolutePath();
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(f);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 0, out); // bmp is your Bitmap instance
                            // PNG is a lossless format, the compression factor (100) is ignored
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
            }.execute();

        userProfile.setFlag(true);
        SharedPrefLibrary.saveData(getActivity(),userProfile);

          /*  GraphRequestAsyncTask graphRequestAsyncTask1 = new GraphRequest(
                    loginResult.getAccessToken(),
                    "/me/picture.type(large)",
                    null,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {


                            try {
                                image_value[0] = new URL("https://graph.facebook.com/"+profile.getId()+"/picture" );
                                InputStream in = (InputStream) image_value[0].getContent();
                                bmp[0] = BitmapFactory.decodeStream(in);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).executeAsync();*/



















/*
            HttpURLConnection urlConnection = null;
            try {
                urlConnection = (HttpURLConnection) image_value.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoOutput(true);
                urlConnection.connect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
*/



                GraphRequestAsyncTask graphRequestAsyncTask = new GraphRequest(
                        loginResult.getAccessToken(),
                        "/me/friends",
                        null,
                        HttpMethod.GET,
                        new GraphRequest.Callback() {
                            public void onCompleted(GraphResponse response) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                try {
                                    JSONArray rawName = response.getJSONObject().getJSONArray("data");
                                    SharedPrefLibrary.saveFriendList(getActivity(), rawName.toString());
                                    System.out.println(rawName);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).executeAsync();


            }



        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };



}
