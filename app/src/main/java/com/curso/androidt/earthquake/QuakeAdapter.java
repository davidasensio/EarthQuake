package com.curso.androidt.earthquake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
    private Bitmap bitMap = null;

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
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
        ImageView imageViewIconDirection = (ImageView) convertView.findViewById(R.id.imageViewIconDirection);

        txtMagnitude.setText("M ".concat(String.valueOf(quake.getMagnitude())));
        txtTitle.setText(quake.getTitle());
        txtDescription.setText(String.format("%.0f Km | %dº", quake.getProximity(), Long.valueOf(quake.getBearingAngle())));
        txtDate.setText(new SimpleDateFormat("dd/MM HH:mm").format(quake.getDate()));

        if (quake.getMagnitude() >= 7) {
            imageView.setBackground(context.getDrawable(R.drawable.small_icon_red)); //Red
        } else if (quake.getMagnitude() >= 4.5) {
            imageView.setBackground(context.getDrawable(R.drawable.small_icon_orange)); //Yellow
        } else {
            imageView.setBackground(context.getDrawable(R.drawable.small_icon_green)); //Green
        }

        //Rotate direction arrow
        if (bitMap == null) {
            bitMap = ((BitmapDrawable)context.getDrawable(R.drawable.black_arrow)).getBitmap();
            bitMap = Bitmap.createScaledBitmap(bitMap, 40, 40, true);
        }
        float angle = quake.getBearingAngle();

        Matrix matrix = new Matrix();
        imageViewIconDirection.setScaleType(ImageView.ScaleType.MATRIX);   //required
        matrix.postRotate(angle);

        Bitmap bMapRotate = Bitmap.createBitmap(bitMap, 0, 0, bitMap.getWidth(), bitMap.getHeight(), matrix, true);
        imageViewIconDirection.setImageBitmap(bMapRotate);

        return convertView;
    }
}
