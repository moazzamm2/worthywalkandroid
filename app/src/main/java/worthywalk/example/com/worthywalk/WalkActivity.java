package worthywalk.example.com.worthywalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.sql.Timestamp;

public class WalkActivity extends AppCompatActivity implements StepListener {
    int [] icons=  new int[]{R.drawable.ic_runer_silhouette_running_fast,R.drawable.ic_man_cycling,R.drawable.ic_treadmill};
    int [] circle=new int[]{R.drawable.walkcircle,R.drawable.cyclecircle,R.drawable.treadmillcircle};
    int [] colors=new int[]{R.color.walk,R.color.cycle,R.color.treadmill};
    int steps=0;
    static WalkActivity Instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
//    TextView textView, tv_x, tv_y, tv_z;
    SensorManager sensorManager;
    Sensor accelerometer;
    Button btn;

    public static WalkActivity getInstance() {
        return Instance;
    }
    ImageView iconset;
    TextView calorie,distance,timer;
    int index=0;
    TextView tap;
    Button startbtn;
    RelativeLayout relativeLayout;
    RelativeLayout relativeLayoutprog;
    ProgressBar progressBar;
    boolean start =false;
    boolean tapped=false;
    StepDetector stepDetector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        Instance = this;
        Permission();
        relativeLayoutprog = (RelativeLayout) findViewById(R.id.relativeprog);
        relativeLayout=(RelativeLayout) findViewById(R.id.relativeLayout);
        tap=(TextView)findViewById(R.id.tap);
    progressBar=(ProgressBar) findViewById(R.id.progressBar5);
    iconset=(ImageView)findViewById(R.id.iconview);
    calorie=(TextView)findViewById(R.id.calorie);
        distance=(TextView)findViewById(R.id._kms);
        timer=(TextView)findViewById(R.id.time);
       startbtn=(Button) findViewById(R.id.start);
//
        stepDetector=new StepDetector();
        stepDetector.registerListener(this);


     relativeLayoutprog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!start){

                    tap.setVisibility(View.GONE);
                    tapped=true;
                    index= index%3;
                    iconset.setBackgroundResource(icons[index]);
                    relativeLayout.setBackgroundResource(circle[index]);
                    progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(),colors[index]), PorterDuff.Mode.SRC_IN );;
                    index++;

                }

            }
        });


startbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

       if(tapped) {
           if (start) {
               startbtn.setText("START");
               startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark), PorterDuff.Mode.MULTIPLY);

               Intent serviceIntent = new Intent(WalkActivity.this, SensorForeground.class);
               serviceIntent.setAction("Stop");
               ContextCompat.startForegroundService(WalkActivity.this, serviceIntent);
               fusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
               start = false;
           } else {
               startbtn.setText("STOP");
               startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark), PorterDuff.Mode.MULTIPLY);
               Intent serviceIntent = new Intent(WalkActivity.this, SensorForeground.class);
               serviceIntent.setAction("Start");
               ContextCompat.startForegroundService(WalkActivity.this, serviceIntent);;
               fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
               start = true;
           }
       }
    }
});

    }



    private void Permission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        UpdateLocation();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(), "Accept Location", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {

                    }

                }).check();

    }

    private void UpdateLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        BuildLocationRequest();


    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, MyLocationService.class);
        intent.setAction(MyLocationService.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void BuildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setSmallestDisplacement(2f);

    }

    public void UpdateTextView(final String Val) {
        WalkActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                textView.setText(Val);
                Intent intent = new Intent(WalkActivity.this,SensorForeground.class);
                intent.putExtra("intent",Val);
            }
        });
        Toast.makeText(getApplicationContext(),Val,Toast.LENGTH_SHORT).show();
    }

    public void UpdateSensorTV(int steps) {
        WalkActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                tv_x.setText(Val);
            }
        });
    }


    @Override
    public void step(long timeNs) {
       steps++;
       calorie.setText(String.valueOf(steps));

    }
}
