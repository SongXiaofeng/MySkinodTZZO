package com.skinod.tzzo.skinod.utils;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "sxfff";
    private static boolean debugLogOn = true;

    public LogUtil() {
    }

    public static boolean isDebugLogOn() {
        return debugLogOn;
    }

    public static void setDebugLogOn(boolean debugLogOn) {
        debugLogOn = debugLogOn;
    }

    public static void v(String tag, String msg) {
        if (debugLogOn) {
            Log.v(TAG + tag, msg);
        }
    }

    public static void v(String tag, String msg, Throwable tr) {
        if (debugLogOn) {
            Log.v(TAG + tag, msg, tr);
        }

    }

    public static void d(String msg) {
        if (debugLogOn) {
            Log.d(TAG, msg);
        }

    }

    public static void d(String tag, String msg) {
        if (debugLogOn) {
            Log.d(TAG + tag, msg);
        }

    }

    public static void d(String tag, String msg, Throwable tr) {
        if (debugLogOn) {
            Log.d(TAG + tag, msg, tr);
        }

    }

    public static void d(String tag, String method, String msg) {
        if (debugLogOn) {
            Log.d(TAG + tag, method + ";" + msg);
        }

    }

    public static void i(String tag, String msg) {
        if (debugLogOn) {
            Log.i(TAG + tag, msg);
        }

    }

    public static void i(String tag, String msg, Throwable tr) {
        if (debugLogOn) {
            Log.i(TAG + tag, msg, tr);
        }

    }

    public static void w(String tag, String msg) {
        if (debugLogOn) {
            Log.w(TAG + tag, msg);
        }

    }

    public static void w(String tag, String msg, Throwable tr) {
        if (debugLogOn) {
            Log.w(TAG + tag, msg, tr);
        }

    }

    public static void e(String tag, String msg) {
        if (debugLogOn) {
            Log.e(TAG + tag, msg);
        }

    }

    public static void e(String msg) {
        if (debugLogOn) {
            Log.e(TAG, msg);
        }

    }

    public static void e(String tag, String msg, Throwable tr) {
        if (debugLogOn) {
            Log.e(TAG + tag, msg, tr);
        }

    }

    public static void systemv(String tag, String msg) {
    }

    public static void systemv(String tag, String msg, Throwable tr) {
    }

    public static void systemd(String tag, String msg) {
    }

    public static void systemd(String tag, String msg, Throwable tr) {
    }

    public static void systemd(String tag, String method, String msg) {
    }

    public static void systemi(String tag, String msg) {
    }

    public static void systemi(String tag, String msg, Throwable tr) {
    }

    public static void systemw(String tag, String msg) {
    }

    public static void systemw(String tag, String msg, Throwable tr) {
    }

    public static void systeme(String tag, String msg) {
    }

    public static void systeme(String tag, String msg, Throwable tr) {
    }
}