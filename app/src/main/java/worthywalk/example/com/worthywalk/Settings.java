package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.facebook.login.LoginManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import worthywalk.example.com.worthywalk.Models.User;

public class Settings extends AppCompatActivity {
TextView editprofile,leaderboard, suggestions,deleteaccount,Privacy;
FirebaseAuth mauth;
FirebaseFirestore db;
Toolbar mToolbar;

LoginManager loginManager;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    Gson gson;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        editprofile=(TextView) findViewById(R.id.editprofile);
        leaderboard=(TextView) findViewById(R.id.removeleaderboard);
        suggestions=(TextView) findViewById(R.id.suggestions);
//        deleteaccount=(TextView)findViewById(R.id.deleteaccount);
        Privacy=(TextView)findViewById(R.id.Privacy);

        mauth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        gson=new Gson();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String users=sharedpreferences.getString("User","a");
        user= gson.fromJson(users,User.class);
        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Settings.this,register.class);
                startActivity(intent);
            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

                                        user.permission=false;
                                        String userjson=gson.toJson(user);
                                        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                                        prefsEditor.putString("User",userjson);
                                        prefsEditor.commit();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        finish();
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
                Intent intent=new Intent(Settings.this,Writetous.class);
                startActivity(intent);
            }
        });

        Privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent=new Intent(Settings.this, Webview.class);
            startActivity(intent);



            }
        });


//        deleteaccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Settings.this);
//                alertDialogBuilder.setMessage("Are You sure you want to delete your account you wont be able to retrieve it back !");
//                alertDialogBuilder.setPositiveButton("yes",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//                                final DocumentReference docRef=db.collection("Users").document(mauth.getCurrentUser().getUid());
//                                final DocumentReference docRef1=db.collection("Monthlywalk").document(mauth.getCurrentUser().getUid());
//
//                                final Map<String, Object> docData = new HashMap<>();
//
//                                docData.put("Permission",false);
//
//
//                                db.runTransaction(new Transaction.Function<Void>() {
//                                    @Nullable
//                                    @Override
//                                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                                        if(loginManager.getInstance()!=null)                                 loginManager.getInstance().logOut();
//                                        FirebaseAuth.getInstance().getCurrentUser().delete();
//                                        transaction.delete(docRef);
//                                        transaction.delete(docRef1);
//
//                                        return null;
//                                    }
//                                }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
//                                        prefsEditor.remove("User").commit();
//                                        Intent intent=new Intent(Settings.this,login.class);
//                                        startActivity(intent);
//
//
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//
//
//                                                Toast.makeText(Settings.this,e.getMessage(),Toast.LENGTH_LONG).show();
//                                    }
//                                });
//
//                            }
//                        });
//
//                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        finish();
//                    }
//                });
//
//                AlertDialog alertDialog = alertDialogBuilder.create();
//                alertDialog.show();
//            }
//        });

    }
}
