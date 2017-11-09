package ilya.vitaly.alcotestdroid.GameUI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ilya.vitaly.alcotestdroid.Entities.Player;
import ilya.vitaly.alcotestdroid.R;
import ilya.vitaly.alcotestdroid.Services.GpsLocation;

public class OutcomeActivity extends AppCompatActivity {

    private Animation animTranslate;
    private final int ANIMATION_DURATION = 300;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private FirebaseDatabase db;
    private DatabaseReference dbRef;
    private TextView outcomeText, resultsText;
    private ImageView imageView;

    private int[] gameResultList;
    private double[] currentLocation;
    private String gameType;
    private String userName;
    private boolean isWin;
    private int timeSecs, steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_outcome);

        animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);
        outcomeText = findViewById(R.id.outcome_text);
        imageView = findViewById(R.id.winner_looser);
        resultsText = findViewById(R.id.results_text);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference("Players");

        Bundle bundle = getIntent().getExtras();
        isWin = bundle.getBoolean("isWin");
        gameResultList = bundle.getIntArray("results");
        gameType = bundle.getString("type");

        currentLocation = bundle.getDoubleArray("location");

        timeSecs = gameResultList[0];
        steps = gameResultList[1];

        if(user != null)
            userName = user.getEmail().split("\\@")[0].substring(0, 1).toUpperCase() + user.getEmail().substring(1);
        else
            userName = "Guest";

        if(isWin && inLeaderBoard()){
            if(userExists()){

            }else{
                currentLocation = bundle.getDoubleArray("location");
                dbRef.setValue(new Player(user.getEmail(),
                        String.valueOf(timeSecs), String.valueOf(steps), gameType));
            }
        }

        outcomeText.setText(userName + ", you are: " );
        imageView.setImageResource(isWin ? R.drawable.winner: R.drawable.looser);


        Button exitBtn = findViewById(R.id.btn_ok);
        Button tryAgain = findViewById(R.id.btn_try_again);

        exitBtn.setOnClickListener(new View.OnClickListener() {
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

        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animTranslate);
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(OutcomeActivity.this, GameActivity.class);
                                startActivity(intent);
                                finish();
                            } }, ANIMATION_DURATION);
            }
        });
    }

    private boolean userExists() {
        return false;
    }

    private boolean inLeaderBoard() {
        return true;
    }
}
