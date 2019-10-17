package worthywalk.example.com.worthywalk;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    Button signIn;
    TextView signUp,forgot;
    String token;
    public static String TAG="facebook login";
    User user = new User();
    private FirebaseAuth mAuth;
    EditText email, password;
    public static final String MyPREFERENCES = "MyPrefs";
    SharedPreferences sharedpreferences;
    FirebaseFirestore db;
    LoginButton loginButton;
    String id;

    FBuser fBuser;
    private static final String EMAIL = "email";
    // ..
// Initialize Firebase Auth
    CallbackManager callbackManager ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        callbackManager = CallbackManager.Factory.create();
        forgot=(TextView) findViewById(R.id.forgot);
        email = (EditText) findViewById(R.id.emailAddress);
        password = (EditText) findViewById(R.id.password);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL,"public_profile"));


forgot.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        String ema=email.getText().toString();
        if(ema.length()>3) {
            FirebaseAuth.getInstance().sendPasswordResetEmail(ema)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                                Toast.makeText(getApplicationContext(), "Email sent", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                Log.d(TAG, task.getException().getMessage());
                            }
                        }
                    });
        }else {
            Toast.makeText(getApplicationContext(),"Enter correct email address",Toast.LENGTH_LONG).show();
        }
    }
});
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code

            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(),"User Canceled",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code

                Toast.makeText(getApplicationContext(),exception.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        if (mAuth.getCurrentUser() != null) {
            id = mAuth.getCurrentUser().getUid();
        }
        signUp = (TextView) findViewById(R.id.login_signup);
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        //signIn=(Button)findViewById(R.id.btSignIn);
        signUp = (TextView) findViewById(R.id.login_signup);
        signIn = (Button) findViewById(R.id.btSignIn);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(login.this, Signup.class);
                startActivity(i);

            }
        });
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailid = email.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if (!emailid.isEmpty() && !pass.isEmpty()) validateUser(emailid, pass);

            }
        });


    }
    private void handleFacebookAccessToken(final AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getdoc(user.getUid());
//                            loaduserprofile(token);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                if(currentAccessToken==null){
                    mAuth.signOut();
                    Toast.makeText(getApplicationContext(),"User logged out",Toast.LENGTH_LONG).show();
                }else
                {
                    loaduserprofile(currentAccessToken);
                }
        }
    };

    public void loaduserprofile(final AccessToken accessToken){

        GraphRequest request=GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {

                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("email");
                    String id=object.getString("id");
                    String image_url="https://graph.facebook.com/"+id+"/picture?type-normal";
                    fBuser=new FBuser(first_name,last_name,email,image_url);
                    handleFacebookAccessToken(accessToken);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        Bundle parameters=new Bundle();
        parameters.putString("fields","first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();




    }

    void validateUser(final String emailid, String pass) {
        mAuth.signInWithEmailAndPassword(emailid, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            User user = new User();

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    getdoc(id=mAuth.getCurrentUser().getUid());


                } else {
                    Toast.makeText(login.this, "ID or Password Incorrect", Toast.LENGTH_SHORT).show();
                }

            }


        });

    }
    private void getdoc(String id) {


        db.collection("Users").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.getData()!=null) {
                        Log.d("document", document.getData().toString());
                        user = document.toObject(User.class);
                        Gson gson = new Gson();
                        Map<String, Object> usermap = new HashMap<>();
//                                    usermap=document.getData();

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

                        Log.d("hrllp", document.toString());

                        String userjson = gson.toJson(user);
                        SharedPreferences.Editor prefsEditor = sharedpreferences.edit();
                        prefsEditor.putString("User", userjson);
                        prefsEditor.commit();


                        sendnewtoken();

                    }else{
                        Log.d("enteredfbuser","checkking");
                        if(fBuser!=null){
                            Intent intent =new Intent(login.this,register.class);
                            intent.putExtra("fbuser",fBuser);
                            startActivity(intent);


                        }
                    }


                } else {
                    Log.d("FragNotif", "get failed with ", task.getException());
                }


            }

        });

    }
    public void sendnewtoken() {


                    token = sharedpreferences.getString("Token","");


                    if (token != null) {

                        Map<String, Object> doc = new HashMap<>();
                        doc.put("Token", token);
                        try {
<<<<<<< HEAD
                            db.collection("Users").document(id).collection("Token").document(token).set(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
=======
                            db.collection("Users").document(id).update(doc).addOnCompleteListener(new OnCompleteListener<Void>() {
>>>>>>> 154b1189317702729c2efc3a5975026cb8c951bc
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Void document = task.getResult();
                                    }

                                    Intent intent = new Intent(login.this, MainActivity.class);
                                    intent.putExtra("User", user);

                                    startActivity(intent);
                                    finish();

                                }

                            });
                        }catch (Exception e){
                            Log.d("error",e.getMessage());
                            Intent intent = new Intent(login.this, MainActivity.class);
                            intent.putExtra("User", user);

                            startActivity(intent);
                            finish();
                        }
                    }else {
                        Intent intent = new Intent(login.this, MainActivity.class);
                        intent.putExtra("User", user);

                        startActivity(intent);
                        finish();

                    }


//        db.collection("Users").document(id).update(doc);


                }



    }


