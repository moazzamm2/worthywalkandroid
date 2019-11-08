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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import java.time.Year;
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
    boolean fn,ln,pn,gn,ht,wt,dy,mn,yr;
    User updateuser;
    boolean update=false;

    StringBuilder dateOfBirth=new StringBuilder();

    ProgressBar pbloading;
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
        pbloading=(ProgressBar) findViewById(R.id.pbLoading);
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


        String userss=sharedpreferences.getString("User","a");
        if(!userss.equals("a")){
            update=true;
            updateuser= gson.fromJson(userss,User.class);
            firstname.setText(updateuser.Firstname);
            lastname.setText(updateuser.Lastname);
            height.setText(String.valueOf(updateuser.Height));
            weight.setText(String.valueOf(updateuser.Weight));
            String phnses=updateuser.Phone.toString().substring(3);

            phone.setText(phnses);
        }
//        day.addTextChangedListener(this);
//        month.addTextChangedListener(this);
//        year.addTextChangedListener(this);
        day.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(dateOfBirth.length()==0&day.length()==2)
                {
                    dateOfBirth.append(s);
                    day.clearFocus();
                    month.requestFocus();
                    month.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if(dateOfBirth.length()==2)
                {

                    dateOfBirth.deleteCharAt(0);
                    dateOfBirth.deleteCharAt(1);

                }

            }

            public void afterTextChanged(Editable s) {
                if(dateOfBirth.length()==0)
                {

                    day.requestFocus();
                }

            }
        });
        month.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(month.length()==2)
                {
                    dateOfBirth.append(s);
                    month.clearFocus();
                    year.requestFocus();
                    year.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if(dateOfBirth.length()==2)
                {

                    dateOfBirth.deleteCharAt(0);
//                    dateOfBirth.deleteCharAt(1);

                }

            }

            public void afterTextChanged(Editable s) {
                if(dateOfBirth.length()==0)
                {

                    month.requestFocus();
                }

            }
        });
        year.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(year.length()==4)
                {
                    dateOfBirth.append(s);
                    month.clearFocus();
                    year.requestFocus();
                    year.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if(dateOfBirth.length()==2)
                {

                    dateOfBirth.deleteCharAt(0);
//                    dateOfBirth.deleteCharAt(1);

                }

            }

            public void afterTextChanged(Editable s) {
                if(dateOfBirth.length()==0)
                {

                    month.requestFocus();
                }

            }
        });


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

                if (pn && ht && wt && dy && mn && yr) {
                    pbloading.setVisibility(View.VISIBLE);
                    fname = firstname.getText().toString();
                    fname = fname.substring(0, 1).toUpperCase() + fname.substring(1);
                    lname = lastname.getText().toString();
                    lname = lname.substring(0, 1).toUpperCase() + lname.substring(1);

                    phn = phone.getText().toString();
                    phn = "+92" + phn;
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

                    String token = sharedpreferences.getString("Token", "");

                    if (fbuser != null)
                        user = new User(fname, lname, phn, gend, hei, wei, age, d, 500, fbuser.imageurl, 500, false, token);
                    else if(update){

                        user = new User(fname, lname, phn, gend, hei, wei, age, d, updateuser.Knubs, "", updateuser.totalknubs, false, token);
                    }else
                        user = new User(fname, lname, phn, gend, hei, wei, age, d, 500, "", 500, false, token);

                    String userjson = gson.toJson(user);
                    SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                    prefsEditor.putBoolean("Newuser",true);
                    prefsEditor.putString("User", userjson);

                    if(!update){
                        prefsEditor.putFloat("Totaldistance", 0);
                        prefsEditor.putFloat("Totalcalorie", 0);
                        prefsEditor.putInt("Totalknubs", 0);
                        prefsEditor.putInt("Totalsteps", 0);

                    }
                    prefsEditor.commit();
                    if(update){
                        updatedata(user, users.getUid());

                    }else {
                        sendata(user, users.getUid());

                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"Form Validation failed , please provide the correct Information !",Toast.LENGTH_LONG).show();

                }
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
                docData.put("Totalknubs",user.totalknubs);
                docData.put("Permission",user.permission);
                docData.put("Token",user.token);



                docData2.put("Totalcalorie",0.0);
                docData2.put("Totaldistance",0.0);
                docData2.put("Totalsteps",0);
                docData2.put("Totalknubs",500);



                FirebaseFirestore db = FirebaseFirestore.getInstance();

               final DocumentReference docRef= db.collection("Users").document(uid);


                final DocumentReference docRef2=db.collection("Monthlywalk").document(uid);
                db.runTransaction(new Transaction.Function<Void>() {
                    @Nullable
                    @Override
                    public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {



                            transaction.set(docRef,docData );
                            transaction.set(docRef2,docData2);



                        return null;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pbloading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("User",user);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                            Log.d("uploaded","done");
                            finish();
                    }
                });


//

            }
        });
    }
    private void updatedata(final User user, String uid) {
        final Map<String, Object> docData = new HashMap<>();


        docData.put("Firstname", user.Firstname);
        docData.put("Lastname", user.Lastname);
        docData.put("Phone", user.Phone);
        docData.put("Gender", user.Gender);
        docData.put("Height", user.Height);
        docData.put("Weight", user.Weight);
        docData.put("Age", user.Age);
        docData.put("DOB", user.Dob);
        docData.put("Knubs",user.Knubs );
        docData.put("Profilepicture",user.imageurl);
        docData.put("Totalknubs",user.totalknubs);
        docData.put("Permission",user.permission);
        docData.put("Token",user.token);




        FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef= db.collection("Users").document(uid);


        final DocumentReference docRef2=db.collection("Monthlywalk").document(uid);
        db.runTransaction(new Transaction.Function<Void>() {
            @Nullable
            @Override
            public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {




                    transaction.set(docRef,docData );


                return null;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pbloading.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), e.getMessage() , Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("User",user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                startActivity(intent);

                Log.d("uploaded","done");
                finish();
            }
        });


//

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
        if(firstname.getText().toString().length()==0) {

            firstname.setError("This field can not be empty");
            fn=false;
        }else {
            fn=true;
        }


        boolphone=phone.getText().toString().matches("3[0-9]{9}");

        if(phone.getText().toString().length()<10) phone.setError("Enter 10 digit");
        else if(boolphone) pn=true ;
        else phone.setError("3XXXXXXXXX");

        if(day.getText().toString().length()==0){
            dy=false;
            day.setError("can not be empty");
        }

        else if(Integer.parseInt(day.getText().toString())>31){
            day.setError("Enter valid day");
              dy=false;
        }
        else{
            dy=true;
        }

        if(month.getText().toString().length()==0){
            mn=false;
            month.setError("can not be empty");
        }
        else if(Integer.parseInt(month.getText().toString())>12){
            mn=false;

            month.setError("Enter valid month");
        }else {
            mn=true;

        }

        if(year.getText().toString().length()==0){
            year.setError("can not be empty");
        yr=false;
        }
        else if(Integer.parseInt(year.getText().toString())<1940 || Integer.parseInt(year.getText().toString())>2019){
            year.setError("Not a valid year");
            yr=false;

        }
        else yr=true;




        if(height.getText().toString().length()==0){
            ht=false;
            height.setError("can not be empty");
        }
        else if(Float.parseFloat(height.getText().toString())<3.0 ||Float.parseFloat(height.getText().toString())>14.0) {
            height.setError("enter valid Height");
            ht=false;
        }else  ht=true;

        if(weight.getText().toString().length()==0){
            ht=false;
            weight.setError("can not be empty");
        }
       else if(Float.parseFloat(weight.getText().toString())<20 || Float.parseFloat(weight.getText().toString())>600){
           wt=false;
           weight.setError("Enter valid Weight");
        }else{
           wt=true;
        }




    }
}
