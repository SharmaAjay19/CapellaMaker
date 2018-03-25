package com.ajsharm.capellamaker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajsharm on 3/24/2018.
 */

public class CapellaTrack {
    @SerializedName("trackId")
    public String TrackId;
    @SerializedName("trackName")
    public String TrackName;
    @SerializedName("filePath")
    public String FilePath;
}
