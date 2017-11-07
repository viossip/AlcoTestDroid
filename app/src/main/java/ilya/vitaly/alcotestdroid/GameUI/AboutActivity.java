package ilya.vitaly.alcotestdroid.GameUI;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import ilya.vitaly.alcotestdroid.R;

public class AboutActivity extends AppCompatActivity {

    Animation animTranslate;
    private final int ANIMATION_DURATION = 300;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_about);

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
    }
}
