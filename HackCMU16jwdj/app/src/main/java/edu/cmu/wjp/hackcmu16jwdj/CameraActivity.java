package edu.cmu.wjp.hackcmu16jwdj;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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


import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bill on 9/16/2016.
 */
public class CameraActivity extends Activity {
    public static final String FILE_NAME = "tmp.jpg";
    private static final String CLOUD_VISION_API_KEY = "AIzaSyDIEdX6JQuzItaYgeeuOcWgPGyQaxhUdQo";

    private Camera appCamera;
    private CameraView appCameraView;
    private Button captureButton;
    private Button newPictureButton;
    private Button showMoreButton;
    private TextView primarySourceWord;
    private TextView primaryTranslatedWord;
    private TextView moreTranslationsPane;

    private ImageView appMainImage;

    private boolean isShowingMore;

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

    private String inCountryCode;
    private String outCountryCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);

        CreateSpinners(savedInstanceState);

        appCamera = CameraUtil.getCameraInstance();
        Camera.Parameters appCameraParameters = appCamera.getParameters();
        appCameraParameters.setRotation(90);
        int w =0,h =0;
        for (Camera.Size s: appCameraParameters.getSupportedPictureSizes()) {
            //Log.d("Resolution Sizes: ", "width: "+s.width+" height: "+s.height+" ratio: "+s.width/(float)(s.height)+" "+1920/1080.0);
            if (s.height > h && Math.abs((s.width / (float) (s.height) - 16.0 / 9.0)) < 0.1 && s.height < 1080) {
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


        primarySourceWord = (TextView) findViewById(R.id.primary_source_word);
        primaryTranslatedWord = (TextView) findViewById(R.id.primary_translated_word);
        moreTranslationsPane = (TextView) findViewById(R.id.more_translations_pane);

        moreTranslationsPane.setMovementMethod(new ScrollingMovementMethod());

        moreTranslationsPane.setVisibility(View.GONE);
        primarySourceWord.setVisibility(View.GONE);
        primaryTranslatedWord.setVisibility(View.GONE);
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
        showMoreButton = (Button) findViewById(R.id.button_show_more);
        showMoreButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isShowingMore) {
                            appMainImage.setVisibility(View.GONE);
                            primarySourceWord.setVisibility(View.GONE);
                            primaryTranslatedWord.setVisibility(View.GONE);
                            moreTranslationsPane.setVisibility(View.VISIBLE);
                            isShowingMore = true;
                        } else {
                            appMainImage.setVisibility(View.VISIBLE);
                            primarySourceWord.setVisibility(View.VISIBLE);
                            primaryTranslatedWord.setVisibility(View.VISIBLE);
                            moreTranslationsPane.setVisibility(View.GONE);
                            isShowingMore = false;
                        }

                    }
                }
        );
        showMoreButton.setVisibility(View.GONE);
        // Add a listener to the ShowMore button
        newPictureButton = (Button) findViewById(R.id.button_new_picture);
        newPictureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isShowingMore) {
                            appMainImage.setVisibility(View.VISIBLE);
                            primarySourceWord.setVisibility(View.VISIBLE);
                            primaryTranslatedWord.setVisibility(View.VISIBLE);
                            moreTranslationsPane.setVisibility(View.GONE);
                            isShowingMore = false;
                        }
                        showMoreButton.setVisibility(View.GONE);
                        newPictureButton.setVisibility(View.GONE);
                        appMainImage.setVisibility(View.GONE);
                        captureButton.setVisibility(View.VISIBLE);
                        appCameraView.setVisibility(View.VISIBLE);
                        primarySourceWord.setVisibility(View.GONE);
                        primaryTranslatedWord.setVisibility(View.GONE);
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

            }
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
            }
        } else {
            //Toast.makeText(this, R.string.image_picker_error, Toast.LENGTH_LONG).show();
        }
    }

    public File getCameraFile() {
        File dir = getFilesDir();//Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }


    private void callCloudVision(final Bitmap bitmap) throws IOException {
        // Switch text to loading
        //put "loading" code here

        // Do the real work in an async task, because we need to use the network anyway
        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder(httpTransport, jsonFactory, null);
                    builder.setVisionRequestInitializer(new
                            VisionRequestInitializer(CLOUD_VISION_API_KEY));
                    Vision vision = builder.build();

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(new ArrayList<AnnotateImageRequest>() {{
                        AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();

                        // Add the image
                        Image base64EncodedImage = new Image();
                        // Convert the bitmap to a JPEG
                        // Just in case it's a format that Android understands but Cloud Vision
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
                        byte[] imageBytes = byteArrayOutputStream.toByteArray();

                        // Base64 encode the JPEG
                        base64EncodedImage.encodeContent(imageBytes);
                        annotateImageRequest.setImage(base64EncodedImage);

                        // add the features we want
                        annotateImageRequest.setFeatures(new ArrayList<Feature>() {{
                            Feature labelDetection = new Feature();
                            labelDetection.setType("LABEL_DETECTION");
                            labelDetection.setMaxResults(10);
                            add(labelDetection);
                        }});

                        // Add the list of one thing to the request
                        add(annotateImageRequest);
                    }});

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    //Log.d(TAG, "created Cloud Vision request object, sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();
                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    //Log.d(TAG, "failed to make API request because " + e.getContent());
                } catch (IOException e) {
                    //Log.d(TAG, "failed to make API request because of other IOException " +
                            //e.getMessage());
                }
                return "Cloud Vision API request failed. Check logs for details.";
            }

            protected void onPostExecute(String result) {
                moreTranslationsPane.setText(result);
                showMoreButton.setVisibility(View.VISIBLE);
                newPictureButton.setVisibility(View.VISIBLE);
                primarySourceWord.setVisibility(View.VISIBLE);
                primaryTranslatedWord.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        String message = "I found these things:\n\n";

        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message += String.format(" %s, %s", TranslateUtil.translate(label.getDescription(), inCountryCode), TranslateUtil.translate(label.getDescription(), outCountryCode));
                message += "\n";
            }
        } else {
            message += "nothing";
        }

        return message;
    }
    public void alert(String text) {
        CharSequence alert = text;
        Toast.makeText(getApplicationContext(), alert, Toast.LENGTH_LONG).show();

    }

    private String GetLangCodeFromLang( String lang)
    {
        for (int i = 0; i < languagesAndCodes.length; i += 1)
        {
            if(languagesAndCodes[i] == lang)
            {
                return languagesAndCodes[i+1];
            }
        }
        return "en";
    }

    public void CreateSpinners(Bundle savedInstanceState)
    {
        String languages[] = new String[languagesAndCodes.length/2];
        String langCodes[] = new String[languagesAndCodes.length/2];

        for (int i = 0; i < languagesAndCodes.length; i+=2)
        {
            languages[i/2] = languagesAndCodes[i];
        }

        for (int i = 1; i < languagesAndCodes.length; i+=2)
        {
            langCodes[(i-1)/2] = languagesAndCodes[i];
        }

        spCountryNative = (Spinner) findViewById(R.id.spCountryNative);
        spCountryLearn = (Spinner) findViewById(R.id.spCountryLearn);

        // Initialize and set Adapter
        spCountryNativeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        spCountryNativeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountryNative.setAdapter(spCountryNativeAdapter);
        spCountryNative.setSelection(spCountryNativeAdapter.getPosition("English"));

        spCountryNative.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String scountry = adapter.getItemAtPosition(position).toString();
                inCountryCode = GetLangCodeFromLang(scountry);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spCountryLearnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languages);
        spCountryLearnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountryLearn.setAdapter(spCountryLearnAdapter);
        spCountryLearn.setSelection(spCountryLearnAdapter.getPosition("Spanish"));

        spCountryLearn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String scountry = adapter.getItemAtPosition(position).toString();
                outCountryCode = GetLangCodeFromLang(scountry);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }
}
