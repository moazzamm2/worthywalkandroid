package worthywalk.example.com.worthywalk;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class SensorHandler implements SensorEventListener,StepListener{
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static int Steps;
    private double x=0.0,y=0.0,z=0.0;
    private static boolean started = false;
    private static Handler handler = new Handler( );
    private StepDetector stepDetector;
    private static Context myContext;

    public SensorHandler(Context cntxt){
        myContext=cntxt;

    }

    private Runnable runnable = new Runnable( ) {
        @Override
        public void run() {
            TIMER();
        }
    };

    public void TIMER(){

        if (started == true) {
            stop ( );
            stopsensor();

        }
        if(started == false){
            start ();
            Sensor();

        }
    }

    public void Sensor( ){

        //  serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");

        stepDetector=new StepDetector();
        stepDetector.registerListener(this);
        sensorManager =(SensorManager) myContext.getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public static boolean stop() {

        started(false);

                /*= false;*/

        Log.d("unregister","listener unregistered");
        handler.removeCallbacksAndMessages(null);


        return started;
    }

    public boolean start() {
        started(true);
                /*= true;*/
        handler.postDelayed (runnable, 1000);
        return check();
    }

    public boolean stopsensor(){


        sensorManager.unregisterListener(this,accelerometer);
        Toast.makeText(myContext,"STOP SENSOR",Toast.LENGTH_LONG).show();
        return false;

    }

    public static void started(Boolean str){

        SharedPreferences settings = myContext.getSharedPreferences("Consumption",0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean ("stateCheck",str);

        editor.putInt(WalkActivity.SESSION_NUMBER,0);
        editor.apply();
    }

    public boolean check(){
        SharedPreferences shared = myContext.getSharedPreferences("Consumption", MODE_PRIVATE);
        boolean channel = (shared.getBoolean("stateCheck", false));
        return  channel;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        if(x != sensorEvent.values[0]||y != sensorEvent.values[1]|| z != sensorEvent.values[2])
        {
            x =sensorEvent.values[0];
            y =sensorEvent.values[1];
            z =sensorEvent.values[2];

            stepDetector.updateAccel(sensorEvent.timestamp,sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);
            String v = String.valueOf(x+"\n"+y+"\n"+z+"\n");


//            WalkActivity.getInstance().UpdateSensorTV(sensorEvent.timestamp,sensorEvent.values[0],sensorEvent.values[1],sensorEvent.values[2]);
          WalkActivity.getInstance().UpdateSensorTV(Steps);

            Log.d("Sensor", "onSensorChanged: "+sensorEvent.values[0]+"\n"
                    +sensorEvent.values[1]+"\n"+
                    sensorEvent.values[2]);
            Intent intent = new Intent(myContext, SensorForeground.class);
            intent.putExtra("time",sensorEvent.timestamp);
            intent.putExtra("x",sensorEvent.values[0]);
            intent.putExtra("y",sensorEvent.values[1]);
            intent.putExtra("z",  sensorEvent.values[2]);
            intent.putExtra("steps",Steps);

            //                Push(v);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i)
    {
    }

    @Override
    public void step(long timeNs) {
        Steps++;
        SensorForeground.steps++;
        MyLocationService.walk.setSteps(Steps);
        Log.d("steps", String.valueOf(Steps));
    }
}
