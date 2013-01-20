package com.wiloon.android.rsslab.beans;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/9/12
 * Time: 10:27 PM
 */
public class Feed implements GoogleReaderItem, UnReadCount {
    private String id;
    private String title;
    private String categoryId;
    private ArrayList<Tag> categories;
    private String sortId;
    private String firstItemMSec;
    private String unReadCount;
    private String newestItemTimestamp;

    public Feed(String id, String title, ArrayList<Tag> categories, String sortId, String firstItemMSec) {
        this.id = id;
        this.title = title;
        this.categories = categories;
        this.sortId = sortId;
        this.firstItemMSec = firstItemMSec;
    }

    public Feed(String id, String unReadCount, String newestItemTimestamp) {
        this.id = id;
        this.unReadCount = unReadCount;
        this.newestItemTimestamp = newestItemTimestamp;
    }

    public Feed(String id, String title, ArrayList<Tag> categories, String sortId, String firstItemMSec, String unReadCount, String newestItemTimestamp) {
        this.id = id;
        this.title = title;
        this.categories = categories;
        this.sortId = sortId;
        this.firstItemMSec = firstItemMSec;
        this.unReadCount = unReadCount;
        this.newestItemTimestamp = newestItemTimestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Tag> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Tag> categories) {
        this.categories = categories;
    }

    public String getSortId() {
        return sortId;
    }

    public void setSortId(String sortId) {
        this.sortId = sortId;
    }

    public String getFirstItemMSec() {
        return firstItemMSec;
    }

    public void setFirstItemMSec(String firstItemMSec) {
        this.firstItemMSec = firstItemMSec;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String getLabel() {
        return getTitle();
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
        return title + "|" + unReadCount;
    }
}
