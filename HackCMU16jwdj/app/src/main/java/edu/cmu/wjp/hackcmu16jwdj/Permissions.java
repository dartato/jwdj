package edu.cmu.wjp.hackcmu16jwdj;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
/**
 * Created by Bill on 9/16/2016.
 */
public class Permissions {
    public static boolean checkAndRequestPermission (final Activity activity, final int permissionCode, String permission) {
        if (ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_DENIED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)) {
                AlertDialog.Builder  alertBuilder = new AlertDialog.Builder(activity);
                alertBuilder.setTitle("Camera Permissions");
                alertBuilder.setMessage("Camera permissions are needed to take pictures of things...");
                alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, permissionCode);
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA}, permissionCode);
            }
        } else {
            return true;
        }
        return false;
    }
}
