package worthywalk.example.com.worthywalk;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

interface baseAuth {
   Task<AuthResult> signInWithEmailAndPassword(String email, String password);
    Task<AuthResult> createUserWithEmailAndPassword(String email, String password);
   String currentUser();
   void signOut();

}

class Auth implements baseAuth{
    final FirebaseAuth _firebaseAuth=FirebaseAuth.getInstance();


    @Override
    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        final Task<AuthResult> user = _firebaseAuth.signInWithEmailAndPassword(email, password);
        return user;

    }

    @Override
    public Task<AuthResult> createUserWithEmailAndPassword(String email, String password) {
        final Task<AuthResult> user =_firebaseAuth.createUserWithEmailAndPassword(email, password);
        return user;
    }

    @Override
    public String currentUser() {
     final FirebaseUser firebaseUser=_firebaseAuth.getCurrentUser();
     return firebaseUser.getUid();
    }

    @Override
    public void signOut() {
         _firebaseAuth.signOut();
         return;
    }
}
