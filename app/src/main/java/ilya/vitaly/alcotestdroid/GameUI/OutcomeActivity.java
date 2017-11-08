package ilya.vitaly.alcotestdroid.GameUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;

import ilya.vitaly.alcotestdroid.R;
import ilya.vitaly.alcotestdroid.Services.GpsLocation;

public class OutcomeActivity extends AppCompatActivity {

    GpsLocation gps;
    Animation animTranslate;
    private final int ANIMATION_DURATION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_outcome);
    }
}
