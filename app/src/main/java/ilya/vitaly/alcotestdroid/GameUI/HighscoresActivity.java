package ilya.vitaly.alcotestdroid.GameUI;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import ilya.vitaly.alcotestdroid.R;

public class HighscoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_highscores);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        ListFragment lf = new ListFragment();
        fragmentTransaction.add(R.id.frame_container,lf);
        fragmentTransaction.commit();

        ImageButton tableButton = (ImageButton)findViewById(R.id.btnTable);
        tableButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.frame_container)).commit();
                ListFragment lf = new ListFragment();
                fragmentTransaction.add(R.id.frame_container,lf);
                fragmentTransaction.commit();
            }
        } );



//        ImageButton returnButton = (ImageButton)findViewById(R.id.btnReturn);

    }
}
