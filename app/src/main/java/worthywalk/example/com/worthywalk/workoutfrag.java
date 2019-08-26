package worthywalk.example.com.worthywalk;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

public class workoutfrag extends AppCompatActivity {
    int index=0;
    boolean start=false;
    Resources res = getResources();

    int [] icons=  new int[]{R.drawable.ic_runer_silhouette_running_fast,R.drawable.ic_man_cycling,R.drawable.treadmillcircle};
  int [] circle=new int[]{R.drawable.walkcircle,R.drawable.cyclecircle,R.drawable.treadmillcircle};
  Drawable[] progbar=new Drawable[]{ResourcesCompat.getDrawable(res,R.drawable.ic_runer_silhouette_running_fast,null),
          ResourcesCompat.getDrawable(res,R.drawable.ic_man_cycling,null),
          ResourcesCompat.getDrawable(res,R.drawable.ic_treadmill,null)};
  ImageView iconview=new ImageView(this);
  TextView title=new TextView(this);
    TextView pace=new TextView(this);
    Button startbtn;
    LinearLayout linearLayout=new LinearLayout(this);
    ProgressBar walkprogressBar=new ProgressBar(this);
    ProgressBar cycleprogressBar=new ProgressBar(this);
    ProgressBar treadmillprogressBar=new ProgressBar(this);

    RelativeLayout rlprog= new RelativeLayout(this);
RelativeLayout rlcenter=new RelativeLayout(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workoutfrag);
        walkprogressBar=(ProgressBar) findViewById(R.id.walkprog);
        cycleprogressBar=(ProgressBar) findViewById(R.id.cycleprog);
        treadmillprogressBar=(ProgressBar) findViewById(R.id.treadmillprog);
        linearLayout=(LinearLayout) findViewById(R.id.textlayout);
        iconview=(ImageView) findViewById(R.id.iconview);
        startbtn=(Button) findViewById(R.id.start);
        setprogbar();
        if(start){
            iconview.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }else{
            iconview.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(start) start=false;
                else start=true;
            }
        });



    }
    public void setprogbar(){
        if(index==0){
            walkprogressBar.setVisibility(View.VISIBLE);
            cycleprogressBar.setVisibility(View.GONE);
            treadmillprogressBar.setVisibility(View.GONE);
        }else if(index==1){
            walkprogressBar.setVisibility(View.GONE);
            cycleprogressBar.setVisibility(View.VISIBLE);
            treadmillprogressBar.setVisibility(View.GONE);
        }
        if(index==2){
            walkprogressBar.setVisibility(View.GONE);
            cycleprogressBar.setVisibility(View.VISIBLE);
            treadmillprogressBar.setVisibility(View.GONE);
        }
    }
    public void setdata(int index){
   iconview.setImageResource(icons[index]);



    }
    public void OnClick(View view) {

        if(!start){
            if(index>2) index=0;
            index++;
            setdata(index);
            recreate();
        }

    }
}
