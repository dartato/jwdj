package edu.cmu.wjp.hackcmu16jwdj;

import android.Manifest;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;

public class MainPanel extends AppCompatActivity {
    public static final int CAMERA_PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);
    }


    public void openCamera() {
        Permissions.checkAndRequestPermission(this,CAMERA_PERMISSIONS_REQUEST, Manifest.permission.CAMERA);
       // Intent intent = new Intent()
    }
}
