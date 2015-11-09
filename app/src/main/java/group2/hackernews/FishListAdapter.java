package group2.hackernews;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Zovin on 10/7/2015.
 * A custom adapter to list stories
 */
public class FishListAdapter extends ArrayAdapter<Story> {
    private ArrayList<Story> stories;

    public FishListAdapter(Context context, int textViewResourceId, ArrayList<Story> items){
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
            v = vi.inflate(R.layout.fish_layout, null);
        }

        Story o = stories.get(position);

        return v;
    }
}