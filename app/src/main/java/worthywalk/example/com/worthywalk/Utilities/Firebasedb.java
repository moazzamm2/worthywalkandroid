package worthywalk.example.com.worthywalk.Utilities;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import worthywalk.example.com.worthywalk.Models.cardInfo;

public class Firebasedb {
    FirebaseFirestore db;





    public  Firebasedb(){
        db=FirebaseFirestore.getInstance();
    }

    public ArrayList<String> getstoreads(){
        final ArrayList<String> mresources=new ArrayList<>();
        db.collection("StoreAds").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                int i = 0;
                for (Iterator<QueryDocumentSnapshot> it = queryDocumentSnapshots.iterator(); it.hasNext(); ) {
                    QueryDocumentSnapshot doc = it.next();

                    mresources.add(doc.getString("Banner"));
                    i++;

                }

            };


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
           mresources.add("https://firebasestorage.googleapis.com/v0/b/worthywalk-6b82e.appspot.com/o/front.png?alt=media&token=5a434a41-7bca-4229-8ff9-ae0f9ece2220");
            }
        });

        return mresources;


    }
    public List<cardInfo> getstorecards(String Category){
        final List<cardInfo> carddata=new ArrayList<>();

        if(Category.equals("All")){
            db.collection("Deals")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    String id=doc.getId();
                                    carddata.add(new cardInfo((List<String>) doc.get("Location"),doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"),  String.valueOf(doc.get("Knubs")), id,String.valueOf(doc.get("Passcode")),doc.getBoolean("Online"),doc.getString("Brandid"),doc.getString("Description")));
                                }

                            } else {
                                Log.w("loaddata", "Error getting documents.", task.getException());
                            }
                        }


                    });
        }else {
            db.collection("Deals").whereEqualTo("Category",Category)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    String id = doc.getId();
                                    carddata.add(new cardInfo((List<String>) doc.get("Location"),doc.getString("Logo"), doc.getString("Banner"), doc.getString("Brandname"), String.valueOf(doc.get("Knubs")), id, String.valueOf(doc.get("Passcode")), doc.getBoolean("Online"), doc.getString("Brandid"), doc.getString("Description")));
                                }

                                } else {
                                Log.w("loaddata", task.getException().getMessage(), task.getException());
                            }
                        }


                    });
        }



        return carddata;
    }














}
