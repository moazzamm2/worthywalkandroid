package worthywalk.example.com.worthywalk;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.Duration;

import static java.lang.Math.asin;
import static java.lang.Math.sqrt;

public class walksession implements Serializable {

    int steps=0;
    int StoppedMilliseconds=0;
    Double Calorie=0.0;
    Double Distance =0.0;
    int Knubs=0;
    List<LatLng> location=new ArrayList<>();
    Double discardeddistance=0.0;



    public walksession(int steps, int time, Double calorie, Double distance, int knubs) {
        this.steps = steps;
        this.StoppedMilliseconds = time;
        Calorie = calorie;
        Distance = distance;
        Knubs = knubs;
    }
    public walksession(){}



    public void setSteps(int steps){
        this.steps=steps;
    }
    public void caculatecalorie(double Weight,double duration){



        Calorie = 6.0 *(Weight) * (duration);


    }

    public void setDistance(double kms) {
        this.Distance=kms;
    }

    public void setdiscardDistance(double totalDistance) {
        this.discardeddistance+=totalDistance;
    }




}

