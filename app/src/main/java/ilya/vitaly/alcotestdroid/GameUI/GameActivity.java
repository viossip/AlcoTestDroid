package ilya.vitaly.alcotestdroid.GameUI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wonderkiln.camerakit.CameraView;

import ilya.vitaly.alcotestdroid.R;

public class GameActivity extends AppCompatActivity {

    private CameraView cameraView;
    private ProgressBar progressBar;
    private TextView txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);

        txtTime = findViewById(R.id.txt_time);
        cameraView = findViewById(R.id.camera_view);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setMax(100);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }


}
