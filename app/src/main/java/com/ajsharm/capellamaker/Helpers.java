package com.ajsharm.capellamaker;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by ajsharm on 3/24/2018.
 */

public class Helpers {
    public static MediaPlayer mediaPlayer;

    public Helpers(){
        this.mediaPlayer = new MediaPlayer();
    }

    public static AllProjects readFromFile(String filePath){
        Gson gson = new Gson();
        try {
            AllProjects projects = (AllProjects) gson.fromJson(getStringFromFile(filePath), AllProjects.class);
            return projects;
        }
        catch(Exception e) {
            return null;
        }
    }

    public static void fileSync(Context context, AllProjects projectList, String rootName){
        ArrayList allProjects = new ArrayList<String>();
        ArrayList allTracks = new ArrayList<String>();
        for(CapellaProject proj: projectList.projects){
            allProjects.add(proj.projectName);
            for(ProjectTrack track: proj.tracks){
                allTracks.add(track.track.FilePath);
            }
        }
        String path = Environment.getExternalStorageDirectory() + "/" + rootName;
        File f = new File(path);
        File[] files = f.listFiles();
        for (File inFile : files) {
            if (inFile.isDirectory()) {
                if (allProjects.indexOf(inFile.getName()) >= 0){
                    File trackFolder = new File(inFile.getAbsolutePath() + "/" + "tracks");
                    File[] trackPaths = trackFolder.listFiles();
                    for (File filePath: trackPaths){
                        if (allTracks.indexOf(filePath.getAbsolutePath()) >= 0){
                            continue;
                        }
                        else{
                            filePath.delete();
                        }
                    }
                }
                else{
                    deleteRecursive(inFile);
                }
            }
        }
    }

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);
        fileOrDirectory.delete();
    }

    public static void dumpToFile(String filePath, AllProjects projectList, Context context){
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

    private static void writeToFile(String data,Context context, String filePath) {
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
        } catch (IOException e) {
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

    public static boolean deleteFile(String path){
        File file = new File(path);
        if (!file.exists()){
            return true;
        }
        else{
            file.delete();
            return true;
        }
    }

    public static void playMusic(String path){
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

    public static void stopMusic(){
        if (mediaPlayer != null){
            mediaPlayer.release();
        }
    }
}
