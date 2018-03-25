package com.ajsharm.capellamaker;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ajsharm on 3/25/2018.
 */

public class CustomListAdapter extends ArrayAdapter {
    private final Activity context;
    private final ArrayList<CapellaProject> projects;

    public CustomListAdapter(Activity context, ArrayList<CapellaProject> projects) {
        super(context, R.layout.project_list_layout);
        this.context = context;
        this.projects = projects;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.project_list_layout, null, true);

        TextView projectLabel = (TextView) rowView.findViewById(R.id.projectLabel);
        projectLabel.setText(this.projects.get(position).projectName);
        return rowView;
    }
}
