package com.ajsharm.capellamaker;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
/**
 * Created by ajsharm on 3/24/2018.
 */

public class AllProjects {
    @SerializedName("projects")
    public ArrayList<CapellaProject> projects;

    public AllProjects(){
        projects = new ArrayList<CapellaProject>();
    }
}
