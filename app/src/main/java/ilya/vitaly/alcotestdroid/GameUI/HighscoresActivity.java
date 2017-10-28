package ilya.vitaly.alcotestdroid.GameUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import ilya.vitaly.alcotestdroid.R;

public class HighscoresActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_highscores);
    }
}
