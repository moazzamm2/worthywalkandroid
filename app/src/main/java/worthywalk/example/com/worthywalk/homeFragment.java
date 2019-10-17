package worthywalk.example.com.worthywalk;

import android.app.Activity;
<<<<<<< HEAD
import android.content.Context;
=======
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
>>>>>>> 154b1189317702729c2efc3a5975026cb8c951bc
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
<<<<<<< HEAD
=======

import java.util.HashMap;
import java.util.Map;
>>>>>>> 154b1189317702729c2efc3a5975026cb8c951bc

public class homeFragment extends Fragment {
    TextView name,totaldistance,totalcalorie,totalsteps,totalknubs;
    Button startactivity;
    User user;
    LoginManager loginManager;
    public homeFragment(User user) {
    this.user=user;
    }
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    float calorie,distance;
    int knubs,steps;
ImageButton logout;
FirebaseAuth mAuth;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

       View view= inflater.inflate(R.layout.dashboard,container,false);

       logout=(ImageButton) view.findViewById(R.id.logout);
        startactivity=(Button) view.findViewById(R.id.startactivitybutton);
        name=(TextView) view.findViewById(R.id.t1name);
        totalcalorie=(TextView) view.findViewById(R.id.caloriedashboard);
        totalsteps=(TextView) view.findViewById(R.id.stepsdashboard);
        totaldistance=(TextView) view.findViewById(R.id.distancedashboard);
        totalknubs=(TextView) view.findViewById(R.id.knubsdashboard);



        mAuth=FirebaseAuth.getInstance();
        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if(user!=null){
        String fullname=user.Firstname+" "+user.Lastname;
        name.setText(fullname);
        setdetails();
    }

logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
<<<<<<< HEAD
        mAuth.signOut();
        loginManager.getInstance().logOut();
        Intent intent=new Intent(getActivity(),login.class);
        startActivity(intent);
        sharedpreferences.edit().remove("User").commit();
        getActivity().finish();
=======

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setTitle("Logout");
        alertDialogBuilder.setMessage("Are you sure you want to Logout ? ");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        mAuth.signOut();

                        sharedpreferences.edit().remove("User").commit();

                        loginManager.getInstance().logOut();

                        Intent intent=new Intent(getActivity(),login.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getActivity().finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

>>>>>>> 154b1189317702729c2efc3a5975026cb8c951bc
    }
});

    startactivity.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getActivity(),WalkActivity.class);
            intent.putExtra("User",user);
            startActivity(intent);
        }
    });

    return view;
    }

    private void setdetails() {
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        String uid=auth.getUid();
       final DocumentReference docref=db.collection("Monthlywalk").document(uid);



            docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                   @Override
                   public void onSuccess(DocumentSnapshot documentSnapshot) {

                       calorie=  Float.parseFloat( String.valueOf(documentSnapshot.get("Totalcalorie")));
                       knubs=Integer.parseInt( String.valueOf(documentSnapshot.get("Totalknubs")));
                       steps=Integer.parseInt( String.valueOf(documentSnapshot.get("Totalsteps")));
                       distance=Float.valueOf(String.valueOf(documentSnapshot.get("Totaldistance")));

                       totalcalorie.setText(String.valueOf(calorie));
                       totaldistance.setText(String.valueOf(distance));
                       totalknubs.setText(String.valueOf(knubs));
                       totalsteps.setText(String.valueOf(steps));


                       SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                       prefsEditor.putFloat("Totaldistance",distance);
                       prefsEditor.putFloat("Totalcalorie",calorie);
                       prefsEditor.putInt("Totalknubs",knubs);
                       prefsEditor.putInt("Totalsteps",steps);



                       prefsEditor.commit();


                   }
               }).addOnFailureListener(new OnFailureListener() {
           @Override

           public void onFailure(@NonNull Exception e) {
               Log.d("knubcheck",e.getMessage());
               distance=sharedpreferences.getFloat("Totaldistance",0);
               knubs=sharedpreferences.getInt("Totalknubs",0);
               calorie=sharedpreferences.getFloat("Totalcalorie",0);
               steps=sharedpreferences.getInt("Totalsteps",0);
               totalcalorie.setText(String.valueOf(calorie));
               totaldistance.setText(String.valueOf(distance));
               totalknubs.setText(String.valueOf(knubs));
               totalsteps.setText(String.valueOf(steps));



           }
       });
    }
}
