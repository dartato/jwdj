package edu.cmu.wjp.hackcmu16jwdj;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Bill on 9/16/2016.
 */
public class CameraView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder parentSurface;
    private Camera appCamera;

    public CameraView(Context context, Camera camera) {
        super(context);
        appCamera = camera;
        appCamera.setDisplayOrientation(90);
        parentSurface = getHolder(); //That way we know when the underlying surface is destroyed
        parentSurface.addCallback(this);

        parentSurface.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); //for old phones
    }

    public void surfaceCreated(SurfaceHolder holder) {
        try {
            appCamera.setPreviewDisplay(holder);
            appCamera.startPreview();
        } catch (IOException e) {

        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (parentSurface.getSurface() == null) return; //parent does not exist

        try {
            appCamera.stopPreview();
        } catch (Exception e) {

            Log.d("surfaceChanged", e.getMessage());
        }

        //I think this is where we set the size of the preview/camera

        try {
            appCamera.setPreviewDisplay(parentSurface);
            appCamera.startPreview();
        } catch (Exception e) {
            Log.d("surfaceChanged", e.getMessage());
        }
    }

    public void startPreview() {
        try {
            appCamera.setPreviewDisplay(parentSurface);
            appCamera.startPreview();
        } catch (Exception e) {
            Log.d("startPreview", e.getMessage());
        }
    }

}
