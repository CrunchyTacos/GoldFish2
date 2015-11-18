package group2.hackernews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class BigFishActivity extends AppCompatActivity {


    private MainRequestQueue getter = MainRequestQueue.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_fish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                get_scales("jl");
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
                    Toast.makeText(BigFishActivity.this, response.getString("karma"), Toast.LENGTH_SHORT).show();
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
        Toast.makeText(getApplicationContext(),"Blue Scale Pressed", Toast.LENGTH_SHORT).show();
    }

    public void purpleAction (View v){
        Toast.makeText(getApplicationContext(),"Purple Scale Pressed", Toast.LENGTH_SHORT).show();
    }

    public void redAction (View v){
        Toast.makeText(getApplicationContext(),"Red Scale Pressed", Toast.LENGTH_SHORT).show();
    }

    public void greenAction (View v){
        Toast.makeText(getApplicationContext(),"Green Scale Pressed", Toast.LENGTH_SHORT).show();
    }

    public void goldAction (View v){
        Toast.makeText(getApplicationContext(),"Gold Scale Pressed", Toast.LENGTH_SHORT).show();
    }
}
