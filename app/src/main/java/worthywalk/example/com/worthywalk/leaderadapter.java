package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class leaderadapter extends RecyclerView.Adapter {

Context context;
    List<leaderinfo> list=new ArrayList<>();


    leaderadapter(List<leaderinfo> list,Context context){
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



        public listViewHolder(View view) {
            super(view);
            index=(TextView) view.findViewById(R.id.index);
            name=(TextView) view.findViewById(R.id.name);
            distance=(TextView)view.findViewById(R.id.distance);




        }

        public void bindView(int position){
            final leaderinfo info=list.get(position);
            index.setText(String.valueOf(position+1));
        name.setText(info.name);
        distance.setText(info.distance);

        }
    }
}
