package com.ajsharm.capellamaker;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ajsharm on 3/24/2018.
 */
class ProjectTrack{
    @SerializedName("track")
    public CapellaTrack track;
    @SerializedName("delayMs")
    public int DelayMs;
    @SerializedName("effect")
    public String Effect;
    @SerializedName("isMute")
    public Boolean IsMute;
}

public class CapellaProject {
    @SerializedName("projectName")
    public String projectName;
    @SerializedName("createdDate")
    public String createdDate;
    @SerializedName("updatedDate")
    public String updatedDate;
    @SerializedName("tracks")
    public ArrayList<ProjectTrack> tracks;
    @SerializedName("maxTracks")
    public int maxTracks;

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
