package worthywalk.example.com.worthywalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.Inflater;

public class Leaderboardfrag extends Fragment {
RecyclerView recyclerView;
List<leaderinfo> data=new ArrayList<>();
leaderadapter adapter;
Date endingtime;
TextView time;
CountDownTimer cTimer = null;
Gson gson;
long milliseconds;
Button yes,no;
FirebaseFirestore db;
RelativeLayout permission,leader;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    long milli=0;
User user;
    Map<Integer,String> month=new HashMap();
    FirebaseAuth mAuth;

    public Leaderboardfrag(User usermain) {
        user=usermain;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_leaderboardfrag,container,false);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        recyclerView =(RecyclerView) view.findViewById(R.id.leaderview);
        time=(TextView) view.findViewById(R.id.time);
        yes=(Button) view.findViewById(R.id.yesbtn);
        leader=(RelativeLayout) view.findViewById(R.id.leaderlayout);
        permission=(RelativeLayout) view.findViewById(R.id.permissionlayout);
        gson=new Gson();
        loaddata();

        no=(Button) view.findViewById(R.id.nobtn);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        month.put(1,"Jan");
        month.put(2,"Feb");
        month.put(3,"Mar");
        month.put(4,"Apr");
        month.put(5,"May");
        month.put(6,"Jun");
        month.put(7,"Jul");
        month.put(8,"Aug");
        month.put(9,"Sept");
        month.put(10,"Oct");
        month.put(11,"Nov");
        month.put(12,"Dec");
        checkuser();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage("Do you want to compete in Leaderboard for exciting prizes, Everyone can see your score !");
                            alertDialogBuilder.setPositiveButton("yes",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {
                                            Toast.makeText(getActivity(),"You clicked yes button",Toast.LENGTH_LONG).show();
                                            final DocumentReference docRef=db.collection("Users").document(mAuth.getUid());
                                            final Map<String, Object> docData = new HashMap<>();

                                            docData.put("Permission",true);
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
                                                    leader.setVisibility(View.VISIBLE);
                                                    permission.setVisibility(View.GONE);
                                                    String userjson=gson.toJson(user);
                                                    SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                                                    prefsEditor.putString("User",userjson);
                                                    prefsEditor.commit();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                                                }
                                            });

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
                }

        });

        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());


        recyclerView.setLayoutManager(layout);


        return view;
    }

    private void checkuser() {

        if(user.permission){
            loadtime();
            leader.setVisibility(View.VISIBLE);
            permission.setVisibility(View.GONE);
        }

    }


    private void loadtime() {
        db.collection("Leaderboard").document("detail").get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document=task.getResult();

                        if(document!=null) {
                            endingtime = document.getDate("endingtime");
                            Calendar calendar = new GregorianCalendar(2008, 01, 01);
                             calendar.setTime(endingtime);
                             int mon=calendar.get(Calendar.MONTH);
                            String str= month.get(mon+1);

                               Log.d("month",str);
                               String end=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+" " +str+"'"+String.valueOf(calendar.get(Calendar.YEAR)));
//                            long current= Calendar.getInstance().get(Calendar.MILLISECOND);
//                            final long millisecond=milli-current;
                            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
                            time.setText(end);


                        }
                    }
                }
        );
    }


    private void loaddata() {


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final CollectionReference leader = db.collection("Users");
        leader.orderBy("Totalknubs", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){

                    for(QueryDocumentSnapshot doc:task.getResult()){
                        if(doc.getBoolean("Permission")){
                            String name=doc.getString("Firstname")+" "+doc.getString("Lastname");
                            String id=doc.getId();
                            String knubs=String.valueOf(doc.get("Totalknubs"));
                            if(doc.getString("Firstname")!=null)  data.add(new leaderinfo(name,knubs,id));
                            Log.d("infoo",name);

                        }
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
