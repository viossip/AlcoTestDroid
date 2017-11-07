package ilya.vitaly.alcotestdroid.GameUI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import ilya.vitaly.alcotestdroid.Controls.GetElements;
import ilya.vitaly.alcotestdroid.R;

public class ChooseGameActivity extends AppCompatActivity implements View.OnClickListener{

    private List<Button> buttonsInActivity;
    Animation animTranslate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_choose_game);

        buttonsInActivity = new ArrayList();
        GetElements.getAllButtons((ViewGroup)findViewById(android.R.id.content), buttonsInActivity);

        animTranslate = AnimationUtils.loadAnimation(this, R.anim.anim_translate);

        for (Button b : buttonsInActivity) {
            b.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        v.startAnimation(animTranslate);

        if(v.getId() != R.id.btn_return){
            Intent intent = new Intent(ChooseGameActivity.this, GameActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString( "type" ,v.getTag().toString());
            intent.putExtras(bundle);
            startActivity(intent);
        }
        else
            finish();
    }
}
