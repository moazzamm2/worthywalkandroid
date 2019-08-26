package worthywalk.example.com.worthywalk;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

    Gson gson;
    String firstname;
    String lastname;
    String phone;
    String gender;
    float height;
    float weight;
    int age;
    Date Dob;
    int knubs;


    public User(String firstname, String lastname, String phone, String gender, float height, float weight, int age, Date dob, int knubs) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.gender = gender;
        this.height = height;
        this.weight = weight;
        this.age = age;
        Dob = dob;
        this.knubs = knubs;

    }

   public String getjson(User data){
        String users=gson.toJson(data);
        return users;
   }
 public User(String json){
        User user=gson.fromJson(json, User.class);
        this.firstname=user.firstname;
        this.lastname=user.lastname;
        this.phone=user.phone;
        this.age=user.age;
        this.gender=user.gender;
        this.Dob=user.Dob;
        this.height=user.height;
        this.weight=user.weight;
        this.knubs=user.knubs;



    }

}
