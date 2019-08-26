package worthywalk.example.com.worthywalk;

import java.io.Serializable;

/**
 * Created by moazzam on 10/14/2018.
 */

public class cardInfo implements Serializable{
    String logo;
    String resturant;
    String imgurl;
    String points;
    String deal_id;

    public cardInfo( String logo,String imgurl,String resturant, String points,String deal_id) {
        this.logo=logo;
        this.resturant = resturant;
        this.imgurl = imgurl;
        this.points = points;
        this.deal_id=deal_id;
    }


}
