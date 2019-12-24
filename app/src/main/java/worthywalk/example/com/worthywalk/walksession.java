package worthywalk.example.com.worthywalk;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import worthywalk.example.com.worthywalk.Models.User;

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
    public void caculatecalorie(User user, double speed){

        Calorie = (0.035 * user.Weight) + (speed / user.Height) *0.029*(user.Weight);


    }

    public void setDistance(double kms) {
        this.Distance=kms;
    }

    public void setdiscardDistance(double totalDistance) {
        this.discardeddistance+=totalDistance;
    }
}

