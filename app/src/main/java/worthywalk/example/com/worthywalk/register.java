package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class register extends AppCompatActivity implements TextWatcher {
    String fname, lname, phn, gend, days, months, years,image;
    ;
    float hei, wei;

    FirebaseAuth mAuth;
    EditText firstname;
    EditText lastname;
    EditText phone;
    Spinner gender;
    EditText height;
    EditText weight;
    EditText day;
    EditText month;
    EditText year;
    FloatingActionButton go;
    Context context;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    String token;
    CircleImageView profile_picture;
    FBuser fbuser;
Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        phone = (EditText) findViewById(R.id.phone);
        profile_picture=(CircleImageView)findViewById(R.id.profile_image);

        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        day = (EditText) findViewById(R.id.day);
        month = (EditText) findViewById(R.id.month);
        year = (EditText) findViewById(R.id.year);
        gender = (Spinner) findViewById(R.id.gender);
        go = (FloatingActionButton) findViewById(R.id.go);
        mAuth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Intent intent=getIntent();
        fbuser= (FBuser) intent.getSerializableExtra("fbuser");
        firstname.addTextChangedListener(this);
        lastname.addTextChangedListener(this);
        phone.addTextChangedListener(this);
        height.addTextChangedListener(this);
        weight.addTextChangedListener(this);
        day.addTextChangedListener(this);
        month.addTextChangedListener(this);
        year.addTextChangedListener(this);

        image="";
        if(fbuser!=null){
            firstname.setText(fbuser.firstname);
            lastname.setText(fbuser.secondname);
            Picasso.get().load(fbuser.imageurl).fit().into(profile_picture);
            image=fbuser.imageurl;

//            firstname.setText(fbuser.firstname);

        }
        final FirebaseUser users = mAuth.getCurrentUser();
        context = this;
        gender.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                gend=adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fname = firstname.getText().toString();
                fname= fname.substring(0,1).toUpperCase()+fname.substring(1);
                lname = lastname.getText().toString();
                lname= lname.substring(0,1).toUpperCase()+lname.substring(1);

                phn = phone.getText().toString();
                hei = Float.parseFloat(height.getText().toString());
                wei = Float.parseFloat(weight.getText().toString());
                days = day.getText().toString();
                months = month.getText().toString();
                years = year.getText().toString();


                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String dob = years + months + days;
                Date d = null;
                try {
                    d = sdf.parse(dob);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Date currentTime = Calendar.getInstance().getTime();
                int age = Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(years);
                User user;
                if(fbuser!=null) user  = new User(fname, lname,phn,gend, hei, wei, age, d, 500,fbuser.imageurl);
                else  user  = new User(fname, lname,phn,gend, hei, wei, age, d, 500,"");
                String userjson=gson.toJson(user);
                SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                prefsEditor.putString("User",userjson);
                prefsEditor.commit();
                sendata(user, users.getUid());
            }

            private void validate(){

            }
            private void sendata(final User user, String uid) {
                final Map<String, Object> docData = new HashMap<>();
                final Map<String, Object> docData2 = new HashMap<>();
                final Map<String, Object> docData3 = new HashMap<>();


                docData.put("Firstname", user.Firstname);
                docData.put("Lastname", user.Lastname);
                docData.put("Phone", user.Phone);
                docData.put("Gender", user.Gender);
                docData.put("Height", user.Height);
                docData.put("Weight", user.Weight);
                docData.put("Age", user.Age);
                docData.put("DOB", user.Dob);
                docData.put("Knubs", 500);
                docData.put("Profilepicture",user.imageurl);

                docData2.put("Totalcalorie",0.0);
                docData2.put("Totaldistance",0.0);
                docData2.put("Totalsteps",0);
                docData2.put("Totalknubs",500);



                String token=sharedpreferences.getString("Token","");
                docData3.put("Token",token);


                FirebaseFirestore db = FirebaseFirestore.getInstance();

               final DocumentReference docRef= db.collection("Users").document(uid);
               final DocumentReference docRef3= db.collection("Users").document(uid).collection("Token").document(token);

                final DocumentReference docRef2=db.collection("Monthlywalk").document(uid);
                db.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {


                        transaction.set(docRef,docData );
                        transaction.set(docRef2,docData2);
                        transaction.set(docRef3,docData3);
                        return null;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("User",user);
                            startActivity(intent);
                            finish();
                            Log.d("uploaded","done");
                    }
                });

//                db.collection("Users").document(uid).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Intent intent = new Intent(context, MainActivity.class);
//                            intent.putExtra("User",user);
//                            startActivity(intent);
//                            finish();
//                            Log.d("uploaded","done");
//
//                        }
//                    }
//                });
//                final Map<String, Object> doc = new HashMap<>();
//                doc.put("token_id",token);
//
//                db.collection("Token").document(uid).set(doc);


            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        boolean boolphone=false;
        if(firstname.getText().toString().length()==0) firstname.setError("This field can not be empty");
        if(lastname.getText().toString().length()==0) lastname.setError("This field can not be empty");
        boolphone=phone.getText().toString().matches("03[0-9]{9}");

        if(phone.getText().toString().length()<11) phone.setError("Enter 11 digit");
        else if(boolphone) boolphone=true ;
        else phone.setError("03XXXXXXXXX");

        if(day.getText().toString().length()==0)day.setError("can not be empty");

        else if(Integer.parseInt(day.getText().toString())>31) day.setError("Enter valid date");

        if(month.getText().toString().length()==0)month.setError("can not be empty");
        else if(Integer.parseInt(month.getText().toString())>12) month.setError("Enter valid month");

        if(year.getText().toString().length()==0)year.setError("can not be empty");
        else if(Integer.parseInt(year.getText().toString())<1940 || Integer.parseInt(year.getText().toString())>2019) year.setError("Not a valid year");
        if(day.getText().toString().length()==0)day.setError("can not be empty");


        if(height.getText().toString().length()==0)height.setError("can not be empty");
        else if(Float.parseFloat(height.getText().toString())<3.0 ||Float.parseFloat(height.getText().toString())>14.0) height.setError("enter valid Height");
        if(weight.getText().toString().length()==0)weight.setError("can not be empty");
       else if(Float.parseFloat(weight.getText().toString())<20 || Float.parseFloat(weight.getText().toString())>600) weight.setError("Enter valid Weight");




    }
}
