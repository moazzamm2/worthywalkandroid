package worthywalk.example.com.worthywalk;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Session {


    public int bon;
    public int index;
    public double Boost;
    double distance;
    double caloriesburnt;
    long timespent;
    int steps;

        double discardeddistance;
    int Knubs;


    public Session(double boost,int index,int bon,int steps,double distance, double caloriesburnt, long timespent, int knubs,double calculatediscardeddistance) {

        this.Boost=boost;
        this.bon=bon;
        this.index=index;

        this.distance = distance;
        this.steps=steps;
        this.caloriesburnt = caloriesburnt;
        this.timespent = timespent;
        Knubs = knubs;
        discardeddistance=calculatediscardeddistance;

    }




}
