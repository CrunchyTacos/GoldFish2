package group2.hackernews;

import android.app.Activity;
import android.content.Intent;

import group2.hackernews.R;

/**
 * This file was used from: http://www.c-sharpcorner.com/UploadFile/88b6e5/change-theme-of-layout-runtime-by-button-click-in-android/
 *
 * Used to change the theme of the application
 *
 * Created by Zovin on 11/30/2015.
 */
public class themeUtils {
    private static int sTheme;

    public final static int THEME_DEFAULT = 0;
    public final static int BLUE = 1;
    public final static int RED = 2;
    public final static int PURPLE = 3;
    public final static int GREEN = 4;
    public final static int GOLD = 5;

    /**
     * Set the theme of the Activity, and restart it by creating a new activity of the same type
     */
    public static void changeToTheme(Activity activity, int theme){
        sTheme = theme;
        activity.finish();
        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration.*/
    public static void  onActivityCreateSetTheme(Activity activity){
        switch (sTheme){
            default:
            case THEME_DEFAULT:
            case BLUE:
                activity.setTheme(R.style.BlueFish);
                break;
            case RED:
                activity.setTheme(R.style.RedFish);
                break;
            case PURPLE:
                activity.setTheme(R.style.PurpleFish);
                break;
            case GREEN:
                activity.setTheme(R.style.GreenFish);
                break;
            case GOLD:
                activity.setTheme(R.style.GoldFish);
                break;
        }
    }
}
