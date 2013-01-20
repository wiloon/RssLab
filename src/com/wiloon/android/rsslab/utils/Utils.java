package com.wiloon.android.rsslab.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 6/28/12
 * Time: 9:40 PM
 */
public class Utils {

    public static String streamReader(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer();
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return sb.toString();
    }

    public static boolean isTagId(String id) {
        boolean isTagId = false;
        int index = id.indexOf("user");
        if (index == 0) {
            isTagId = true;
        }
        return isTagId;
    }

    public static boolean isFeedId(String id) {
        boolean isFeedId = false;
        int index = id.indexOf("feed");
        if (index == 0) {
            isFeedId = true;
        }
        return isFeedId;
    }


    public static long getTimestamp() {
        return System.currentTimeMillis();
    }

    public static String filterAD(String content) {
        String pattern = "<p><iframe.*</iframe></p>";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(content);
        content = matcher.replaceAll("");
        return content;
    }
}
