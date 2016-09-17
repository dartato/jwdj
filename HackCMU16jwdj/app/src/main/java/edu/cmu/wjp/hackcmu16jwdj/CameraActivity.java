package edu.cmu.wjp.hackcmu16jwdj;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;


/**
 * Created by Bill on 9/16/2016.
 */
public class CameraActivity extends Activity {
    private Camera appCamera;
    private CameraView appCameraView;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {

        //String languages[];
        //String langCodes[];

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_panel);

        appCamera = CameraUtil.getCameraInstance();

        appCameraView = new CameraView(this, appCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(appCameraView);

        spCountryNative = (Spinner) findViewById(R.id.spCountryNative);
        spCountryLearn = (Spinner) findViewById(R.id.spCountryLearn);

        // Initialize and set Adapter
        spCountryNativeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languagesAndCodes);
        spCountryNativeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountryNative.setAdapter(spCountryNativeAdapter);

        spCountryNative.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String scountry = adapter.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        spCountryLearnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, languagesAndCodes);
        spCountryLearnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountryLearn.setAdapter(spCountryLearnAdapter);

        spCountryLearn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int position, long id) {
                String scountry = adapter.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
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
}
