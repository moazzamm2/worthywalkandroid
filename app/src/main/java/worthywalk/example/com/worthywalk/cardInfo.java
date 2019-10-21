package worthywalk.example.com.worthywalk;

import java.io.Serializable;

/**
 * Created by moazzam on 10/14/2018.
 */

public class cardInfo implements Serializable{
    String logo;
    String Name;
    String imgurl;
    String points;
    String deal_id;
    String passcode;
    String Website;
    String Fb;

    Boolean online;

    public cardInfo( String logo,String imgurl,String Name, String points,String deal_id,String passcode,boolean online,String Website,String Fb) {
        this.logo=logo;
        this.Name = Name;
        this.imgurl = imgurl;
        this.points = points;
        this.deal_id=deal_id;
        this.passcode=passcode;
        this.online=online;
        this.Website=Website;
        this.Fb=Fb;
    }


    public cardInfo() {

    }
}
