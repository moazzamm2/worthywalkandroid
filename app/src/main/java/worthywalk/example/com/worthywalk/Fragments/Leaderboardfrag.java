package worthywalk.example.com.worthywalk.Fragments;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import worthywalk.example.com.worthywalk.Models.User;
import worthywalk.example.com.worthywalk.Models.leaderinfo;
import worthywalk.example.com.worthywalk.R;
import worthywalk.example.com.worthywalk.leaderadapter;
import worthywalk.example.com.worthywalk.testuser;

public class Leaderboardfrag extends Fragment {
RecyclerView recyclerView;
List<leaderinfo> data=new ArrayList<>();
leaderadapter adapter;
Date endingtime;
TextView time,spon;
ImageView banner;
CountDownTimer cTimer = null;
Gson gson;
long milliseconds;
Button yes,no;
FirebaseFirestore db;
RelativeLayout permission,leader;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    List<testuser> testuserlist=new ArrayList<>();
    long milli=0;
User user;
    Map<Integer,String> month=new HashMap();
    FirebaseAuth mAuth;
    String imageurl="";
    List<User> userlist=new ArrayList<>();
    public Leaderboardfrag(User usermain) {
        user=usermain;
    }
    boolean newuser=true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.activity_leaderboardfrag,container,false);
        db=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        banner=(ImageView) view.findViewById(R.id.leaderimage);

        recyclerView =(RecyclerView) view.findViewById(R.id.leaderview);
        time=(TextView) view.findViewById(R.id.time);
        spon=(TextView) view.findViewById(R.id.sponsored);

        yes=(Button) view.findViewById(R.id.yesbtn);
        leader=(RelativeLayout) view.findViewById(R.id.leaderlayout);
        permission=(RelativeLayout) view.findViewById(R.id.permissionlayout);
        gson=new Gson();


        no=(Button) view.findViewById(R.id.nobtn);
        sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String userjson=sharedpreferences.getString("User","a");
        newuser=sharedpreferences.getBoolean("Newuser",true);


//        dataprocess();
        if(!userjson.equals("a")) user=gson.fromJson(userjson,User.class);


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
        loaddata();
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
                                                    loadtime();
                                                    newusercall();

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

    private void newusercall() {
        if(newuser){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
            alertDialogBuilder.setTitle("Did you know ! ");
            alertDialogBuilder.setMessage("Shopping from the store doesn't deduct the knubs from your leaderboard," +
                    " Now you can enjoy amazing discounts and compete for the prize at the same time !");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            newuser=true;
                            String userjson=gson.toJson(user);
                            SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                            prefsEditor.putBoolean("Newuser",false);
                            prefsEditor.commit();


                        }
                    });



            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private void checkuser() {

        if(user.permission){
            loadtime();
            leader.setVisibility(View.VISIBLE);
            permission.setVisibility(View.GONE);
        }else {
            leader.setVisibility(View.GONE);
            permission.setVisibility(View.VISIBLE);
        }

    }


    private void loadtime() {
        db.collection("Leaderboard").document("detail").get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document=task.getResult();

                        if(document!=null) {
                            String sponsors= document.getString("Sponsored");
                            endingtime = document.getDate("endingtime");
                            Calendar calendar = new GregorianCalendar(2008, 01, 01);
                             calendar.setTime(endingtime);
                             int mon=calendar.get(Calendar.MONTH);
                             spon.setText(sponsors);
                            String str= month.get(mon+1);

                               Log.d("month",str);
                               String end=String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+" " +str+"'"+String.valueOf(calendar.get(Calendar.YEAR)));
//                            long current= Calendar.getInstance().get(Calendar.MILLISECOND);
//                            final long millisecond=milli-current;
                            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
                            time.setText(end);

                            imageurl=document.getString("Image");
                            Picasso.get().load(imageurl).fit().into(banner);

                        }
                    }
                }
        );
    }

//    private void loaduserprocess(){
//        for (testuser i:testuserlist
//             ) {
//
//            int seconds = i.Dob;
//            Date date = new Date(seconds * 1000);
//            userlist.add(new User(i.Firstname,i.Lastname,i.Phone,i.Gender,i.Height,i.Weight,i.Age,date,i.Knubs,i.imageurl,i.totalknubs,i.permission,i.token));
//
//
//
//        }
//        loaddata();
//
//
//
//
//    }

    private void loaddata() {






        final FirebaseFirestore db = FirebaseFirestore.getInstance();


//        mx7tHgcspaS4NwwqjbzdoRsRWFk1


//        vaQawKnTsWckpvwNgPnBeOZa19i2
        final CollectionReference leaders = db.collection("Users");
        final CollectionReference leader = db.collection("User");

//
//
//        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//            Map<String,Object> ok=new HashMap<>();
//
//            int count=0;
//                for (final DocumentSnapshot doc:
//                     queryDocumentSnapshots) {
//
//
//
//                    final Map<String, Object> finalDocdata = new HashMap<>();
//                    finalDocdata.put("Totalknubs",0);
//
//                    db.runTransaction(new Transaction.Function<Void>() {
//                        @Nullable
//                        @Override
//                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//                            transaction.update(leaders.document(doc.getId()), finalDocdata);
//
//
//
//                            return null;
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//
//
//                }
//                Log.d("checkdata",String.valueOf(count));
//
//            }
//        });


//        db.collection("Users").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                for (final DocumentSnapshot doc:queryDocumentSnapshots
//                     ) {
//
//                    db.runTransaction(new Transaction.Function<Void>() {
//                        @Nullable
//                        @Override
//                        public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//
//                            transaction.set(leaders.document(doc.getId()),doc);
//
//                            return null;
//                        }
//                    });

//                }
//            }
//        });


//        db.runTransaction(new Transaction.Function<Void>() {
//            @Nullable
//            @Override
//            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
//
//
//                CollectionReference collectionReference=transaction.g(leader);
//
//
//
//
//
//
//
//                return null;
//            }
//        })



//        final CollectionReference leaders = db.collection("Monthlywalk").document("vaQawKnTsWckpvwNgPnBeOZa19i2" +
//                "").collection("November");
//
//        leaders.orderBy("Datetime", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//
//                if(task.isSuccessful()){
////                    Toast.makeText(getContext(),"Sucees",Toast.LENGTH_LONG).show();
//
//                    for(QueryDocumentSnapshot doc:task.getResult()){
//
//
////                        String name=doc.getId();
////                        String knubs=String.valueOf(doc.get("KnubsEarned"));
////                        data.add(new leaderinfo(name,knubs,name));
//
//
////                            String name=doc.getString("Firstname")+" "+doc.getString("Lastname");
//                            String name=doc.getId();
//                            String id=doc.getId();
//                            String knubs=doc.getLong("KnubsEarned").toString();
//                            data.add(new leaderinfo(name,knubs,id));
//                            Log.d("infoo",name);
//
//////
//
//
//                    }
//                    adapter=new leaderadapter(data,getContext());
//                    recyclerView.setAdapter(adapter);
//                }else {
//                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//
//

//
//
//
////

        leaders.orderBy("Totalknubs", Query.Direction.DESCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {


                if(task.isSuccessful()){
//                    Toast.makeText(getContext(),"Sucees",Toast.LENGTH_LONG).show();

                    for(QueryDocumentSnapshot doc:task.getResult()){


//                        String name=doc.getId();
//                        String knubs=String.valueOf(doc.get("KnubsEarned"));
//                        data.add(new leaderinfo(name,knubs,name));

                if(doc.getBoolean("Permission")){
                    String name=doc.getString("Firstname")+" "+doc.getString("Lastname");
                    String id=doc.getId();
                    String knubs=String.valueOf(doc.get("Totalknubs"));
                    if(doc.getString("Firstname")!=null)  data.add(new leaderinfo(name,knubs,id));
                    Log.d("infoo",name);

                }
////


                         }
                    adapter=new leaderadapter(data,getContext());
                    recyclerView.setAdapter(adapter);
                }else {
                    Toast.makeText(getContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public  Map<String,Object> adddata(User users){
        final Map<String, Object> docData = new HashMap<>();

        docData.put("Firstname", users.Firstname);
        docData.put("Lastname", users.Lastname);
        docData.put("Phone", users.Phone);
        docData.put("Gender", users.Gender);
        docData.put("Height", users.Height);
        docData.put("Weight", users.Weight);
        docData.put("Age", users.Age);
        docData.put("DOB", users.Dob);
        docData.put("Knubs", 500);
        docData.put("Profilepicture",users.imageurl);
        docData.put("Totalknubs",users.totalknubs);
        docData.put("Permission",users.permission);
        docData.put("Token",users.token);


        return docData;
    }

//
//    private void dataprocess(){
//
//
//
//    }


}
