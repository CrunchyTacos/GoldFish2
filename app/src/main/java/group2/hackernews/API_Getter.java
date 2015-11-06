package group2.hackernews;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by david on 10/15/15.
 */
public class API_Getter extends AppCompatActivity {

    private MainRequestQueue getter = MainRequestQueue.getInstance();
    private StoryListAdapter topAdapter;
    private ArrayList<Story> stories = new ArrayList<>();
    private String title_url = "https://hacker-news.firebaseio.com/v0/item/";
    private ListView topList;
    public API_Getter(ListView view){
        this.topList = view;
        topAdapter = new StoryListAdapter(topList.getContext(), R.layout.list_item, stories);
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
                new AlertDialog.Builder(getApplicationContext())
                        .setMessage(error.getMessage())
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .show();
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
                topAdapter.get_stories().add(story);
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

    private Story fill_story(JSONObject obj){
        Story story = new Story();
        try {
            //This is for normal stories
            story.setTitle(obj.getString("title"));
            story.setBy(obj.getString("by"));
            story.setScore(Integer.toString(obj.getInt("score")));
        } catch (JSONException e) {
            //This is for comments due to comments having no title
            try {
                story.setTitle(obj.getString("text"));
                story.setBy(obj.getString("by"));
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        try {

            story.setUri(obj.getString("url"));
            story.setType(obj.getString("type"));
            story.setKids(obj.getJSONArray("kids"));
            story.setText(obj.getString("text"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return story;
    }

    public void clear_processing(){
        getter.cancel();
        topAdapter.clear();
        topAdapter.notifyDataSetChanged();
    }
}
