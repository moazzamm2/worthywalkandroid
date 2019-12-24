package worthywalk.example.com.worthywalk;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import worthywalk.example.com.worthywalk.Models.leaderinfo;

public class leaderadapter extends RecyclerView.Adapter {

Context context;
    List<leaderinfo> list=new ArrayList<>();


    public leaderadapter(List<leaderinfo> list, Context context){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater minflator=LayoutInflater.from(parent.getContext());
        View view=minflator.inflate(R.layout.leaderboardcard,parent,false);

        return new listViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((listViewHolder)holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return list.size();

    }

    private class listViewHolder extends RecyclerView.ViewHolder {

        TextView index;
        TextView name;
        TextView distance;
        RelativeLayout cards;

//        ImageView winner,second;



        public listViewHolder(View view) {
            super(view);
            index=(TextView) view.findViewById(R.id.index);
            name=(TextView) view.findViewById(R.id.name);
            distance=(TextView)view.findViewById(R.id.distance);
            cards=(RelativeLayout) view.findViewById(R.id.card);

//            winner=(ImageView) view.findViewById(R.id.win);
//            second=(ImageView) view.findViewById(R.id.second);




        }

        @SuppressLint("ResourceAsColor")
        public void bindView(int position){
            final leaderinfo info=list.get(position);
            if(position==0){
//                index.setVisibility(View.GONE);

                index.setTextColor(Color.WHITE);
                name.setTextColor(Color.WHITE);
                distance.setTextColor(Color.WHITE);
                cards.setBackgroundColor( Color.parseColor("#f3c300"));
//                winner.setVisibility(View.VISIBLE);
//                second.setVisibility(View.GONE);
            }else if(position==1){
//                index.setVisibility(View.GONE);

                index.setTextColor(Color.WHITE);
                name.setTextColor(Color.WHITE);
                distance.setTextColor(Color.WHITE);

                cards.setBackgroundColor(Color.GRAY);

//                winner.setVisibility(View.GONE);
//
//                second.setVisibility(View.VISIBLE);
            }else {
                cards.setBackgroundColor(Color.WHITE);
                index.setTextColor(Color.BLACK);
                name.setTextColor(Color.parseColor("#018D83"));
                distance.setTextColor(Color.parseColor("#f3c300"));
//                    index.setVisibility(View.VISIBLE);
//                winner.setVisibility(View.GONE);
//                    second.setVisibility(View.GONE);

            }
            index.setText(String.valueOf(position+1));
        name.setText(info.name);
        distance.setText(info.distance);

        }
    }
}
