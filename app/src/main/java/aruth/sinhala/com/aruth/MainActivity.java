package aruth.sinhala.com.aruth;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.os.Looper;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new MyBrowser());

        String url = "http://13.58.202.127";
        webview.setWebChromeClient(new WebChromeClient());
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webview.loadUrl(url);


        SendfeedbackJob job = new SendfeedbackJob();
        job.execute();


    }

    public void showDialog(String message) {

    }

    public String updateRequest() {
        int thisAppVesion = 0;
        String updateURL = "http://13.58.202.127/version.php";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = null;

            response = httpclient.execute(new HttpGet(new URI(updateURL)));
//
//        StatusLine statusLine = response.getStatusLine();
//        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//            response.getEntity().writeTo(out);
//            String responseString = out.toString();
//            out.close();
//            return responseString;
//        } else{
//            //Closes the connection.
//            response.getEntity().getContent().close();
//            return "false";
//        }
            return response.toString();
        } catch (Exception e) {
            return e.toString();
        }
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


    class SendfeedbackJob extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String[] params) {
            int thisAppVesion = 0;//change this everytime updating the app
            //this can be used to check internet connectivity too
            String updateURL = "http://13.58.202.127/version.php";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = null;

                response = httpclient.execute(new HttpGet(new URI(updateURL)));

                StatusLine statusLine = response.getStatusLine();
                if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    response.getEntity().writeTo(out);
                    String responseString = out.toString();
                    out.close();
                    int newAppVersion = Integer.parseInt(responseString.trim());
                    if (newAppVersion > thisAppVesion) {
                        return "true";
                    } else {
                        return "false1";
                    }
//                    return "_"+responseString.trim()+"_";
                } else {
                    //Closes the connection.
                    response.getEntity().getContent().close();
                    return "false2";
                }
            } catch (Exception e) {
                //return "false3";
                return e.toString();
            }

        }

        @Override
        protected void onPostExecute(String message) {
            if (message == "true") {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(MainActivity.this);
                dlgAlert.setMessage("මෙම යෙදවුම සඳහා නව යාවත්කාලයක් ඇත. කරුණාකර නව යාවත්කාලය ස්ථාපනය කරගන්න.");
                dlgAlert.setTitle("යාවත්කාලීන කරන්න!");
                dlgAlert.setPositiveButton("හරි", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();
            }
        }
    }
}

