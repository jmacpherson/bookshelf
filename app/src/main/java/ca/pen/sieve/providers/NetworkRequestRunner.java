package ca.pen.sieve.providers;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import androidx.annotation.MainThread;

public class NetworkRequestRunner {

    public static final String TAG = "NetworkRequestRunner";
    private static NetworkRequestRunner sInstance;

    private ThreadFactory mThreadFactory;

    public static NetworkRequestRunner getInstance() {
        if(sInstance == null) {
            sInstance = new NetworkRequestRunner(Executors.defaultThreadFactory());
        }
        return sInstance;
    }

    public NetworkRequestRunner(ThreadFactory threadFactory) {
        mThreadFactory = threadFactory;
    }

    @MainThread
    public void run(Runnable runnable) {
        try {
            mThreadFactory.newThread(runnable).start();
        } catch(Throwable ex) {
            Log.i(TAG, "Caught exception: " + ex.toString());
        }
    }
}
