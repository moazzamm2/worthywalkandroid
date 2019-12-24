package worthywalk.example.com.worthywalk;

import java.util.Date;

public class testuser {

    public String Firstname;
    public String Lastname;
    public String Phone;
    public String Gender;
    public float Height;
    public float Weight;
    public int Age;
    public int Dob;
    public int Knubs;
    public String  imageurl;

    public int totalknubs;
    public boolean permission;
    public String token;

    public testuser(String imageurl, String lastname, String token, String gender, double weight,String firstname,int totalknubs,int Dob,String phone, int knubs,boolean permission, double height, int age) {
        Firstname = firstname;
        Lastname = lastname;
        Phone = phone;
        Gender = gender;
        Height = (float) height;
        Weight = (float) weight;
        Age = age;
        this.Dob = Dob;
        Knubs = knubs;
        this.imageurl=imageurl;
        this.totalknubs=totalknubs;
        this.permission=permission;
        this.token=token;
    }


}
