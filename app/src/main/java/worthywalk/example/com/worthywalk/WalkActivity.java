package worthywalk.example.com.worthywalk;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import static java.lang.Math.asin;
import static java.lang.Math.sqrt;

public class WalkActivity extends AppCompatActivity  {
    int [] icons=  new int[]{R.drawable.ic_runer_silhouette_running_fast,R.drawable.ic_man_cycling,R.drawable.ic_treadmill};
    int [] circle=new int[]{R.drawable.walkcircle,R.drawable.cyclecircle,R.drawable.treadmillcircle};
    int [] colors=new int[]{R.color.walk,R.color.cycle,R.color.treadmill};
    int steps=0;
    Context context;
    static WalkActivity Instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
//    TextView textView, tv_x, tv_y, tv_z;
    SensorManager sensorManager;
    Sensor accelerometer;
    Double _kms,cal;

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
    FloatingActionButton fabbutton;
Chronometer chronometer;
    User user=new User();
    LatLng point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        Instance = this;
        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("User");
        Permission();
        setviews();
        setbuttons();


//
        stepDetector=new StepDetector();
        context=this;

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

fabbutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.bonusdailog);
        dialog.setTitle("Bonus");
        final EditText text = (EditText) dialog.findViewById(R.id.promo);
        Button dialogButton = (Button) dialog.findViewById(R.id.use);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x = text.getText().toString().trim();

//                        Query q=mDatabase.child("Bounus").orderByChild("Code").equalTo(x);

//                        q.addChildEventListener(new ChildEventListener() {
//                            @Override
//                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                String x = dataSnapshot.getKey();
//                                Float y = dataSnapshot.child("multiply").getValue(Float.class);
//                                divideby= divideby/y;
//                            }
//
//                            @Override
//                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                            }
//
//                            @Override
//                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                            }
//
//                            @Override
//                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                            }
//                        });
                dialog.dismiss();
            }

        });
        dialog.show();
    }

});

    }

    private void setbuttons() {

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tapped) {
                    if (start) {
                        startbtn.setText("START");
                        startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark), PorterDuff.Mode.MULTIPLY);
                        timerstop();
                        Intent serviceIntent = new Intent(WalkActivity.this, SensorForeground.class);
                        serviceIntent.setAction("Stop");
                        ContextCompat.startForegroundService(WalkActivity.this, serviceIntent);
                        fusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
                       calculate();


                        final Dialog dialog = new Dialog(context);

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        /////make map clear
                        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

                        dialog.setContentView(R.layout.finishdilog);////your custom content
                       TextView dialogbtn=(TextView) dialog.findViewById(R.id.finishworkout);
                       TextView steps=(TextView) dialog.findViewById(R.id.steps);
                       TextView discard=(TextView) dialog.findViewById(R.id.discardeddistance);
                        MapView mMapView = (MapView) dialog.findViewById(R.id.map);
                        discard.setText(String.format("%.2f",MyLocationService.walk.discardeddistance));
                        steps.setText(String.valueOf(MyLocationService.walk.steps));

                        dialog.show();

                        MapsInitializer.initialize(context);


                        mMapView.onCreate(dialog.onSaveInstanceState());
                        mMapView.onResume();

                        mMapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(final GoogleMap googleMap) {
                                googleMap.setMyLocationEnabled(true);
                                if(point!=null) {
                                    CameraPosition myPosition = new CameraPosition.Builder()
                                            .target(point).zoom(17).bearing(90).tilt(30).build();

                                    googleMap.animateCamera(
                                            CameraUpdateFactory.newCameraPosition(myPosition));////your lat lng
                                }
                                googleMap.addPolyline(new PolylineOptions()
                                        .addAll(MyLocationService.loc)
                                        .width(5)
                                        .color(Color.RED));
                            }
                        });


//               Button dialogButton = (Button) dialog.findViewById(R.id.map);
// if button is clicked, close the custom dialog
                        dialogbtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        start = false;
                    } else {
                        timerstart();
                        startbtn.setText("STOP");
                        startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark), PorterDuff.Mode.MULTIPLY);
                        Intent serviceIntent = new Intent(WalkActivity.this, SensorForeground.class);
                        serviceIntent.putExtra("User",user);
                        serviceIntent.setAction("Start");
                        ContextCompat.startForegroundService(WalkActivity.this, serviceIntent);;
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
                        start = true;
                    }
                }
            }
        });



    }

    private void calculate() {




    }

    private void setviews() {
        relativeLayoutprog = (RelativeLayout) findViewById(R.id.relativeprog);
        relativeLayout=(RelativeLayout) findViewById(R.id.relativeLayout);
        tap=(TextView)findViewById(R.id.tap);
        fabbutton=(FloatingActionButton) findViewById(R.id.promobutton) ;
        progressBar=(ProgressBar) findViewById(R.id.progressBar5);
        iconset=(ImageView)findViewById(R.id.iconview);
        calorie=(TextView)findViewById(R.id.calorie);
        distance=(TextView)findViewById(R.id._kms);
        chronometer=(Chronometer) findViewById(R.id.time);
        startbtn=(Button) findViewById(R.id.start);
    }

    private void timerstart() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

    }

    private void timerstop(){
        chronometer.stop();

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

    public void UpdateTextView(final LatLng loc) {
        WalkActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                textView.setText(Val);
                Intent intent = new Intent(WalkActivity.this,SensorForeground.class);
//                intent.putExtra("intent",Val);
                Log.d("location12",MyLocationService.loc.toString());
                distance.setText(String.format("%.2f", MyLocationService.walk.Distance));

               point=loc;
                long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
//               MyLocationService.walk.caculatecalorie(55,elapsedMillis/);
            }
        });
        Toast.makeText(getApplicationContext(),loc.toString(),Toast.LENGTH_SHORT).show();
    }

    public void UpdateSensorTV(int steps) {
        WalkActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                tv_x.setText(Val);
            }
        });
    }






}
