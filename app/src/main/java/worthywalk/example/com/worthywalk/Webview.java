package worthywalk.example.com.worthywalk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class Webview extends AppCompatActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        webView=(WebView) findViewById(R.id.webview);
// specify the url of the web page in loadUrl function

        webView.loadUrl("https://worthy-walk.flycricket.io/privacy.html?fbclid=IwAR0Eq2OqovG3xdhsqaDIkcRqhL_aANh30DcZ5q6wJhcTSe3w5LZ16zoW-80");
    }
}
