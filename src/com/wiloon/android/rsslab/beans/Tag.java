package com.wiloon.android.rsslab.beans;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/7/12
 * Time: 5:23 PM
 */
public class Tag implements GoogleReaderItem, UnReadCount {
    private String id;
    private String tagSortId;
    private String label;
    private String unReadCount;
    private String newestItemTimestamp;

    public Tag() {

    }

    public Tag(String id, String tagSortId) {

        this.id = id;
        this.tagSortId = tagSortId;
        this.label = this.splitToLabel(id);

    }

    public Tag(String id, String tagSortId, String label) {

        this.id = id;
        this.tagSortId = tagSortId;
        this.label = label;
    }

    public Tag(String id, String tagSortId, String label, String unReadCount, String newestItemTimestamp) {

        this.id = id;
        this.tagSortId = tagSortId;

        this.unReadCount = unReadCount;
        this.newestItemTimestamp = newestItemTimestamp;
        if (label == null) {
            this.label = this.splitToLabel(id);

        } else {
            this.label = label;
        }
    }

    public Tag(String tagId) {
        this.id = tagId;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;

    }

    public String getTagSortId() {
        return tagSortId;
    }

    public void setTagSortId(String tagSortId) {
        this.tagSortId = tagSortId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    private String splitToLabel(String id) {
        String[] arr = id.split("/");
        return arr[arr.length - 1];
    }

    public String getUnReadCount() {
        return unReadCount;
    }

    public void setUnReadCount(String unReadCount) {
        this.unReadCount = unReadCount;
    }

    public String getNewestItemTimestamp() {
        return newestItemTimestamp;
    }

    public void setNewestItemTimestamp(String newestItemTimestamp) {
        this.newestItemTimestamp = newestItemTimestamp;
    }

    public String toString() {
        return label + "|" + unReadCount;
    }
}
