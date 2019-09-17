package worthywalk.example.com.worthywalk;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class homeFragment extends Fragment {
    TextView name;
    Button startactivity;
    User user;
    LoginManager loginManager;
    public homeFragment(User user) {
    this.user=user;
    }

ImageButton logout;
FirebaseAuth mAuth;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.dashboard,container,false);

       logout=(ImageButton) view.findViewById(R.id.logout);
    startactivity=(Button) view.findViewById(R.id.startactivitybutton);
    name=(TextView) view.findViewById(R.id.t1name);
    mAuth=FirebaseAuth.getInstance();
    if(user!=null){
        String fullname=user.Firstname+" "+user.Lastname;
        name.setText(fullname);

    }

logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        mAuth.signOut();
        loginManager.getInstance().logOut();
        Intent intent=new Intent(getActivity(),login.class);
        startActivity(intent);
        getActivity().finish();
    }
});

    startactivity.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(getActivity(),WalkActivity.class);
            intent.putExtra("User",user);
            startActivity(intent);
        }
    });

    return view;
    }
}
