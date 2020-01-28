package worthywalk.example.com.worthywalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import worthywalk.example.com.worthywalk.Models.User;
import worthywalk.example.com.worthywalk.Models.faq;
import worthywalk.example.com.worthywalk.Models.leaderinfo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Writetous extends AppCompatActivity {
    EditText suggest;
    Button send;
    RecyclerView recyclerView;
    String suggestions;
    faqadapter adapter;
    Toolbar mToolbar;

    Gson gson;
    FirebaseAuth auth;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    User user;
    String id;
    List<faq> list;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writetous);
        gson=new Gson();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String users=sharedpreferences.getString("User","a");
        user= gson.fromJson(users,User.class);
        auth=FirebaseAuth.getInstance();
        suggest=(EditText) findViewById(R.id.suggest);
        recyclerView=(RecyclerView) findViewById(R.id.recyclerView);
        send=(Button) findViewById(R.id.sendbtn);
        list=new ArrayList<>();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadquestions();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              suggestions=  suggest.getText().toString();
              if(suggestions.length()>0){
                  final Map<String,Object> map=new HashMap<>();
                  id=auth.getCurrentUser().getUid();
                  map.put("Userid",id);
                  map.put("Suggestions",suggestions);
                  map.put("username",user.Firstname);
                  FirebaseFirestore db=FirebaseFirestore.getInstance();
                  final DocumentReference docref=db.collection("Suggestions").document();

                  db.runTransaction(new Transaction.Function<Void>() {
                      @Nullable
                      @Override
                      public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {

                          transaction.set(docref,map);


                          return null;
                      }
                  }).addOnSuccessListener(new OnSuccessListener<Void>() {
                      @Override
                      public void onSuccess(Void aVoid) {
                          suggest.getText().clear();
                          Toast.makeText(Writetous.this,"Thankyou for Writing to us",Toast.LENGTH_LONG).show();
                      }
                  }).addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception e) {
                          Toast.makeText(Writetous.this,"Message not sent",Toast.LENGTH_LONG).show();

                      }
                  });

              }
            }
        });

        RecyclerView.LayoutManager layout = new LinearLayoutManager(this);


        recyclerView.setLayoutManager(layout);




    }




    public void loadquestions(){

        final FirebaseFirestore db = FirebaseFirestore.getInstance();



        final CollectionReference leaders = db.collection("Questions");


        leaders.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if(task.isSuccessful()){
//                    Toast.makeText(getContext(),"Sucees",Toast.LENGTH_LONG).show();
                    int index=1;

                    for(QueryDocumentSnapshot doc:task.getResult()){


//                        String name=doc.getId();
//                        String knubs=String.valueOf(doc.get("KnubsEarned"));
//                        data.add(new leaderinfo(name,knubs,name));


                            String qusetion=String.valueOf(index)+". "+doc.getString("Question");
                            String answer=String.valueOf(doc.get("Answer"));
                            if(doc.getString("Question")!=null)  list.add(new faq(qusetion,answer));


                       index++;
////


                    }
                    adapter=new faqadapter(list,Writetous.this);
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(getApplicationContext(),task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
