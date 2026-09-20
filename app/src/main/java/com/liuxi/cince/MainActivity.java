package com.liuxi.cince;

import android.app.Activity;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends Activity implements TextToSpeech.OnInitListener {
    private WebView web;
    private TextToSpeech tts;
    private volatile boolean ttsReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        web = new WebView(this);
        web.setKeepScreenOn(true);
        setContentView(web);
        WebSettings s = web.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDomStorageEnabled(true);
        s.setMediaPlaybackRequiresUserGesture(false);
        s.setAllowFileAccess(true);
        web.setWebViewClient(new WebViewClient());
        web.setOverScrollMode(View.OVER_SCROLL_NEVER);
        tts = new TextToSpeech(this, this);
        web.addJavascriptInterface(new Bridge(), "AndroidTTS");
        web.loadUrl("file:///android_asset/index.html");
    }

    @Override
    public void onInit(int status) {
        if (status != TextToSpeech.SUCCESS) {
            ttsReady = false;
            return;
        }
        int r = tts.setLanguage(Locale.SIMPLIFIED_CHINESE);
        ttsReady = (r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED);
        tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override public void onStart(String id) { }
            @Override public void onDone(String id) { done(); }
            @Override public void onError(String id) { done(); }
        });
        if (!ttsReady) {
            runOnUiThread(() -> Toast.makeText(MainActivity.this,
                "Çince ses verisi yok. Ayarlar > Metin okuma > Google TTS > Çince ses indir.",
                Toast.LENGTH_LONG).show());
        }
    }

    private void done() {
        web.post(() -> web.evaluateJavascript("window.__ttsDone && window.__ttsDone()", null));
    }

    private class Bridge {
        @JavascriptInterface
        public boolean speak(final String text, final float rate, final float pitch) {
            if (!ttsReady) return false;
            runOnUiThread(() -> {
                tts.setSpeechRate(rate);
                tts.setPitch(pitch);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "u" + System.nanoTime());
            });
            return true;
        }

        @JavascriptInterface
        public void stop() {
            runOnUiThread(() -> tts.stop());
        }

        @JavascriptInterface
        public boolean isReady() {
            return ttsReady;
        }
    }

    @Override
    public void onBackPressed() {
        web.evaluateJavascript("window.__back ? window.__back() : false", value -> {
            if (!"true".equals(value)) {
                MainActivity.super.onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }
}
