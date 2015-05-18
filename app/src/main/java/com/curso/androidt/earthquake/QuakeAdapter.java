package com.curso.androidt.earthquake;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by androidt on 14/05/2015.
 */
public class QuakeAdapter extends BaseAdapter {

    private List<Quake> listQuakes;
    private int reslayout;
    private Context context;

    public QuakeAdapter(List<Quake> listQuakes, int reslayout, Context context) {
        this.listQuakes = listQuakes;
        this.reslayout = reslayout;
        this.context = context;
    }

    @Override
    public int getCount() {
        return listQuakes.size();
    }

    @Override
    public Object getItem(int position) {
        return listQuakes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(reslayout, null);
        }

        Quake quake = listQuakes.get(position);

        TextView txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        TextView txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
        TextView txtMagnitude = (TextView) convertView.findViewById(R.id.txtMagnitude);
        TextView txtDate = (TextView) convertView.findViewById(R.id.txtDate);

        txtTitle.setText(quake.getTitle());
        txtDescription.setText((quake.getLink()));
        if (txtMagnitude != null) {
            txtMagnitude.setText(String.valueOf(quake.getMagnitude()));
        }
        if (quake.getDate() != null) {
            txtDate.setText(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss   ").format(quake.getDate()));
        }

        return convertView;
    }
}
