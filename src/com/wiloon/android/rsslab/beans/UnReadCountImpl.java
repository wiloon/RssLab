package com.wiloon.android.rsslab.beans;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 3:21 PM
 */
public class UnReadCountImpl implements GoogleReaderItem {
    //maybe tag id or feed id
    private String id;
    private String count;
    private String newestItemTimestamp;

    public UnReadCountImpl(String id, String count, String newestItemTimestamp) {
        this.id = id;
        this.count = count;
        this.newestItemTimestamp = newestItemTimestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getNewestItemTimestamp() {
        return newestItemTimestamp;
    }

    public void setNewestItemTimestamp(String newestItemTimestamp) {
        this.newestItemTimestamp = newestItemTimestamp;
    }
}
