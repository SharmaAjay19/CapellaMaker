package com.ajsharm.capellamaker;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;
/**
 * Created by ajsharm on 3/24/2018.
 */

public class AllProjects {
    @SerializedName("projects")
    public List<CapellaProject> projects;
}
