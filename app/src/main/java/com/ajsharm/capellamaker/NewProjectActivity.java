package com.ajsharm.capellamaker;

import android.app.Activity;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.UUID;

public class NewProjectActivity extends AppCompatActivity {
    AllProjects projectList;
    String projectsFilePath;
    String projectName;
    CapellaProject project;
    AlertDialog.Builder alertDialog;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button addTrackButton;
    Button recordButton;
    Button playButton;
    Button confirmButton;
    EditText newtrackName;
    String audioFilePath;
    Helpers helpers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);
        projectsFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CapellaMaker/projects.json";
        helpers = new Helpers();
        projectList = helpers.readFromFile(projectsFilePath);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String pName = extras.getString("projectName");
            projectName = pName;
            this.setTitle(projectName);
        }
        else{
            setResult(0);
            finish();
        }
        project = new CapellaProject(projectName);
        projectList.projects.add(project);
        helpers.dumpToFile(projectsFilePath, projectList, getApplicationContext());
        helpers.createDirIfNotExists(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CapellaMaker/" + projectName + "/tracks");
        addTrackButton = (Button) findViewById(R.id.addTrackButton);
        recordButton = (Button) findViewById(R.id.recordButton);
        playButton = (Button) findViewById(R.id.playButton);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        newtrackName = (EditText) findViewById(R.id.newTrackName);
        //audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Record1.mp3";
        recordButton.setEnabled(false);
        playButton.setEnabled(false);
        confirmButton.setEnabled(false);
        alertDialog = new AlertDialog.Builder(this);
        addTrackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewTrackClick();
            }
        });
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
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConfirmClick();
            }
        });
    }
    public void onNewTrackClick(){
        if (newtrackName.getText() == null || newtrackName.getText().equals("Track Name")){
            alert("Please enter a valid track name");
            return;
        }
        else{
            this.recordButton.setEnabled(true);
        }
    }

    public void onRecordClick(){
        if (recordButton.getText().equals("RECORD")) {
            if (newtrackName.getText() == null || newtrackName.getText().equals("Track Name")){
                alert("Please enter a valid track name");
                return;
            }
            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                audioFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CapellaMaker/" + projectName + "/tracks/" + newtrackName.getText() + ".mp3";
                mediaRecorder.setOutputFile(audioFilePath);
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
            confirmButton.setEnabled(true);
            recordButton.setText("RECORD");
        }
    }

    public void onPlayClick(){
        if (playButton.getText().equals("PLAY")) {
            playButton.setText("STOP");
            recordButton.setEnabled(false);
            try {
                mediaPlayer = new MediaPlayer();
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

    public void onConfirmClick(){
        ProjectTrack newTrack = new ProjectTrack();
        newTrack.DelayMs = 0;
        newTrack.Effect = "NONE";
        newTrack.IsMute = false;
        CapellaTrack track = new CapellaTrack();
        track.FilePath = audioFilePath;
        track.TrackName = newtrackName.getText().toString();
        track.TrackId = UUID.randomUUID().toString();
        newTrack.track = track;
        project.AddTrack(newTrack);
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
