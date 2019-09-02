package worthywalk.example.com.worthywalk;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.os.health.SystemHealthManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.Semaphore;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import static java.lang.Thread.sleep;
public class workoutActivity extends AppCompatActivity implements StepListener {
    private TextView TvSteps,Textcal,Texttimer,Textpoints,Textdistance;
    private Button BtnStop;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps: ";
    private int numSteps=0;
    ProgressBar progressBar;
    private boolean isWalking=false;
    private long startTime=00L,timeMiliseconds=00L,updateTime=00L,timeSwapbuff=0l;
    Handler customHandler = new Handler();
    Handler calorieHandler = new Handler();
    Handler PointHandler= new Handler();
    Handler DistanceHandler = new Handler();
    private StepListener listener;

    public void registerListener(StepListener listener) {
        this.listener = listener;
    }

    boolean start=false;

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeMiliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapbuff + timeMiliseconds;
            int secs = (int) (updateTime/1000);
            int mins = secs/60;secs%=60;
            int hr = mins/60;mins%=60;
            Texttimer.setText(""+hr+":"+String.format("%2d",mins)+":"+String.format("%2d",secs));
            customHandler.postDelayed(this,500);
        }
    };
    Runnable updatecalorie = new Runnable() {
        @Override
        public void run() {
            float rate = (float) 0.04;
            rate= rate*numSteps;
            Textcal.setText(rate+"");
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
            Textdistance.setText(String.format("%3f",kms));
            DistanceHandler.postDelayed(this,0);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_workout);
      SensorManager  sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
//        progressBar.setMax(150);

//
//        TvSteps= (TextView)findViewById(R.id.stepcount);
//        Textpoints = (TextView)findViewById(R.id.pointcount);
        Texttimer= (TextView)findViewById(R.id.time);
        Textcal= (TextView)findViewById(R.id.calorie);
        Textdistance = (TextView)findViewById(R.id._kms);

        BtnStop = (Button) findViewById(R.id.start);
        BtnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(start) {
//                    sensorManager.unregisterListener(workoutActivity.this);
                    BtnStop.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.green));
                    BtnStop.setText("Start");
//                    isWalking=false;
                    timeSwapbuff+=timeMiliseconds;
                    customHandler.removeCallbacks(updateTimerThread);
                    PointHandler.removeCallbacks(updatePoint);
                    calorieHandler.removeCallbacks(updatecalorie);

//                    linearLayout.setVisibility(View.VISIBLE);
//                    title.setVisibility(View.GONE);
                    BtnStop.setText("Stop");
                    Intent stopIntent = new Intent(workoutActivity.this,  StepCounterService.class);
                    stopIntent.setAction("StopActivity");
                    startService(stopIntent);
                    start=false;

                }else{
//                    isWalking=true;
//                    sensorManager.registerListener(workoutActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);
//                    BtnStop.setText("Stop");
//                    BtnStop.setBackgroundColor(getResources().getColor(R.color.red));
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread,0);
                    PointHandler.postDelayed(updatePoint,1000);
                    calorieHandler.postDelayed(updatecalorie,1000);
                    DistanceHandler.postDelayed(updateDistance,0);
//                    linearLayout.setVisibility(View.GONE);
//                    title.setVisibility(View.VISIBLE);
                    BtnStop.setText("Start");

                    Intent startIntent = new Intent(workoutActivity.this, StepCounterService.class);
                    startIntent.setAction("StartActivity");
                  startService(startIntent);
                    start = true;


                }
            }
        });
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        Log.d("Apbuscheckkarain", "step: "+String.valueOf(numSteps));

    }


}