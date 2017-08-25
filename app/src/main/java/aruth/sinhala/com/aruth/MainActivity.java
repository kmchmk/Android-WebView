package aruth.sinhala.com.aruth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private static final String URL = "www.sinhalaaruth.tk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new MyBrowser());
        webview.setWebChromeClient(new MyChromeBrowser());
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.loadUrl("http://" + URL);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.equals("mailto:sinhalaaruth@gmail.com")){
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_EMAIL, new String[]{"sinhalaaruth@gmail.com"});
                i.putExtra(Intent.EXTRA_SUBJECT, "සිංහල අරුත් පිළිබඳව");
                i.putExtra(Intent.EXTRA_TEXT, "සිංහල අරුත් කණ්ඩායම වෙත,");
                startActivity(i);
                return true;
            }
            else if (Uri.parse(url).getHost().equals(URL)) {
                return false;
            }
            else {
                view.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                return true;
            }
        }
    }

    private class MyChromeBrowser extends WebChromeClient {
        private ProgressDialog mProgress;
        @Override
        public void onProgressChanged(WebView view, int progress) {
            if (mProgress == null) {
                mProgress = new ProgressDialog(MainActivity.this);
                mProgress.show();
            }
            mProgress.setMessage("Loading...");
            if (progress == 100) {
                mProgress.dismiss();
                mProgress = null;
            }
        }
    }
}

