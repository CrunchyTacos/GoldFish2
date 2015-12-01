package group2.hackernews;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

//by Matthew
public class LoginActivity extends AppCompatActivity {

    private int loginflag = 0;
    private WebView loginwebview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.login_toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("GoldFish");

        String URL = getIntent().getExtras().getString("URL");

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(URL.contains("/login=?goto")){
            loginflag = 1;
        }

        loginwebview = new WebView(this);
        loginwebview.getSettings().setJavaScriptEnabled(true);
        loginwebview.getSettings().setBuiltInZoomControls(true);
        loginwebview.setWebViewClient(new MyWebViewClient());
        setContentView(loginwebview);
        loginwebview.loadUrl(URL);
    }

    public class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            finish();
            return true;
        }
    }
}
