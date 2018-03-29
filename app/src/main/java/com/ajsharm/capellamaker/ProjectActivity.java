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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.UUID;

public class ProjectActivity extends AppCompatActivity {
    AllProjects projectList;
    String projectsFilePath;
    String projectFolderPath;
    String projectName;
    CapellaProject project;
    AlertDialog.Builder alertDialog;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    Button addTrackButton;
    Button recordButton;
    Button playButton;
    Button confirmButton;
    Button discardButton;
    EditText newtrackName;
    ListView trackList;
    String audioFilePath;
    Helpers helpers;
    Boolean trackPlaying;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_project);

        //Read the list of all projects
        projectsFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CapellaMaker/projects.json";
        helpers = new Helpers();
        projectList = helpers.readFromFile(projectsFilePath);

        //Get the new project name
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

        projectFolderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CapellaMaker/" + projectName + "/";

        for(int i=0; i<projectList.projects.size(); i++){
            if(projectList.projects.get(i).projectName.equals(projectName)){
                project = projectList.projects.get(i);
            }
        }

        //SHOW Tracks
        renderTrackList();

        //Application logic
        trackPlaying = false;
        addTrackButton = (Button) findViewById(R.id.addTrackButton);
        recordButton = (Button) findViewById(R.id.recordButton);
        playButton = (Button) findViewById(R.id.playButton);
        confirmButton = (Button) findViewById(R.id.confirmButton);
        discardButton = (Button) findViewById(R.id.discardButton);
        newtrackName = (EditText) findViewById(R.id.newTrackName);
        recordButton.setEnabled(false);
        playButton.setEnabled(false);
        confirmButton.setEnabled(false);
        discardButton.setEnabled(false);
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
        discardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDiscardClick();
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
                audioFilePath = projectFolderPath + "tracks/" + newtrackName.getText() + ".mp3";
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
            discardButton.setEnabled(true);
            recordButton.setText("RECORD");
        }
    }

    public void onPlayClick(){
        if (playButton.getText().equals("PLAY")) {
            playButton.setText("STOP");
            recordButton.setEnabled(false);
            confirmButton.setEnabled(false);
            discardButton.setEnabled(false);
            try {
                if(mediaPlayer == null){
                    mediaPlayer = new MediaPlayer();
                }
                else{
                    mediaPlayer.release();
                    mediaPlayer = new MediaPlayer();
                }
                mediaPlayer.setDataSource(audioFilePath);
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        playButton.setText("PLAY");
                        recordButton.setEnabled(true);
                        confirmButton.setEnabled(true);
                        discardButton.setEnabled(true);
                    }
                });
                mediaPlayer.prepare();
                mediaPlayer.start();
            }
            catch(Exception e) {}
        }
        else if (playButton.getText().equals("STOP")){
            mediaPlayer.stop();
            mediaPlayer.release();
            recordButton.setEnabled(true);
            confirmButton.setEnabled(true);
            discardButton.setEnabled(true);
            playButton.setText("PLAY");
        }
    }

    public void onConfirmClick(){
        ProjectTrack newTrack = new ProjectTrack();
        newTrack.DelayMs = 0;
        newTrack.Effect = "NONE";
        newTrack.IsMute = false;
        CapellaTrack track = new CapellaTrack();
        track.FilePath = audioFilePath.toString();
        track.TrackName = newtrackName.getText().toString();
        track.TrackId = UUID.randomUUID().toString();
        newTrack.track = track;
        project.AddTrack(newTrack);
        helpers.dumpToFile(projectsFilePath, projectList, getApplicationContext());
        recordButton.setEnabled(false);
        playButton.setEnabled(false);
        confirmButton.setEnabled(false);
        discardButton.setEnabled(false);
        renderTrackList();
    }

    public void onDiscardClick(){
        if (audioFilePath != null){
            mediaPlayer.release();
            helpers.deleteFile(audioFilePath);
            audioFilePath = null;
            recordButton.setEnabled(true);
            playButton.setEnabled(false);
            confirmButton.setEnabled(false);
            discardButton.setEnabled(false);
        }
    }

    public void renderTrackList(){
        trackList = (ListView) findViewById(R.id.trackList);
        ArrayList<String> data = new ArrayList<String>();
        for (int i=0; i<project.tracks.size(); i++){
            data.add(project.tracks.get(i).track.TrackName);
        }
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.track_list_layout, data);
        trackList.setAdapter(adapter);
        trackList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        trackList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String trackName = trackList.getAdapter().getItem(position).toString();
                CapellaTrack playTrack = null;
                for(int i=0; i<project.tracks.size(); i++){
                    if (project.tracks.get(i).track.TrackName.equals(trackName)){
                        playTrack = project.tracks.get(i).track;
                    }
                }
                if (playTrack != null){
                    playMusic(playTrack.FilePath);
                }
            }
        });
    }

    public void playMusic(String path){
        try {
            if(mediaPlayer == null){
                mediaPlayer = new MediaPlayer();
            }
            else{
                mediaPlayer.release();
                mediaPlayer = new MediaPlayer();
            }
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch(Exception e) {}
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
