package worthywalk.example.com.worthywalk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import worthywalk.example.com.worthywalk.Models.User;
import worthywalk.example.com.worthywalk.Models.cardInfo;


public class Dealslist extends Fragment {

    TextView tv;
    RecyclerView rev;
    List<cardInfo> data = new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;
    storelistadapter adapter;
    FirebaseFirestore db;

    String category;
    User user = new User();
    MainActivity activity;
    String request_url = "http://localhost/sliderjsonoutput.php";

    public Dealslist(String category, User user) {
        this.category = category;
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.storerecycle, container, false);
        db = FirebaseFirestore.getInstance();
        rev = (RecyclerView) rootview.findViewById(R.id.recyclerView);
        tv = (TextView) rootview.findViewById(R.id.comingsoon);
        load();

        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());
        rev.setLayoutManager(layout);

        return rootview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                User update = (User) data.getSerializableExtra("result");

                activity = (MainActivity) getActivity();
                activity.updateuser(update);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }//onActivity

    private void loadstore() {
        load();


    }

    private void load() {

        if (category.equals("All")) {
            CollectionReference orders = db.collection("Deals");
            orders
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                tv.setVisibility(View.GONE);
                                rev.setVisibility(View.VISIBLE);
                                QuerySnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.isEmpty()) {
                                    rev.setVisibility(View.GONE);
                                    tv.setVisibility(View.VISIBLE);
                                    Log.d("gone", "coming soon ka on scene");

                                } else {

                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String id = doc.getId();
                                        data.add(new cardInfo((List<String>) doc.get("Location"),doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"),  String.valueOf(doc.get("Knubs")), id,String.valueOf(doc.get("Passcode")),doc.getBoolean("Online"),doc.getString("Brandid"),doc.getString("Description")));


                                    }
                                }
                                adapter = new storelistadapter(data, getContext());
                                adapter.setOnItemClickListener(new storelistadapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {

                                        cardInfo card = new cardInfo();
                                        card = data.get(position);

                                        Intent intent = new Intent(getActivity(), Avail.class);
                                        intent.putExtra("card", card);
                                        intent.putExtra("user", user);
                                        startActivityForResult(intent, 1);


                                    }
                                });
                                rev.setAdapter(adapter);


                            } else {
                                Log.w("loaddata", "Error getting documents.", task.getException());
                            }
                        }


                    });
        } else {
            CollectionReference orders = db.collection("Deals");
            orders.whereEqualTo("Category", category)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                tv.setVisibility(View.GONE);
                                rev.setVisibility(View.VISIBLE);
                                QuerySnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.isEmpty()) {
                                    rev.setVisibility(View.GONE);
                                    tv.setVisibility(View.VISIBLE);
                                    Log.d("gone", "coming soon ka on scene");

                                } else {

                                    for (QueryDocumentSnapshot doc : task.getResult()) {
                                        String id = doc.getId();
                                        data.add(new cardInfo((List<String>) doc.get("Location"),doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"),  String.valueOf(doc.get("Knubs")), id,String.valueOf(doc.get("Passcode")),doc.getBoolean("Online"),doc.getString("Brandid"),doc.getString("Description")));


                                    }
                                }
                                adapter = new storelistadapter(data, getContext());
                                adapter.setOnItemClickListener(new storelistadapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {

                                        cardInfo card = new cardInfo();
                                        card = data.get(position);

                                        Intent intent = new Intent(getActivity(), Avail.class);
                                        intent.putExtra("card", card);
                                        intent.putExtra("user", user);
                                        startActivityForResult(intent, 1);


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
}
