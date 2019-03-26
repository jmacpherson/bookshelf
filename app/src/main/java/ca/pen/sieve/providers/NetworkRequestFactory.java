package ca.pen.sieve.providers;

import android.net.Network;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.inject.Inject;

import androidx.annotation.MainThread;

public class NetworkRequestFactory {

    public static final String TAG = "NetworkRequestFactory";
    private static NetworkRequestFactory sInstance;

    private ThreadFactory mThreadFactory;

    public static NetworkRequestFactory getInstance() {
        if(sInstance == null) {
            sInstance = new NetworkRequestFactory(Executors.defaultThreadFactory());
        }
        return sInstance;
    }

    public NetworkRequestFactory(ThreadFactory threadFactory) {
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
