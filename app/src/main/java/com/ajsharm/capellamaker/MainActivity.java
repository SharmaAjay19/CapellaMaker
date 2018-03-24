package com.ajsharm.capellamaker;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder alertDialog;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button recordButton;
    Button playButton;
    String audioFilePath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recordButton = (Button) findViewById(R.id.recordButton);
        playButton = (Button) findViewById(R.id.playButton);
        audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Record1.mp3";
        playButton.setEnabled(false);
        mediaRecorder = new MediaRecorder();
        mediaPlayer = new MediaPlayer();
        alertDialog = new AlertDialog.Builder(this);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(audioFilePath);
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRecordClick();
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPlayClick();
            }
        });
    }

    public void onRecordClick(){
        if (recordButton.getText().equals("RECORD")) {
            try {
                mediaRecorder.prepare();
                recordButton.setEnabled(true);
                recordButton.setText("STOP");
                mediaRecorder.start();
            } catch (Exception e) {
                alert(e.toString());
            }
        } else if (recordButton.getText().equals("STOP")) {
            mediaRecorder.stop();
            mediaRecorder.release();
            playButton.setEnabled(true);
            recordButton.setText("RECORD");
        }
    }

    public void onPlayClick(){
        if (playButton.getText().equals("PLAY")) {
            playButton.setText("STOP");
            recordButton.setEnabled(false);
            try {
                mediaPlayer.setDataSource(audioFilePath);
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
            catch(Exception e) {}
        }
        else if (playButton.getText().equals("STOP")){
            mediaPlayer.stop();
            mediaPlayer.release();
            recordButton.setEnabled(true);
            playButton.setText("PLAY");
        }
    }

    public void alert(String message){
        alertDialog.setMessage(message);
        alertDialog.setTitle("Alert");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.create().show();
    }
}
