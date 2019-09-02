package worthywalk.example.com.worthywalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Avail extends AppCompatActivity {


    EditText e1,e2,e3,e4,e5;
    Button avail ,report;
    cardInfo card;
    ImageView banner;
    FirebaseAuth mAuth;
    User user=new User();


    StringBuilder sb=new StringBuilder();

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avail);
        banner=(ImageView) findViewById(R.id.image);
        e1=(EditText) findViewById(R.id.edit1);
        e2=(EditText) findViewById(R.id.edit2);
        e3=(EditText) findViewById(R.id.edit3);
        e4=(EditText) findViewById(R.id.edit4);
        e5=(EditText) findViewById(R.id.edit5);
        avail=(Button) findViewById(R.id.avail);
        report=(Button) findViewById(R.id.report);
        mAuth=FirebaseAuth.getInstance();
        Intent intent=getIntent();
        card= (cardInfo) intent.getSerializableExtra("card");
        user=(User) intent.getSerializableExtra("user");
        if(card!=null){
            Picasso.get().load(card.imgurl).fit().into(banner);
        }

        avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

          transaction();

            }
        });
        e1.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&e1.length()==1)
                {
                    sb.append(s);
                    e1.clearFocus();
                    e2.requestFocus();
                    e2.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if(sb.length()==1)
                {

                    sb.deleteCharAt(0);

                }

            }

            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {

                    e1.requestFocus();
                }

            }
        });

        e2.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&e2.length()==1)
                {
                    sb.append(s);
                    e2.clearFocus();
                    e3.requestFocus();
                    e3.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if(sb.length()==1)
                {

                    sb.deleteCharAt(0);

                }

            }

            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {

                    e2.requestFocus();
                }

            }
        });


        e3.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&e3.length()==1)
                {
                    sb.append(s);
                    e3.clearFocus();
                    e4.requestFocus();
                    e4.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if(sb.length()==1)
                {

                    sb.deleteCharAt(0);

                }

            }

            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {

                    e3.requestFocus();
                }

            }
        });


        e4.addTextChangedListener(new TextWatcher(){
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if(sb.length()==0&e4.length()==1)
                {
                    sb.append(s);
                    e4.clearFocus();
                    e5.requestFocus();
                    e5.setCursorVisible(true);

                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if(sb.length()==1)
                {

                    sb.deleteCharAt(0);

                }

            }

            public void afterTextChanged(Editable s) {
                if(sb.length()==0)
                {

                    e4.requestFocus();
                }

            }
        });




    }
    public void transaction(){
        final String id=mAuth.getUid();
        final FirebaseFirestore db=FirebaseFirestore.getInstance();

        Map<String,Object> map=new HashMap<>();
        final Map<String,Object> usermap=new HashMap<>();

        map.put("Dealid",card.deal_id);
        map.put("Userid",id);
        map.put("Totalbill",e5.getText().toString());
        map.put("Date", Calendar.getInstance().getTime());

        db.collection("Redeem").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    int knub=user.Knubs- Integer.parseInt(card.points);
                    user.Knubs=knub;

                    db.collection("Users").document(id).update("Knubs",knub).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
if(task.isSuccessful()){
    Intent returnIntent = new Intent();
    returnIntent.putExtra("result",user);
    setResult(Activity.RESULT_OK,returnIntent);
    finish();

}else{
    Toast.makeText(getApplicationContext(),"error updating",Toast.LENGTH_LONG).show();
}
                        }
                    });



                }
            }
        });




    }








}
