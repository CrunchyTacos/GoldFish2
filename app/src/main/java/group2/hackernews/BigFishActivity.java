package group2.hackernews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.ThemeUtils;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class BigFishActivity extends AppCompatActivity {


    private MainRequestQueue getter = MainRequestQueue.getInstance();

    private static int sTheme;

    public final static int BLUE = 1;
    public final static int RED = 2;
    public final static int PURPLE = 3;
    public final static int GREEN = 4;
    public final static int GOLD = 5;

    private SharedPreferences settings;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getApplicationContext().getSharedPreferences("theme", 0);
        sTheme = settings.getInt("sTheme", 0);
        CookieManager cookieManager = CookieManager.getInstance();
        final String cookies = cookieManager.getCookie("https://news.ycombinator.com/");


        switch (sTheme){
            default:
            case BLUE:
                setTheme(R.style.BlueFish);
                break;
            case RED:
                setTheme(R.style.RedFish);
                break;
            case GREEN:
                setTheme(R.style.GreenFish);
                break;
            case GOLD:
                setTheme(R.style.GoldFish);
                break;
            case PURPLE:
                setTheme(R.style.PurpleFish);
                break;
        }

        setContentView(R.layout.activity_big_fish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] first = cookies.split(";");
                String[] second = first[1].split("=");
                String[] third = second[1].split("&");
                String user = third[0];
                get_scales("user");
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void get_scales(String id){
        String user_url = "https://hacker-news.firebaseio.com/v0/user/";
        String uri = user_url + id + ".json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri,"", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int karma = Integer.parseInt(response.getString("karma"));
                    int level = 1;
                    if(karma > 5 && karma < 10) level = 2;
                    else if (karma < 20) level = 3;
                    else if (karma < 40) level = 4;
                    else if (karma < 80) level = 5;
                    else if (karma < 160) level = 6;
                    else if (karma < 320) level = 7;
                    else if (karma < 640) level = 8;
                    else level = 9;
                    Toast.makeText(BigFishActivity.this, "Fish Level:  " + level, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    new AlertDialog.Builder(getApplicationContext())
                            .setMessage(error.getMessage())
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        getter.add(jsonObjectRequest);
    }

    public void blueAction (View v){
        Toast.makeText(getApplicationContext(), "Blue Scale Pressed " + this.getTheme().toString(), Toast.LENGTH_SHORT).show();
        //themeUtils.changeToTheme(this, themeUtils.BLUE);
        sTheme = BLUE;
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sTheme",BLUE);
        editor.apply();
        recreate();
    }

    public void purpleAction (View v){
        Toast.makeText(getApplicationContext(),"Purple Scale Pressed", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sTheme",PURPLE);
        editor.apply();
        recreate();
    }

    public void redAction (View v){
        Toast.makeText(getApplicationContext(),"Red Scale Pressed", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sTheme",RED);
        editor.apply();
        recreate();
    }

    public void greenAction (View v){
        Toast.makeText(getApplicationContext(),"Green Scale Pressed", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sTheme",GREEN);
        editor.apply();
recreate();    }

    public void goldAction (View v){
        Toast.makeText(getApplicationContext(),"Gold Scale Pressed", Toast.LENGTH_SHORT).show();
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("sTheme",GOLD);
        editor.apply();
        recreate();
    }
}
