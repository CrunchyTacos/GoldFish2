package group2.hackernews;

import android.content.Context;
import android.text.Html;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        Story o = stories.get(position);
        ImageView fish = (ImageView) v.findViewById(R.id.image);
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.lay);

        int width = displayMetrics.widthPixels;
        width = width/3 - 42;

        if (Integer.parseInt(o.getScore()) < 250) {
            layout.setGravity(RelativeLayout.ALIGN_PARENT_RIGHT);
            fish.setPadding((Integer.parseInt(o.getScore()) / width), 0, 0, 0);
        } else if (Integer.parseInt(o.getScore()) < 500){
            layout.setGravity(RelativeLayout.CENTER_IN_PARENT);
            fish.setPadding((Integer.parseInt(o.getScore()) / width), 0, 0, 0);
        } else{
            layout.setGravity(RelativeLayout.ALIGN_PARENT_LEFT);
        }

        return v;
    }
}