package worthywalk.example.com.worthywalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import worthywalk.example.com.worthywalk.Models.User;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Writetous extends AppCompatActivity {
    EditText suggest;
    Button send;
    String suggestions;
    Gson gson;
    FirebaseAuth auth;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    User user;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_writetous);
        gson=new Gson();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final String users=sharedpreferences.getString("User","a");
        user= gson.fromJson(users,User.class);
        auth=FirebaseAuth.getInstance();
        suggest=(EditText) findViewById(R.id.suggest);
        send=(Button) findViewById(R.id.sendbtn);

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




    }
}
