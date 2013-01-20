package com.wiloon.android.rsslab.beans;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/21/12
 * Time: 8:44 PM
 */
public class ArticleImage {
    private String id;
    private String articleId;
    private String imgFilePath;

    public ArticleImage() {

    }

    public ArticleImage(String id, String articleId, String imgFilePath) {
        this.id = id;
        this.articleId = articleId;
        this.imgFilePath = imgFilePath;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getImgFilePath() {
        return imgFilePath;
    }

    public void setImgFilePath(String imgFilePath) {
        this.imgFilePath = imgFilePath;
    }


}
