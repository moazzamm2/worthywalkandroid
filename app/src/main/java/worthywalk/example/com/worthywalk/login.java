package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
        Button signIn;
        TextView signUp;
User user=new User();
    private FirebaseAuth mAuth;
    EditText email,password;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    FirebaseFirestore db;

    // ..
// Initialize Firebase Auth

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_login);
          email=(EditText)findViewById(R.id.emailAddress);
        password=(EditText)findViewById(R.id.password);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         db = FirebaseFirestore.getInstance();


        mAuth = FirebaseAuth.getInstance();
            signUp=(TextView) findViewById(R.id.login_signup);
            FirebaseUser user=mAuth.getCurrentUser();

            if(user!=null){
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                finish();

            }
            //signIn=(Button)findViewById(R.id.btSignIn);
            signUp=(TextView)findViewById(R.id.login_signup);
            signIn = (Button)findViewById(R.id.btSignIn);

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(login.this,Signup.class);
                    startActivity(i);

                }
            });
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String emailid=email.getText().toString().trim();
                    String pass=password.getText().toString().trim();
                    if(!emailid.isEmpty()&&!pass.isEmpty()) validateUser(emailid,pass);

                }
            });


        }
        void validateUser(String id,String pass){
            mAuth.signInWithEmailAndPassword(id, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
User user=new User();
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {

                       getdoc();
Intent intent=new Intent(login.this,MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("value", user);
                        intent.putExtras(bundle);

                        startActivity(intent);
                        finish();




                    }
                    else{
                        Toast.makeText(login.this, "ID or Password Incorrect", Toast.LENGTH_SHORT).show();
                    }

                }

                private void getdoc() {
                    String id =mAuth.getCurrentUser().getUid();

                    db.collection("Users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document != null) {
                                   user = document.toObject(User.class);
                                    Gson gson=new Gson();
                                    Map<String,Object> usermap=new HashMap<>();
                                    usermap=document.getData();
                                    usermap.get("First_name");
//                                    user.firstname= (String) usermap.get("First_name");
//                                    user.lastname= (String) usermap.get("Last_name");
//                                    user.knubs= (int) usermap.get("Knubs");
//                                    user.gender= (String) usermap.get("Gender");
//                                    user.weight= (float) usermap.get("Weight");
//                                    user.Dob= (Date) usermap.get("DOB");
//
//                                    user.height= (float) usermap.get("Height");
//                                    user.age= (int) usermap.get("Age");
//                                    user.phone= (String) usermap.get("Phone");

                                    Log.d("hrllp",document.toString());

                                        String userjson=gson.toJson(user);
                                    SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                                    prefsEditor.putString("User",userjson);
                                    prefsEditor.commit();

                                    Log.d("letsee","check karo kiyascene hai"+ user.Firstname);

                                }

                            } else {
                                Log.d("FragNotif", "get failed with ", task.getException());
                            }


                        }

                    });

                }
            });

        }





}
