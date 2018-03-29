package com.ajsharm.capellamaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    Button newProjectButton;
    EditText newProjectName;
    AlertDialog.Builder alertDialog;
    AllProjects projectList;
    String projectsFilePath;
    ProjectListAdapter projectListAdapter;
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
        renderProjectList();
        newProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNewProjectClick();
            }
        });
    }

    public void renderProjectList(){
        projectListAdapter = new ProjectListAdapter(this, R.layout.project_list_layout, projectList.projects);
        listView.setAdapter(projectListAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CapellaProject Project = (CapellaProject) listView.getAdapter().getItem(position);
                onExistingProjectClick(Project.projectName);
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
            renderProjectList();
            //Create project directory and tracks folder
            String projectFolderPath = getString(R.string.app_name) + "/" + projectName;
            Helpers.createDirIfNotExists(projectFolderPath + "/tracks/");
            Intent i = new Intent(this, ProjectActivity.class);
            i.putExtra("projectName", projectName);
            startActivityForResult(i, 0);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        projectList = Helpers.readFromFile(projectsFilePath);
        renderProjectList();
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Helpers.dumpToFile(projectsFilePath, projectList, getApplicationContext());
        Helpers.fileSync(getApplicationContext(), projectList, getString(R.string.app_name));
    }

    public void onExistingProjectClick(String projectName){
        Helpers.dumpToFile(projectsFilePath, projectList, getApplicationContext());
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
