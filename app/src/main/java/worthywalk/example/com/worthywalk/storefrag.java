package worthywalk.example.com.worthywalk;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class storefrag extends Fragment  {
    int currentPage = 0;
    List<cardInfo> fooddata = new ArrayList<>();
    List<cardInfo> clothedata = new ArrayList<>();
    List<cardInfo> healthdata = new ArrayList<>();
    List<cardInfo> fundata = new ArrayList<>();
    List<cardInfo> salondata = new ArrayList<>();
    TextView name;
    TextView knubs;
FirebaseFirestore db;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    String[] mresources={"https://cloud.24hours.pk/images/deals/full/85911501156462.jpg",
    "https://cloud.24hours.pk/images/deals/full/54801504780102.jpg",
    "https://www.parhlo.com/wp-content/uploads/2016/06/china-grill.jpeg"
    };
    User user=new User();
    public storefrag(User user) {
        this.user=user;
    }
    Updateuser mlistener;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.store, container, false);
        name=(TextView) rootview.findViewById(R.id.t1name);
        knubs=(TextView) rootview.findViewById(R.id.t1points);
        name.setText(user.Firstname+user.Lastname);

        knubs.setText(String.valueOf(user.Knubs));
      final ViewPager mViewPager = (ViewPager) rootview.findViewById(R.id.pager);
        mlistener=(Updateuser) getActivity();
        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getContext(), mresources);
        loaddata();
        mViewPager.setAdapter(mCustomPagerAdapter);

        final Handler handler = new Handler();
       db=FirebaseFirestore.getInstance();
        final Runnable Update = new Runnable() {
            public void run() {
                int NUM_PAGES=mresources.length;
                if (currentPage == NUM_PAGES-1) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        BottomNavigationView bnv = (BottomNavigationView) rootview.findViewById(R.id.navigationView);
        bnv.setOnNavigationItemSelectedListener(navlistner);
        getChildFragmentManager().beginTransaction().replace(R.id.container, new Dealslist("food",user)).commit();

    return rootview;
    }

    private void loaddata() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference orders = db.collection("Deal");
                db.collection("Deal").whereEqualTo("Category","food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id=doc.getId();
                                fooddata.add(new cardInfo("https://i.pinimg.com/736x/35/39/88/3539889f8d4988d18a53801882e39090.jpg", doc.getString("Image"), doc.getString("Brandid"),  String.valueOf(doc.get("Knubs")), id));
                            }


                        } else {
                            Log.w("loaddata", "Error getting documents.", task.getException());
                        }
                    }


                });
        db.collection("Deal").whereEqualTo("Category","apparel")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id=doc.getId();
                                clothedata.add(new cardInfo("https://i.pinimg.com/736x/35/39/88/3539889f8d4988d18a53801882e39090.jpg", doc.getString("Image"), doc.getString("Brandid"),  String.valueOf(doc.get("Knubs")), id));
                            }


                        } else {
                            Log.w("loaddata", "Error getting documents.", task.getException());
                        }
                    }


                });
        db.collection("Deal").whereEqualTo("Category","salon")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id=doc.getId();
                                salondata.add(new cardInfo("https://i.pinimg.com/736x/35/39/88/3539889f8d4988d18a53801882e39090.jpg", doc.getString("Image"), doc.getString("Brandid"),  String.valueOf(doc.get("Knubs")), id));
                            }


                        } else {
                            Log.w("salondata", "Error getting documents.", task.getException());
                        }
                    }


                });

        db.collection("Deal").whereEqualTo("Category","health")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id=doc.getId();
                                healthdata.add(new cardInfo("https://i.pinimg.com/736x/35/39/88/3539889f8d4988d18a53801882e39090.jpg", doc.getString("Image"), doc.getString("Brandid"),  String.valueOf(doc.get("Knubs")), id));
                            }


                        } else {
                            Log.w("loaddata", "Error getting documents.", task.getException());
                        }
                    }


                });

        db.collection("Deal").whereEqualTo("Category","gym")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id=doc.getId();
                                fundata.add(new cardInfo("https://i.pinimg.com/736x/35/39/88/3539889f8d4988d18a53801882e39090.jpg", doc.getString("Image"), doc.getString("Brandid"),  String.valueOf(doc.get("Knubs")), id));
                            }


                        } else {
                            Log.w("loaddata", "Error getting documents.", task.getException());
                        }
                    }


                });





    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;

                    if (item.getItemId() == R.id.foodtab) {
                        selected = new Dealslist("food",user);
                    } else if (item.getItemId() == R.id.clothingtab) {
                        Bundle b = new Bundle();
                        b.putString("type", "clothing");
                        selected = new Dealslist("apparel",user);
                        ;
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.healthTab) {
                        Bundle b = new Bundle();
                        b.putString("type", "health");
                        selected = new Dealslist("gym",user);
                        ;
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.salonTab) {
                        Bundle b = new Bundle();
                        b.putString("type", "salon");
//                        selected = new food_deals("health")

                        selected=new Dealslist("gym",user);
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.gymtab) {
                        Bundle b = new Bundle();
                        b.putString("type", "gym");
                        selected = new Dealslist("fun",user) ;
                        selected.setArguments(b);
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                    return true;
                }
            };



    interface Updateuser{
        public void updateuser(User user);
    }
}
