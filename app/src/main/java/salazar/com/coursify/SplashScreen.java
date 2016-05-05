package salazar.com.coursify;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class SplashScreen extends ActionBarActivity {

    private static int SPLASH_TIME_OUT = 2000;
    public static Context contextOfApplication;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_splash_screen);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                SharedPrefLibrary sharedPRef=new SharedPrefLibrary();
                String flag=sharedPRef.loadFlag(getApplicationContext());
                System.out.println("in sp "+flag);
                if(flag.compareTo("false")==0){
                    Intent i = new Intent(SplashScreen.this, FBLogin2.class);
                    startActivity(i);}
                else{
                    Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(intent);
                }


                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }
}
