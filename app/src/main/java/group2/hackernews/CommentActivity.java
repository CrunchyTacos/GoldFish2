package group2.hackernews;

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

    API_Getter processor;
    ListView commentList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        //This currently does nothing, but I don't want to remove it yet in case we decide to implement more functionality
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
