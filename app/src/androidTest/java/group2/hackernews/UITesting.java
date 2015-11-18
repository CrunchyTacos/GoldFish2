package group2.hackernews;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;
import android.test.ActivityInstrumentationTestCase2;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static android.support.test.espresso.IdlingResource.ResourceCallback;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Created by david on 11/9/15.
 */
//Written by David
public class UITesting {

        //I would prefer to have each UI test have a specific function for each component but
        //espresso starts a new activity if I do it that way.

        @Rule
        public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

        @Test
        public void run_tests(){

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //List Displays stories
            onData(Matchers.anything()).inAdapterView(withId(R.id.list))
                    .atPosition(1)
                    .check(matches(isEnabled()));

            //Is the button there?
            onView(withText("Top")).check(matches(isDisplayed()));

            //make sure the switch story button works
            onView(withText("Top")).perform(click());
            onView(withText("Ask")).check(matches(isDisplayed()));

            //Option to open in browser is available
            onData(Matchers.anything()).inAdapterView(withId(R.id.list))
                    .atPosition(1)
                    .perform(longClick());
            onView(withText("Open in Browser")).check(matches(isDisplayed()));
            //Opens an article in a browser, I cant get an assert to work with espresso.
            onView(withText("Open in Browser")).perform(click());


        }


}
