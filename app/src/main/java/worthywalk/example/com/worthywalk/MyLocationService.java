package worthywalk.example.com.worthywalk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;


import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.asin;
import static java.lang.Math.sqrt;

public class MyLocationService extends BroadcastReceiver {
    User user=new User();
   static walksession walk=new walksession();
    long oldtime,newtime;

    public static List<LatLng> loc=new ArrayList<LatLng>();
       public static double kms=0;


    public static final String ACTION_PROCESS_UPDATE = "com.example.forgroundapp.UPDATELOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {


        if (intent != null) {
            final String action = intent.getAction();
            user=(User) intent.getSerializableExtra("User");

            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    Location location = result.getLastLocation();
                    loc.add(new LatLng(location.getLatitude(),location.getLongitude()));
                       Long time= System.currentTimeMillis();
                    calculatedistance(loc,time);


                    String location_string = new StringBuilder("" + location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();
                    try {
                        WalkActivity.getInstance().UpdateTextView(loc.get(loc.size()-1));
                    } catch (Exception ex) {
                        Toast.makeText(context, location_string, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }
    public void calculatedistance(List<LatLng> gpsList,long time){
        newtime=time;

        double totalDistance = 0.0;
        long delay=newtime-oldtime;
        if(gpsList.size()>1 ) {
            for (int i = 0; i < gpsList.size()-1; i++) {
                double p = 0.017453292519943295;
                double cosa= Math.cos((gpsList.get(i + 1).latitude - gpsList.get(i).latitude) * p) / 2;
                double cosb= Math.cos(gpsList.get(i).latitude * p);
                double cosc=    Math.cos(gpsList.get(i + 1).latitude * p);
                double cosd=    (1 - Math.cos((gpsList.get(i + 1).longitude - gpsList.get(i).longitude) * p)) / 2;
                double a = 0.5 -cosa +cosb*cosc*cosd;


                double distance = 12742 * asin(sqrt(a));
                totalDistance += distance;
                kms=totalDistance;
                walk.setDistance(kms);
            }


        }

        if(gpsList.size()>1 ) {

            double dist1lat = gpsList.get(gpsList.size() - 2).latitude;
            double dist1long = gpsList.get(gpsList.size() - 2).longitude;
            double dist2lat = gpsList.get(gpsList.size() - 1).latitude;
            double dist2long = gpsList.get(gpsList.size() - 1).longitude;
            double p = 0.017453292519943295;
            double cosa = Math.cos((dist2lat - dist1lat) * p) / 2;
            double cosb = Math.cos(dist1lat * p);
            double cosc = Math.cos(dist2lat * p);
            double cosd = (1 - Math.cos((dist2long - dist1long) * p)) / 2;
            double a = 0.5 - cosa + cosb * cosc * cosd;


            double distance = 12742 * asin(sqrt(a));
            totalDistance += distance;
            if (totalDistance > 0.0124274) walk.setdiscardDistance(totalDistance);

        }

        }
    public void setloc(Location location){


    }
}