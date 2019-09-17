package worthywalk.example.com.worthywalk;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.type.LatLng;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import static worthywalk.example.com.worthywalk.App.CHANNEL_ID;


public class SensorForeground extends Service {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    User user=new User();
    @Override
    public void onCreate() {
        super.onCreate();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        user= (User) intent.getSerializableExtra("User");
        //do heavy work on a background thread
        //stopSelf();
        SensorHandler handlerLivingLight = new SensorHandler(this);

        if (intent.getAction().equals("Start")) {
            Log.i("Foreground", "Received Start Foreground Intent ");
            handlerLivingLight.start();
            String input = intent.getStringExtra("input");
            long time = intent.getLongExtra("time",0);

            float x= intent.getFloatExtra("x",0);
            float y= intent.getFloatExtra("y",0);
            float z= intent.getFloatExtra("z",0);
            int steps=intent.getIntExtra("Steps",0);

            WalkActivity.getInstance().UpdateSensorTV(steps);

//            // Create an Intent for the activity you want to start
//            Intent resultIntent = new Intent(this, WalkActivity.class);
//// Create the TaskStackBuilder and add the intent, which inflates the back stack
//            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//            stackBuilder.addNextIntentWithParentStack(resultIntent);
//// Get the PendingIntent containing the entire back stack
//            PendingIntent resultPendingIntent =
//                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
//
            Intent notificationIntent = new Intent(this, WalkActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, notificationIntent, 0);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("Walk Activity")

                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(101, notification);

            // your start service code
        }
        else if (intent.getAction().equals( "Stop")) {
            Log.i("Foreground", "Received Stop Foreground Intent");
            //your end servce code
            handlerLivingLight.stop();
                        stopForeground(true);
            stopSelf();
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

}