package edu.cmu.wjp.hackcmu16jwdj;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.widget.FrameLayout;


/**
 * Created by Bill on 9/16/2016.
 */
public class CameraActivity extends Activity {
    private Camera appCamera;
    private CameraView appCameraView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);

        appCamera = CameraUtil.getCameraInstance();

        appCameraView = new CameraView(this, appCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(appCameraView);
    }

    protected void onPause() {
        super.onPause();
        releaseCamera();
    }
    protected void releaseCamera() {
        if (appCamera != null) {
            appCamera.release();
            appCamera = null;
        }
    }
}
