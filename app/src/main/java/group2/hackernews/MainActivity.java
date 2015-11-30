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
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    private ListView topList;
    private API_Getter processor;
    private Intent intent;
    private int story_tracker = 1;

    final static String topStories = "https://hacker-news.firebaseio.com/v0/topstories.json";
    final static String askStories = "https://hacker-news.firebaseio.com/v0/askstories.json";
    final static String jobStories = "https://hacker-news.firebaseio.com/v0/jobstories.json";
    final static String newStories = "https://hacker-news.firebaseio.com/v0/newstories.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ProgressDialog progressDialog = ProgressDialog.show(this, "Loading", "Loading...");

        //Find the main listview
        topList = (ListView) findViewById(R.id.list);

        //Make a processor and attach the ListView to it
        processor = new API_Getter(topList, 1);

        //Tell the processor to fill the list with the url.
        processor.use_url_to_get_IDArray_then_process(topStories);

        //Set the list items to be clickable
        topList.setClickable(true);

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

        android.webkit.CookieManager.getInstance().removeAllCookies(null);

        progressDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.add(0, 0, 0, "Login").setShortcut('3', 'c');
        menu.add(0, 1, 1, "Logout");
        return true;
    }

    //open a webbrowser using url
    public void browser1(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    //created by Elizabeth Durkin
     //share a url and choose sharing method
    public void share(String url){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, url);     //might have to remove uri.parse to just url
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }
    
    //Commands for the options and toolbar widgets
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //If we add more options in the pulldown menu more cases will be added
        switch (item.getItemId()) {
            case 0: // Login menu option selected
                loginpage();
                return true;
            case 1:
                logout();
                return true;
            case R.id.scale_button:
                Intent intent = new Intent(MainActivity.this, BigFishActivity.class);
                startActivity(intent);
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
            menu.add(0, 13, 3, "Share"); //Open share is 13

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
                //Get the cookies saved from webview
                CookieManager cookieManager = CookieManager.getInstance();
                String cookies = cookieManager.getCookie("https://news.ycombinator.com/");
                //Get the story ID
                Story story = (Story) topList.getItemAtPosition(adapterContextMenuInfo.position);
                String id = story.getId();
                //upvote the story
                processor.upvote_story(cookies, id);
                return true;
            case 12: // Open in browser selected
                Story story_item1 = (Story) topList.getItemAtPosition(adapterContextMenuInfo.position);

                if (story_item1.getUri() == null)
                    Toast.makeText(getApplicationContext(), "Can't open article", Toast.LENGTH_LONG).show();
                else
                    browser1(story_item1.getUri());
                return true;
            case 13: //share
                Story story_item2 = (Story) topList.getItemAtPosition(adapterContextMenuInfo.position);
                if (story_item2.getUri() == null)
                    Toast.makeText(getApplicationContext(), "Nothing to share", Toast.LENGTH_LONG).show();
                else
                    share(story_item2.getUri());
                return true;
            default:
                Toast.makeText(getApplicationContext(), "I don't know what you pressed", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    //by Matthew
    public void loginpage(){

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        String URL = "https://news.ycombinator.com/login?goto=news";
        intent.putExtra("URL", URL);
        startActivity(intent);
    }

    //by Matthew
    public void logout(){

        android.webkit.CookieManager.getInstance().removeAllCookies(null);
        Toast.makeText(MainActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
    }
}
