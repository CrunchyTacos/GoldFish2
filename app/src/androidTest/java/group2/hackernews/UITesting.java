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
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
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
import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * Created by david on 11/9/15.
 */
public class UITesting {

        @Rule
        public final ActivityTestRule<MainActivity> main = new ActivityTestRule<>(MainActivity.class);

        @Test
        public void test_long_click_Main (){
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            onData(Matchers.anything()).inAdapterView(withId(R.id.list))
                    .atPosition(1)
                    .perform(longClick());
            onView(withText("CommentActivity")).check(ViewAssertions.matches(isDisplayed()));
        }


}
