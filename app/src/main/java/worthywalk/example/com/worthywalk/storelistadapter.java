package worthywalk.example.com.worthywalk;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class storelistadapter extends RecyclerView.Adapter{


    List<cardInfo> data=new ArrayList<>();
    Context context;
    public storelistadapter(List<cardInfo> a,Context cont){
        data=a;
        context=cont;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater minflator=LayoutInflater.from(parent.getContext());
        View view=minflator.inflate(R.layout.storecard,parent,false);

        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ((listViewHolder)holder).bindView(position);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    private class listViewHolder extends ViewHolder implements View.OnClickListener{
        ImageView logo;
        TextView resturant;
        ImageView banner;
        TextView points;


        public listViewHolder(View itemView) {
            super(itemView);
            logo=(ImageView) itemView.findViewById(R.id.img1);
            resturant=(TextView) itemView.findViewById(R.id.Resturant);
            banner=(ImageView) itemView.findViewById(R.id.img);
            points=(TextView) itemView.findViewById(R.id.t1points);
            itemView.setOnClickListener(this);

        }

        public void bindView (int position){
            final cardInfo a =data.get(position);
            Picasso.get().load(a.logo).fit().into(logo);
            Picasso.get().load(a.imgurl).fit().into(banner);
            resturant.setText(a.resturant);
            points.setText(a.points);
        }
        @Override
        public void onClick(View view) {

        }
    }

}
