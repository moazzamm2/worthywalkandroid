package worthywalk.example.com.worthywalk;

import android.content.Context;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
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
Context context;
    String[] mresources=new String[1];
    User user=new User();
    public storefrag(User user) {
        this.user=user;
    }
    Updateuser mlistener;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.store, container, false);
          mresources[0]="https://firebasestorage.googleapis.com/v0/b/worthywalk-6b82e.appspot.com/o/Deals%2FEglett%2Feglettbanner.jpg?alt=media&token=c99c4fa3-e660-4825-b5eb-a3304a0252fd";
//        mresources[2]="https://firebasestorage.googleapis.com/v0/b/worthywalk-6b82e.appspot.com/o/storeads%2Fdisc3.jpg?alt=media&token=37983408-e653-48fb-8189-25a64299e914";
//        mresources[0]="https://firebasestorage.googleapis.com/v0/b/worthywalk-6b82e.appspot.com/o/storeads%2Fdisc1.jpg?alt=media&token=a9115b36-3a39-442f-a1f5-618cafe396bf";
//        mresources[1]="https://firebasestorage.googleapis.com/v0/b/worthywalk-6b82e.appspot.com/o/storeads%2Fdisc2.jpg?alt=media&token=47ca47e1-8166-4611-8574-26d8a590a3ed";

        name=(TextView) rootview.findViewById(R.id.t1name);
        knubs=(TextView) rootview.findViewById(R.id.t1points);
        name.setText(user.Firstname+" "+user.Lastname);
        knubs.setText(String.valueOf(user.Knubs));
      final ViewPager mViewPager = (ViewPager) rootview.findViewById(R.id.pager);
        mlistener=(Updateuser) getActivity();

        context=getContext();



        loaddata();

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getContext(), mresources);
        final Handler handler = new Handler();

        final Runnable Update = new Runnable() {
            public void run() {
                int NUM_PAGES = mresources.length;
                if (currentPage == NUM_PAGES) {
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
        mViewPager.setAdapter(mCustomPagerAdapter);

        BottomNavigationView bnv = (BottomNavigationView) rootview.findViewById(R.id.navigationView);
        bnv.setOnNavigationItemSelectedListener(navlistner);
        getChildFragmentManager().beginTransaction().replace(R.id.container, new Dealslist("Fashion",user)).commit();

    return rootview;
    }

    private void getbaners(final ViewPager mViewPager) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //
        db.collection("StoreAds").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                final ArrayList<String> mresources=new ArrayList<>();
                int i = 0;
                for (Iterator<QueryDocumentSnapshot> it = queryDocumentSnapshots.iterator(); it.hasNext(); ) {
                    QueryDocumentSnapshot doc = it.next();

                    mresources.add( doc.getString("Banner"));
                    i++;

                }
                final String[] arr=new String[mresources.size()];
                CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getContext(), mresources.toArray(arr));
                final Handler handler = new Handler();

                final Runnable Update = new Runnable() {
                    public void run() {
                        int NUM_PAGES = arr.length;
                        if (currentPage == NUM_PAGES) {
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
                mViewPager.setAdapter(mCustomPagerAdapter);

//
//

//
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getContext(), mresources);
//                final Handler handler = new Handler();
//
//                final Runnable Update = new Runnable() {
//                    public void run() {
//                        int NUM_PAGES = mresources.length;
//                        if (currentPage == NUM_PAGES) {
//                            currentPage = 0;
//                        }
//                        mViewPager.setCurrentItem(currentPage++, true);
//                    }
//                };
//
//                timer = new Timer(); // This will create a new Thread
//                timer.schedule(new TimerTask() { // task to be scheduled
//                    @Override
//                    public void run() {
//                        handler.post(Update);
//                    }
//                }, DELAY_MS, PERIOD_MS);
//                mViewPager.setAdapter(mCustomPagerAdapter)
            }
        });
    }



    private void loaddata() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference orders = db.collection("Deal");
                db.collection("Deal").whereEqualTo("Category","Fashion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id=doc.getId();
                                clothedata.add(new cardInfo(doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"),  String.valueOf(doc.get("Knubs")), id,String.valueOf(doc.get("Passcode")),doc.getBoolean("Online"),"",String.valueOf(doc.get("Fb")),doc.getString("Brandid")));
                            }

                        } else {
                            Log.w("loaddata", "Error getting documents.", task.getException());
                        }
                    }


                });
        db.collection("Deal").whereEqualTo("Category","Food")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id=doc.getId();
                                fooddata.add(new cardInfo(doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"),  String.valueOf(doc.get("Knubs")), id,String.valueOf(doc.get("Passcode")),doc.getBoolean("Online"),"",String.valueOf(doc.get("Fb")),doc.getString("Brandid")));
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
                                salondata.add(new cardInfo(doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"),  String.valueOf(doc.get("Knubs")), id,String.valueOf(doc.get("Passcode")),doc.getBoolean("Online"),"",String.valueOf(doc.get("Fb")),doc.getString("Brandid")));
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
                                healthdata.add(new cardInfo(doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"),  String.valueOf(doc.get("Knubs")), id,String.valueOf(doc.get("Passcode")),doc.getBoolean("Online"),"",String.valueOf(doc.get("Fb")),doc.getString("Brandid")));                            }


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
                                fundata.add(new cardInfo(doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"),  String.valueOf(doc.get("Knubs")), id,String.valueOf(doc.get("Passcode")),doc.getBoolean("Online"),"",String.valueOf(doc.get("Fb")),doc.getString("Brandid")));                            }


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
                        selected = new Dealslist("Food",user);
                    } else if (item.getItemId() == R.id.clothingtab) {
                        Bundle b = new Bundle();
                        b.putString("type", "Fashion");
                        selected = new Dealslist("Fashion",user);
                        ;
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.healthTab) {
                        Bundle b = new Bundle();
                        b.putString("type", "Health");
                        selected = new Dealslist("Health",user);
                        ;
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.funtab) {
                        Bundle b = new Bundle();
                        b.putString("type", "Entertainment");
//                        selected = new food_deals("health")

                        selected=new Dealslist("Entertainment",user);
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.otherTab) {
                    Bundle b = new Bundle();
                    b.putString("type", "Other");
//                        selected = new food_deals("health")

                    selected=new Dealslist("Other",user);
                    selected.setArguments(b);
                }
                    getChildFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                    return true;
                }
            };


    interface Updateuser{
        public void updateuser(User user);
    }

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of HomeFragment");
        super.onResume();
    }
    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of HomeFragment");
        super.onPause();

    }

}
