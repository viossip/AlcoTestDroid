package ilya.vitaly.alcotestdroid.GameUI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.wonderkiln.camerakit.CameraView;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import ilya.vitaly.alcotestdroid.R;

public class GameActivity extends AppCompatActivity {

    private enum AlcoholPosition {
        LEFT, RIGHT, FRONT, BACK, STABLE, FALL
    }

    private SensorManager sensorManager;
    private Sensor accelerometerSensor, magnetometerSensor, stepsSensor;

    private AlertDialog showPopup;
    private CameraView cameraView;
    private ProgressBar progressBar;
    private TextView txtTime;
    private ImageView imgAlcohol;
    private Button btnStartGame;
    public View v;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];

    private GameTimer gameTimer;

    private boolean zIsStable, yIsStable, isLoose, isPopupOpen;
    private int steps = -1;

    private long lastTime;
    private final int TIME_THRESHOLD = 100;
    private final int STEPS_TO_WIN = 21;
    private final double MAX_STABLE_Y_ANGEL = 0.05;
    private final double MAX_STABLE_Z_ANGEL = 0.05;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Disable screen shutdown.

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        txtTime = findViewById(R.id.txt_time);
        cameraView = findViewById(R.id.camera_view);
        imgAlcohol = findViewById(R.id.img_alcohol);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setMax(100);
        isPopupOpen = false;

        gameTimer = new GameTimer(60000, 1000, txtTime);
        steps = 0;
        startPopup();
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(stabilityListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(stabilityListener, magnetometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(stepCounterListener, stepsSensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onBackPressed(){
        Intent toLanding = new Intent(GameActivity.this, MainActivity.class);
        startActivity(toLanding);
        finish();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopSensors();
        gameTimer.cancel();
        cameraView.stop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        finish();
    }

    public void startPopup() {

        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_start_position, null, false);

        isPopupOpen = true;
        btnStartGame = view.findViewById(R.id.btn_start_game);

        dialogBuilder.setView(view);

        showPopup = dialogBuilder.show();
        showPopup.setView(v);
        showPopup.setOnKeyListener(new Dialog.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface arg0, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    showPopup.dismiss();
                    onBackPressed();
                }
                return true;
            }
        });
    }

    public class GameTimer extends CountDownTimer {
        public TextView txtTime;
        DateFormat formatter = new SimpleDateFormat("mm:ss");


        public GameTimer(long millisInFuture, long countDownInterval, TextView txtTime) {
            super(millisInFuture, countDownInterval);
            this.txtTime = txtTime;
        }

        @Override
        public void onTick(long millisToEnd) {
            txtTime.setText(formatter.format(millisToEnd));
        }
        @Override
        public void onFinish() {
            cameraView.stop();
            if (steps >= STEPS_TO_WIN) {
                stopSensors();
                finish();
            } else {
                changeAlcoholImg(AlcoholPosition.FALL);
            }
        }
    }

    public void enableStartButton() {
        btnStartGame.setBackgroundResource(R.drawable.button_start_now);
        btnStartGame.setClickable(true);

        btnStartGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPopupOpen = false;
                gameTimer.start();
                cameraView.start();
                showPopup.dismiss();
            }
        });
    }



    public SensorEventListener stabilityListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor orientationSensor, int acc) {
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor == accelerometerSensor) {
                System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
                mLastAccelerometerSet = true;
            } else if (event.sensor == magnetometerSensor) {
                System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
                mLastMagnetometerSet = true;
            }
            if (mLastAccelerometerSet && mLastMagnetometerSet && System.currentTimeMillis() - lastTime > TIME_THRESHOLD) {
                SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
                SensorManager.getOrientation(mR, mOrientation);
                getRotationDirections(mOrientation[1], mOrientation[2]);
                lastTime = System.currentTimeMillis();
            }
        }
    };

    public void changeAlcoholImg(AlcoholPosition alcoholPosition) {
        switch (alcoholPosition) {
            case STABLE:
                imgAlcohol.setBackgroundResource(R.drawable.whiskey_stable);
                imgAlcohol.setScaleX(1f);
                break;
            case RIGHT:
                imgAlcohol.setBackgroundResource(R.drawable.whiskey_right);
                imgAlcohol.setScaleX(1f);
                break;
            case LEFT:
                imgAlcohol.setBackgroundResource(R.drawable.whiskey_left);
                imgAlcohol.setScaleX(-1f);
                break;
            case FRONT:
                imgAlcohol.setBackgroundResource(R.drawable.whiskey_front);
                imgAlcohol.setScaleX(1f);
                break;
            case BACK:
                imgAlcohol.setBackgroundResource(R.drawable.whiskey_back);
                imgAlcohol.setScaleX(1f);
                break;
            case FALL:
                if (!isPopupOpen) {
                    //TODO: Loose outcome intent, stop game.
                }
                break;
        }
    }

    private void getRotationDirections(float y,  float z) {
        yIsStable = y >= -MAX_STABLE_Y_ANGEL && y <= MAX_STABLE_Y_ANGEL;
        zIsStable = z >= -MAX_STABLE_Z_ANGEL && z <= MAX_STABLE_Z_ANGEL;

        boolean ySlopedLeft = yIsStable && z < 0.15 && z > MAX_STABLE_Z_ANGEL;
        boolean ySlopedRight = yIsStable && z > -0.15 && z < -MAX_STABLE_Z_ANGEL;

        boolean zSlopedLeft = zIsStable && y > MAX_STABLE_Y_ANGEL && y <= 0.1;
        boolean zSlopedRight = zIsStable && y >= -0.1 && y < -MAX_STABLE_Y_ANGEL;

        isLoose = y > 0.1 || y < -0.1 || z > 0.15 || z < -0.15;

        if (yIsStable && zIsStable) {

            if (isPopupOpen) {
                enableStartButton();
            } else {
                changeAlcoholImg(AlcoholPosition.STABLE);
            }
        } else if (isPopupOpen) {

            btnStartGame.setBackgroundResource(R.drawable.button_stand_steadily);
            btnStartGame.setClickable(false);

        } else if (!isPopupOpen) {

            if (ySlopedLeft) {
                changeAlcoholImg(AlcoholPosition.LEFT);
            }
            if (ySlopedRight) {
                changeAlcoholImg(AlcoholPosition.RIGHT);
            }
            if (zSlopedLeft) {
                changeAlcoholImg(AlcoholPosition.FRONT);
            }
            if (zSlopedRight) {
                changeAlcoholImg(AlcoholPosition.BACK);
            }
            if (isLoose) {
                changeAlcoholImg(AlcoholPosition.FALL);
            }
        }
    }

    public SensorEventListener stepCounterListener = new SensorEventListener() {

        public void onAccuracyChanged(Sensor stepCounter, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            steps++;

            progressBar.setProgress(steps);
            if (steps >= STEPS_TO_WIN) {
                //TODO: Game loose intent and outcome activity, stop sensors.
            }
        }
    };

    public void stopSensors(){
        sensorManager.unregisterListener(stabilityListener);
        sensorManager.unregisterListener(stepCounterListener);
    }
}
