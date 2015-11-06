package group2.hackernews;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    private ListView topList;
    private ListView jobList;
    private API_Getter processor;
    private ArrayList<Story> comments = new ArrayList<>();
    private Intent intent;
    private int story_tracker = 1;

    final static String topStories = "https://hacker-news.firebaseio.com/v0/topstories.json";
    final static String askStories = "https://hacker-news.firebaseio.com/v0/askstories.json";
    final static String jobStories = "https://hacker-news.firebaseio.com/v0/jobstories.json";
    final static String newStories = "https://hacker-news.firebaseio.com/v0/newstories.json";

    private StoryListAdapter jobAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Loading...");

        //Find the main listview
        topList = (ListView) findViewById(R.id.list);

        //Make a processor and attach the ListView to it
        processor = new API_Getter(topList);

        //Tell the processor to fill the list with the url.
        processor.use_url_to_get_IDArray_then_process(topStories);

        //Set the list items to be clickable
        topList.setClickable(true);

        //What the item does when the view is pressed and held
        topList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Get the story, create the Intent, put the comment id's into the intent,
                //start the comment activity with the intent
                Story o = (Story) topList.getItemAtPosition(position);
                String string = o.getKids().toString();

                intent = new Intent(MainActivity.this, CommentActivity.class);

                if (o.getUri() == null)
                    Toast.makeText(getApplicationContext(), "Can't open comments", Toast.LENGTH_LONG).show();

                intent.putExtra("kids", string);
                startActivity(intent);
                return true;
            }
        });

        //What the item does when its a quick push
        topList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                //Get the story, start a browser at the url in the story
                Story o = (Story) topList.getItemAtPosition(position);
                intent = new Intent(MainActivity.this, CommentActivity.class);

                if (o.getUri() == null)
                    Toast.makeText(getApplicationContext(), "Can't open article", Toast.LENGTH_LONG).show();
                else
                    browser1(o.getUri());
            }
        });
        progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //adds another option to the options menu, this ideally needs to be done in the XML file
        menu.add(0, 0, 0, "Login").setShortcut('3', 'c');
        return true;
    }

    //open a browser using url
    public void browser1(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    //Commands for the options and toolbar widgets
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //If we add more options in the pulldown menu more cases will be added
        switch (item.getItemId()) {
            case 0:
                browser1("https://news.ycombinator.com/login?goto=news");
                return true;

        }

        //Button to change the story type
        if(item.getItemId() == R.id.story_changer){
            processor.clear_processing();
            if(story_tracker == 1){
                processor.use_url_to_get_IDArray_then_process(askStories);
                story_tracker++;
                item.setTitle("Ask");
            }
            else if (story_tracker == 2){
                processor.use_url_to_get_IDArray_then_process(jobStories);
                story_tracker++;
                item.setTitle("Jobs");
            }
            else if (story_tracker == 3){
                processor.use_url_to_get_IDArray_then_process(newStories);
                story_tracker++;
                item.setTitle("New");
            }
            else {
                processor.use_url_to_get_IDArray_then_process(topStories);
                story_tracker = 1;
                item.setTitle("Top");
            }
        }

        return super.onOptionsItemSelected(item);
    }
}