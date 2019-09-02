package worthywalk.example.com.worthywalk;

import java.io.Serializable;

/**
 * Created by moazzam on 10/14/2018.
 */

public class leaderinfo implements Serializable{
    String name;
    String distance;
    String ids;

    public leaderinfo(String name, String distance, String ids) {
        this.name = name;
        this.distance = distance;
        this.ids = ids;
    }

    public leaderinfo() {

    }
}
