package com.ajsharm.capellamaker;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * Created by ajsharm on 3/24/2018.
 */

public class Helpers {
    public AllProjects readFromFile(String filePath){
        Gson gson = new Gson();
        try {
            AllProjects projects = (AllProjects) gson.fromJson(getStringFromFile(filePath), AllProjects.class);
            return projects;
        }
        catch(Exception e) {
            return null;
        }
    }

    public void dumpToFile(String filePath, AllProjects projectList, Context context){
        Gson gson = new Gson();
        try{
            String content = gson.toJson(projectList);
            writeToFile(content, context, filePath);
            return;
        }
        catch(Exception e){
            return;
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static String getStringFromFile (String filePath) throws Exception {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    private void writeToFile(String data,Context context, String filePath) {
        try {
            File root = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CapellaMaker", "");
            if (!root.exists()) {
                root.mkdirs();
            }
            File myFile = new File(filePath);
            FileWriter writer = new FileWriter(myFile);
            writer.append(data);
            writer.flush();
            writer.close();
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(context, "Failed " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(Environment.getExternalStorageDirectory(), path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                ret = false;
            }
        }
        return ret;
    }
}
