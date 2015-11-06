package group2.hackernews;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//Sets up the Volley queue for Asynchronous data retrieval
public class MainRequestQueue extends Application {
    private RequestQueue mRequestQueue;
    private static MainRequestQueue mMainRequestQueue;


    public static final String TAG = MainRequestQueue.class.getName();
    @Override
    public void onCreate(){
        super.onCreate();
        mMainRequestQueue = this;
        mRequestQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public static synchronized MainRequestQueue getInstance(){
        return mMainRequestQueue;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancel() {
        mRequestQueue.cancelAll(TAG);
    }
}

