package br.com.tattobr.openglestests;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //startActivity(new Intent(getApplicationContext(), ChangeScreenColorActivity.class));
        //startActivity(new Intent(getApplicationContext(), ShapesActivity.class));
        //startActivity(new Intent(getApplicationContext(), ColoredShapesActivity.class));
        //startActivity(new Intent(getApplicationContext(), TextureActivity.class));
        //startActivity(new Intent(getApplicationContext(), MultipleTexturesActivity.class));
        //startActivity(new Intent(getApplicationContext(), MultipleTexturesRotationActivity.class));
        //startActivity(new Intent(getApplicationContext(), MultipleTexturesAccelerometerActivity.class));
        startActivity(new Intent(getApplicationContext(), AspectRatioActivity.class));
    }
}
