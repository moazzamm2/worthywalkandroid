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
import android.util.Log;

import androidx.annotation.RequiresApi;

import static android.content.Context.MODE_PRIVATE;

public class SensorHandler implements SensorEventListener,StepListener{
    SensorManager sensorManager;
    Sensor accelerometer;
    int Steps;
    double x=0.0,y=0.0,z=0.0;
    public boolean started = false;
    public Handler handler = new Handler( );
StepDetector stepDetector;
    Context myContext;

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
        Sensor();

        if (started == true) {
            stop ( );
        }
        if(started == false){
            start ();
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

    public boolean stop() {

        started(false);
                /*= false;*/
        handler.removeCallbacksAndMessages(null);
        return started;
    }

    public boolean start() {
        started(true);
                /*= true;*/
        handler.postDelayed (runnable, 1000);
        return check();
    }

    public void started(Boolean str){

        SharedPreferences settings = myContext.getSharedPreferences("Consumption",0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean ("stateCheck",str);
        editor.commit();



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
        Log.d("steps", String.valueOf(Steps));
    }
}