package worthywalk.example.com.worthywalk;

import android.content.Intent;
import android.os.Bundle;
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

public class login extends AppCompatActivity {
        Button signIn;
        TextView signUp;
        final baseAuth mAuth=new Auth();
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_login);

            //signIn=(Button)findViewById(R.id.btSignIn);
            signUp=(TextView)findViewById(R.id.login_signup);
            signIn = (Button)findViewById(R.id.btSignIn);

            signUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(login.this,Signup.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                }
            });
            signIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText a=(EditText)findViewById(R.id.emailAddress);
                    String emailid=a.getText().toString().trim();
                    a=(EditText)findViewById(R.id.password);
                    String pass=a.getText().toString().trim();
                    if(!emailid.isEmpty()&&!pass.isEmpty()) validateUser(emailid,pass);

                }
            });


        }
        void validateUser(String id,String pass){
            mAuth.signInWithEmailAndPassword(id, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        startActivity(new Intent(login.this, MainActivity.class));
                        finish();
                    }
                    else{
                        Toast.makeText(login.this, "ID or Password Incorrect", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }





}
