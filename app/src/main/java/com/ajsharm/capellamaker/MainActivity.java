package com.ajsharm.capellamaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {
    Button newProjectButton;
    EditText newProjectName;
    AlertDialog.Builder alertDialog;
    AllProjects projectList;
    String projectsFilePath;
    Helpers helpers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helpers = new Helpers();
        alertDialog = new AlertDialog.Builder(this);
        newProjectButton = (Button) findViewById(R.id.newProjectButton);
        newProjectName = (EditText) findViewById(R.id.newProjectName);
        projectsFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/projects.json";
        projectList = helpers.readFromFile(projectsFilePath);
        if (projectList == null){
            projectList = new AllProjects();
            helpers.dumpToFile(projectsFilePath, projectList, getApplicationContext());
        }
        newProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewProjectClick();
            }
        });
    }

    public void onNewProjectClick(){
        if (newProjectName.getText() == null || newProjectName.getText().equals("ProjectName")){
            alert("Please provide a valid project name");
            return;
        }
        else {
            Intent i = new Intent(this, NewProjectActivity.class);
            i.putExtra("projectName", newProjectName.getText().toString());
            startActivityForResult(i, 0);
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
