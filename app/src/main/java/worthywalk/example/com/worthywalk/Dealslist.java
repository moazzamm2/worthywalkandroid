package worthywalk.example.com.worthywalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class Dealslist extends Fragment {

    RecyclerView rev;
    List<cardInfo> data = new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;
    storelistadapter adapter;
    String category;
    User user=new User();
    MainActivity activity;
    String request_url = "http://localhost/sliderjsonoutput.php";
Dealslist(String category,User user){
    this.category=category;
    this.user=user;
}
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.storerecycle, container, false);
        rev = (RecyclerView) rootview.findViewById(R.id.recyclerView);

        load();

        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());
        rev.setLayoutManager(layout);

        return rootview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
               User update= (User) data.getSerializableExtra("result");

                activity= (MainActivity) getActivity();
                activity.updateuser(update);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivity

    private void loadstore() {
        load();

//        rev.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), rev, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        // do whatever
//                        cardInfo a = data.get(position);
//                        Intent display = new Intent(getContext(), storecard.class);
//                        display.putExtra("sellectedoffer", a);
//                        startActivity(display);
//
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
                        // do whatever

//                    }
//                })
//        );

//        storelistadapter

    }

    private void load() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference orders = db.collection("Deal");
        db.collection("Deal").whereEqualTo("Category",category)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                String id=doc.getId();
                                data.add(new cardInfo("https://i.pinimg.com/736x/35/39/88/3539889f8d4988d18a53801882e39090.jpg", doc.getString("Image"), doc.getString("Brandid"),  String.valueOf(doc.get("Knubs")), id));
                            }
                            adapter = new storelistadapter(data, getContext());
                            adapter.setOnItemClickListener(new storelistadapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {

                                    cardInfo card= new cardInfo();
                                    card=data.get(position);
                                    if(user.Knubs>Integer.parseInt(card.points)){
                                        Intent intent =new Intent(getActivity(),Avail.class);
                                        intent.putExtra("card",card);
                                        intent.putExtra("user",user);
                                        startActivityForResult(intent,1);
                                    }else{

                                        Toast.makeText(getActivity(), "You don't have enough Knubs ,Start Earning! ", Toast.LENGTH_SHORT).show();


                                    }



                                }
                            });
                            rev.setAdapter(adapter);


                        } else {
                            Log.w("loaddata", "Error getting documents.", task.getException());
                        }
                    }


                });



    }


}
