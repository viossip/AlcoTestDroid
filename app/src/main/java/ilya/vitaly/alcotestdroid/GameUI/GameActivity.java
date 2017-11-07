package ilya.vitaly.alcotestdroid.GameUI;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.CountDownTimer;
import android.os.Handler;
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
    private AnimationDrawable animation;
    private ImageView endGameAnimation;

    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mR = new float[9];
    private float[] mOrientation = new float[3];
    private int[] gameResultList;
    private double[] currentLocation;
    private DateFormat formatter = new SimpleDateFormat("mm:ss");

    private GameTimer gameTimer;

    private boolean zIsStable, yIsStable, isLoose, isPopupOpen;
    private int steps = -1;
    private int steps_to_win;
    private long time_to_win;
    private long current_time;
    private String gameType;

    private long lastTime;
    private final int TIME_THRESHOLD = 100;
    private final double MAX_STABLE_Y_ANGEL = 0.1;
    private final double MAX_STABLE_Z_ANGEL = 0.1;
    private static final int ANIMATION_DURATION = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_game);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); //Disable screen shutdown.
        setGameType(getIntent().getExtras());

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        stepsSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        txtTime = findViewById(R.id.txt_time);
        cameraView = findViewById(R.id.camera_view);
        imgAlcohol = findViewById(R.id.img_alcohol);
        progressBar = findViewById(R.id.progress_bar);
        endGameAnimation = findViewById(R.id.end_game_animation);

        progressBar.setMax(steps_to_win);
        isPopupOpen = false;

        gameTimer = new GameTimer(time_to_win, 1000, txtTime);
        steps = 0;
        startPopup();
    }

    private void setGameType(Bundle extras) {
        switch (extras.getString("type")){
            case "Simple":
                steps_to_win = 10;
                time_to_win = 40 * 1000;
                gameType = "Simple";
                break;
            case "Advanced":
                steps_to_win = 20;
                time_to_win = 20* 1000;
                gameType = "Advanced";
                break;
            case "Training":
                steps_to_win = -1;
                time_to_win = -1;
                gameType = "Advanced";
                break;
        }
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

        public GameTimer(long millisInFuture, long countDownInterval, TextView txtTime) {
            super(millisInFuture, countDownInterval);
            this.txtTime = txtTime;
        }

        @Override
        public void onTick(long millisToEnd) {
            current_time = time_to_win - millisToEnd;
            txtTime.setText(formatter.format(millisToEnd));
        }
        @Override
        public void onFinish() {
            if(time_to_win!= -1)
                //cameraView.stop();
                if (steps >= steps_to_win) {
                    stopSensors();
                    onGameEnd(true);
                } else {
                    changeAlcoholImg(AlcoholPosition.FALL);
                    onGameEnd(false);
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
                    stopSensors();
                    onGameEnd(false);

                    //TODO: Loose outcome intent, stop game.

                }
                break;
        }
    }

    private void onGameEnd(boolean isWin) {
        endGameAnimation(isWin);

        gameResultList= new int [3];
        gameResultList[0]= isWin ? 1 : 0;
        gameResultList[1]=(int) (current_time / 1000) % 60 ; // seconds
        gameResultList[2]= steps;

        currentLocation = new double[2];
        //currentLocation[0] = gps.getLatitude();
        //currentLocation[1] = gps.getLongitude();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(GameActivity.this, OutcomeActivity.class);
                bundle.putIntArray("results", gameResultList);
                bundle.putString("type", gameType);
                bundle.putDoubleArray("location", currentLocation);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }, ANIMATION_DURATION);
    }

    private void getRotationDirections(float y,  float z) {
        yIsStable = y >= -MAX_STABLE_Y_ANGEL && y <= MAX_STABLE_Y_ANGEL;
        zIsStable = z >= -MAX_STABLE_Z_ANGEL && z <= MAX_STABLE_Z_ANGEL;

        boolean ySlopedLeft = yIsStable && z < 0.2 && z > MAX_STABLE_Z_ANGEL;
        boolean ySlopedRight = yIsStable && z > -0.2 && z < -MAX_STABLE_Z_ANGEL;

        boolean zSlopedLeft = zIsStable && y > MAX_STABLE_Y_ANGEL && y <= 0.15;
        boolean zSlopedRight = zIsStable && y >= -0.15 && y < -MAX_STABLE_Y_ANGEL;

        isLoose = y > 0.15 || y < -0.15 || z > 0.2 || z < -0.2;

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
            if (!isPopupOpen) {
                steps++;
                progressBar.setProgress(steps);
                if (steps >= steps_to_win && steps_to_win != -1) {
                    stopSensors();
                    onGameEnd(true);
                }
            }
        }
    };

    public void endGameAnimation(boolean isWin){
        if(isWin)
            endGameAnimation.setBackgroundResource(R.drawable.animation_win);
        else
            endGameAnimation.setBackgroundResource(R.drawable.animation_loss);

        gameTimer.cancel();
        animation = (AnimationDrawable) endGameAnimation.getBackground();
        animation.start();
    }

    public void stopSensors(){
        sensorManager.unregisterListener(stabilityListener);
        sensorManager.unregisterListener(stepCounterListener);
    }
}
