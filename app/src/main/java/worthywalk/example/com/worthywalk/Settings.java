package worthywalk.example.com.worthywalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {
TextView editprofile,leaderboard, suggestions,deleteaccount;
FirebaseAuth mauth;
FirebaseFirestore db;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    Gson gson;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editprofile=(TextView) findViewById(R.id.editprofile);
        leaderboard=(TextView) findViewById(R.id.removeleaderboard);
        suggestions=(TextView) findViewById(R.id.suggestions);
        deleteaccount=(TextView)findViewById(R.id.deleteaccount);
        mauth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        gson=new Gson();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.this,register.class);
                startActivity(intent);
            }
        });

        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Settings.this);
                alertDialogBuilder.setMessage("You will no longer be competing in Leaderboard, Confirm by pressing yes");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                final DocumentReference docRef=db.collection("Users").document(mauth.getCurrentUser().getUid());
                                final Map<String, Object> docData = new HashMap<>();

                                docData.put("Permission",false);
                                db.runTransaction(new Transaction.Function<Void>() {
                                    @Nullable
                                    @Override
                                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                        transaction.update(docRef,docData);
                                        return null;
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        user.permission=true;

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                //        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        });

                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        suggestions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        deleteaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
}
