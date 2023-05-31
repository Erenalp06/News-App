package com.teksen.newsapp.service;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.widget.Toast;

import com.teksen.newsapp.NewsDetailsActivity;

import java.util.HashMap;
import java.util.Locale;

public class TTSService {
    private TextToSpeech mTTS;
    private Context context;

    public TTSService(Context context) {
        this.context = context;
    }


    public void speakText(String text, Locale language, float speechRate, float pitch){

        if (mTTS != null) {
            mTTS.stop();
            mTTS.shutdown();
        }

        mTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = mTTS.setLanguage(language);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, "Language not supported", Toast.LENGTH_SHORT).show();
                    } else {
                        mTTS.setPitch(pitch);
                        mTTS.setSpeechRate(speechRate);
                        mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else {
                    Toast.makeText(context, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void stopSpeaking() {
        if (mTTS != null) {
            mTTS.stop();
        }
    }
}
