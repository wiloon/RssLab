package com.wiloon.android.rsslab.beans;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/15/12
 * Time: 11:07 PM
 */
public class Article implements GoogleReaderItem {
    private String id;
    private String timestampUsec;
    private String articleId;
    private String feedId;
    private String title;
    private String content;
    private String href;
    private String readStatus;
    private String imgSyncStatus;

    public Article() {

    }

    public Article(String id, String timestampUsec, String articleId, String href, String feedId, String title, String content) {
        this.id = id;
        this.timestampUsec = timestampUsec;
        this.feedId = feedId;
        this.title = title;
        this.content = content;
        this.href = href;
        this.articleId = articleId;
    }

    public Article(String id, String timestampUsec) {
        this.id = id;
        this.timestampUsec = timestampUsec;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }


    public void setContent(String content) {
        this.content = content;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }


    public String getTimestampUsec() {
        return timestampUsec;
    }

    public void setTimestampUsec(String timestampUsec) {
        this.timestampUsec = timestampUsec;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }

    public String getImgSyncStatus() {
        return imgSyncStatus;
    }

    public void setImgSyncStatus(String imgSyncStatus) {
        this.imgSyncStatus = imgSyncStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
