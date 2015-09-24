package com.github.jlmd.animatedcircleloadingview.sample.ACTIVITY;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.audiofx.AudioEffect;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.app.jlmd.animatedcircleloadingview.sample.R;
import com.github.jlmd.animatedcircleloadingview.AnimatedCircleLoadingView;
import com.jpardogo.android.googleprogressbar.library.GoogleProgressBar;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Mainak Karmakar on 15/09/2015.
 */
public class HP_PA_HOME extends Activity implements RecognitionListener, TextToSpeech.OnInitListener {

    ImageButton home_speech_imgbtn;
    TextView home_speech_txtvw, home_powered_txt, footer_marque_txt;
    Typeface typeFace;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG = "HP_PA_HOME";
    private AnimatedCircleLoadingView animatedCircleLoadingView;
    private TextToSpeech tts;
    String globaltext;
    Animation animScale;
    GoogleProgressBar mProgressBar;
    String tempresult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        globaltext = "Welcome to Bytech India H.P Partner Assists";
        tts = new TextToSpeech(this, this);
        System.out.println("speech status onstart" + tts.isSpeaking());
        //layout declaration
        hppa_dcl_layout();
        //setting the animation
        animScale = AnimationUtils.loadAnimation(this,
                R.anim.anim_scale);
        //layout reference
        hppa_dcl_layout_variables();
        //widget fonts
        hppa_set_widget_fonts();

        //configure speech recognizer
        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);


        //  speakOut(globaltext);


    }//oncreate ends here

    public void hppa_dcl_layout() {

        setContentView(R.layout.hp_pa_home);

    }

    public void hppa_dcl_layout_variables() {

        home_speech_imgbtn = (ImageButton) findViewById(R.id.home_speech_imgbtn);
        home_speech_txtvw = (TextView) findViewById(R.id.home_speech_txtvw);
        home_powered_txt = (TextView) findViewById(R.id.footer_powered_txt);
        mProgressBar = (GoogleProgressBar) findViewById(R.id.google_progress);
        footer_marque_txt = (TextView) findViewById(R.id.footer_marque_txt);

    }

    public void hppa_set_widget_fonts() {

        typeFace = Typeface.createFromAsset(getAssets(),
                "fonts/OpenSans-Regular.ttf");
        home_speech_txtvw.setTypeface(typeFace);
        home_powered_txt.setTypeface(typeFace);

    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("speech status onresume" + tts.isSpeaking());

        home_speech_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.startAnimation(animScale);
                speech.startListening(recognizerIntent);
                home_speech_imgbtn.setClickable(false);
                mProgressBar.setVisibility(View.VISIBLE);

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        if (speech != null) {
            speech.destroy();

            Log.i(LOG_TAG, "destroy");
        }

    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        home_speech_txtvw.setText("Listening . ");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
        home_speech_txtvw.setText("Listening . .");

    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        //  progressBar.setIndeterminate(true);
        //   toggleButton.setChecked(false);
        home_speech_txtvw.setText("Listening . . .");
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        home_speech_txtvw.setText(errorMessage);
        speakOut(errorMessage + " please tab and speak again");
        //    toggleButton.setChecked(false);
        home_speech_imgbtn.setClickable(true);
        mProgressBar.setVisibility(View.GONE);

    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
        home_speech_txtvw.setText("Listen");

    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
        Toast.makeText(HP_PA_HOME.this, "result " + arg0, Toast.LENGTH_SHORT).show();
        home_speech_txtvw.setText("Listening . . . .");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
        home_speech_txtvw.setText("Listening");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";
        tempresult = matches.get(0);
        home_speech_txtvw.setText(matches.get(0));
        speakOut("Do you mean " + "\t" + tempresult);
        home_speech_imgbtn.setClickable(true);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        //   progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }


    @Override
    public void onInit(int status) {

        System.out.println("Enterd init Function");
        System.out.println("tts init status out" + status);
        if (status == TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.ENGLISH);
            System.out.println("tts init status if" + status);
            // tts.setPitch(5); // set pitch level

            // tts.setSpeechRate(2); // set speech speed rate
            System.out.println("languge status out" + result);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                System.out.println("languge status if" + result);
                Log.e("TTS", "Language is not supported");
            } else {
                speakOut(globaltext);
                System.out.println("languge status else" + result);
            }

        } else {
            Log.e("TTS", "Initilization Failed");
            System.out.println("tts init status else" + status);
        }

    }

    private void speakOut(String text) {
        System.out.println("Entered Speakout");
        System.out.println("Speakout text" + text);
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
