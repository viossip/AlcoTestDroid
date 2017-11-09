package ilya.vitaly.alcotestdroid.GameUI;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import ilya.vitaly.alcotestdroid.Entities.User;
import ilya.vitaly.alcotestdroid.R;

public class ListFragment extends Fragment{
    private ArrayList<User> users = new ArrayList<>();

    public ListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_fragment, container, false);
        ListView myListView = view.findViewById(R.id.List_view);
        /*DataSnapshot res =

        if (res.getCount() == 0)
        {
            // show message
        }

        else
        {
            int counter = 1;

            while(res.moveToNext())
            {
                String name = res.getString(0);
                int time = res.getInt(3);
                users.add(new User(name,time,counter++));

            }
        }*/

        /*TableAdapter tableAdapter = new TableAdapter(getActivity(),R.layout.table_adapter,users);
        myListView.setAdapter(tableAdapter);*/
        return view;
    }
}
