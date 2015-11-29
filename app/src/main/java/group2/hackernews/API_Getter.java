package group2.hackernews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by david on 10/15/15.
 * modifieds by ZK
 */
public class API_Getter extends AppCompatActivity {

    private MainRequestQueue getter = MainRequestQueue.getInstance();
    private BaseAdapter topAdapter;
    private ArrayList<Story> stories = new ArrayList<>();
    private String title_url = "https://hacker-news.firebaseio.com/v0/item/";

    private ListView topList;
    
    public API_Getter(ListView view, int viewOpt){
        this.topList = view;

        // I'm trying to modulize the adapter to work with the redesign - ZK
        switch (viewOpt) {
            case 0:
                topAdapter = new StoryListAdapter(topList.getContext(), R.layout.list_item, stories);
                break;
            default:
                topAdapter = new FishListAdapter(topList.getContext(), R.layout.fish_layout, stories);
        }
    }
    //Empty for testing
    public API_Getter(){
        //Do Nothing
    }

    //Gets the JSON Array filled with article ID's depending on the type of post.  ie Top, Show, Ask...
    public void use_url_to_get_IDArray_then_process(String source) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, source, "", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //uses the id list to make a call to get detailed post info
                //from JSON objects using the ID's in the array.
                try {
                    for (int i = 0; i < response.length() / 10; i++) {
                        //Make the JSON request for each id.
                        get_JSON_from_HN_and_set_UI_elements(response.getString(i));
                    }

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
                }catch(NullPointerException e){
                    e.printStackTrace();
                }
            }

        });
        getter.add(jsonArrayRequest);
    }

    //Gets a JSON Object from HackerNews and populates the list.
    public void get_JSON_from_HN_and_set_UI_elements(String id){
        String uri = title_url + id + ".json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, uri,"", new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //get the list ready to populate
                topList.setAdapter(topAdapter);
                Story story = fill_story(response);
                stories.add(story);
                topAdapter.notifyDataSetChanged();
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

    public void upvote_story(final String cookies, String id) {
        String url = "https://news.ycombinator.com/item?id=" + id;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ERROR", "error => " + error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Cookie", cookies);
                return params;
            }
        };
        getter.add(postRequest);

    }

    public Story fill_story(JSONObject obj){
        Story story = new Story();

        try {
            if(obj.getString("type").equals("comment")){
                //This is for comments
                    story.setTitle(obj.getString("text"));
                story.setBy(obj.getString("by"));
                story.setKids(obj.getJSONArray("kids"));
                    story.setId(obj.getString("id"));
            } else{
                //This is for stories
                    story.setScore(obj.getString("score"));
                    story.setBy(obj.getString("by"));
                    story.setTitle(obj.getString("title"));
                    story.setType(obj.getString("type"));
                    story.setKids(obj.getJSONArray("kids"));
                story.setId(obj.getString("id"));
                    story.setUri(obj.getString("url"));
                    //story.setText(obj.getString("text"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return story;
    }

    public void clear_processing(){
        getter.cancel();
//        topAdapter.clear();
        stories.clear();
        topAdapter.notifyDataSetChanged();
    }
}
