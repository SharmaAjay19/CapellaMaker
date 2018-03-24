package com.ajsharm.capellamaker;

import android.graphics.Paint;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ajsharm on 3/24/2018.
 */
class ProjectTrack{
    @SerializedName("track")
    public CapellaTrack track;
    @SerializedName("DelayMs")
    public int DelayMs;
    @SerializedName("Effect")
    public String Effect;
    @SerializedName("IsMute")
    public Boolean IsMute;
}

public class CapellaProject {
    @SerializedName("projectName")
    private String projectName;
    @SerializedName("createdDate")
    private String createdDate;
    @SerializedName("updatedDate")
    private String updatedDate;
    @SerializedName("tracks")
    private List<ProjectTrack> tracks;
    @SerializedName("maxTracks")
    private int maxTracks;

    public CapellaProject(String projectName){
        this.projectName = projectName;
        this.createdDate = DateFormat.getDateTimeInstance().format(new Date());
        this.updatedDate = DateFormat.getDateTimeInstance().format(new Date());
        this.maxTracks = 5;
        this.tracks = new ArrayList<ProjectTrack>();
    }

    public Boolean AddTrack(ProjectTrack newTrack){
        if (this.tracks.size() >= this.maxTracks){
            return false;
        }
        else{
            this.tracks.add(newTrack);
            return true;
        }
    }

    public Boolean RemoveTrack(String trackId){
        int i;
        for(i=0; i<this.tracks.size(); i++){
            if (this.tracks.get(i).track.TrackId.equals(trackId)){
                break;
            }
        }
        if (i == this.tracks.size()){
            return false;
        }
        else{
            this.tracks.remove(i);
            return true;
        }
    }

    public void SetTrackDelay(String trackId, int delay){
        int i;
        for(i=0; i<this.tracks.size(); i++) {
            if (this.tracks.get(i).track.TrackId.equals(trackId)) {
                this.tracks.get(i).DelayMs = delay;
                break;
            }
        }
    }
}
