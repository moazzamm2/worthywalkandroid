package worthywalk.example.com.worthywalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class Leaderboardfrag extends Fragment {
RecyclerView recyclerView;
List<leaderinfo> data=new ArrayList<>();
leaderadapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_leaderboardfrag,container,false);
        recyclerView =(RecyclerView) view.findViewById(R.id.leaderview);
//data.add(new leaderinfo("Moazzam Maqsood","3000","abc"));
        loaddata();
        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layout);


        return view;
    }

    private void loaddata() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference leader = db.collection("Users");
        leader.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc:task.getResult()){
                        String name=doc.getString("Firstname")+doc.getString("Lastname");
                        String id=doc.getId();
                        String knubs=String.valueOf(doc.get("Knubs"));
                        if(doc.getString("Firstname")!=null)  data.add(new leaderinfo(name,knubs,id));
                        Log.d("infoo",name);
                    }
                    adapter=new leaderadapter(data,getContext());
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(),"NO DATA FOUND",Toast.LENGTH_LONG).show();
                }
            }
        });

    }


}
