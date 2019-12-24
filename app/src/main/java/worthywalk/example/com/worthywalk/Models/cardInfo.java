package worthywalk.example.com.worthywalk.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by moazzam on 10/14/2018.
 */

public class cardInfo implements Serializable{
    public String logo;
    public List<String> location;
    public String imgurl;
    public String points;
    public String deal_id;
    public String Brand_id;
    public String Brand_name;
    public String description;
    public String passcode;



    public Boolean online;

    public cardInfo(List<String> location, String logo,String imgurl,String Name, String points,String deal_id,String passcode,boolean online,String Brandid,String description) {
        this.logo=logo;

        this.imgurl = imgurl;
        this.points = points;
        this.deal_id=deal_id;
        this.passcode=passcode;
        this.online=online;
        this.Brand_name=Name;
        this.Brand_id=Brandid;
        this.description=description;
        this.location=location;
        }


    public cardInfo() {

    }
}
