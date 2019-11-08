package worthywalk.example.com.worthywalk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import org.w3c.dom.Document;

import java.text.DecimalFormat;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class WalkActivity extends AppCompatActivity implements Chronometer.OnChronometerTickListener {
    int [] icons=  new int[]{R.drawable.ic_runer_silhouette_running_fast,R.drawable.ic_man_cycling,R.drawable.ic_treadmill};
    int [] circle=new int[]{R.drawable.walkcircle,R.drawable.cyclecircle,R.drawable.treadmillcircle};
    int [] colors=new int[]{R.color.walk,R.color.cycle,R.color.treadmill};
    Context context;
    public static WalkActivity Instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean multiplyer=false;
    int bonus=1;

    public static final String START = "Start";
    public static final String STOP = "Stop";
    public static final String INDEX = "Index";
    public static final String SESSION_NUMBER = "SessionNumber";
    float distancemon;
    int knubsmon;
    float caloriemon;
    int  stepsmon;
    int invalid=0;
    int dbtotalknubs=0;
    double discardeddistance;

    public static WalkActivity getInstance() {
        return Instance;
    }
    ImageView iconset;
    TextView calorie,distance;
    FirebaseFirestore db;

    int index;

    TextView tap;
    Button startbtn;
    RelativeLayout relativeLayout;
    RelativeLayout relativeLayoutprog;
    ProgressBar progressBar;
    boolean start =false;

    boolean tapped=false,saved;

    StepDetector stepDetector;
    FloatingActionButton fabbutton;
    public static Chronometer chronometer;
    User user=new User();
    LatLng point;
    String  useruid;
    double distanceOnMap=0;

    private boolean isServiceRunning;
    private int indexOfImage;
    FirebaseAuth mauth;
    TextView stepsDialog,knubsDialog;
    TextView caloriesDialog;
    int newknubs;
    TextView distanceDialog;
    TextView discardDialog;
    public static final String MyPREFERENCES = "MyPrefs" ;
    LocationManager lm;
    SharedPreferences sharedpreferences;
    int bool=0;

    Gson gson;
    boolean gps_enabled;

    double discarddistance;
    Session session;
    long dbknubs;


    double newdistance,newdiscarddistance,newcalculatedknubs, newoldknubs;


    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        context=this;
        db=FirebaseFirestore.getInstance();
        useruid=FirebaseAuth.getInstance().getUid();
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = false;
        setviews();


        isServiceRunning = SensorForeground.isServiceRunning;
//        indexOfImage = SensorForeground.index;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         saved=sharedpreferences.getBoolean("Saved",true);
         multiplyer=sharedpreferences.getBoolean("Multiplier",false);
        String userjson=sharedpreferences.getString("User","a");
        gson = new Gson();


        if(!saved){
        Log.d("checking","SAVE ChecK");
           String info= sharedpreferences.getString("Session","");


            session=gson.fromJson(info,Session.class);



        }

        indexOfImage=sharedpreferences.getInt("Index",0)-1;


        if(isServiceRunning){
            start = true;
            tapped = true;

            // Set Button
            startbtn.setText("STOP");
            startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark), PorterDuff.Mode.MULTIPLY);

            // Set Images
            iconset.setBackgroundResource(icons[indexOfImage]);
            relativeLayout.setBackgroundResource(circle[indexOfImage]);
            progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(),colors[indexOfImage]), PorterDuff.Mode.SRC_IN );;
            tap.setVisibility(View.GONE);

            index = indexOfImage;

            chronometer.setBase(SensorForeground.getTime());
            chronometer.start();

            UpdateTextViews();
        }

        Instance = this;
        Intent intent=getIntent();
        user=(User) intent.getSerializableExtra("User");

        if(user==null){
            user=gson.fromJson(userjson,User.class);
        }

        newoldknubs=user.Knubs;

        Permission();
        setbuttons();

        stepDetector=new StepDetector();

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
              final  String x = text.getText().toString().trim();
                if(x.length()>0){
                    Log.d("promocheckx",x);
                    db.collection("Multiplyer").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()){


                                for(QueryDocumentSnapshot doc: task.getResult()){
                                    Log.d("promocheckp",doc.getString("Promo"));

                                    if(doc.getString("Promo").equals(x)){
                                        Log.d("promocheckp",doc.getString("Promo"));

                                     multiplyer=true;

                                    }
                                }
                                if(multiplyer){
                                    Toast.makeText(getApplicationContext(),"PromocodeActivated",Toast.LENGTH_LONG).show();

                                    SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                                    prefsEditor.putBoolean("Multiplier",multiplyer);
                                    prefsEditor.commit();
                                }else{
                                    Toast.makeText(getApplicationContext(),"Invalid Promocode",Toast.LENGTH_LONG).show();


                                }

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
                    Toast.makeText(getApplicationContext(),"Enter Promocode",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();

            }

//



        });

        dialog.show();

    }

});

    }


    private boolean validity(){
        double dist=MyLocationService.getDistance(SensorForeground.loc)-validdiscardeddistance();
        int stepss=SensorForeground.steps;
        double stepdistratio= SensorForeground.steps/dist;
        double diststepsratio=dist/SensorForeground.steps;
        long elapsedMillis = SystemClock.elapsedRealtime() - SensorForeground.getTime();
        double timedistratio= (elapsedMillis/1000)/dist;
        Log.d("Timecheck",String.valueOf(elapsedMillis));

        if(stepdistratio<0.6){
            bonus=1;
        }
        if(stepdistratio>=0.6 && stepdistratio<0.9){
            bonus=2;
        }else if(stepdistratio>=0.9 && stepdistratio<=1.6){
            bonus=3;
        }else if(stepdistratio>1.6){
            bonus=1;
        }






        if(timedistratio<0.5) {
            bonus = 1;


        } if(stepss>15 && dist==0){
            invalid=3;
            return false;
        }

return true;
    }

    private void setbuttons() {

        startbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                isServiceRunning = SensorForeground.isServiceRunning;
                try {
                    gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                } catch(Exception ex) {}

                if(tapped) {





                    if(saved) {


                        if (gps_enabled) {


                            if (start) {





                                calculateknubs();


                                chronometer.stop();
                                UpdateTextViews();

                                startbtn.setText("START");
                                startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark), PorterDuff.Mode.MULTIPLY);
                                final Intent serviceIntent = new Intent(WalkActivity.this, SensorForeground.class);
                                serviceIntent.setAction(STOP);
                                serviceIntent.putExtra(INDEX, index);

                                boolean valid;

                                if(index>1){
                                    valid=true;
                                }else{
                                   valid= validity();
                                }

                                if (valid) {
                                    saveSessionDetails();


                                final Dialog dialog = new Dialog(context);

                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                /////make map clear
                                dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


                                dialog.setContentView(R.layout.finishdilog);////your custom content
                                TextView dialogbtn = dialog.findViewById(R.id.finishworkout);
                                TextView errordist = dialog.findViewById(R.id.errordistance);

                                    stepsDialog = dialog.findViewById(R.id.stepsdialog);
                                caloriesDialog = dialog.findViewById(R.id.coloriedialog);
                                distanceDialog = dialog.findViewById(R.id.distancedialog);
                                discardDialog = dialog.findViewById(R.id.discardeddistance);
                                knubsDialog = dialog.findViewById(R.id.knubsdialog);
                                TextView bonustv=dialog.findViewById(R.id.bonus);



                                    if(bonus==1){
                                        bonustv.setText("Bad Walk");
                                        bonustv.setTextColor(R.color.red);
                                    }else if(bonus==2){
                                        bonustv.setText("Good Walk");
                                        bonustv.setTextColor(R.color.yellow);
                                    }else if(bonus==3){
                                        bonustv.setText("Perfect Walk");
                                        bonustv.setTextColor(R.color.green);
                                    }
                                MapView mMapView = dialog.findViewById(R.id.map);

                                stepsDialog.setText(String.valueOf(SensorForeground.getStepCount()));
                                // Values based on STEPS
                                //caloriesDialog.setText(String.valueOf(SensorForeground.getCaloriesBurnt()));
                                //distanceDialog.setText(df2.format(SensorForeground.getDistance()));

                                // Values based on POINTS on Map
                                distanceOnMap = MyLocationService.getDistance(SensorForeground.loc) - calculatediscardeddistance();
                                double discarddistanced = calculatediscardeddistance();
                                distanceDialog.setText(String.valueOf(distanceOnMap));
                                caloriesDialog.setText(String.valueOf(MyLocationService.getCalories(distanceOnMap)));

                                discardDialog.setText(String.valueOf(discarddistanced));
                                knubsDialog.setText(String.valueOf(newknubs));
                                errordist.setError("These meters were discarded due to the inaccuracy of GPS .");
                                dialog.show();

                                MapsInitializer.initialize(context);

                                mMapView.onCreate(dialog.onSaveInstanceState());
                                mMapView.onResume();
                                try {
                                    point = SensorForeground.loc.get(SensorForeground.loc.size() - 1);
                                } catch (Exception e) {

                                }
                                mMapView.getMapAsync(new OnMapReadyCallback() {
                                    @Override
                                    public void onMapReady(final GoogleMap googleMap) {
                                        googleMap.setMyLocationEnabled(true);
                                        if (point != null) {
                                            CameraPosition myPosition = new CameraPosition.Builder()
                                                    .target(point).zoom(17).bearing(90).tilt(30).build();

                                            googleMap.animateCamera(
                                                    CameraUpdateFactory.newCameraPosition(myPosition));////your lat lng
                                        }
                                        googleMap.addPolyline(new PolylineOptions()
                                                .addAll(SensorForeground.loc)
                                                .width(5)
                                                .color(Color.GREEN));
                                    }
                                });


                                //  Button dialogButton = (Button) dialog.findViewById(R.id.map);
                                // if button is clicked, close the custom dialog
                                dialogbtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        resetSession();

                                        dialog.dismiss();
                                    }
                                });
                                ContextCompat.startForegroundService(WalkActivity.this, serviceIntent);
                                fusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
                                start = false;

                            }else{
                                    ContextCompat.startForegroundService(WalkActivity.this, serviceIntent);

                                    fusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
                                    start = false;
                                    String message="";
                                    if(invalid==1){
                                        message="Travelling on a vehicle, Worthy walk doesn't count this as a walk !";
                                    }else   if(invalid==2){
                                        message="Either your Gps is not helping or you moving in Car! Just take the screenshot of this screen and message us on FB page if this was a mistake , untill we make things better. Sorry for inconviniance";
                                    }else if(invalid==3){
                                        message="Shaking your hand is not a good option to earn Knubs, Go out for a walk cause its Worthy";

                                    }
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(WalkActivity.this);
                                    alertDialogBuilder.setTitle("Invalid Walk !");
                                    alertDialogBuilder.setMessage(message);
                                    alertDialogBuilder.setPositiveButton("Ok",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface arg0, int arg1) {


                                                    resetSession();

                                                }
                                            });




                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();


                                }





                            } else {
                                if (!isServiceRunning) {

                                    SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                                    prefsEditor.putInt("Index",index);
                                    prefsEditor.commit();

                                    startbtn.setText("STOP");
                                    startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark), PorterDuff.Mode.MULTIPLY);
                                    Intent serviceIntent = new Intent(WalkActivity.this, SensorForeground.class);
                                    serviceIntent.putExtra("User", user);
                                    serviceIntent.setAction(START);
                                    ContextCompat.startForegroundService(WalkActivity.this, serviceIntent);
                                    fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
                                    start = true;

                                    // This give time to other things to initialize first
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            chronometer.setBase(SensorForeground.getTime());
                                            chronometer.setOnChronometerTickListener(WalkActivity.this);
                                            chronometer.start();
                                            startNewSession();
                                            UpdateTextViews();
                                        }
                                    }, 100);
                                }
                            }


                        } else {
                            Toast.makeText(context, "Open up your locations ! Before starting your activity", Toast.LENGTH_LONG).show();
                        }
                    }
                    else if(!saved) {
                       AlertDialog.Builder builder = new AlertDialog.Builder(context);


                                //Uncomment the below code to Set the message and title from the strings.xml file
                                builder.setMessage("Session not saved please save it before starting a new one") .setTitle("Session not Saved")
                                        .setCancelable(false)
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                saveSessionDetails();

                                            }
                                        })
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();
                                                Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                //Creating dialog box
                                AlertDialog alert = builder.create();
                                //Setting the title manually

                                alert.show();


                    }
                }
            }
        });
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
//        testingDistance = findViewById(R.id.textViewTesting);
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

    private double validdiscardeddistance(){
        double TotalDistance=0;
        List<LatLng> points=new ArrayList<LatLng>();
        points = SensorForeground.loc;
        if(points.size()>1){

            float[] results = new float[1];
            for(int i = 0 ; i < points.size()-2 ; i++) {

                Location.distanceBetween(
                        points.get(i).latitude,
                        points.get(i).longitude,
                        points.get(i + 1).latitude,
                        points.get(i + 1).longitude,
                        results
                );


                    if (results[0] > 15 && results[0]<0)         TotalDistance += results[0];

                }

            }

            return TotalDistance;
        }
    private double calculatediscardeddistance(){
        double TotalDistance=0;
        List<LatLng> points=new ArrayList<LatLng>();
      points = SensorForeground.loc;
        if(points.size()>1){

            float[] results = new float[1];
            for(int i = 0 ; i < points.size()-2 ; i++) {

                Location.distanceBetween(
                        points.get(i).latitude,
                        points.get(i).longitude,
                        points.get(i + 1).latitude,
                        points.get(i + 1).longitude,
                        results
                );
                if(indexOfImage==0){

                    if (results[0] > 10 && results[0]<0)         TotalDistance += results[0];
                } else if(indexOfImage==1){
                    if (results[0] > 20 && results[0]<0)         TotalDistance += results[0];

else {
                        if (results[0] > 5 && results[0]<0)         TotalDistance += results[0];

                    }
                }
            }
      discarddistance=TotalDistance;

        }
        // Divide TotalDistance by 2 to get the accurate result
        return Double.parseDouble(df2.format(discarddistance/2));




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





    public void UpdateSensorTV(int steps) {
        WalkActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {

//                tv_x.setText(Val);
            }
        });
    }
    public void calculateknubs(){

            if(indexOfImage>1){
                double steps= SensorForeground.steps*0.03;
                newknubs=(int) Math.round(steps);

            }else{
                double steps=0;
                double cal=0;
                double totaldistance=0;
                if(bonus==1){
                     steps= SensorForeground.steps*0.03;
                     cal= SensorForeground.getCaloriesBurnt()*1.2;
                     totaldistance=distanceOnMap*0.05;

                }else if(bonus==2){
                    steps= SensorForeground.steps*0.04;
                    cal= SensorForeground.getCaloriesBurnt()*1.3;
                    totaldistance=distanceOnMap*0.05;

                }else if(bonus==3){
                    steps= SensorForeground.steps*0.05;
                    cal= SensorForeground.getCaloriesBurnt()*1.5;
                    totaldistance=distanceOnMap*0.06;

                }

                if(newknubs<0) newknubs=0;
                newknubs=(int) Math.round(steps+cal+totaldistance);

            }

    }
    public void UpdateTextViews(){

        // Values based on POINTS on Map
        double distanceOnMap = MyLocationService.getDistance(SensorForeground.loc);
        distance.setText(String.valueOf(distanceOnMap));
        calorie.setText(String.valueOf(MyLocationService.getCalories(distanceOnMap)));

        // Values based on STEPS
        //distance.setText(df2.format(SensorForeground.getDistance()));
        //calorie.setText(String.valueOf(SensorForeground.getCaloriesBurnt()));
    }

    private void resetSession(){
        SensorForeground.steps = 0;
        SensorForeground.index = 0;
        SensorForeground.chronometer.stop();
        SensorForeground.loc.clear();
    }

    private void saveSessionDetails(){
        final double distanceCovered;
        final double caloriesBurnt;
        final long timeSpent;
        final List<LatLng> pathCoordinates;
        final int knubs;

        if(!saved){
           distanceCovered = session.distance;
            caloriesBurnt = session.caloriesburnt;
            timeSpent = session.timespent;
            knubs=session.Knubs;
            pathCoordinates = session.pathCoordinates;

        }else {

             distanceCovered = Double.parseDouble(df2.format(MyLocationService.getDistance(SensorForeground.loc)-calculatediscardeddistance()));
             caloriesBurnt = SensorForeground.getCaloriesBurnt();
             timeSpent = SensorForeground.getTime();
             pathCoordinates = SensorForeground.loc;
             knubs=newknubs;

        }

        final DocumentReference docRef=db.collection("Users").document(useruid);
        final DocumentReference docRef2=db.collection("Session").document();
        final DocumentReference docRef3=db.collection("Monthlywalk").document(useruid);


        final Map<String, Object> docData = new HashMap<>();

        docData.put("DistanceCovered",distanceCovered);
        docData.put("CalorieBurnt",caloriesBurnt);
        docData.put("Timespent",timeSpent);
        docData.put("PathCordinates",pathCoordinates);
        docData.put("KnubsEarned",knubs);
        docData.put("UserId",useruid);


         final Map<String, Object> docData3 = new HashMap<>();
         final SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
          saveinpreferences(distanceCovered,caloriesBurnt);

          knubsmon=sharedpreferences.getInt("Totalknubs",0);
        distancemon=sharedpreferences.getFloat("Totaldistance",0);
          stepsmon=sharedpreferences.getInt("Totalsteps",0);
            caloriemon=sharedpreferences.getFloat("Totalcalorie",0);


    db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                DocumentSnapshot snapshot = transaction.get(docRef);
                DocumentSnapshot snapshot2 = transaction.get(docRef3);


                double distancemontemp=snapshot2.getDouble("Totaldistance");
                double caloriemontemp=snapshot2.getDouble("Totalcalorie");
                long knubsmontemp=snapshot2.getLong("Totalknubs");
                long stepsmontemp=snapshot2.getLong("Totalsteps");




                knubsmon=(int)knubsmontemp+knubs;
                distancemon=(float)distancemontemp+(float)distanceCovered;
                caloriemon=(float)caloriemontemp+(float)caloriesBurnt;
                stepsmon=(int)stepsmontemp+SensorForeground.steps;
                docData3.put("Totalcalorie",caloriemon);
                docData3.put("Totaldistance",distancemon);
                docData3.put("Totalsteps",stepsmon);
                docData3.put("Totalknubs" ,knubsmon);
                Log.d("totalknubs",String.valueOf(dbtotalknubs));

                     dbtotalknubs=Integer.parseInt(String.valueOf(snapshot.get("Totalknubs")))+knubs;

                   dbknubs=snapshot.getLong("Knubs")+knubs;
                    transaction.update(docRef, "Knubs",dbknubs);
                    transaction.update(docRef,"Totalknubs",dbtotalknubs);

                    transaction.set(docRef2,docData );
                    transaction.update(docRef3,docData3);




                return null;

            }
        }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {


             session=new Session(distanceCovered,caloriesBurnt,timeSpent,pathCoordinates,knubs);
             Gson gson=new Gson();
             Log.d("Kiyascn",e.getMessage());
             String  info=gson.toJson(session);
             saved=false;
             prefsEditor.putString("Session",info);
             prefsEditor.putBoolean("Saved",false);


             prefsEditor.commit();

             Toast.makeText(getApplicationContext(),e.getMessage(),

                     Toast.LENGTH_SHORT).show();
         }
     }).addOnSuccessListener(new OnSuccessListener() {
         @Override
         public void onSuccess(Object o) {


            user.Knubs=(int)dbknubs;
            user.totalknubs=dbtotalknubs;
             MainActivity.updateuserthroughactivity(user);
             String userjson=gson.toJson(user);
             SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
             prefsEditor.putString("User",userjson);




             saved=true;
                 prefsEditor.putBoolean("Saved", true);
                 prefsEditor.putFloat("Totaldistance", distancemon);
                 prefsEditor.putFloat("Totalcalorie", caloriemon);
                 prefsEditor.putInt("Totalknubs", knubsmon);
                 prefsEditor.putInt("Totalsteps", stepsmon);
                 prefsEditor.commit();
             Toast.makeText(getApplicationContext(),"Session Saved Succesfully",
                     Toast.LENGTH_SHORT).show();
             sharedpreferences.edit().remove("Multiplier").commit();
             sharedpreferences.edit().remove("Session").commit();


         }
     });


        //TODO Save above detail in database

    }

    private void saveinpreferences(double distanceCovered,double caloriesBurnt) {

        distancemon=sharedpreferences.getFloat("Totaldistance",0);
        knubsmon=sharedpreferences.getInt("Totalknubs",0);
        caloriemon=sharedpreferences.getFloat("Totalcalorie",0);
        stepsmon=sharedpreferences.getInt("Totalsteps",0);

        knubsmon=knubsmon+newknubs;
        distancemon=distancemon+(float)distanceCovered;
        caloriemon=caloriemon+(float)caloriesBurnt;
        stepsmon=stepsmon+SensorForeground.steps;

    }

    private void startNewSession(){


        distancemon=sharedpreferences.getFloat("Totaldistance",0);
        knubsmon=sharedpreferences.getInt("Totalknubs",0);
        caloriemon=sharedpreferences.getFloat("Totalcalorie",0);
        stepsmon=sharedpreferences.getInt("Totalsteps",0);




        resetSession();
    }


    @Override
    public void onChronometerTick(Chronometer chronometer) {


        //This will update the TextViews every second
        UpdateTextViews();
    }

}
