package worthywalk.example.com.worthywalk;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] mResources;

    public CustomPagerAdapter(Context context, String[] resources) {
        mContext = context;
        if(resources!=null) {
            mResources = resources;
        }else         mResources[0]="https://firebasestorage.googleapis.com/v0/b/worthywalk-6b82e.appspot.com/o/Deals%2FEglett%2Feglettbanner.jpg?alt=media&token=c99c4fa3-e660-4825-b5eb-a3304a0252fd";

            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.imageviewer, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        Picasso.get().load(mResources[position]).fit().into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}

