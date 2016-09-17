package edu.cmu.wjp.hackcmu16jwdj;

import android.hardware.Camera;

/**
 * Created by Bill on 9/16/2016.
 */
public class CameraUtil {
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e){
            return getCameraInstance();
        }
        return c;
    }
}
