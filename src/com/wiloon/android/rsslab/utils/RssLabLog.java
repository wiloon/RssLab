package com.wiloon.android.rsslab.utils;

import android.util.Log;
import com.wiloon.android.rsslab.common.AppConstant;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/21/12
 * Time: 11:06 PM
 */
public class RssLabLog {
    public static void info(String tag, String... msg) {
        String message = parseMsgArray(msg);
        Log.i(AppConstant.LOG_PREFIX_INFO + "　" + tag, message);
    }

    public static void info(String tag, boolean bln) {
        Log.i(AppConstant.LOG_PREFIX_INFO + "　" + tag, String.valueOf(bln));
    }

    public static void info(String tag, int i) {
        Log.i(AppConstant.LOG_PREFIX_INFO + "　" + tag, String.valueOf(i));
    }

    public static void debug(String tag, String... msg) {
        String message = parseMsgArray(msg);
        Log.d(AppConstant.LOG_PREFIX_DEBUG + "　" + tag, message);
    }

    public static void debug(String functionName, String msg, int i) {

        Log.d(AppConstant.LOG_PREFIX_DEBUG + "　" + functionName, msg + " " + i);
    }

    public static void debug(String tag, boolean bln) {
        Log.d(AppConstant.LOG_PREFIX_DEBUG + "　" + tag, String.valueOf(bln));
    }

    public static void debug(String tag, boolean bln, String... msg) {
        String message = parseMsgArray(msg);
        Log.d(AppConstant.LOG_PREFIX_DEBUG + "　" + tag, String.valueOf(bln) + " " + message);
    }

    public static void debug(String tag, int i) {
        Log.d(AppConstant.LOG_PREFIX_DEBUG + "　" + tag, String.valueOf(i));
    }

    public static void error(String tag, String... msg) {
        String message = parseMsgArray(msg);
        Log.e(tag, message);
    }

    private static String parseMsgArray(String[] msg) {
        String separator = AppConstant.LOG_SEPARATOR;
        String message = separator;
        if (msg != null && msg.length > 0) {
            StringBuffer sb = new StringBuffer();
            for (String s : msg) {
                if (s != null) {
                    sb.append(separator + s);
                } else {
                    sb.append(separator);
                }
            }
            message = sb.toString();
        } else {
            message = separator;
        }
        return message;
    }
}
