package worthywalk.example.com.worthywalk;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    FloatingActionButton next;
    EditText emailid,password1,password2;
    FirebaseAuth mAuth;
    String Email="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getSupportActionBar().hide();

        setContentView(R.layout.activity_signup);
        mAuth=FirebaseAuth.getInstance();



        emailid=(EditText)findViewById(R.id.emailid);
        password1=(EditText)findViewById(R.id.password1);
        password2=(EditText)findViewById(R.id.password2);
//
//        back=(ImageButton)findViewById(R.id.backButton);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

        next=(FloatingActionButton)findViewById(R.id.reg1_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailid.getText().toString().isEmpty() || password1.getText().toString().isEmpty() || password2.getText().toString().isEmpty()){
                    Toast.makeText(Signup.this, "Error Please enter the credentials", Toast.LENGTH_SHORT).show();
                } else{

                    String id,p1,p2;
                    id=emailid.getText().toString().trim();
                    p1=password1.getText().toString().trim();
                    p2=password2.getText().toString().trim();
                    signUp(id,p1,p2);


                    }



            }
        });
    }
    public void signUp(final String id, String p1, String p2){
        if(p1.equals(p2)){
            mAuth.createUserWithEmailAndPassword(id,p1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Email=id;
                        Intent i=new Intent(Signup.this,register.class);
                        i.putExtra("AfterLogin","false");
                        i.putExtra("Email",Email);
                        startActivity(i);
//                        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
                        finish();
                    }
                    else{
                        Toast.makeText(Signup.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }

}
