package worthywalk.example.com.worthywalk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static worthywalk.example.com.worthywalk.App.CHANNEL_ID;

public class Avail extends AppCompatActivity {


    EditText e1,e2,e3,e4,e5;
    Button avail ,report;
    cardInfo card;
    ImageView banner;
    FirebaseAuth mAuth;
    User user;
    String pass;
    String passcode;
    String message;
        TextView text;
    final String[] Selected = new String[]{"January", "February", "March", "April",
            "May", "June", "July", "Augest", "September", "October", "November", "December"};

    StringBuilder sb=new StringBuilder();
    String promo;
    int month;

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
        text=(TextView) findViewById(R.id.text5);
        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH);
        avail=(Button) findViewById(R.id.avail);

        mAuth=FirebaseAuth.getInstance();

        Intent intent=getIntent();

        card= (cardInfo) intent.getSerializableExtra("card");
        if(card.online){
            e5.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);

        }else {
            e5.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);

        }
        user=(User) intent.getSerializableExtra("user");
        if(card!=null){
            Picasso.get().load(card.imgurl).fit().into(banner);
            passcode=card.passcode;
        }

        if(card.Fb.length()>0) {
            message = "Get this Passcode from the Brands Fb page here is their username " + card.Fb;
            text.setText(message);
        }
        avail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(user.Knubs>=Integer.parseInt(card.points)){
                    if(e1.getText().toString().length()>0 || +e2.getText().toString().length()>0 ||+e3.getText().toString().length()>0 ||+e4.getText().toString().length()>0) {
                        pass=e1.getText().toString()+e2.getText().toString()+e3.getText().toString()+e4.getText().toString();
                        if(pass.equals(passcode))                   transaction();
                        else Toast.makeText(getApplicationContext(),"Type Valid Passcode",Toast.LENGTH_LONG).show();
                    }                    else Toast.makeText(getApplicationContext(),"Enter Passcode",Toast.LENGTH_LONG).show();

                }else{
                    Toast.makeText(getApplicationContext(),"You Dont have enough knubs ! Start Earning . ",Toast.LENGTH_LONG).show();
                }

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
                else if(sb.length()>1){
                    e1.setText(s.subSequence(1, s.length()));
                    e1.setSelection(1);
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

                }     else if(sb.length()>1){
                    e2.setText(s.subSequence(1, s.length()));
                    e2.setSelection(1);
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

                }     else if(sb.length()>1){
                    e3.setText(s.subSequence(1, s.length()));
                    e3.setSelection(1);
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

                }     else if(sb.length()>1){
                    e4.setText(s.subSequence(1, s.length()));
                    e4.setSelection(1);
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


        if(!card.online) {
            final Map<String, Object> map = new HashMap<>();
            final Map<String, Object> usermap = new HashMap<>();

            map.put("Dealid", card.deal_id);
            map.put("Userid", id);
            map.put("Totalbill", e5.getText().toString());
            map.put("Date", Calendar.getInstance().getTime());
            int knub = user.Knubs - Integer.parseInt(card.points);
            user.Knubs = knub;


            usermap.put("Knubs",knub);

            final DocumentReference docRef=db.collection("Redeem").document();
            final DocumentReference docRef2=db.collection("Users").document(id);

            db.runTransaction(new Transaction.Function<Void>() {
                @Nullable
                @Override
                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {


                    transaction.set(docRef,map);
                    transaction.update(docRef2,usermap);
                    return null;
                }
            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    OutletAlertdialog();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "error updating", Toast.LENGTH_LONG).show();

                }
            });

        }else {


            db.collection("Promocodes").document(card.deal_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        final DocumentSnapshot snapshot=task.getResult();
//                        final Map<String, Object> map = new HashMap<>();

//                        List<Promocodes> group = (List<Promocodes>) snapshot.get("Promocodes");
                        List<String> group = (List<String>) snapshot.get("Promocodes");

                       promo=group.get(0);


                        if(promo!=null){
                            final Map<String, Object> map = new HashMap<>();
                            final Map<String, Object> usermap = new HashMap<>();
                            final Map<String, Object> promomap = new HashMap<>();
                            final Map<String, Object> dealmap = new HashMap<>();
                            final Map<String, Object> brandmap = new HashMap<>();
                            map.put("Dealid", card.deal_id);
                            map.put("Userid", id);
                            map.put("Promocode",promo);
                            map.put("Date", Calendar.getInstance().getTime());
                            int knub = user.Knubs - Integer.parseInt(card.points);
                            user.Knubs = knub;
                            group.remove(0);
                            promomap.put("Promocodes",group);

                            usermap.put("Knubs",knub);
                            db.runTransaction(new Transaction.Function<Void>() {
                                @Nullable
                                @Override
                                public Void apply(@NonNull Transaction transaction) throws FirebaseFirestoreException {
                                    final DocumentReference docRef=db.collection("Onlineredeem").document();

                                    final DocumentReference docRef4=db.collection("Deal").document(card.Brand_id);
                                    final DocumentReference docRef5=db.collection("Brand").document(card.Brand_id).collection("Sales").document(Selected[month]);


                                    final DocumentReference docRef2=db.collection("Users").document(id);
                                    final DocumentReference docRef3=db.collection("Promocodes").document(card.deal_id);

                                    DocumentSnapshot dealsnap=transaction.get(docRef4);
                                    DocumentSnapshot brandsnap=transaction.get(docRef5);
                                    long dealcount= dealsnap.getLong("Count");
                                    long brandsalecount= brandsnap.getLong("Count");
                                    dealcount++;
                                    brandsalecount++;
                                    dealmap.put("Count",dealcount);
                                    brandmap.put("Count",brandsalecount);
                                    transaction.set(docRef,map);
                                    transaction.update(docRef2,usermap);
                                    transaction.update(docRef3,promomap);
                                    transaction.update(docRef4,dealmap);
                                    transaction.update(docRef5,brandmap);

                                    return null;
                                }
                            }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    createNotification(promo);
                                    Alertdialog();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

                                }
                            });

                        }



                    }
                }
            });




        }



    }


    public void createNotification(String promocode) {
        Intent intent = new Intent( this , MainActivity. class );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent resultIntent = PendingIntent.getActivity( this , 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri notificationSoundURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mNotificationBuilder = new NotificationCompat.Builder( this,CHANNEL_ID);
        mNotificationBuilder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("PROMOCODE")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("Here is your Promocode Please Screenshot it ! >>>" + promo +"<<< If You still loose your promocode just send us problem through suggestions we will get back to you"

                        ))
                .setAutoCancel( true )
                .setSound(notificationSoundURI)
                .setContentIntent(resultIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, mNotificationBuilder.build());
    }
    private void Alertdialog() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Avail.this);
        alertDialogBuilder.setTitle("THANKYOU");
        alertDialogBuilder.setMessage("YOUR KNUBS ARE NOW "+String.valueOf(user.Knubs)+" !\n Your Promocode for " + card.Name+ " is \n  >>>"+ promo+"<<<  \nUse it in thier Online store while buying out remember or Screenshot it so you dont lose it . \nHAPPY SHOPPING ! " );
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", user);
//                        returnIntent.putExtra("Promo", promo);
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }
    private void OutletAlertdialog() {


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Avail.this);
        alertDialogBuilder.setTitle("THANKYOU");
        alertDialogBuilder.setMessage("YOUR KNUBS ARE NOW"+String.valueOf(user.Knubs)+"!\n" +" Have Fun at"+ card.Name+"!" );
        alertDialogBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {


                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result", user);

                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();

                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();



    }

}
