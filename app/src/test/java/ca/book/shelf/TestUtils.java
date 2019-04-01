package ca.book.shelf;

import android.text.TextUtils;
import android.util.Log;

import mockit.Mock;
import mockit.MockUp;

public class TestUtils {
    public static void mockLogging() {
        new MockUp<Log>() {
            @Mock
            public int i(String tag, String message) {
                return 1;
            }

            @Mock
            public int e(String tag, String message) {
                return 1;
            }
        };
    }

    public static void mockTextUtils() {
        new MockUp<TextUtils>() {
            @Mock
            public boolean isEmpty(CharSequence str) {
                return str == null || str.equals("");
            }
        };
    }
}
