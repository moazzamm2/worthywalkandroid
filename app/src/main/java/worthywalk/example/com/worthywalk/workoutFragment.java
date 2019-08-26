package worthywalk.example.com.worthywalk;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
} factory method to
 * create an instance of this fragment.
 */
public class workoutFragment extends Fragment {
    int index=0;
    boolean start=false;
//    Resources res = getClass().getClassLoader().getResources();

//    Drawable[] progbar=new Drawable[]{ResourcesCompat.getDrawable(res,R.drawable.ic_runer_silhouette_running_fast,null),
//            ResourcesCompat.getDrawable(res,R.drawable.ic_man_cycling,null),
//            ResourcesCompat.getDrawable(res,R.drawable.ic_treadmill,null)};
    ImageView iconview;
    TextView title;
    TextView pace;
    Button startbtn;
    LinearLayout linearLayout;
   Activity activity=(Activity) getActivity();

//    RelativeLayout rlprog;
//    RelativeLayout rlcenter;
public interface MyFragmentCallback{
    public void theMethod();
}
    private MyFragmentCallback callback;
    public void onAttach(Activity activity){
        callback = (MyFragmentCallback) activity;
        super.onAttach(activity);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);
        startbtn = (Button) view.findViewById(R.id.start);
        linearLayout=(LinearLayout) view.findViewById(R.id.textlayout);
        title=(TextView) view.findViewById(R.id.mode);
//
//        if(start){
//            iconview.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.VISIBLE);
//        }else{
//            iconview.setVisibility(View.GONE);
//            linearLayout.setVisibility(View.VISIBLE);
//        }

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),workoutActivity.class);
                startActivity(intent);
                if (start){ start = false;
                    linearLayout.setVisibility(View.VISIBLE);
                    title.setVisibility(View.GONE);


//                    if(activity instanceof MainActivity){
//                       MainActivity myactivity = (MainActivity) activity;
//                        myactivity.checkstart();
//                    }

                }
                else {
                    linearLayout.setVisibility(View.GONE);
                    title.setVisibility(View.VISIBLE);

                    start = true;


                }
                }
        });

return view;
    }


    public void setdata(int index){


    }
    public void OnClick(View view) {

        if(!start){
            if(index>2) index=0;
            index++;
            setdata(index);
        }

    }

}
