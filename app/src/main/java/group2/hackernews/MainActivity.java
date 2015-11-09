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
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    ListView topList;
    ListView jobList;
    API_Getter processor;
    private ArrayList<Story> comments = new ArrayList<>();
    Intent intent;
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
        /*topList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            //Moved to Menu Option selected

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
        });*/

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

        //Give list items ability to create context Menu - ZK
        registerForContextMenu(topList);

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
            case 0: // Login menu option selected
                browser1("https://news.ycombinator.com/login?goto=news");
                return true;
            case R.id.story_changer: //Cleaned up button changer to look nice with other menu options (Originally David, mod Zovin
                processor.clear_processing();
                switch (story_tracker){
                    case 1:
                        processor.use_url_to_get_IDArray_then_process(askStories);
                        story_tracker++;
                        item.setTitle("Ask");
                        break;
                    case 2:
                        processor.use_url_to_get_IDArray_then_process(jobStories);
                        story_tracker++;
                        item.setTitle("Jobs");
                        break;
                    case 3:
                        processor.use_url_to_get_IDArray_then_process(newStories);
                        story_tracker++;
                        item.setTitle("New");
                        break;
                    default:
                        processor.use_url_to_get_IDArray_then_process(topStories);
                        story_tracker = 1;
                        item.setTitle("Top");
                        break;
                }
                return super.onOptionsItemSelected(item);

            default:
                Toast.makeText(getApplicationContext(), "I don't know what you pressed", Toast.LENGTH_LONG).show();
                break;
        }

        // This code was moved to the switch case
        /*//Button to change the story type
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
        }*/
        return onOptionsItemSelected(item);

    }

    /*
    Sets the layout for the context menu created by longpresses on list items
    Overrided by Zovin
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        if(v.getId() == R.id.list){ //Check if list item is chosen
            //Setup to access list item information
            ListView listView = (ListView) v;
            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

            //Gets the story object at the selected item.
            Story storyItem = (Story) listView.getItemAtPosition(adapterContextMenuInfo.position);

            //Setup to manipulate context menu layout
            LayoutInflater layoutInflater = this.getLayoutInflater();
            View header = layoutInflater.inflate(R.layout.storyitem_contextmenu, null);

            //Get the xml objects from the context menu header
            TextView title  = (TextView) header.findViewById(R.id.title);
            TextView by     = (TextView) header.findViewById(R.id.by);
            TextView score  = (TextView) header.findViewById(R.id.score);

            //Set the xml object values
            title.setText(storyItem.getTitle());
            by.setText(storyItem.getBy());
            score.setText(storyItem.getScore());

            //Set context menu properties
            menu.setHeaderView(header);
            menu.add(0, 10, 0, "View " + (storyItem.getKids() == null ? "0" : storyItem.getKids().length()) + " Comments"); //Comments item selection is 10
            menu.add(0, 11, 1, "Up Vote"); // Up Vote item selection is 11
            menu.add(1, 12, 2, "Open in Browser"); // Browser open is 12

            //Inflate the menu into view
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.blank_context_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        // Setup to get information for item, especially for context menu option selections
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo;
        adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        //Decide what to do
        switch (item.getItemId()){
            case 10: // View comments option selected
                //Get the story, create the Intent, put the comment id's into the intent,
                //start the comment activity with the intent
                Toast.makeText(getApplicationContext(), "Opening Comments",Toast.LENGTH_LONG).show();
                Story story_item = (Story) topList.getItemAtPosition(adapterContextMenuInfo.position);
                String string = story_item.getKids().toString();

                intent = new Intent(MainActivity.this, CommentActivity.class);

                if (story_item.getUri() == null)
                    Toast.makeText(getApplicationContext(), "Can't open comments", Toast.LENGTH_LONG).show();

                intent.putExtra("kids", string);
                startActivity(intent);
                return true;
            case 11: // Up vote option selected
                Toast.makeText(getApplicationContext(), "Up Vote not made yet", Toast.LENGTH_LONG).show();
                return true;
            case 12: // Open in browser selected
                Story story_item1 = (Story) topList.getItemAtPosition(adapterContextMenuInfo.position);
                intent = new Intent(MainActivity.this, CommentActivity.class);

                if (story_item1.getUri() == null)
                    Toast.makeText(getApplicationContext(), "Can't open article", Toast.LENGTH_LONG).show();
                else
                    browser1(story_item1.getUri());
                return true;
            default:
                Toast.makeText(getApplicationContext(), "I don't know what you pressed", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }
}