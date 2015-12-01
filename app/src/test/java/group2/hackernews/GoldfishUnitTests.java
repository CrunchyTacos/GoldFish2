package group2.hackernews;

import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.mockito.Mockito.*;

import group2.hackernews.API_Getter;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class GoldfishUnitTests {

    @Test
    public void test_fill_story_main_type(){

        JSONObject obj = new JSONObject();
        JSONArray jarray = new JSONArray();

        try {
            obj.put("type", "story");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            jarray.put(0,1234);
            jarray.put(1,2345);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("title","title");
            assertNotNull(obj.getString("title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("by", "by");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("score", "17");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("url", "http//blah");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("kids", jarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("text", "text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("type", "story");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        API_Getter getter = new API_Getter();

        Story story;
        story = getter.fill_story(obj);

        assertNotNull(story.getBy());
        assertNotNull(story.getKids());
        assertNotNull(story.getScore());
        assertNotNull(story.getTitle());
        assertNotNull(story.getType());
        assertNotNull(story.getUri());
    }

    @Test
    public void test_fill_story_comments(){

        JSONObject obj = new JSONObject();
        JSONArray jarray = new JSONArray();

        try {
            jarray.put(0,1234);
            jarray.put(1,2345);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("by", "by");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("kids", jarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("text", "text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            obj.put("type", "comment");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        API_Getter getter = new API_Getter();

        Story story;
        story = getter.fill_story(obj);



        assertNotNull(story.getBy());
        assertNotNull(story.getKids());
        assertNotNull(story.getTitle());
    }

}

