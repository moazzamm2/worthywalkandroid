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
import worthywalk.example.com.worthywalk.Models.faq;
import worthywalk.example.com.worthywalk.Models.leaderinfo;



public class faqadapter  extends RecyclerView.Adapter {

    Context context;
    List<faq> list=new ArrayList<>();


    public faqadapter(List<faq> list, Context context){
        this.list=list;
        this.context=context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater minflator=LayoutInflater.from(parent.getContext());
        View view=minflator.inflate(R.layout.faqcard,parent,false);

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

        TextView question;
        TextView answer;


//        ImageView winner,second;



        public listViewHolder(View view) {
            super(view);
           question=(TextView) view.findViewById(R.id.question);
            answer=(TextView) view.findViewById(R.id.answer);


//            winner=(ImageView) view.findViewById(R.id.win);
//            second=(ImageView) view.findViewById(R.id.second);




        }


        public void bindView(int position){
            final faq info=list.get(position);



            question.setText(info.qusetion);
            answer.setText(info.answer);



        }
    }
}
