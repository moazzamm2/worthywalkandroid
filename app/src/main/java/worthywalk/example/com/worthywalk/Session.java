package worthywalk.example.com.worthywalk;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Session {


    double distance;
    double caloriesburnt;
    long timespent;
    List<LatLng> pathCoordinates;
    int Knubs;


    public Session(double distance, double caloriesburnt, long timespent, List<LatLng> pathCoordinates, int knubs) {
        this.distance = distance;
        this.caloriesburnt = caloriesburnt;
        this.timespent = timespent;
        this.pathCoordinates = pathCoordinates;
        Knubs = knubs;

    }

    public Session(){}




}
