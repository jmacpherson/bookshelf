package ca.book.shelf.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;


/**
 * Abstraction so that I can test Repository and MainViewModel
 * without having to deal with the complexity of mocking multiple
 * types of MutableLiveData
 */
public class ToastUtils {

    private static ToastUtils sInstance;

    private MutableLiveData<String> message = new MutableLiveData<>();

    public static ToastUtils getInstance() {
        if(sInstance == null) {
            sInstance = new ToastUtils();
        }

        return sInstance;
    }

    private ToastUtils() {}

    public void post(String string) {
        message.postValue(string);
    }

    public void observe(final Context context) {
        message.observe((LifecycleOwner) context, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if(!TextUtils.isEmpty(s)) {
                    Toast toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
}
