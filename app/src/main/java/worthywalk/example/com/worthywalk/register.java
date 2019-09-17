package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
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
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class register extends AppCompatActivity {
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
    String token;
    CircleImageView profile_picture;
    FBuser fbuser;
    SharedPreferences sharedpreferences;
Gson gson=new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

                User user = new User(fname, lname,phn,gend, hei, wei, age, d, 500,fbuser.imageurl);

                String userjson=gson.toJson(user);
                SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                prefsEditor.putString("User",userjson);
                prefsEditor.commit();
                sendata(user, users.getUid());
            }

            private void sendata(final User user, String uid) {
                final Map<String, Object> docData = new HashMap<>();

                FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            token=task.getResult().getToken();



                        }
                    }
                });

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
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Users").document(uid).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            intent.putExtra("User",user);
                            startActivity(intent);
                            finish();
                            Log.d("uploaded","done");

                        }
                    }
                });
                final Map<String, Object> doc = new HashMap<>();
                doc.put("token_id",token);

                db.collection("Token").document(uid).set(doc);


            }
        });
    }
}
