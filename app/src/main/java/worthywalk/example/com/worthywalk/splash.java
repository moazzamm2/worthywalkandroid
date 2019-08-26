package worthywalk.example.com.worthywalk;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
     final baseAuth baseauth= new Auth();
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
            /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
//                if(baseauth.currentUser()==null){
//                    Intent mainIntent = new Intent(splash.this,login.class);
//                    splash.this.startActivity(mainIntent);
//                }else{
                    Intent mainIntent = new Intent(splash.this,login.class);
                    splash.this.startActivity(mainIntent);
//                }

                splash.this.finish();

            }
        }, SPLASH_DISPLAY_LENGTH);



    }

}
