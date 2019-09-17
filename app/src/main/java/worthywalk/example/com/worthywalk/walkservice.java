package worthywalk.example.com.worthywalk;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.gms.common.internal.Constants;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class walkservice extends IntentService  {

    SensorManager sensorManager;
    MyStepListener listener;
    private static final String NOTIFICATION_CHANNEL_ID = "channelId" ;
    private static final String NOTIFICATION_CHANNEL_ID_SERVICE ="walkservice" ;
    private int numSteps=0;
    private StepDetector simpleStepDetector;
    private long startTime=00L,timeMiliseconds=00L,updateTime=00L,timeSwapbuff=0l;
    Intent intent;
    Handler customHandler = new Handler();
    Handler calorieHandler = new Handler();
    Handler PointHandler= new Handler();
    Handler DistanceHandler = new Handler();
    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeMiliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapbuff + timeMiliseconds;
            int secs = (int) (updateTime/1000);
            int mins = secs/60;secs%=60;
            int hr = mins/60;mins%=60;
            customHandler.postDelayed(this,500);
        }
    };
    Runnable updatecalorie = new Runnable() {
        @Override
        public void run() {
            float rate = (float) 0.04;
            rate= rate*numSteps;
            Log.d(TAG, "run: colorie ");
            Log.d(TAG, String.format("%f",rate));
            calorieHandler.postDelayed(this,3000);
        }
    };

    Runnable updatePoint = new Runnable() {
        @Override
        public void run() {
            float point=0 ;

            point= numSteps/40;

//            Textpoints.setText(String.format("%4f",point));
            PointHandler.postDelayed(this,600);
        }
    };

    Runnable updateDistance = new Runnable() {
        @Override
        public void run() {
            float kms =0;
            kms = (numSteps/2000)*(float)1.609;
            DistanceHandler.postDelayed(this,0);
        }
    };
    public static final String BROADCAST_ACTION = "Hello World";
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "worthywalk.example.com.worthywalk.action.FOO";
    private static final String ACTION_BAZ = "worthywalk.example.com.worthywalk.action.BAZ";

    // TODO: Rename parameters
    private static final String TAG="walkstart";
    private static final String EXTRA_PARAM1 = "worthywalk.example.com.worthywalk.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "worthywalk.example.com.worthywalk.extra.PARAM2";

    public walkservice() {
        super("walkservice");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        intent=new Intent(BROADCAST_ACTION);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
if(intent.getAction().equals("StartActivity")) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        shownotification();
    }

    sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
    listener=new MyStepListener();
    numSteps=listener.steps;
    startTime = SystemClock.uptimeMillis();
    customHandler.postDelayed(updateTimerThread, 0);
    PointHandler.postDelayed(updatePoint, 1000);
    calorieHandler.postDelayed(updatecalorie, 1000);
    DistanceHandler.postDelayed(updateDistance, 0);
}
        else if (intent.getAction().equals("StopActivity")) {
            Log.i(TAG, "Received Stop Foreground Intent");
            //your end servce code
            stopForeground(true);
            stopSelf();
        }

        return START_STICKY;

    }
@RequiresApi(api = Build.VERSION_CODES.O)
public void shownotification(){

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.createNotificationChannel(new NotificationChannel(NOTIFICATION_CHANNEL_ID_SERVICE, "*****", NotificationManager.IMPORTANCE_HIGH));
    }
    String NOTIFICATION_CHANNEL_ID = "pl.***.***";
    String channelName = "Communication Service";
    NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);

    NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    assert manager != null;
    manager.createNotificationChannel(chan);
    NotificationCompat.Builder builder=new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
    builder.setSmallIcon(R.mipmap.ic_launcher)
    .setContentText(String.format("%d",numSteps))
    .setContentTitle("WORTHY WALK");
    builder.setChannelId(NOTIFICATION_CHANNEL_ID);

    Notification notification=builder.build();
startForeground(123,notification);
}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     *
     *
     */
    // TODO: Customize helper method



    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */




    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */


    /**
     * Handle action Baz in the provided background thread with the provided
     * parameters.
     */



    private class MyStepListener implements SensorEventListener {
    int steps;
        private static final int ACCEL_RING_SIZE = 50;
        private static final int VEL_RING_SIZE = 10;

        // change this threshold according to your sensitivity preferences
        private static final float STEP_THRESHOLD = 50f;

        private static final int STEP_DELAY_NS = 250000000;

        private int accelRingCounter = 0;
        private float[] accelRingX = new float[ACCEL_RING_SIZE];
        private float[] accelRingY = new float[ACCEL_RING_SIZE];
        private float[] accelRingZ = new float[ACCEL_RING_SIZE];
        private int velRingCounter = 0;
        private float[] velRing = new float[VEL_RING_SIZE];
        private long lastStepTimeNs = 0;
        private float oldVelocityEstimate = 0;

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                updateAccel(
                        event.timestamp, event.values[0], event.values[1], event.values[2]);

            }
        }

        private void updateAccel(long timeNs, float x, float y, float z) {
            float[] currentAccel = new float[3];
            currentAccel[0] = x;
            currentAccel[1] = y;
            currentAccel[2] = z;

            // First step is to update our guess of where the global z vector is.
            accelRingCounter++;
            accelRingX[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[0];
            accelRingY[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[1];
            accelRingZ[accelRingCounter % ACCEL_RING_SIZE] = currentAccel[2];

            float[] worldZ = new float[3];
            worldZ[0] = SensorFilter.sum(accelRingX) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
            worldZ[1] = SensorFilter.sum(accelRingY) / Math.min(accelRingCounter, ACCEL_RING_SIZE);
            worldZ[2] = SensorFilter.sum(accelRingZ) / Math.min(accelRingCounter, ACCEL_RING_SIZE);

            float normalization_factor = SensorFilter.norm(worldZ);

            worldZ[0] = worldZ[0] / normalization_factor;
            worldZ[1] = worldZ[1] / normalization_factor;
            worldZ[2] = worldZ[2] / normalization_factor;

            float currentZ = SensorFilter.dot(worldZ, currentAccel) - normalization_factor;
            velRingCounter++;
            velRing[velRingCounter % VEL_RING_SIZE] = currentZ;

            float velocityEstimate = SensorFilter.sum(velRing);

            if (velocityEstimate > STEP_THRESHOLD && oldVelocityEstimate <= STEP_THRESHOLD
                    && (timeNs - lastStepTimeNs > STEP_DELAY_NS)) {
                listener.step(timeNs);
                lastStepTimeNs = timeNs;
                numSteps++;
            }
            oldVelocityEstimate = velocityEstimate;
        }

        private void step(long timeNs) {
            steps++;
            intent.putExtra("Provider", numSteps);
            sendBroadcast(intent);
        }


        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }
}
