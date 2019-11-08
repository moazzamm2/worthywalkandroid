package worthywalk.example.com.worthywalk;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import static  worthywalk.example.com.worthywalk.App.CHANNEL_ID;


public class SensorForeground extends Service {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    User user=new User();

    //To see whether service is running or not
    public static boolean isServiceRunning = false;

    // Index of image
    public static int index = 0;

    // For the timer
    public static Chronometer chronometer;

    // This will contain Longitude and Latitude points of the route
    public static List<LatLng> loc;

    // This will count the steps
    public static int steps;


    @Override
    public void onCreate() {
        super.onCreate();

        WalkActivity walkActivity = WalkActivity.getInstance();

        isServiceRunning = true;
        chronometer = new Chronometer(this);
        loc = new ArrayList<>();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        user= (User) intent.getSerializableExtra("User");
        //do heavy work on a background thread
        //stopSelf();
        SensorHandler handlerLivingLight = new SensorHandler(this);

        if (intent.getAction().equals(WalkActivity.START)) {
            Log.i("Foreground", "Received Start Foreground Intent ");
            handlerLivingLight.start();
            timerstart();
            String input = intent.getStringExtra("input");
            long time = intent.getLongExtra("time",0);

            float x= intent.getFloatExtra("x",0);
            float y= intent.getFloatExtra("y",0);
            float z= intent.getFloatExtra("z",0);
            int steps=intent.getIntExtra("Steps",0);

            WalkActivity.getInstance().UpdateSensorTV(steps);

            Intent notificationIntent = new Intent(this, WalkActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Walk Activity")
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher).build();

            startForeground(101, notification);

            //Get index of image
            index = intent.getIntExtra(WalkActivity.INDEX,0);

        }
        else if (intent.getAction().equals( WalkActivity.STOP)) {
            Log.i("Foreground", "Received Stop Foreground Intent");
            //your end servce code
            handlerLivingLight.stop();
            stopForeground(true);
            stopSelf();
            isServiceRunning = false;
            timerstop();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Start the timer
    private void timerstart() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                Log.i("TTT","tick");
            }
        });
        chronometer.start();
    }

    //Stop the timer
    private void timerstop(){
        chronometer.stop();
    }

    public static long getTime(){
        return chronometer.getBase();
    }

    // 2.5 steps = 1 meter on average
    // since we detect motion of one forward step we divide the steps by 1.25
    public static double getDistance() {
        return steps / 1.25;
    }

    //27 steps burn 1KiloCalories
    public static double getCaloriesBurnt(){
        return steps/27;
    }

    //Step count
    public static int getStepCount(){
        return steps;
    }
}