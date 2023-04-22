package com.example.a41;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity {
    Button button, buttonR;
    CountDownTimer countDownTimer, countDownTimerR;

    Boolean isPaused = false;
    Boolean isPausedR = false;
    Boolean doneWorkout = false;
    Boolean doneRest = false;
    Integer timeRemain,timeRemainR;
    ProgressBar progressBar, progressBarR;


    public void timerReset(View view) { //This function reset related boolean and time parameter to initial status on both timers.
        resetRest();
        resetWorkout();

    }
    public void resetRest(){//This function reset related boolean and time parameter to initial status on Rest time timer.
        TextView textViewR = findViewById(R.id.textViewTimerRest);
        if (countDownTimerR != null){
            countDownTimerR.cancel();
            progressBarR.setProgress(0);
            isPausedR = true;
            countDownTimerR = null;
            timeRemainR = 0;
            isPausedR = false;
        }
        else {
            progressBarR.setProgress(0);

        }
        doneWorkout = false;
        doneRest = false;
        textViewR.setText("");
    }
    public void resetWorkout(){//This function reset related boolean and time parameter to initial status on Work Out timer.
        TextView textView = findViewById(R.id.textViewTimer);
        if (countDownTimer != null){
            countDownTimer.cancel();
            progressBar.setProgress(0);
            isPaused = true;
            countDownTimer = null;
            timeRemain = 0;
            button.setText("Start");
            isPaused = false;

        }
        else {
            progressBar.setProgress(0);
        }
        doneWorkout = false;
        doneRest = false;
        textView.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgress(0); // set the progress to 0% when application created
        EditText timeInsert = findViewById(R.id.editTextInsertTime);

        progressBarR = findViewById(R.id.progressBarRest);
        progressBarR.setProgress(0); // set the progress to 0% when application created

        EditText timeInsertR = findViewById(R.id.editTextInsertTimeRest);
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //When click start button, EditText estimated current value and pass it to countdown function if value is valid.
                String timeText = timeInsert.getText().toString();
                if (timeText.isEmpty() || timeText.equals("0")) {
                    Toast.makeText(MainActivity.this, "Please insert desired WorkOut time", Toast.LENGTH_SHORT).show();
                    return;
                }//a toast error message will be shown and the method will return without starting the countdown timer.
                // Otherwise, the countdown timer will be started as usual

                String timeTextR = timeInsertR.getText().toString();
                if (timeTextR.isEmpty() || timeTextR.equals("0")) {
                    Toast.makeText(MainActivity.this, "Please insert desired Rest time", Toast.LENGTH_SHORT).show();
                    return;
                }//a toast error message will be shown and the method will return without starting the countdown timer.
                // Otherwise, the countdown timer will be started as usual



                if (countDownTimer == null) {
                    resetWorkout();
                    resetRest();
                    startNewCountdownTimer();//Check status of countDownTimer to avoid generate extra countdown on ongoing countdown.
                    button.setText("Stop");
                } else if (isPaused) {
                    countDownTimerR.cancel();
                    isPausedR = true;

                    startPausedTimer();
                    isPaused = false;
                    button.setText("Stop");//Check isPaused Boolean to make sure countdown is ticking and change text of button during counting.
                } else {
                    countDownTimer.cancel();
                    isPaused = true;

                    if (countDownTimerR == null){
                        startNewCountdownTimerR();

                    }
                    else {
                        startPausedTimerR();
                        isPausedR = false;
                    }
                    button.setText("Resume");//Check isPaused Boolean to make sure countdown is paused and change text of button during pausing.

                }
            }
        });

    }

    private void startNewCountdownTimer() {
        //Start a new countdown and project progress on progressbar based on customise time(seconds)
        TextView ViewTimer = findViewById(R.id.textViewTimer);
        EditText timeInsert = findViewById(R.id.editTextInsertTime);
        String timeText = timeInsert.getText().toString();
        doneWorkout = false;
        int maxSecond = Integer.parseInt(timeText);//Turn user input string into integer and set it as max time for new countdown.
        countDownTimer = new CountDownTimer(maxSecond*1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                //When countdown started, progressBar reveal the progress of countdown.
                // timeRemain variable recorded remain time for pause function.
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                progressBar.setMax(maxSecond);
                progressBar.setProgress(progressBar.getMax() - secondsRemaining);
                String time = Integer.toString(secondsRemaining);
                timeRemain = secondsRemaining;
                ViewTimer.setText(time);

            }

            public void onFinish() {
                //Play alert, toast message when countdown finished.
                Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_SHORT).show();
                countDownTimer = null;//Set this variable to null to allow system generate new countdown.
                button.setText("Restart");
                doneWorkout = true;
                playAlert();
                if (doneRest == false){
                    if (countDownTimerR == null){
                        startNewCountdownTimerR();

                    }
                    else {
                        startPausedTimerR();
                        isPausedR = false;
                    }
                }

            }
        };
        countDownTimer.start();
    }

    private void startPausedTimer(){
        //Resume the existing countdown by creating a new countdown with adjusted max time.
        TextView ViewTimer = findViewById(R.id.textViewTimer);

        int maxSecond = timeRemain;
        countDownTimer = new CountDownTimer(maxSecond*1000, 1000) {
            @Override
            public void onTick(long l) {
                int secondsRemaining = (int) (l / 1000);
                progressBar.setProgress(progressBar.getMax() - secondsRemaining);
                String time = Integer.toString(secondsRemaining);
                timeRemain = secondsRemaining;
                ViewTimer.setText(time);
            }

            @Override
            public void onFinish() {
                //Play alert, toast message when countdown finished.
                Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_SHORT).show();
                countDownTimer = null;//Set this variable to null to allow system generate new countdown.
                button.setText("Restart");
                doneWorkout = true;
                playAlert();
                if (doneRest == false){
                    if (countDownTimerR == null){
                        startNewCountdownTimerR();

                    }
                    else {
                        startPausedTimerR();
                        isPausedR = false;
                    }
                }

            }
        };
        countDownTimer.start();
    }

    private void startNewCountdownTimerR() {
        //In rest section, start a new countdown and project progress on progressbar based on customise time(seconds)
        TextView ViewTimer = findViewById(R.id.textViewTimerRest);
        EditText timeInsert = findViewById(R.id.editTextInsertTimeRest);
        String timeText = timeInsert.getText().toString();

        int maxSecond = Integer.parseInt(timeText);//Turn user input string into integer and set it as max time for new countdown.
        countDownTimerR = new CountDownTimer(maxSecond*1000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                //When countdown started, progressBar reveal the progress of countdown.
                // timeRemain variable recorded remain time for pause function.
                int secondsRemaining = (int) (millisUntilFinished / 1000);
                progressBarR.setMax(maxSecond);
                progressBarR.setProgress(progressBarR.getMax() - secondsRemaining);
                String time = Integer.toString(secondsRemaining);
                timeRemainR = secondsRemaining;
                ViewTimer.setText(time);
            }

            public void onFinish() {
                //Play alert, toast message when countdown finished.
                Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_SHORT).show();
                countDownTimerR = null;//Set this variable to null to allow system generate new countdown.
                playAlert();
                doneRest = true;
                startPausedTimer();
                isPaused = false;

            }

        };
        countDownTimerR.start();
    }

    private void startPausedTimerR(){
        //In rest section, resume the existing countdown by creating a new countdown with adjusted max time.
        TextView ViewTimer = findViewById(R.id.textViewTimerRest);

        int maxSecond = timeRemainR;
        countDownTimerR = new CountDownTimer(maxSecond* 1000, 1000) {

            @Override
            public void onTick(long l) {
                //When countdown resume, progressBar's max value adjusted base on timeRemain variable's value.
                // timeRemain variable recorded remain time for pause function.
                int secondsRemaining = (int) (l / 1000);
                progressBarR.setProgress(progressBarR.getMax() - secondsRemaining);
                String time = Integer.toString(secondsRemaining);
                timeRemainR = secondsRemaining;
                ViewTimer.setText(time);
            }

            @Override
            public void onFinish() {
                //Play alert, toast message when countdown finished.
                Toast.makeText(MainActivity.this, "Countdown finished", Toast.LENGTH_SHORT).show();
                countDownTimerR = null;//Set this variable to null to allow system generate new countdown.
                playAlert();
                doneRest = true;
                startPausedTimer();
                isPaused = false;
            }
        };
        countDownTimerR.start();
    }

    public void playAlert() {
        // Play the alert sound using MediaPlayer
        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.alert); // Replace R.raw.alert_sound with the actual resource ID of your alert sound file
        mediaPlayer.start();

    }
}












