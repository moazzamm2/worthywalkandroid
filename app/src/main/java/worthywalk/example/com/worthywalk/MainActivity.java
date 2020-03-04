package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import worthywalk.example.com.worthywalk.Fragments.Leaderboardfrag;
import worthywalk.example.com.worthywalk.Fragments.homeFragment;
import worthywalk.example.com.worthywalk.Fragments.storefrag;
import worthywalk.example.com.worthywalk.Models.User;
import worthywalk.example.com.worthywalk.Utilities.Firebasedb;

public class MainActivity extends AppCompatActivity implements storefrag.Updateuser {
    ArrayList<String> mresources=new ArrayList<>();
    public static final String MyPREFERENCES = "MyPrefs" ;
    String[] arr;
    Firebasedb firebasedb;
    static SharedPreferences sharedpreferences;
boolean start=false;
    //    private PermissionsManager permissionsManager;
//    private LocationEngine locationEngine;
//    private MapboxMap mapboxMap;
//    private Location originlocation;
//    private long DEFAULT_INTERVAL_IN_MILLISECONDS = 1000L;
//    private long DEFAULT_MAX_WAIT_TIME = DEFAULT_INTERVAL_IN_MILLISECONDS * 5;
    // Variables needed to listen to location updates
    // Variables needed to listen to location updates

    // Variables needed to add the location engine
    // Variables needed to listen to location update

    // Variables needed to listen to location updates
//    private MainActivityLocationCallback callback = new MainActivityLocationCallback(this);
LoginManager loginManager;
    Avail avail=new Avail();
    private Toolbar toolbar;
    private TextView mTextMessage;
    private TextView title;
    String screen="Workout";
    static  Gson gson;
    String[] array;
    ArrayList<String> mResources=new ArrayList<>();
    public static User usermain=new User();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
          if(!start) {
              screen="Dash Board";
              Fragment selectedfragment = new homeFragment(usermain);


              switch (item.getItemId()) {
                  case R.id.navigation_home:
                      screen = "Workout";
                      selectedfragment = new Leaderboardfrag(usermain);
                      break;
                  case R.id.navigation_dashboard:
                      screen = "Dash Board";
                      selectedfragment = new homeFragment(usermain);
                      break;
                  case R.id.navigation_notifications:
                      screen = "Store";
                      selectedfragment = new storefrag(usermain, mResources);
                      break;

              }

              getSupportFragmentManager().beginTransaction().replace(R.id.fragment, selectedfragment).commit();
          }
            return true;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        Mapbox.getInstance(this, "pk.eyJ1IjoibW9henphbW0yIiwiYSI6ImNqeTRud211cTFjZzgzYmxld2h1aTV0NXMifQ.mrwqGXzKmlTWKgouCjOG0A");
        super.onCreate(savedInstanceState);

        gson=new Gson();
        Intent intent=getIntent();
        try {
            usermain= (User) intent.getExtras().getSerializable("User");

        }catch (Error r){
            Toast.makeText(getApplicationContext(),"Login Error !",Toast.LENGTH_LONG).show();

        }


        firebasedb=new Firebasedb();
        mResources=firebasedb.getstoreads();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new homeFragment(usermain)).commit();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        FirebaseMessaging.getInstance().subscribeToTopic("Worthywalk")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed";
                        if (!task.isSuccessful()) {
                            msg ="Subscription failed";
                        }
                        Log.d("FCM", msg);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });



        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }


    void checkstart(){

}

    void validlogin(){




    }

    @Override
    public void updateuser(User user) {
        this.usermain=user;
        String userjson=gson.toJson(user);
        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
        prefsEditor.putString("User",userjson);
        prefsEditor.commit();
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment,new storefrag(user,mResources)).commit();
    }
    public static void updateuserthroughactivity(User user) {
        usermain=user;
        String userjson=gson.toJson(user);
        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
        prefsEditor.putString("User",userjson);
        prefsEditor.commit();
    }


//    @Override
//    public void userupdate(User user) {
//        this.user=user;
//
//    }

//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    @Override
//    public void onExplanationNeeded(List<String> permissionsToExplain) {
//        Toast.makeText(this, R.string.user_location_permission_explanation, Toast.LENGTH_LONG).show();
//
//    }
//
//    @Override
//    public void onPermissionResult(boolean granted) {
//        if (granted) {
//            if (mapboxMap.getStyle() != null) {
//                        initLocationEngine();
//            }
//        } else {
//            Toast.makeText(this, R.string.user_location_permission_not_granted, Toast.LENGTH_LONG).show();
//
//        }
//
//    }
//    @SuppressLint("MissingPermission")
//    private void initLocationEngine() {
//        locationEngine = LocationEngineProvider.getBestLocationEngine(this);
//
//        LocationEngineRequest request = new LocationEngineRequest.Builder(DEFAULT_INTERVAL_IN_MILLISECONDS)
//                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
//                .setMaxWaitTime(DEFAULT_MAX_WAIT_TIME).build();
//
//        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
//        locationEngine.getLastLocation(callback);
//
//    }
//
//
//    private static class MainActivityLocationCallback
//            implements LocationEngineCallback<LocationEngineResult> {
//
//        private final WeakReference<MainActivity> activityWeakReference;
//
//        MainActivityLocationCallback(MainActivity activity) {
//            this.activityWeakReference = new WeakReference<>(activity);
//        }
//
//        /**
//         * The LocationEngineCallback interface's method which fires when the device's location has changed.
//         *
//         * @param result the LocationEngineResult object which has the last known location within it.
//         */
//        @SuppressLint("StringFormatInvalid")
//        @Override
//        public void onSuccess(LocationEngineResult result) {
//            MainActivity activity = activityWeakReference.get();
//
//            if (activity != null) {
//                Location location = result.getLastLocation();
//                activity.routeCoordinates.add(Point.fromLngLat(result.getLastLocation().getLongitude(),result.getLastLocation().getLatitude()));
//                if (location == null) {
//                    return;
//                }
//
//// Create a Toast which displays the new location's coordinates
//                Toast.makeText(activity, String.format(activity.getString(R.string.new_location),
//                        String.valueOf(result.getLastLocation().getLatitude()), String.valueOf(result.getLastLocation().getLongitude())),
//                        Toast.LENGTH_SHORT).show();
//
//// Pass the new location to the Maps SDK's LocationComponent
//                if (activity.mapboxMap != null && result.getLastLocation() != null) {
//                    activity.mapboxMap.getLocationComponent().forceLocationUpdate(result.getLastLocation());
//                }
//            }
//        }
//
//        /**
//         * The LocationEngineCallback interface's method which fires when the device's location can not be captured
//         *
//         * @param exception the exception message
//         */
//        @Override
//        public void onFailure(@NonNull Exception exception) {
//            Log.d("LocationChangeActivity", exception.getLocalizedMessage());
//            MainActivity activity = activityWeakReference.get();
//            if (activity != null) {
//                Toast.makeText(activity, exception.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
//            }
//        }
    }











