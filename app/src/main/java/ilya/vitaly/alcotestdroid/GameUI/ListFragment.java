package ilya.vitaly.alcotestdroid.GameUI;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;

import ilya.vitaly.alcotestdroid.Entities.Game;
import ilya.vitaly.alcotestdroid.Entities.User;
import ilya.vitaly.alcotestdroid.R;

import static android.content.ContentValues.TAG;

public class ListFragment extends Fragment{
    private ArrayList<User> users = new ArrayList<>();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    String listType;



    public void setListType(String listType) {
        this.listType = listType;
    }

    public ListFragment() {

        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_fragment, container, false);
        ListView myListView = view.findViewById(R.id.List_view);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference("Users");

       // Read from DB
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            private void showData(DataSnapshot dataSnapshot){

                DataSnapshot usersDB = dataSnapshot.child("ID");
                for(DataSnapshot ds : usersDB.getChildren()){
                    String key = (String) ds.getKey();
                    User user = new User();
                    String name = (ds.child("name").getValue(String.class)); //set the name
                    String email = (ds.child("email").getValue(String.class));
                    String id = ((ds).child("id").getValue(String.class));
                    user.setID(id);
                    user.setName(name);
                    user.setEmail(email);
                    users.add(user);
//                            users.add(uInfo.getName());
//                            array.add(uInfo.getEmail());
//                            array.add(uInfo.getPhone_num());
                        }
                    }








            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });


//        if (res.getCount() == 0)
//        {
//            // show message
//        }
//
//        else
//        {
//            int counter = 1;
//
//            while(res.moveToNext())
//            {
//                String name = res.getString(0);
//                int time = res.getInt(3);
//                users.add(new User(name,time,counter++));
//
//            }
//        }*/


        TableAdapter tableAdapter = new TableAdapter(getActivity(),R.layout.table_adapter,users);
        myListView.setAdapter(tableAdapter);
        return view;
    }

    
}
