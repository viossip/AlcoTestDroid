package ilya.vitaly.alcotestdroid.GameUI;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import java.util.ArrayList;

import ilya.vitaly.alcotestdroid.Entities.User;

class TableAdapter extends ArrayAdapter<User> {
    private Context context;
    private int layoutResourceId;
    private ArrayList<User> users;

    public TableAdapter(Context context, int layoutResourceId, ArrayList<User> users) {

        super(context, layoutResourceId, users);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.users = users;
    }

    public View getView(int position, View row, ViewGroup parent) {

            /*String name = users.get(position).getName();
            int time = users.get(position).getTime();
            int id = users.get(position).getID();*/

            /*LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResourceId, parent, false);

            TextView Txnumber = row.findViewById(R.id.number);
            TextView Txname = row.findViewById(R.id.name);
            TextView Txtime = row.findViewById(R.id.score);
            String strScore = (String.format("%02d:%02d", ((time % 3600) / 60), time % 60));
            Txnumber.setText(id + "");
            Txname.setText(name);
            Txtime.setText(strScore + "");*/

        return row;
    }
}