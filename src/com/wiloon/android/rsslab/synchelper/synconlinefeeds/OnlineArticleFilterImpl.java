package com.wiloon.android.rsslab.synchelper.synconlinefeeds;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 9/2/12
 * Time: 12:56 PM
 */
public class OnlineArticleFilterImpl implements OnlineArticleFilter {
    protected Pattern pattern;

    public OnlineArticleFilterImpl(String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    public String getArticleContent(String html) {
        String content  ="";
        Matcher matcher = pattern.matcher(html);
        boolean find = matcher.find();
        if (find) {
            content = matcher.group(1);
        }

        return content;
    }

}
