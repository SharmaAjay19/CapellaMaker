package com.ajsharm.capellamaker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button newProjectButton;
    EditText newProjectName;
    AlertDialog.Builder alertDialog;
    AllProjects projectList;
    String projectsFilePath;
    ArrayAdapter adapter;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Helpers.createDirIfNotExists(getString(R.string.app_name));
        alertDialog = new AlertDialog.Builder(this);
        listView = (ListView) findViewById(R.id.yourProjectsList);
        newProjectButton = (Button) findViewById(R.id.newProjectButton);
        newProjectName = (EditText) findViewById(R.id.newProjectName);
        projectsFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/projects.json";
        projectList = Helpers.readFromFile(projectsFilePath);
        if (projectList == null){
            projectList = new AllProjects();
            Helpers.dumpToFile(projectsFilePath, projectList, getApplicationContext());
        }
        else{
            alert(Integer.toString(projectList.projects.size()) + " projects loaded!");
        }
        ArrayList<String> data = new ArrayList<String>();
        for(int i=0; i<projectList.projects.size(); i++){
            data.add(projectList.projects.get(i).projectName);
        }
        adapter = new ArrayAdapter(this, R.layout.project_list_layout, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String ProjectName = listView.getAdapter().getItem(position).toString();
                onExistingProjectClick(ProjectName);
            }
        });
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
            String projectName = newProjectName.getText().toString();
            //Create new project, add it to list and dump to file.
            CapellaProject project = new CapellaProject(projectName);
            projectList.projects.add(project);
            Helpers.dumpToFile(projectsFilePath, projectList, getApplicationContext());

            //Create project directory and tracks folder
            String projectFolderPath = getString(R.string.app_name) + "/" + projectName;
            Helpers.createDirIfNotExists(projectFolderPath + "/tracks/");
            Intent i = new Intent(this, ProjectActivity.class);
            i.putExtra("projectName", projectName);
            startActivityForResult(i, 0);
        }
    }

    public void onExistingProjectClick(String projectName){
        Intent i = new Intent(this, ProjectActivity.class);
        i.putExtra("projectName", projectName);
        startActivityForResult(i, 0);
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
