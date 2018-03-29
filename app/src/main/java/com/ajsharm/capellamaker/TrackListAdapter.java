package com.ajsharm.capellamaker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ajsharm on 3/25/2018.
 */

public class TrackListAdapter extends ArrayAdapter<ProjectTrack> {
    public TrackListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public TrackListAdapter(Context context, int resource, List<ProjectTrack> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.track_list_layout, null);
        }

        final ProjectTrack p = getItem(position);

        if (p != null) {
            TextView textLabel = (TextView) v.findViewById(R.id.trackLabel);
            Button button = (Button) v.findViewById(R.id.deleteButton);
            if (textLabel != null) {
                textLabel.setText(p.track.TrackName);
                textLabel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Playing " + p.track.TrackName, Toast.LENGTH_LONG).show();
                        Helpers.playMusic(p.track.FilePath);
                    }
                });
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