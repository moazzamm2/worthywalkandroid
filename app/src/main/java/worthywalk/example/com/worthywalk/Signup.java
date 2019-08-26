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

public class Signup extends AppCompatActivity {
    FloatingActionButton next;
    EditText emailid,password1,password2;
    final baseAuth mAuth=new Auth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        getSupportActionBar().hide();


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
                String id,p1,p2;
                id=emailid.getText().toString().trim();
                p1=password1.getText().toString().trim();
                p2=password2.getText().toString().trim();
                signUp(id,p1,p2);
            }
        });
    }
    public void signUp(String id,String p1,String p2){
        if(p1.equals(p2)){
            mAuth.createUserWithEmailAndPassword(id,p1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Intent i=new Intent(Signup.this,MainActivity.class);
                        i.putExtra("AfterLogin","false");
                        startActivity(i);
                        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
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
