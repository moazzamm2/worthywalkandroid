package worthywalk.example.com.worthywalk;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Timer;
import java.util.TimerTask;

public class storefrag extends Fragment {
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000; // time in milliseconds between successive task executions.

    String[] mresources={"https://cloud.24hours.pk/images/deals/full/85911501156462.jpg",
    "https://cloud.24hours.pk/images/deals/full/54801504780102.jpg",
    "https://www.parhlo.com/wp-content/uploads/2016/06/china-grill.jpeg"
    };
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.store, container, false);
      final ViewPager mViewPager = (ViewPager) rootview.findViewById(R.id.pager);

        CustomPagerAdapter mCustomPagerAdapter = new CustomPagerAdapter(getContext(), mresources);

        mViewPager.setAdapter(mCustomPagerAdapter);
        final Handler handler = new Handler();
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
        getChildFragmentManager().beginTransaction().replace(R.id.container, new food_deals()).commit();

    return rootview;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;

                    if (item.getItemId() == R.id.foodtab) {
                        selected = new food_deals();
                    } else if (item.getItemId() == R.id.clothingtab) {
                        Bundle b = new Bundle();
                        b.putString("type", "clothing");
                        selected = new food_deals();
                        ;
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.healthTab) {
                        Bundle b = new Bundle();
                        b.putString("type", "health");
                        selected = new food_deals();
                        ;
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.salonTab) {
                        Bundle b = new Bundle();
                        b.putString("type", "salon");
                        selected = new food_deals();
                        ;
                        selected.setArguments(b);
                    } else if (item.getItemId() == R.id.gymtab) {
                        Bundle b = new Bundle();
                        b.putString("type", "gym");
                        selected = new food_deals();
                        ;
                        selected.setArguments(b);
                    }
                    getChildFragmentManager().beginTransaction().replace(R.id.container, selected).commit();
                    return true;
                }
            };

}
