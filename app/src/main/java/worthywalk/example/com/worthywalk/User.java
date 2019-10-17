package worthywalk.example.com.worthywalk;
import java.io.Serializable;
import java.util.Date;

public class User implements Serializable {

//    Gson gson;
    public String Firstname;
    public String Lastname;
    public String Phone;
    public String Gender;
    public float Height;
    public float Weight;
    public int Age;
    public Date Dob;
    public int Knubs;
    String  imageurl;
<<<<<<< HEAD


    public User(String firstname, String lastname, String phone, String gender, float height, float weight, int age, Date dob, int knubs,String  imageurl) {
=======
    int totalknubs;
    boolean permission;
    String token;


    public User(String firstname, String lastname, String phone, String gender, float height, float weight, int age, Date dob, int knubs,String  imageurl,int totalknubs,boolean permission,String token) {
>>>>>>> 154b1189317702729c2efc3a5975026cb8c951bc
        Firstname = firstname;
        Lastname = lastname;
        Phone = phone;
        Gender = gender;
        Height = height;
        Weight = weight;
        Age = age;
        Dob = dob;
        Knubs = knubs;
        this.imageurl=imageurl;
<<<<<<< HEAD

=======
        this.totalknubs=totalknubs;
        this.permission=permission;
        this.token=token;
>>>>>>> 154b1189317702729c2efc3a5975026cb8c951bc
    }





    public User(){};




// public User(String json){
//        User user=gson.fromJson(json, User.class);
//        this.firstname=user.firstname;
//        this.lastname=user.lastname;
//        this.phone=user.phone;
//        this.age=user.age;
//        this.gender=user.gender;
//        this.Dob=user.Dob;
//        this.height=user.height;
//        this.weight=user.weight;
//        this.knubs=user.knubs;
//
//
//
//    }

}
