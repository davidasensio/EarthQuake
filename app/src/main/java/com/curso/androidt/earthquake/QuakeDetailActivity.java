package com.curso.androidt.earthquake;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.curso.androidt.earthquake.util.QuakeUtil;

import java.text.SimpleDateFormat;


public class QuakeDetailActivity extends Activity {
    private Quake quake = null;
    private Bitmap bitMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quake_detail);

        this.quake = (Quake)getIntent().getExtras().get("quake");

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quake_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void init() {
        if (quake != null) {

            TextView txtTitle = (TextView) findViewById(R.id.txtDetailTitle);
            TextView txtMagnitude = (TextView) findViewById(R.id.txtDetailMagnitude);
            TextView txtDate = (TextView) findViewById(R.id.txtDetailDate);
            TextView txtLink = (TextView) findViewById(R.id.txtDetailLink);
            TextView txtDetailLocation = (TextView) findViewById(R.id.txtDetailLocation);
            ImageView imageView = (ImageView) findViewById(R.id.imageViewDetailIconMagnitude);
            ImageView imageViewIconDirection = (ImageView) findViewById(R.id.imageViewDetailIconDirection);
            Button btnViewOnMap = (Button) findViewById(R.id.btnDetailViewOnMap);

            btnViewOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QuakeUtil.showOnMap(QuakeDetailActivity.this, quake);
                }
            });
            txtMagnitude.setText("M ".concat(String.valueOf(quake.getMagnitude())));
            txtTitle.setText(quake.getTitle());
            txtLink.setText(quake.getLink());
            txtDetailLocation.setText(String.format("Lat, Long: [%.7f, %.7f]\nDistance: %.0f Km\nDirection: %dº", quake.getLatitude(), quake.getLongitude(), quake.getProximity(), Long.valueOf(quake.getBearingAngle())));
            txtDate.setText(new SimpleDateFormat("dd/MM HH:mm").format(quake.getDate()));

            if (quake.getMagnitude() >= 7) {
                imageView.setBackground(getDrawable(R.drawable.small_icon_red)); //Red
            } else if (quake.getMagnitude() >= 4.5) {
                imageView.setBackground(getDrawable(R.drawable.small_icon_orange)); //Yellow
            } else {
                imageView.setBackground(getDrawable(R.drawable.small_icon_green)); //Green
            }

            //Rotate direction arrow
            if (bitMap == null) {
                bitMap = ((BitmapDrawable) getDrawable(R.drawable.black_arrow)).getBitmap();
                bitMap = Bitmap.createScaledBitmap(bitMap, 80, 80, true);
            }
            float angle = quake.getBearingAngle();

            Matrix matrix = new Matrix();
            imageViewIconDirection.setScaleType(ImageView.ScaleType.MATRIX);   //required
            matrix.postRotate(angle);

            Bitmap bMapRotate = Bitmap.createBitmap(bitMap, 0, 0, bitMap.getWidth(), bitMap.getHeight(), matrix, true);
            imageViewIconDirection.setImageBitmap(bMapRotate);
        }
    }
}
