package worthywalk.example.com.worthywalk;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Session {


    double distance;
    double caloriesburnt;
    long timespent;

        double discardeddistance;
    int Knubs;


    public Session(double distance, double caloriesburnt, long timespent, int knubs,double calculatediscardeddistance) {
        this.distance = distance;
        this.caloriesburnt = caloriesburnt;
        this.timespent = timespent;
        Knubs = knubs;
        discardeddistance=calculatediscardeddistance;

    }




}
