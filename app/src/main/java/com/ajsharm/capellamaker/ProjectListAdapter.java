package com.ajsharm.capellamaker;

/**
 * Created by ajsharm on 3/29/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
/**
 * Created by ajsharm on 3/25/2018.
 */

public class ProjectListAdapter extends ArrayAdapter<CapellaProject> {
    public ProjectListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ProjectListAdapter(Context context, int resource, List<CapellaProject> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.project_list_layout, null);
        }

        final CapellaProject p = getItem(position);

        if (p != null) {
            TextView textLabel = (TextView) v.findViewById(R.id.projectLabel);
            Button button = (Button) v.findViewById(R.id.deleteButton);
            if (textLabel != null) {
                textLabel.setText(p.projectName);
            }

            if (button != null) {
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        remove(p);
                    }
                });
            }
        }

        return v;
    }

}