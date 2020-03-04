package worthywalk.example.com.worthywalk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import worthywalk.example.com.worthywalk.Models.Promocode;
import worthywalk.example.com.worthywalk.Models.User;

public class WalkActivity extends AppCompatActivity implements Chronometer.OnChronometerTickListener {
    int [] icons=  new int[]{R.drawable.ic_runer_silhouette_running_fast,R.drawable.ic_man_cycling};
    int [] circle=new int[]{R.drawable.walkcircle,R.drawable.cyclecircle};
    int [] colors=new int[]{R.color.walk,R.color.cycle};
    Context context;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public static WalkActivity Instance;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;
    boolean multiplyer=false;
    List<String> promocodes=new ArrayList<>();

    int bonus=0;
    boolean validpromo=false;

    boolean gpspermission=false;

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
    private TextView BonusDialog;

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
    ProgressBar progressBar , pbloading;
    boolean start =false;

    boolean tapped=false,saved;

    StepDetector stepDetector;
    FloatingActionButton fabbutton;
    public static Chronometer chronometer;
    User user=new User();
    LatLng point;
    String  useruid;
    double distanceOnMap=0;
    Map<Integer,String> month=new HashMap();
    String str;
    List<String> userpromos=new ArrayList<>();


    private boolean isServiceRunning;
    int indexOfImage;
    FirebaseAuth mauth;
    TextView stepsDialog,knubsDialog;
    TextView caloriesDialog;
    int newknubs;
    TextView distanceDialog;
    TextView discardDialog;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    LocationManager lm;
    int bool=0;

    Promocode promoObject;
    Gson gson;
    boolean gps_enabled;

    boolean promoactivated=false;
    double discarddistance;
    Session session;

    long dbknubs;


    double newdistance,newdiscarddistance,newcalculatedknubs, newoldknubs;
    List<LatLng> pathcoordinates;

    private static DecimalFormat df2 = new DecimalFormat("#.##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);
        context=this;
        pbloading=(ProgressBar) findViewById(R.id.pbLoading);
        db=FirebaseFirestore.getInstance();
        useruid=FirebaseAuth.getInstance().getUid();
        lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        gps_enabled = false;
        setviews();
        final Promocode promocode;
        month.put(1,"January");
        month.put(2,"Feburary");
        month.put(3,"March");
        month.put(4,"April");
        month.put(5,"May");
        month.put(6,"June");
        month.put(7,"July");
        month.put(8,"August");
        month.put(9,"September");
        month.put(10,"October");
        month.put(11,"November");
        month.put(12,"December");
        Calendar calendar = new GregorianCalendar(2008, 01, 01);
        calendar=Calendar.getInstance();
        int mon=calendar.get(Calendar.MONTH);
        str= month.get(mon+1)+calendar.get(Calendar.YEAR);
        pathcoordinates=new ArrayList<>();


        gson = new Gson();


        isServiceRunning = SensorForeground.isServiceRunning;
//        indexOfImage = SensorForeground.index;
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         saved=sharedpreferences.getBoolean("Saved",true);
        promoactivated=sharedpreferences.getBoolean("Promocode",false);
        String promojson=sharedpreferences.getString("promoClass","a");
        if(promoactivated && !promojson.equals("a")){
            promoObject=new Promocode();
            promoObject=gson.fromJson(promojson,Promocode.class);

        }


        multiplyer=sharedpreferences.getBoolean("Multiplier",false);
        String userjson=sharedpreferences.getString("User","a");


        if(!saved){
        Log.d("checking","SAVE ChecK");
           String info= sharedpreferences.getString("Session","");

           checksaveddata();
            session=gson.fromJson(info,Session.class);




        }

        indexOfImage=sharedpreferences.getInt("Index",0)-1;
        Log.d("indexOFImage", String.valueOf(indexOfImage));


        if(isServiceRunning){
            start = true;
            tapped = true;

            // Set Button
            startbtn.setText("STOP");
            startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_red_dark), PorterDuff.Mode.MULTIPLY);

            // Set Images
          if(indexOfImage<0) {
              indexOfImage++;

              iconset.setBackgroundResource(icons[indexOfImage]);

              relativeLayout.setBackgroundResource(circle[indexOfImage]);
              progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), colors[indexOfImage]), PorterDuff.Mode.SRC_IN);
              ;

              tap.setVisibility(View.GONE);

              index = indexOfImage;
          }else{
              iconset.setBackgroundResource(icons[indexOfImage]);

              relativeLayout.setBackgroundResource(circle[indexOfImage]);
              progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(), colors[indexOfImage]), PorterDuff.Mode.SRC_IN);
              ;

              tap.setVisibility(View.GONE);

              index = indexOfImage;

          }

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
//        requestgpspermission();
        Permission();
        setbuttons();

        stepDetector=new StepDetector();

     relativeLayoutprog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!start){
                    tap.setVisibility(View.GONE);
                    tapped=true;
                    index= index%2;
                    iconset.setBackgroundResource(icons[index]);
                    relativeLayout.setBackgroundResource(circle[index]);
                    progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(getApplicationContext(),colors[index]), PorterDuff.Mode.SRC_IN );
                    indexOfImage=index;
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

                    promocodecheck(x);
//                   if(promocodecheck(x)) ActivatePromo(x);
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

    public void calculateknubs(){
        float calories= (float) MyLocationService.getCalories(distanceOnMap);

        if(index<0) index++;
        double steps=0;
        double cal=0;
        double totaldistance=0;
        if(indexOfImage==1) {
            steps = SensorForeground.steps * 0.04;
            cal = calories * 0.06;
            totaldistance = distanceOnMap * 0.04;

        }else {
            if (bonus == 1) {
                steps = SensorForeground.steps * 0.01;
                cal = calories * 0.2;
                totaldistance = distanceOnMap * 0.05;

            } else if (bonus == 2) {
                steps = SensorForeground.steps * 0.04;
                cal = calories * 0.3;
                totaldistance = distanceOnMap * 0.06;

            } else if (bonus == 3) {
                steps = SensorForeground.steps * 0.05;
                cal = calories * 0.7;
                totaldistance = distanceOnMap * 0.06;

            } else if (bonus == 4) {
                steps = SensorForeground.steps * 0.05;
                cal = calories * 0.2;
                totaldistance = distanceOnMap * 0.02;
            } else {
                steps = SensorForeground.steps * 0.04;
                cal = calories * 0.6;
                totaldistance = distanceOnMap * 0.02;
            }


        }

        if(promoactivated && promoObject!=null)        newknubs = (int) Math.round((steps + cal + totaldistance)*promoObject.Boost) ;

        else   newknubs = (int) Math.round(steps + cal + totaldistance);
        if (newknubs < 0) newknubs = 0;

    }
    private void ActivatePromo(final String promo){
        final SharedPreferences.Editor prefsEditor = sharedpreferences.edit();



        final DocumentReference docRef=db.collection("Users").document(useruid);
        if(!promoObject.Reuseable) {
            db.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                    if (userpromos != null) {
                        userpromos.add(promo);
                    }

                    final Map<String, Object> promoarray = new HashMap<>();
                    promoarray.put("Promocodes", userpromos);

                    transaction.update(docRef, promoarray);


                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    String promojson = gson.toJson(promoObject);
                    promoactivated=true;
                    prefsEditor.putString("promoClass", promojson);
                    prefsEditor.putBoolean("Promocode", true);
                    prefsEditor.commit();
                    Toast.makeText(getApplicationContext(), "Promo Activated", Toast.LENGTH_LONG).show();

                    Toast.makeText(getApplicationContext(), "Your next walk will be boosted by "+ String.valueOf(promoObject.Boost), Toast.LENGTH_LONG).show();

                    pbloading.setVisibility(View.GONE);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pbloading.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG);
                }
            });

        }else {
            String promojson = gson.toJson(promoObject);
            promoactivated=true;

            prefsEditor.putString("promoClass", promojson);
            prefsEditor.putBoolean("Promocode", true);
            prefsEditor.commit();
            pbloading.setVisibility(View.GONE);

            Toast.makeText(getApplicationContext(), "Your next walk will be boosted by "+ String.valueOf(promoObject.Boost), Toast.LENGTH_LONG).show();

        }
    }

    private boolean promocodecheck(final String promo){

        pbloading.setVisibility(View.VISIBLE);
        db.collection("PromocodeWalk").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                boolean validpro;
                boolean checkpromos=false;

                for (QueryDocumentSnapshot snapshot:queryDocumentSnapshots
                     ) {


                    if(promo.equals(snapshot.getId())){
                    checkpromos=true;
                        promoObject= new Promocode(snapshot.getBoolean("Reuseable"),snapshot.getDouble("Boost"));


                        db.collection("Users").document(useruid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                userpromos= (List<String>) snapshot.get("Promocodes");
                                if(userpromos!=null) {
                                    if (userpromos.contains(promo)) {
                                        Toast.makeText(getApplicationContext(), "You have already used this promo" , Toast.LENGTH_LONG).show();

                                        pbloading.setVisibility(View.GONE);
                                    } else {
                                    //    Toast.makeText(getApplicationContext(), "Valid promo", Toast.LENGTH_LONG).show();

                                        validpromo=true;
                                         ActivatePromo(promo);


                                    }
                                }else{

                                    final DocumentReference documentReference=db.collection("Users").document(useruid);
                                    final Map<String,Object> promoarray=new HashMap<>();
                                    final List<String> userpromo=new ArrayList<>();
                                    userpromo.add("firstpromo");

                                    promoarray.put("Promocodes",userpromo);
                                    db.runTransaction(new Transaction.Function<Void>() {
                                        @Nullable
                                        @Override
                                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                                            transaction.update(documentReference,promoarray);
                                            return null;
                                        }
                                    });
                                    Toast.makeText(getApplicationContext(), "Promo not found", Toast.LENGTH_LONG).show();

                                    validpromo=true;

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }

                        });

                    }
                }
                if(!checkpromos){
                    Toast.makeText(getApplicationContext(), "Promo Code not found", Toast.LENGTH_LONG).show();
                    pbloading.setVisibility(View.GONE);

                }
            }
        });
        Log.d("checkbool", String.valueOf(validpromo));
        return validpromo;
        }



    private boolean validity(){
        double dist=MyLocationService.getDistance(SensorForeground.loc)-validdiscardeddistance();
        int stepss=SensorForeground.steps;
        double stepdistratio= SensorForeground.steps/dist;
        double diststepsratio=dist/SensorForeground.steps;
        long elapsedMillis = SystemClock.elapsedRealtime() - SensorForeground.getTime();
        double timedistratio= (elapsedMillis/1000)/dist;
        Log.d("Timecheck",String.valueOf(elapsedMillis));

        if(stepdistratio>3){
            bonus=1;
        }
        if(stepdistratio>=0.6 && stepdistratio<0.9){
            bonus=2;
        }else if(stepdistratio>=0.9 && stepdistratio<=1.6){
            bonus=3;
        }else if(diststepsratio>3) bonus=4;





//
//        if(timedistratio<0.5) {
//            bonus = 1;
//
//
//        } if(stepss>15 && dist==0){
//            invalid=3;
//            return false;
//        }

return true;
    }

    private void setbuttons() {

        startbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                isServiceRunning = SensorForeground.isServiceRunning;

                if(tapped) {





                    if(saved) {

                        if(ContextCompat.checkSelfPermission(WalkActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {

                            if(  ( ActivityCompat.checkSelfPermission(WalkActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)){
                                UpdateLocation();
                            }
                            try {
                                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                            } catch(Exception ex)
                            {
                                Toast.makeText(getApplicationContext(), ex.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }

                            if (gps_enabled) {


                                if (start) {


                                    distanceOnMap = MyLocationService.getDistance(SensorForeground.loc) - calculatediscardeddistance();

                                    calculateknubs();


                                    chronometer.stop();
                                    UpdateTextViews();

                                    startbtn.setText("START");
                                    startbtn.getBackground().setColorFilter(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_green_dark), PorterDuff.Mode.MULTIPLY);
                                    final Intent serviceIntent = new Intent(WalkActivity.this, SensorForeground.class);
                                    serviceIntent.setAction(STOP);
                                    serviceIntent.putExtra(INDEX, index);

                                    boolean valid;

                                    if (index > 1) {
                                        valid = true;
                                    } else {
                                        valid = validity();
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
                                        BonusDialog=dialog.findViewById(R.id.Bonusdialog);



//
//
//                                    if(bonus==1){
//                                        bonustv.setText("Bad Walk");
//                                        bonustv.setTextColor(R.color.red);
//                                    }else if(bonus==2){
//                                        bonustv.setText("Good Walk");
//                                        bonustv.setTextColor(R.color.yellow);
//                                    }else if(bonus==3){
//                                        bonustv.setText("Perfect Walk");
//                                        bonustv.setTextColor(R.color.green);
//                                    }
                                        MapView mMapView = dialog.findViewById(R.id.map);

                                        stepsDialog.setText(String.valueOf(SensorForeground.getStepCount()));

                                        // Values based on STEPS
                                        //caloriesDialog.setText(String.valueOf(SensorForeground.getCaloriesBurnt()));
                                        //distanceDialog.setText(df2.format(SensorForeground.getDistance()));

                                        // Values based on POINTS on Map

                                        double discarddistanced = calculatediscardeddistance();
                                        distanceDialog.setText(String.valueOf(distanceOnMap));
                                        caloriesDialog.setText(String.valueOf(MyLocationService.getCalories(distanceOnMap)));
                                        discardDialog.setText(String.valueOf(calculatediscardeddistance()));

                                        if(promoactivated && promoObject!=null)
                                             BonusDialog.setText(String.valueOf(promoObject.Boost));
//

//                                discardDialog.setText(String.valueOf(MyLocationService.getDiscardDistance(SensorForeground.loc)));
                                        knubsDialog.setText(String.valueOf(newknubs));
//                                errordist.setError("These meters were discarded due to the inaccuracy of GPS .");
//                                errordist.
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

                                    } else {
                                        ContextCompat.startForegroundService(WalkActivity.this, serviceIntent);

                                        fusedLocationProviderClient.removeLocationUpdates(getPendingIntent());
                                        start = false;
                                        String message = "";
                                        if (invalid == 1) {
                                            message = "Travelling on a vehicle, Worthy walk doesn't count this as a walk !";
                                        } else if (invalid == 2) {
                                            message = "Either your Gps is not helping or you moving in Car! Just take the screenshot of this screen and message us on FB page if this was a mistake , untill we make things better. Sorry for inconviniance";
                                        } else if (invalid == 3) {
                                            message = "Shaking your hand is not a good option to earn Knubs, Go out for a walk cause its Worthy";

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
                                        prefsEditor.putInt("Index", index);
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

                            }

                            else {
                                Toast.makeText(context, "Open up your locations ! Before starting your activity", Toast.LENGTH_LONG).show();
                            }




                        }  else {

                            Permission();

                        }
                    }
                    else if(!saved) {

                        checksaveddata();


                    }
                }
            }
        });
    }

    private void requestgpspermission() {
    if(ActivityCompat.shouldShowRequestPermissionRationale(WalkActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)){

        new AlertDialog.Builder(this).setTitle("Permission Needed")
                .setMessage("Location is compulsaory to track your Activity")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       Permission();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create().show();
        }
    else{

        ActivityCompat.requestPermissions(this,new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION
        );
        }
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


    private  void Permissiontest() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {

                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {

                        if (report.areAllPermissionsGranted()) {
                            UpdateLocation();
                            gpspermission=true;

                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();
                            gpspermission=false;
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                        token.continuePermissionRequest();
                    }

                }).onSameThread().check();
    }
    private void Permission() {
        Dexter.withActivity(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        UpdateLocation();
                        gpspermission=true;
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(getApplicationContext(), "Accept Location", Toast.LENGTH_SHORT).show();
                        gpspermission=false;
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
//                        new AlertDialog.Builder(WalkActivity.this).setTitle("Permission Needed")
//                                .setMessage("Location is compulsaory to track your Activity")
//                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        ActivityCompat.requestPermissions(WalkActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION);
//                                    }
//                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        }).create().show();

                        token.continuePermissionRequest();
                    }

                }).onSameThread().check();

    }

    private double validdiscardeddistance(){
        List<LatLng> points=new ArrayList<LatLng>();        double TotalDistance=0;

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

                    if (results[0] > 7 || results[0]<0)         TotalDistance += results[0];
                } else if(indexOfImage==1){
                    if (results[0] > 20 || results[0]<0)         TotalDistance += results[0];

else {
                        if (results[0] > 5 || results[0]<0)         TotalDistance += results[0];

                    }
                }
            }


        }
        // Divide TotalDistance by 2 to get the accurate result
        return Double.parseDouble(df2.format(TotalDistance/2));




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

    public void UpdateTextViews(){

        // Values based on POINTS on Map
        double distanceOnMap = MyLocationService.getDistance(SensorForeground.loc);
        distance.setText(String.valueOf(distanceOnMap));
        calorie.setText(String.valueOf(MyLocationService.getCalories(distanceOnMap)));
//        discarddistance=calculatediscardeddistance();

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
        final int indexs;
        final double booster;
        final double distanceCovered;
        final double caloriesBurnt;
        final long timeSpent;
        final List<LatLng> pathCoordinates;
        final int knubs;
        final int steps;
        final double discarddist;
        final int bon;

        if(!saved){
            String pathjson=sharedpreferences.getString("Path","");

            Type type = new TypeToken<List<LatLng>>() {}.getType();
            pathcoordinates=gson.fromJson(pathjson, type);
            indexs=session.index;
            distanceCovered = session.distance;
            caloriesBurnt = session.caloriesburnt;
            timeSpent = session.timespent;
            knubs=session.Knubs;
            bon=session.bon;
            steps=session.steps;
            pathCoordinates = pathcoordinates;
            discarddist=session.discardeddistance;

            booster=session.Boost;


        }else {

            indexs=indexOfImage;
             distanceCovered = Double.parseDouble(df2.format(MyLocationService.getDistance(SensorForeground.loc)-calculatediscardeddistance()));
             caloriesBurnt = SensorForeground.getCaloriesBurnt();
             discarddist=calculatediscardeddistance();
             timeSpent = SensorForeground.gettimespent();
             steps=SensorForeground.steps;
             bon=bonus;
             pathCoordinates = SensorForeground.loc;
             knubs=newknubs;
             if(promoactivated)booster=promoObject.Boost;
             else booster=0;
        }
        prefin(booster,indexs,bon,steps,distanceCovered,caloriesBurnt,timeSpent,knubs,discarddist,pathCoordinates);

        final DocumentReference docRef=db.collection("Users").document(useruid);
        final DocumentReference docRef2=db.collection("Session").document();
        final DocumentReference docRef3=db.collection("Monthlywalk").document(useruid);
        final DocumentReference docRef4=db.collection("Monthlywalk").document(useruid).collection(str).document(String.valueOf(Calendar.getInstance().getTime()));



        final Map<String, Object> docData = new HashMap<>();
        Date c = Calendar.getInstance().getTime();

        docData.put("Index",indexs);
        docData.put("DistanceCovered",distanceCovered);
        docData.put("CalorieBurnt",caloriesBurnt);
        docData.put("Timespent",timeSpent);
        docData.put("Bonus",bon);
        docData.put("PathCordinates",pathCoordinates);
        docData.put("DiscardedDistance",discarddist);
        docData.put("KnubsEarned",knubs);
        docData.put("Datetime",c);
        docData.put("Steps",steps);
        docData.put("Boost",booster);


        docData.put("UserId",useruid);


         final Map<String, Object> docData3 = new HashMap<>();
         final SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
          saveinpreferences(distanceCovered,caloriesBurnt);

//          knubsmon=sharedpreferences.getInt("Totalknubs",0);
//        distancemon=sharedpreferences.getFloat("Totaldistance",0);
//          stepsmon=sharedpreferences.getInt("Totalsteps",0);
//            caloriemon=sharedpreferences.getFloat("Totalcalorie",0);


    db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                DocumentSnapshot snapshot = transaction.get(docRef);
//                DocumentSnapshot snapshot2 = transaction.get(docRef3);

//
//                double distancemontemp=snapshot2.getDouble("Totaldistance");
//                double caloriemontemp=snapshot2.getDouble("Totalcalorie");
//                long knubsmontemp=snapshot2.getLong("Totalknubs");
//                long stepsmontemp=snapshot2.getLong("Totalsteps");


//
//                knubsmon=(int)knubsmontemp+knubs;
//                distancemon=(float)distancemontemp+(float)distanceCovered;
//                caloriemon=(float)caloriemontemp+(float)caloriesBurnt;
//                stepsmon=(int)stepsmontemp+SensorForeground.steps;
//                docData3.put("Totalcalorie",caloriemon);
//                docData3.put("Totaldistance",distancemon);
//                docData3.put("Totalsteps",stepsmon);
//                docData3.put("Totalknubs" ,knubsmon);
                Log.d("totalknubs",String.valueOf(dbtotalknubs));

                     dbtotalknubs=Integer.parseInt(String.valueOf(snapshot.get("Totalknubs")))+knubs;

                   dbknubs=snapshot.getLong("Knubs")+knubs;
                    transaction.update(docRef, "Knubs",dbknubs);
                    transaction.update(docRef,"Totalknubs",dbtotalknubs);

                    transaction.set(docRef2,docData );
//                    transaction.update(docRef3,docData3);
                    transaction.set(docRef4,docData );





                return null;

            }
        }).addOnFailureListener(new OnFailureListener() {
         @Override
         public void onFailure(@NonNull Exception e) {

//             pathcoordinates=pathCoordinates;

             session=new Session(booster,indexs,bon,steps,distanceCovered,caloriesBurnt,timeSpent,knubs,discarddist);
             Gson gson=new Gson();
             Log.d("Kiyascn",e.getMessage());
             String  info=gson.toJson(session);
             String pathsjson=gson.toJson(pathCoordinates);
             saved=false;
             prefsEditor.putString("Session",info);
             prefsEditor.putString("Path",pathsjson);
             prefsEditor.putBoolean("Saved",false);


             prefsEditor.commit();

             Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),

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
             prefsEditor.putBoolean("Promocode",false);

             promoactivated=false;
             saved=true;
             prefsEditor.putBoolean("Saved", true);
                 prefsEditor.commit();
             Toast.makeText(getApplicationContext(),"Session Saved Succesfully",
                     Toast.LENGTH_SHORT).show();
             sharedpreferences.edit().remove("Multiplier").commit();
             sharedpreferences.edit().remove("Session").commit();
             sharedpreferences.edit().remove("Path").commit();


         }
     });


        //TODO Save above detail in database

    }

    private void prefin(double booster, int indexs, int bon, int steps, double distanceCovered, double caloriesBurnt, long timeSpent, int knubs, double discarddist, List<LatLng> pathCoordinates){
        session=new Session(booster,indexs,bon,steps,distanceCovered,caloriesBurnt,timeSpent,knubs,discarddist);
        Gson gson=new Gson();
        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();

        String  info=gson.toJson(session);
        String pathsjson=gson.toJson(pathCoordinates);
        saved=false;
        prefsEditor.putString("Session",info);
        prefsEditor.putString("Path",pathsjson);
        prefsEditor.putBoolean("Saved",false);


        prefsEditor.commit();
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

    private void checksaveddata(){
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
                        cleardata();
                        dialog.dismiss();
                               }
                });
        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually

        alert.show();

    }

    private void cleardata(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        //Uncomment the below code to Set the message and title from the strings.xml file
        builder.setMessage("Are you sure you want to remove last session to start new walk ?") .setTitle("Warning")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                        saved=true;
                        prefsEditor.putBoolean("Saved", true);
                        prefsEditor.commit();

                        sharedpreferences.edit().remove("Session").commit();
                        sharedpreferences.edit().remove("Path").commit();

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
