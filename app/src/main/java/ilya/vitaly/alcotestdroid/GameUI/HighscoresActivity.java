package ilya.vitaly.alcotestdroid.GameUI;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;

import ilya.vitaly.alcotestdroid.R;

public class HighscoresActivity extends AppCompatActivity {

    Animation animTranslate;
    private final int ANIMATION_DURATION = 300;

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


        animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);

        Button returnButton = findViewById(R.id.btn_return);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() { finish();
                            } }, ANIMATION_DURATION);
            }
        });

//        ImageButton returnButton = (ImageButton)findViewById(R.id.btnReturn);

    }
}
