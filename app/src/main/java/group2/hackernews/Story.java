package group2.hackernews;

import org.json.JSONArray;

/**
 * Created by Zovin on 10/7/2015.
 * These are the properties of the acquired stories
 */
class Story {
    private String title;
    private String by;
    private String score;
    private String uri;
    private JSONArray kids;
    private String text;
    private String type;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getBy(){
        return by;
    }

    public void setBy(String by){
        this.by = by;
    }

    public String getScore(){
        return score;
    }

    public void setScore(String score){
        this.score = score;
    }

    public String getUri(){
        return uri;
    }

    public void setUri(String uri){
        this.uri = uri;
    }

    public JSONArray getKids(){
        return kids;
    }

    public void setKids(JSONArray kids){
        this.kids = kids;
    }

    public String getText(){
        return text;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }
}
