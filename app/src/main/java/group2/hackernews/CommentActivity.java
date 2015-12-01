package group2.hackernews;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

public class CommentActivity extends AppCompatActivity {

    private API_Getter processor;
    private ListView commentList;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = getSharedPreferences("theme",0);
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

        setContentView(R.layout.activity_comment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        JSONArray jarray;

        //Get the intent
        Bundle extras = getIntent().getExtras();

        //save it as a string
        String list = extras.getString("kids");

        //Convert the string into a JSONArray, make a new API_getter for the new ListView, Fill the ListView
        try {
            jarray = new JSONArray(list);
            commentList = (ListView) findViewById(R.id.clist);
            processor = new API_Getter(commentList, 0);
            for(int i = 0; i < jarray.length(); i++){
                try {
                    processor.get_JSON_from_HN_and_set_UI_elements(jarray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
