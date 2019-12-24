package worthywalk.example.com.worthywalk.Models;

import java.io.Serializable;

public class FBuser implements Serializable {

   public String firstname;
    public String secondname;
    public String email;
    public String imageurl;


    public FBuser(String firstname, String secondname, String email, String imageurl) {
        this.firstname = firstname;
        this.secondname = secondname;
        this.email = email;
        this.imageurl = imageurl;
    }

    public FBuser(){}
}

