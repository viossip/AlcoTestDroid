package ilya.vitaly.alcotestdroid.GameUI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ilya.vitaly.alcotestdroid.Controls.GetElements;
import ilya.vitaly.alcotestdroid.R;
import ilya.vitaly.alcotestdroid.Services.PermissionsHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Intent intent;
    private final int ANIMATION_DURATION = 300;
    private List<Button> buttonsInActivity;
    Animation animAlpha, animTranslate;

    private boolean clicked = false;
    private boolean hasPermittions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        buttonsInActivity = new ArrayList();
        GetElements.getAllButtons((ViewGroup)findViewById(android.R.id.content), buttonsInActivity);

        animAlpha = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);

        for (Button b : buttonsInActivity) {
            b.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(animTranslate);
        hasPermittions = PermissionsHandler.requestPermissions(this);

        switch (v.getId()) {
            case R.id.btn_help:
                intent = new Intent(MainActivity.this, HelpActivity.class);
                break;
            case R.id.btn_start_game:
                intent = new Intent(MainActivity.this, ChooseGameActivity.class);
                break;
            case R.id.btn_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                break;
            case R.id.btn_highscores:
                intent = new Intent(MainActivity.this, HighscoresActivity.class);
                break;
            case R.id.btn_login :
                intent = new Intent(MainActivity.this, LoginActivity.class);
                break;
            case R.id.btn_exit_game:
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                finish();
                System.exit(0);
                break;
        }

        if(intent != null){
            if(hasPermittions)
                new Handler().postDelayed(
                        new Runnable() {
                            @Override
                            public void run() { MainActivity.this.startActivity(intent);
                            } }, ANIMATION_DURATION);
            else
                notifyUserPermissions();
        }
    }

    private void notifyUserPermissions() {
        Toast.makeText(this, "Please grant all requested permissions", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (!clicked) {
            Toast.makeText(this, "Click 'Back' button again to exit", Toast.LENGTH_SHORT).show();
            clicked = true;
        } else {
            finish();
        }
    }
}
