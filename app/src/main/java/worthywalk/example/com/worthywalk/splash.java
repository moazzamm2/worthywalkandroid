package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;

public class splash extends AppCompatActivity {
     final baseAuth baseauth= new Auth();
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    public static final String MyPREFERENCES = "MyPrefs" ;
User user;
    SharedPreferences sharedPreferences;
    FirebaseUser fuser;
    Handler dataloadhandler=new Handler();
    FirebaseAuth mAuth;
    boolean login=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth=FirebaseAuth.getInstance();
         fuser=mAuth.getCurrentUser();

        /* New Handler to start the Menu-Activity

         * and close this Splash-Screen after some seconds.*/
        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);



        new Handler().postDelayed(new Runnable(){
                @Override
            public void run() {
                    String userjson=sharedPreferences.getString("User","");
                    Gson gson = new Gson();
                    Log.d("kiyayessethua",userjson);

                    if(fuser!=null && userjson!=null){
                        user=   gson.fromJson(userjson,User.class);
                        Log.d("userset",user.Firstname);
                        login=true;

                    }

                    if(login){
                        Intent mainIntent = new Intent(splash.this,MainActivity.class);
                        mainIntent.putExtra("User",user);
                        splash.this.startActivity(mainIntent);
//                }

                        splash.this.finish();
                        dataloadhandler.removeCallbacks(this);

                    }else {
                        Intent mainIntent = new Intent(splash.this,login.class);
                        splash.this.startActivity(mainIntent);
//                }

                        splash.this.finish();
                    }




            }
        }, SPLASH_DISPLAY_LENGTH);



    }

}
