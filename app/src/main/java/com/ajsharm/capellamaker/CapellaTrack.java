package com.ajsharm.capellamaker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ajsharm on 3/24/2018.
 */

public class CapellaTrack {
    @SerializedName("TrackId")
    public String TrackId;
    @SerializedName("TrackName")
    public String TrackName;
    @SerializedName("FilePath")
    public String FilePath;
}
