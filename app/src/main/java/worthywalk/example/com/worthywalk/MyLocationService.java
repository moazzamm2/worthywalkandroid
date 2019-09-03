package worthywalk.example.com.worthywalk;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.IBinder;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.type.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MyLocationService extends BroadcastReceiver {
   public List<LatLng> loc=new ArrayList<LatLng>();

    public static final String ACTION_PROCESS_UPDATE = "com.example.forgroundapp.UPDATELOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    Location location = result.getLastLocation();


                    LatLng loc2=neLatLng(location.getLatitude(),location.getLongitude()));
                    String location_string = new StringBuilder("" + location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();
                    try {
                        WalkActivity.getInstance().UpdateTextView(location_string);
                    } catch (Exception ex) {
                        Toast.makeText(context, location_string, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }
}