package group2.hackernews;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {

    private ListView topList;
    private API_Getter processor;
    private Intent intent;
    private int story_tracker = 1;
    private SharedPreferences settings;
    public static final String TAG2 = "GoldFish";


    public static String globalArticle = "";
    final static String topStories = "https://hacker-news.firebaseio.com/v0/topstories.json";
    final static String askStories = "https://hacker-news.firebaseio.com/v0/askstories.json";
    final static String jobStories = "https://hacker-news.firebaseio.com/v0/jobstories.json";
    final static String newStories = "https://hacker-news.firebaseio.com/v0/newstories.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        settings = getApplicationContext().getSharedPreferences("theme",0);
        int sTheme;
        try {
            sTheme = settings.getInt("sTheme", 0);
        } catch (NullPointerException e){
            sTheme = 0;
        }

        switch (sTheme){
            default:
            case BigFishActivity.BLUE:
                setTheme(R.style.BlueFish);
                break;
            case BigFishActivity.RED:
                setTheme(R.style.RedFish);
                break;
            case BigFishActivity.GREEN:
                setTheme(R.style.GreenFish);
                break;
            case BigFishActivity.GOLD:
                setTheme(R.style.GoldFish);
                break;
            case BigFishActivity.PURPLE:
                setTheme(R.style.PurpleFish);
                break;
        }

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

                if (o.getUri() == null)
                    Toast.makeText(getApplicationContext(), o.getTitle(), Toast.LENGTH_LONG).show();
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
            if(!storyItem.getUpvoted()){
                menu.add(0, 11, 1, "Up Vote"); // Up Vote item selection is 11
            }
            menu.add(1, 12, 2, "Open in Browser"); // Browser open is 12
            menu.add(0, 13, 3, "Share"); //Open share is 13
            menu.add(0, 14, 4, "Make a comment");//make a comment is 14

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

                story.setUpvoted(true);

                item.setVisible(false);

                return true;
            case 12: // Open in browser selected
                Story story_item1 = (Story) topList.getItemAtPosition(adapterContextMenuInfo.position);

                if (story_item1.getUri() == null)
                    Toast.makeText(MainActivity.this, story_item1.getTitle(), Toast.LENGTH_SHORT).show();
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
            case 14:
                //Daniel
                Story story_item3 = (Story) topList.getItemAtPosition(adapterContextMenuInfo.position);
                String storyID = "https://news.ycombinator.com/item?id=" + story_item3.getId();
                new GetArticleTask().execute(storyID);
                postcommentpage(story_item3.getId());
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

    // ADDED Daniel
    public void postcommentpage(String myArticle){

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        String URL = "https://news.ycombinator.com/item?id=" + myArticle;
        intent.putExtra("URL", URL);
        startActivity(intent);
    }


// ADDED Daniel
private class GetArticleTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        String postResult;
        postResult = "";
        postResult = excuteGet(urls[0]);
        return postResult;
    }

    /**
     * Uses the logging framework to display the output of the fetch
     * operation in the log fragment.
     */
    @Override
    protected void onPostExecute(String result) {
        Log.i(TAG2, result);
    }


    private String excuteGet(String targetURL)
    {
        URL url;
        String charset = "UTF-8";
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            //url = new URL(targetURL  + "/item?id=" + globalArticle);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=" + charset);

            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches(false);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }

            int status = connection.getResponseCode();
            String myStatus = "Status = " + Integer.toString(status);

            rd.close();
            String myString = "GET1=" + response.toString();
            int iTest14 = 99;
            int pos1, pos2, pos2a, pos3a, pos3, pos4, pos5;
            // find "parent" or "item?id= (followed by "parent"  in output string
            //
            // find name="parent" value="fdhajfkdlhal" and extract the "fdhajfkdlhal"
            // and put it into the global variable globalArticle
            String str0, str1, str2, str2a, str3, str3a, str4, str11, str22, str33, str44, strFinal;
            str0 = response.toString();
            str1 = "<span class=\"par\">";
            str2 = "name=\"parent\"";
            str2a = "<a href=\"item?id=";
            str3 = "value=\"";
            str3a = "\">parent";
            str4 = "\"";

            pos1 = str0.toLowerCase().indexOf(str1.toLowerCase());
            pos2 = str0.toLowerCase().indexOf(str2.toLowerCase());
            if ( pos1 > 0) {
                str11 = str0.substring(pos1);
                pos2a = str11.toLowerCase().indexOf(str2a.toLowerCase());
                if (pos2a > 0) {
                    // found "<a href ="item?id="
                    // str22 is string starting with <a href ="item?id="
                    str22 = str11.substring(pos2a);
                    // find ">parent
                    pos3a = str22.toLowerCase().indexOf(str3a.toLowerCase());
                    if (pos3a > 0) {
                        str33 = str22.substring(17); // this gets past the " that is before the article number
                        pos5 = str33.toLowerCase().indexOf(str4.toLowerCase());
                        strFinal = str33.substring(0, pos5);
                        //// str33 is the string starting with value=" after name="hmac"
                        //str33 = str22.substring(pos3a);
                        //// add 7 for the length of value="
                        //pos4 = str33.toLowerCase().indexOf(str4.toLowerCase()) + 7;
                        //// str44 is the string starting with hmac value
                        //str44 = str33.substring(pos4);
                        //// pos5 is the position of the trailing "
                        //pos5 = str44.toLowerCase().indexOf(str4.toLowerCase());
                        //strFinal = str44.substring(0, pos5);
                        globalArticle = strFinal;
                        //globalString = myString;
                        return myString;
                    }
                    else {
                        if (pos2 > 0) {
                            // found name="parent" followed by value="jdkfjdsakf"
                            // and then followed by "parent then extract jdkfjdsakf
                            str22 = str0.substring(pos2);
                            pos3 = str22.toLowerCase().indexOf(str3.toLowerCase());
                            // str33 is the string starting with value=" after name="parent"
                            str33 = str22.substring(pos3);
                            // add 7 for the length of value="
                            //pos4 = str33.toLowerCase().indexOf(str4.toLowerCase())+7;
                            pos4 = str33.toLowerCase().indexOf(str4.toLowerCase()) + 1;
                            // str44 is the string starting with hmac value
                            str44 = str33.substring(pos4);
                            // pos5 is the position of the trailing "
                            pos5 = str44.toLowerCase().indexOf(str4.toLowerCase());
                            strFinal = str44.substring(0, pos5);
                            globalArticle = strFinal;
                            //globalString = myString;
                            return myString;
                            //return response.toString();
                        }

                    }
                }
            }
            else {
                if (pos2 > 0) {
                    // found name="parent" followed by value="jdkfjdsakf"
                    // and then followed by "parent then extract jdkfjdsakf
                    str22 = str0.substring(pos2);
                    pos3 = str22.toLowerCase().indexOf(str3.toLowerCase());
                    // str33 is the string starting with value=" after name="parent"
                    str33 = str22.substring(pos3);
                    // add 7 for the length of value="
                    //pos4 = str33.toLowerCase().indexOf(str4.toLowerCase())+7;
                    pos4 = str33.toLowerCase().indexOf(str4.toLowerCase()) + 1;
                    // str44 is the string starting with hmac value
                    str44 = str33.substring(pos4);
                    // pos5 is the position of the trailing "
                    pos5 = str44.toLowerCase().indexOf(str4.toLowerCase());
                    strFinal = str44.substring(0, pos5);
                    globalArticle = strFinal;
                    //globalString = myString;
                    return myString;
                    //return response.toString();
                }
            }
            return "";
        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
            return "";
        }
    }
}
}