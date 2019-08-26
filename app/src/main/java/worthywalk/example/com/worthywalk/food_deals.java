package worthywalk.example.com.worthywalk;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class food_deals extends Fragment {

    RecyclerView rev;
    List<cardInfo> data = new ArrayList<>();
    private int dotscount;
    private ImageView[] dots;


    String request_url = "http://localhost/sliderjsonoutput.php";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.storerecycle, container, false);
        rev = (RecyclerView) rootview.findViewById(R.id.recyclerView);


        loadstore();

        RecyclerView.LayoutManager layout = new LinearLayoutManager(getActivity());
        rev.setLayoutManager(layout);

        return rootview;
    }

    private void loadstore() {
        load();

//        rev.addOnItemTouchListener(
//                new RecyclerItemClickListener(getContext(), rev, new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        // do whatever
//                        cardInfo a = data.get(position);
//                        Intent display = new Intent(getContext(), storecard.class);
//                        display.putExtra("sellectedoffer", a);
//                        startActivity(display);
//
//                    }
//
//                    @Override
//                    public void onLongItemClick(View view, int position) {
//                        // do whatever
//
//                    }
//                })
//        );

        storelistadapter adapter = new storelistadapter(data, getContext());
        rev.setAdapter(adapter);

    }

    private void load() {
//
//        DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
//
//        final DatabaseReference deals=mDatabase.child("deals");
//
//        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference databaseReference =    mFirebaseDatabase.getReference();
//        DatabaseReference brands=databaseReference.child("brands");
//
//        brands.orderByChild("category").equalTo("food").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                    final brand brand=childDataSnapshot.getValue(brand.class);
//
//                    FirebaseDatabase mFirebaseDatabase1 = FirebaseDatabase.getInstance();
//                    DatabaseReference databaseReference1 =    mFirebaseDatabase1.getReference();
//                    DatabaseReference deals=databaseReference1.child("deals");
//
//                    deals.orderByChild("brand_id").equalTo(brand.brand_id).addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            for(DataSnapshot dealSnapshot: dataSnapshot.getChildren()){
//                                String img_url= dealSnapshot.child("url").getValue(String.class);
//                                String points= dealSnapshot.child("points").getValue(Integer.class)+"  ";
//                                String deal_id=dealSnapshot.child("deal_id").getValue(String.class);
//                                data.add(new cardInfo(brand.getLogo_url(),img_url,brand.name,points,deal_id));
//                                store_adapter adapter=new store_adapter(data,getContext());
//                                rev.setAdapter(adapter);
//
//
//                            }

        data.add(new cardInfo("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAaVBMVEX///9+E0LZucemXX2vbImeTnG6gJmaR2z59Pa1d5Lu4OaIJlGWP2a8hJy4fZakWXqxcIzIm6+rZIPjy9X8+fvy5+yXQWfq2OCMLVfbvMqQNF3DkqjfxNDo1N327vLOprjTr7/Gl6zAjKNJNq4nAAAEi0lEQVR4nO2c6RqiIBhGsbRyT3Mpy7Lu/yJHUXAXmnEequc9v0y/0FMiu4QAAAAAAAAAAAAAAAAAAAAAAAAAALzDc7eE5z7iXvjp6i1/IVXkQfaagGwX8eCbYYvCN6pEDNGVaZpxaGIfQo3PFtG2dehNwuOzReyChgrvwo8X0fwq8iTzh3y4SHYvI68ykR8uolllpPcrIiZEIAIRiEAEIhCBCEQgAhGIQAQiEIEIRL5fxF9Z5KZKRF9XxFY2PlKsK7JX5UEiZ00RO1AmIjfqIdv366vzKG8uCZNYSsT2I+HZ/iepfhZdYpGm+VYUlCi8rxri22aJS1GZ+pvLYlSu2kKGrBTxVF/EGjhfI3JYJK5urTBajFGbz2uKrZMt5+NXEASCyky2d5WV6jWxL37+VoOh4sfvWelj6y5TtEuW7PZVoUgo4SFfRVFW+ZWcBiBdaVRXR5EbPZcWOcfCM/4nfqZhtXZT9/IrIuh8gAhEIAIRiEAEIhCBCEQgAhGIQAQiEPl+Edp/+PoBEToN4PIDIq8q8iAYDPoCEadeGvr89gWVZ9YxHX73Etek7WA3xSbKRPT9MuGjG53vfEG8uiErAAAAAAAAvp2AvnTJXK5URy59AdPEBL5N+84mr2xhvOjGutPjYo+lvkQzTdw5LEbd67aQMTrQm43m1dN+y9T+5bpH3OrUj4tBVj1H/Gwtp9WIjGa93XvdDG497XftJRUn/jMtUL+uqHl30TxzIv3JaApFjrbEv0ZmRQYtc3Uizfu8xOedFgkGPQzKRNImg4jnRU+KsIUxWU7UikRNBnnMRnCmRGKW0U2iWKRZxqZLpDUlwvq5EqJYpMkgSUQe/rbBD8syrfrkb6tJ7iT0yw++SQ5jkSO7saKxSMYT3PpXUrSfxpTHiTkM8E/EbfcZyyKsKDv1hgEyQpqFRdX4QHRufvOxCOvezWgJNBDpopPnaF+XcOqVdZuJWcZzIuzbu56Iw6fAU5F60xiLXNgDqyACkSMJFkX0qc7YCxmv05oTYUvYbJdYe8MweOVCRsRiGT0kUyLnMj1jPxBxDH4Wbd8e5yKJ6ZmePxDJkiSxl0XIi5nUVTzjDZGI3wxkUoRm9nwgQv+7JvHqfkwHItvOl7hIte8uEOHZVQveFWkXYKR5SVpMiaQDEVp71dvErYGIkadp+vwbEZ6haEnyhshrZsxAXsSaEOnypghfolNVGuVFNnNjH8pESM4Weto3eZF0dnXoeiKbN0XaH/d88wcitECcEIkSJm/4DGMVkQdfOlrc3xUZDVu2IinpNAw7Iiyj2526/2kVkd6FNSJVtSGWERk2KVoRJ3EcZyzisod22ElkHRF6xpobLxBtm12gaL1pvy7gTC1u64jwleDbbhrriHR4p2RnHJJudDa1lirhtV/e1zBomfBTdTofGpGQPOuNotrZKRAtdlyyrmUKRIildyijn6HeJ9yR6Ei3Xm5zzOyvt7Xo7mpY16Mb9MeL6y8H5FIfPVU7gzqB6klyZ8eJOzpjTq6jfRLNJgAAAAAAAAAAAAAAAAAAAAAAAAD8Mn8AXVlabrl4J4YAAAAASUVORK5CYII=", "https://www.brandsynario.com/wp-content/uploads/2019/04/ideas-sale-by-gulahmed.jpg", "Gul Ahmed", "400", "542"));
        data.add(new cardInfo("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAaVBMVEX///9+E0LZucemXX2vbImeTnG6gJmaR2z59Pa1d5Lu4OaIJlGWP2a8hJy4fZakWXqxcIzIm6+rZIPjy9X8+fvy5+yXQWfq2OCMLVfbvMqQNF3DkqjfxNDo1N327vLOprjTr7/Gl6zAjKNJNq4nAAAEi0lEQVR4nO2c6RqiIBhGsbRyT3Mpy7Lu/yJHUXAXmnEequc9v0y/0FMiu4QAAAAAAAAAAAAAAAAAAAAAAAAAALzDc7eE5z7iXvjp6i1/IVXkQfaagGwX8eCbYYvCN6pEDNGVaZpxaGIfQo3PFtG2dehNwuOzReyChgrvwo8X0fwq8iTzh3y4SHYvI68ykR8uolllpPcrIiZEIAIRiEAEIhCBCEQgAhGIQAQiEIEIRL5fxF9Z5KZKRF9XxFY2PlKsK7JX5UEiZ00RO1AmIjfqIdv366vzKG8uCZNYSsT2I+HZ/iepfhZdYpGm+VYUlCi8rxri22aJS1GZ+pvLYlSu2kKGrBTxVF/EGjhfI3JYJK5urTBajFGbz2uKrZMt5+NXEASCyky2d5WV6jWxL37+VoOh4sfvWelj6y5TtEuW7PZVoUgo4SFfRVFW+ZWcBiBdaVRXR5EbPZcWOcfCM/4nfqZhtXZT9/IrIuh8gAhEIAIRiEAEIhCBCEQgAhGIQAQiEPl+Edp/+PoBEToN4PIDIq8q8iAYDPoCEadeGvr89gWVZ9YxHX73Etek7WA3xSbKRPT9MuGjG53vfEG8uiErAAAAAAAAvp2AvnTJXK5URy59AdPEBL5N+84mr2xhvOjGutPjYo+lvkQzTdw5LEbd67aQMTrQm43m1dN+y9T+5bpH3OrUj4tBVj1H/Gwtp9WIjGa93XvdDG497XftJRUn/jMtUL+uqHl30TxzIv3JaApFjrbEv0ZmRQYtc3Uizfu8xOedFgkGPQzKRNImg4jnRU+KsIUxWU7UikRNBnnMRnCmRGKW0U2iWKRZxqZLpDUlwvq5EqJYpMkgSUQe/rbBD8syrfrkb6tJ7iT0yw++SQ5jkSO7saKxSMYT3PpXUrSfxpTHiTkM8E/EbfcZyyKsKDv1hgEyQpqFRdX4QHRufvOxCOvezWgJNBDpopPnaF+XcOqVdZuJWcZzIuzbu56Iw6fAU5F60xiLXNgDqyACkSMJFkX0qc7YCxmv05oTYUvYbJdYe8MweOVCRsRiGT0kUyLnMj1jPxBxDH4Wbd8e5yKJ6ZmePxDJkiSxl0XIi5nUVTzjDZGI3wxkUoRm9nwgQv+7JvHqfkwHItvOl7hIte8uEOHZVQveFWkXYKR5SVpMiaQDEVp71dvErYGIkadp+vwbEZ6haEnyhshrZsxAXsSaEOnypghfolNVGuVFNnNjH8pESM4Weto3eZF0dnXoeiKbN0XaH/d88wcitECcEIkSJm/4DGMVkQdfOlrc3xUZDVu2IinpNAw7Iiyj2526/2kVkd6FNSJVtSGWERk2KVoRJ3EcZyzisod22ElkHRF6xpobLxBtm12gaL1pvy7gTC1u64jwleDbbhrriHR4p2RnHJJudDa1lirhtV/e1zBomfBTdTofGpGQPOuNotrZKRAtdlyyrmUKRIildyijn6HeJ9yR6Ei3Xm5zzOyvt7Xo7mpY16Mb9MeL6y8H5FIfPVU7gzqB6klyZ8eJOzpjTq6jfRLNJgAAAAAAAAAAAAAAAAAAAAAAAAD8Mn8AXVlabrl4J4YAAAAASUVORK5CYII=", "https://www.brandsynario.com/wp-content/uploads/2019/04/ideas-sale-by-gulahmed.jpg", "Gul Ahmed", "400", "542"));
        data.add(new cardInfo("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAaVBMVEX///9+E0LZucemXX2vbImeTnG6gJmaR2z59Pa1d5Lu4OaIJlGWP2a8hJy4fZakWXqxcIzIm6+rZIPjy9X8+fvy5+yXQWfq2OCMLVfbvMqQNF3DkqjfxNDo1N327vLOprjTr7/Gl6zAjKNJNq4nAAAEi0lEQVR4nO2c6RqiIBhGsbRyT3Mpy7Lu/yJHUXAXmnEequc9v0y/0FMiu4QAAAAAAAAAAAAAAAAAAAAAAAAAALzDc7eE5z7iXvjp6i1/IVXkQfaagGwX8eCbYYvCN6pEDNGVaZpxaGIfQo3PFtG2dehNwuOzReyChgrvwo8X0fwq8iTzh3y4SHYvI68ykR8uolllpPcrIiZEIAIRiEAEIhCBCEQgAhGIQAQiEIEIRL5fxF9Z5KZKRF9XxFY2PlKsK7JX5UEiZ00RO1AmIjfqIdv366vzKG8uCZNYSsT2I+HZ/iepfhZdYpGm+VYUlCi8rxri22aJS1GZ+pvLYlSu2kKGrBTxVF/EGjhfI3JYJK5urTBajFGbz2uKrZMt5+NXEASCyky2d5WV6jWxL37+VoOh4sfvWelj6y5TtEuW7PZVoUgo4SFfRVFW+ZWcBiBdaVRXR5EbPZcWOcfCM/4nfqZhtXZT9/IrIuh8gAhEIAIRiEAEIhCBCEQgAhGIQAQiEPl+Edp/+PoBEToN4PIDIq8q8iAYDPoCEadeGvr89gWVZ9YxHX73Etek7WA3xSbKRPT9MuGjG53vfEG8uiErAAAAAAAAvp2AvnTJXK5URy59AdPEBL5N+84mr2xhvOjGutPjYo+lvkQzTdw5LEbd67aQMTrQm43m1dN+y9T+5bpH3OrUj4tBVj1H/Gwtp9WIjGa93XvdDG497XftJRUn/jMtUL+uqHl30TxzIv3JaApFjrbEv0ZmRQYtc3Uizfu8xOedFgkGPQzKRNImg4jnRU+KsIUxWU7UikRNBnnMRnCmRGKW0U2iWKRZxqZLpDUlwvq5EqJYpMkgSUQe/rbBD8syrfrkb6tJ7iT0yw++SQ5jkSO7saKxSMYT3PpXUrSfxpTHiTkM8E/EbfcZyyKsKDv1hgEyQpqFRdX4QHRufvOxCOvezWgJNBDpopPnaF+XcOqVdZuJWcZzIuzbu56Iw6fAU5F60xiLXNgDqyACkSMJFkX0qc7YCxmv05oTYUvYbJdYe8MweOVCRsRiGT0kUyLnMj1jPxBxDH4Wbd8e5yKJ6ZmePxDJkiSxl0XIi5nUVTzjDZGI3wxkUoRm9nwgQv+7JvHqfkwHItvOl7hIte8uEOHZVQveFWkXYKR5SVpMiaQDEVp71dvErYGIkadp+vwbEZ6haEnyhshrZsxAXsSaEOnypghfolNVGuVFNnNjH8pESM4Weto3eZF0dnXoeiKbN0XaH/d88wcitECcEIkSJm/4DGMVkQdfOlrc3xUZDVu2IinpNAw7Iiyj2526/2kVkd6FNSJVtSGWERk2KVoRJ3EcZyzisod22ElkHRF6xpobLxBtm12gaL1pvy7gTC1u64jwleDbbhrriHR4p2RnHJJudDa1lirhtV/e1zBomfBTdTofGpGQPOuNotrZKRAtdlyyrmUKRIildyijn6HeJ9yR6Ei3Xm5zzOyvt7Xo7mpY16Mb9MeL6y8H5FIfPVU7gzqB6klyZ8eJOzpjTq6jfRLNJgAAAAAAAAAAAAAAAAAAAAAAAAD8Mn8AXVlabrl4J4YAAAAASUVORK5CYII=", "https://www.brandsynario.com/wp-content/uploads/2019/04/ideas-sale-by-gulahmed.jpg", "Gul Ahmed", "400", "542"));
        data.add(new cardInfo("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAMgAAADICAMAAACahl6sAAAAaVBMVEX///9+E0LZucemXX2vbImeTnG6gJmaR2z59Pa1d5Lu4OaIJlGWP2a8hJy4fZakWXqxcIzIm6+rZIPjy9X8+fvy5+yXQWfq2OCMLVfbvMqQNF3DkqjfxNDo1N327vLOprjTr7/Gl6zAjKNJNq4nAAAEi0lEQVR4nO2c6RqiIBhGsbRyT3Mpy7Lu/yJHUXAXmnEequc9v0y/0FMiu4QAAAAAAAAAAAAAAAAAAAAAAAAAALzDc7eE5z7iXvjp6i1/IVXkQfaagGwX8eCbYYvCN6pEDNGVaZpxaGIfQo3PFtG2dehNwuOzReyChgrvwo8X0fwq8iTzh3y4SHYvI68ykR8uolllpPcrIiZEIAIRiEAEIhCBCEQgAhGIQAQiEIEIRL5fxF9Z5KZKRF9XxFY2PlKsK7JX5UEiZ00RO1AmIjfqIdv366vzKG8uCZNYSsT2I+HZ/iepfhZdYpGm+VYUlCi8rxri22aJS1GZ+pvLYlSu2kKGrBTxVF/EGjhfI3JYJK5urTBajFGbz2uKrZMt5+NXEASCyky2d5WV6jWxL37+VoOh4sfvWelj6y5TtEuW7PZVoUgo4SFfRVFW+ZWcBiBdaVRXR5EbPZcWOcfCM/4nfqZhtXZT9/IrIuh8gAhEIAIRiEAEIhCBCEQgAhGIQAQiEPl+Edp/+PoBEToN4PIDIq8q8iAYDPoCEadeGvr89gWVZ9YxHX73Etek7WA3xSbKRPT9MuGjG53vfEG8uiErAAAAAAAAvp2AvnTJXK5URy59AdPEBL5N+84mr2xhvOjGutPjYo+lvkQzTdw5LEbd67aQMTrQm43m1dN+y9T+5bpH3OrUj4tBVj1H/Gwtp9WIjGa93XvdDG497XftJRUn/jMtUL+uqHl30TxzIv3JaApFjrbEv0ZmRQYtc3Uizfu8xOedFgkGPQzKRNImg4jnRU+KsIUxWU7UikRNBnnMRnCmRGKW0U2iWKRZxqZLpDUlwvq5EqJYpMkgSUQe/rbBD8syrfrkb6tJ7iT0yw++SQ5jkSO7saKxSMYT3PpXUrSfxpTHiTkM8E/EbfcZyyKsKDv1hgEyQpqFRdX4QHRufvOxCOvezWgJNBDpopPnaF+XcOqVdZuJWcZzIuzbu56Iw6fAU5F60xiLXNgDqyACkSMJFkX0qc7YCxmv05oTYUvYbJdYe8MweOVCRsRiGT0kUyLnMj1jPxBxDH4Wbd8e5yKJ6ZmePxDJkiSxl0XIi5nUVTzjDZGI3wxkUoRm9nwgQv+7JvHqfkwHItvOl7hIte8uEOHZVQveFWkXYKR5SVpMiaQDEVp71dvErYGIkadp+vwbEZ6haEnyhshrZsxAXsSaEOnypghfolNVGuVFNnNjH8pESM4Weto3eZF0dnXoeiKbN0XaH/d88wcitECcEIkSJm/4DGMVkQdfOlrc3xUZDVu2IinpNAw7Iiyj2526/2kVkd6FNSJVtSGWERk2KVoRJ3EcZyzisod22ElkHRF6xpobLxBtm12gaL1pvy7gTC1u64jwleDbbhrriHR4p2RnHJJudDa1lirhtV/e1zBomfBTdTofGpGQPOuNotrZKRAtdlyyrmUKRIildyijn6HeJ9yR6Ei3Xm5zzOyvt7Xo7mpY16Mb9MeL6y8H5FIfPVU7gzqB6klyZ8eJOzpjTq6jfRLNJgAAAAAAAAAAAAAAAAAAAAAAAAD8Mn8AXVlabrl4J4YAAAAASUVORK5CYII=", "https://www.brandsynario.com/wp-content/uploads/2019/04/ideas-sale-by-gulahmed.jpg", "Gul Ahmed", "400", "542"));

        storelistadapter adapter = new storelistadapter(data, getContext());
        rev.setAdapter(adapter);


    }

}
