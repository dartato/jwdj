package edu.cmu.wjp.hackcmu16jwdj;

import android.util.Log;

import java.util.*;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.translate.Translate;
import com.google.api.services.translate.TranslateRequestInitializer;
import com.google.api.services.translate.model.TranslationsListResponse;
import com.google.api.services.translate.model.TranslationsResource;
/**
 * Created by John on 9/17/16.
 */
public class TranslateUtil {
    public static final String TRANSLATE_API_KEY = "AIzaSyDIEdX6JQuzItaYgeeuOcWgPGyQaxhUdQo";

    public static String translate(String original, String targetLang){
        try {
            targetLang = targetLang.toUpperCase();
           // Log.d("TranslateUtil", original+"//"+ targetLang);
            HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

            Translate.Builder tBuilder = new Translate.Builder(httpTransport, jsonFactory, null);
            tBuilder.setTranslateRequestInitializer(new TranslateRequestInitializer(TRANSLATE_API_KEY));

            Translate translator = tBuilder.build();


            /*Translate t = new Translate.Builder(
                    com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport(),
                    com.google.api.client.json.gson.GsonFactory.getDefaultInstance(), null)
                    .setApplicationName("hackcmu16jwdj") //Updates to our App-Name
                    .build(); //Creates the translator
            */
            Translate.Translations.List list = translator.new Translations().list(Arrays.asList(original), targetLang);

            TranslationsListResponse response = list.execute();

            return response.getTranslations().get(0).getTranslatedText();
        }
        catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }


    /*public static void main(String[]args){
        System.out.println(translate("Hi my name is Dan the Man.","FR"));
        //______________________________________
         try {
            Translate t = new Translate.Builder(
                    com.google.api.client.googleapis.javanet.GoogleNetHttpTransport.newTrustedTransport()
                    , com.google.api.client.json.gson.GsonFactory.getDefaultInstance(), null)
                    //Need to update this to your App-Name
                    .setApplicationName("hackcmu16jwdj")
                    .build();

            Translate.Translations.List list = t.new Translations().list(
                    Arrays.asList(
                            //Pass in list of strings to be translated
                            "Hello World, my name is Bill, I am a man, I have a fast car",
                            "How to use Google Translate from Java"),
                    //Target language
                    "ES");

            list.setKey("AIzaSyDIEdX6JQuzItaYgeeuOcWgPGyQaxhUdQo");

            TranslationsListResponse response = list.execute();
            for(TranslationsResource tr : response.getTranslations()) {
                System.out.println(tr.getTranslatedText());
            }


        }
        catch(Exception e){
            e.printStackTrace();
        }

        //______________________________________________//
    }
*/
}
