package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.WriteResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class register extends AppCompatActivity {
    String fname, lname, phn, gend, days, months, years;
    ;
    float hei, wei;

    FirebaseAuth mAuth;
    EditText firstname;
    EditText lastname;
    EditText phone;
    Spinner gender;
    TextView gendertext;
    EditText height;
    EditText weight;
    EditText day;
    EditText month;
    EditText year;
    FloatingActionButton go;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        phone = (EditText) findViewById(R.id.phone);
        height = (EditText) findViewById(R.id.height);
        weight = (EditText) findViewById(R.id.weight);
        day = (EditText) findViewById(R.id.day);
        month = (EditText) findViewById(R.id.month);
        year = (EditText) findViewById(R.id.year);
        gender = (Spinner) findViewById(R.id.gender);
        gendertext = (TextView) findViewById(R.id.spinnertext);
        go = (FloatingActionButton) findViewById(R.id.go);
        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser users = mAuth.getCurrentUser();
        context = this;
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fname = firstname.getText().toString();
                lname = lastname.getText().toString();
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
                int age = Calendar.YEAR - Integer.parseInt(years);

                User user = new User(fname, lname, gend, phn, hei, wei, age, d, 0);

                sendata(user, users.getUid());
            }

            private void sendata(User user, String uid) {
                Map<String, Object> docData = new HashMap<>();
                docData.put("First_name", user.firstname);
                docData.put("Last_name", user.lastname);
                docData.put("Phone", user.phone);
                docData.put("Gender", user.gender);
                docData.put("Height", user.height);
                docData.put("Weight", user.weight);
                docData.put("Age", user.age);
                docData.put("DOB", user.Dob);
                docData.put("Knubs", 500);


                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("Users").document(uid).set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(context, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                });


            }
        });
    }
}
