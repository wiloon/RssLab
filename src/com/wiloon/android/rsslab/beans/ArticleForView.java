package com.wiloon.android.rsslab.beans;

import com.wiloon.android.rsslab.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 9/1/12
 * Time: 10:20 PM
 */
public class ArticleForView extends Article {
    private String feedLabel;

    public String getFeedLabel() {
        return feedLabel;
    }

    public void setFeedLabel(String feedLabel) {
        this.feedLabel = feedLabel;
    }

    public String getContentForView() {
        String rtn = getLinkedTitle(super.getTitle()) + "<br>" + this.getFeedLabel() + "<br><br>" + super.getContent();
        rtn = Utils.filterAD(rtn);
        return rtn;
    }

    private String getLinkedTitle(String title) {
        String rtn;
        String href = super.getHref();
        if (href.isEmpty()) {
            rtn = title;
        } else {
            rtn = "<a href=" + super.getHref() + " >" + title + "</a>";
        }
        return rtn;
    }
}
