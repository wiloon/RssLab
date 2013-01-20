package com.wiloon.android.rsslab.beans;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 10:50 AM
 */
public class BeanFactory {
//    public static Tag createTagWithUnReadCount(String id, String unReadCount, String newestItemTimestamp) {
//        return new Tag(id, null, null, unReadCount, newestItemTimestamp);
//    }

    public static Tag createTagWithAllFields(String id, String tagSortId, String label, String unReadCount, String newestItemTimestamp) {
        return new Tag(id, tagSortId, label, unReadCount, newestItemTimestamp);
    }

    public static Tag createTagForView(String id, String label, String unReadCount) {
        return new Tag(id, null, label, unReadCount, null);
    }

    public static Feed createFeedForView(String id, String title, String unReadCount) {
        return new Feed(id, title, null, null, null, unReadCount, null);
    }

    public static Feed newFeedForId(String id) {
        return new Feed(id, null, null, null, null, null, null);
    }

    public static Article createArticleForListView(String articleId, String title, String feedId) {
        return new Article(null, null, articleId, null, feedId, title, null);
    }

    public static ArticleForView createArticleForWebView(String articleId, String title, String content, String feedLabel, String href) {
        ArticleForView articleForView = new ArticleForView();
        articleForView.setArticleId(articleId);
        articleForView.setTitle(title);
        articleForView.setContent(content);
        articleForView.setFeedLabel(feedLabel);
        articleForView.setHref(href);
        return articleForView;
    }

    public static Article createArticleForDelete(String articleId, String feedId, String title) {
        return new Article(null, null, articleId, null, feedId, title, null);
    }

    public static ArticleImage newUnSyncImageArticle(String articleId, String filePath) {
        ArticleImage articleImage = new ArticleImage();
        articleImage.setArticleId(articleId);
        articleImage.setImgFilePath(filePath);
        return articleImage;
    }

    //article
    public static Article newOnlineArticle(String articleId, String href, String title) {
        Article article = new Article();
        article.setArticleId(articleId);
        article.setHref(href);
        article.setTitle(title);
        return article;
    }

}
