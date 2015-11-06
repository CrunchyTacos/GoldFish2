package group2.hackernews;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * Created by Zovin on 10/7/2015.
 * A custom adapter to list stories
 */
public class StoryListAdapter extends ArrayAdapter<Story> {
    private ArrayList<Story> stories;

    public StoryListAdapter(Context context, int textViewResourceId, ArrayList<Story> items){
        super(context, textViewResourceId, items);
        this.stories = items;
    }

    public ArrayList<Story> get_stories(){
        return stories;
    }

    @Override
    //Creates the textview in the layout to be displayed in the listview
    public View getView(int position, View convertView, ViewGroup parent){
        View v = convertView;
        if(v == null){
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.list_item, null);
        }

        Story o = stories.get(position);
        TextView title = (TextView) v.findViewById(R.id.title);
        TextView by = (TextView) v.findViewById(R.id.by);
        title.setText(Html.fromHtml(o.getTitle()));
        if(o.getScore() != null && o.getKids() != null){
            by.setText(o.getScore() + " points by " + o.getBy() + "\n" + "comments: " + o.getKids().length());
        }
        else
            by.setText("by " + o.getBy());

        return v;
    }
}