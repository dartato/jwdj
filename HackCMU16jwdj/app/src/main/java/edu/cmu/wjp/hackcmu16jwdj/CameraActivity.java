package edu.cmu.wjp.hackcmu16jwdj;

import android.app.Activity;
<<<<<<< HEAD
=======
import android.graphics.Bitmap;
import android.graphics.Matrix;
>>>>>>> origin/master
import android.hardware.Camera;
import android.os.Bundle;
<<<<<<< HEAD
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
=======
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.vision.v1.Vision;
import com.google.api.services.vision.v1.VisionRequestInitializer;
import com.google.api.services.vision.v1.model.AnnotateImageRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
import com.google.api.services.vision.v1.model.EntityAnnotation;
import com.google.api.services.vision.v1.model.Feature;
import com.google.api.services.vision.v1.model.Image;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
>>>>>>> origin/master


/**
 * Created by Bill on 9/16/2016.
 */
public class CameraActivity extends Activity {
    private Camera appCamera;
    private CameraView appCameraView;
<<<<<<< HEAD
    private Spinner spCountryNative;
    private Spinner spCountryLearn;
    private ArrayAdapter<String> spCountryNativeAdapter;
    private ArrayAdapter<String> spCountryLearnAdapter;

    private final String languagesAndCodes[] = {"Afrikaans", "af",
                                        "Albanian", "sq",
                                        "Arabic", "ar",
                                        "Azerbaijani", "az",
                                        "Basque", "eu",
                                        "Belarusian", "be",
                                        "Bengali", "bn",
                                        "Bulgarian", "bg",
                                        "Catalan", "ca",
                                        "Chinese","zh-CN",
                                        "Croatian", "hr",
                                        "Czech","cs",
                                        "Danish", "da",
                                        "Dutch", "nl",
                                        "English","en",
                                        "Esperanto","eo",
                                        "Estonian","et",
                                        "Filipino","tl",
                                        "Finnish","fi",
                                        "French","fr",
                                        "Galician","gl",
                                        "Georgian", "ka",
                                        "German","de",
                                        "Greek", "el",
                                        "Gujarati", "gu",
                                        "Haitian Creole", "ht",
                                        "Hebrew", "iw",
                                        "Hindi","hi",
                                        "Hungarian","hu",
                                        "Icelandic", "is",
                                        "Indonesian", "id",
                                        "Irish", "ga",
                                        "Italian", "it",
                                        "Japanese", "ja",
                                        "Kannada", "kn",
                                        "Korean", "ko",
                                        "Latin", "la",
                                        "Latvian", "lv",
                                        "Lithuanian", "lt",
                                        "Macedonian","mk",
                                        "Malay","ms",
                                        "Maltese","mt",
                                        "Norwegian","no",
                                        "Persian","fa",
                                        "Polish","pl",
                                        "Portuguese","pt",
                                        "Romanian","ro",
                                        "Russian","ru",
                                        "Serbian","sr",
                                        "Slovak","sk",
                                        "Slovenian","sl",
                                        "Spanish","es",
                                        "Swahili","sw",
                                        "Swedish","sv",
                                        "Tamil","ta",
                                        "Telugu","te",
                                        "Thai","th",
                                        "Turkish","tr",
                                        "Ukrainian", "uk",
                                        "Urdu","ur",
                                        "Vietnamese","vi",
                                        "Welsh","cy",
                                        "Yiddish","yi"};

    private ArrayMap<String, String> langToCodeMap;

    private String inCountryCode;
    private String outCountryCode;
=======
    private Button captureButton;
    private Button newPictureButton;

    private ImageView appMainImage;
>>>>>>> origin/master

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //String languages[];
        //String langCodes[];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);

        appCamera = CameraUtil.getCameraInstance();
        Camera.Parameters appCameraParameters = appCamera.getParameters();
        appCameraParameters.setRotation(90);
        int w =0,h =0;
        for (Camera.Size s: appCameraParameters.getSupportedPictureSizes()) {
            //Log.d("Resolution Sizes: ", "width: "+s.width+" height: "+s.height+" ratio: "+s.width/(float)(s.height)+" "+1920/1080.0);
            if (s.height > h && Math.abs((s.width / (float) (s.height) - 16.0 / 9.0)) < 0.1) {
                w = s.width;
                h = s.height;

            }
        }
        Log.d("onCreate", "width: "+w+" height: "+h);
        appCameraParameters.setPictureSize(w,h);
        appCamera.setParameters(appCameraParameters);
        appCameraView = new CameraView(this, appCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(appCameraView);
<<<<<<< HEAD

        spCountryNative = (Spinner) findViewById(R.id.spCountryNative);
        spCountryLearn = (Spinner) findViewById(R.id.spCountryLearn);

        // Initialize and set Adapter
        spCountryNativeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languagesAndCodes);
        spCountryNativeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountryNative.setAdapter(spCountryNativeAdapter);
=======
        // Add a listener to the Capture button
        captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera

                        appCamera.takePicture(null, null, appPicture);
                    }
                }
        );
        // Add a listener to the NewPicture button
        newPictureButton = (Button) findViewById(R.id.button_new_picture);
        newPictureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPictureButton.setVisibility(View.GONE);
                        appMainImage.setVisibility(View.GONE);
                        captureButton.setVisibility(View.VISIBLE);
                        appCameraView.setVisibility(View.VISIBLE);
                        appCameraView.startPreview();
                    }
                }
        );
        newPictureButton.setVisibility(View.GONE);
        appMainImage = (ImageView) findViewById(R.id.main_image);

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
    private Camera.PictureCallback appPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getCameraFile();
            if (pictureFile == null){
                Log.d("appPicture", "Error creating media file");
                return;
            }

            try {

                FileOutputStream stream = new FileOutputStream(pictureFile);
                stream.write(data);
                stream.close();
                uploadImage(Uri.fromFile(getCameraFile()));
                Log.d("appPicture", "Received response from google.");

            } catch (FileNotFoundException e) {
                Log.d("appPicture", "File not found: " + e.getMessage());

            } catch (IOException e) {
                Log.d("appPicture", "Error accessing file: " + e.getMessage());
>>>>>>> origin/master

        spCountryNative.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String scountry = adapter.getItemAtPosition(position).toString();
            }
<<<<<<< HEAD
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
=======
        }
    };


    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                // scale the image to save on bandwidth
               // Bitmap bitmap =
                  //      scaleBitmapDown(
                  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);//,
                 //               1920);
                appMainImage.setImageBitmap(bitmap);
                appMainImage.setVisibility(View.VISIBLE);
                appCameraView.setVisibility(View.GONE);
                captureButton.setVisibility(View.GONE);

                callCloudVision(bitmap);

            } catch (IOException e) {
                alert("Something went wrong");
>>>>>>> origin/master
            }
        });

<<<<<<< HEAD
        spCountryLearnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languagesAndCodes);
        spCountryLearnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountryLearn.setAdapter(spCountryLearnAdapter);
=======
    public File getCameraFile() {
        File dir = getFilesDir();//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }


    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        //put "loading" code here
>>>>>>> origin/master

        spCountryLearn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String scountry = adapter.getItemAtPosition(position).toString();
            }
<<<<<<< HEAD
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
=======

            protected void onPostExecute(String result) {
                alert(result);
                newPictureButton.setVisibility(View.VISIBLE);
>>>>>>> origin/master
            }
        });
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
    public void alert(String text) {
        CharSequence alert = text;
        Toast.makeText(getApplicationContext(), alert, Toast.LENGTH_LONG).show();

    }
}
