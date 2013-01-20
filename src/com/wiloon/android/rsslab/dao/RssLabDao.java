package com.wiloon.android.rsslab.dao;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.wiloon.android.rsslab.R;
import com.wiloon.android.rsslab.beans.*;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/1/12
 * Time: 5:27 PM
 */

public class RssLabDao {
    DatabaseHelper dbHelper;
    private static SQLiteDatabase db;
    Activity activity;
    private static RssLabDao instance;

    public RssLabDao(Activity act) {
        this.activity = act;
        String dbName = activity.getString(R.string.dbName);
        dbHelper = DatabaseHelper.getInstance(act, dbName, null, 1);
        db = dbHelper.getWritableDatabase();
    }

    private RssLabDao(SQLiteDatabase db) {
        this.db = db;
    }

    public RssLabDao(DatabaseHelper dbHelper) {
        db = dbHelper.getWritableDatabase();
    }

    public static RssLabDao getInstance() {
        if (instance == null) {
            db = DatabaseHelper.getInstance().getWritableDatabase();
            instance = new RssLabDao(db);
        }
        return instance;
    }

    public List<Tag> getTags() {
        RssLabLog.debug("dao.getTags");
        List<Tag> list = new ArrayList<Tag>();

        try {
            Cursor result = db.rawQuery("SELECT id, tagSortId, label, unReadCount, newestItemTimestamp FROM tag", null);
            result.moveToFirst();
            while (!result.isAfterLast()) {
                String id = result.getString(0);
                String tagSortId = result.getString(1);
                String label = result.getString(2);
                String unReadCount = result.getString(3);
                String newestItemTimestamp = result.getString(4);
                list.add(BeanFactory.createTagWithAllFields(id, tagSortId, label, unReadCount, newestItemTimestamp));

                result.moveToNext();
            }
            result.close();
        } catch (Exception e) {
            Log.e("error: ", e.toString());
        }
        RssLabLog.debug("dao.getTags.return", list.size());
        return list;
    }

    public List<Tag> getTagsForView() {
        RssLabLog.debug("dao.getTagsForView, count>0");
        List<Tag> list = new ArrayList<Tag>();
        //   list.add("All Items");

        String tableName = AppConstant.TABLE_NAME_TAG;
        Cursor result = db.query(tableName, new String[]{"id, label, unReadCount"}, "unReadCount>0", null, null, null, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            String id = result.getString(0);
            String label = result.getString(1);
            String unReadCount = result.getString(2);

            list.add(BeanFactory.createTagForView(id, label, unReadCount));

            result.moveToNext();
        }
        result.close();
        RssLabLog.debug("dao.getTagsForView.return", list.size());
        return list;
    }

    public void insertTags(List<Tag> list) {
        RssLabLog.debug("dao.insertTags: ", "start");
        for (Tag tag : list) {
            ContentValues cv = new ContentValues();
            cv.put("id", tag.getId());
            cv.put("tagSortId", tag.getTagSortId());
            cv.put("label", tag.getLabel());
            db.insert(AppConstant.TABLE_NAME_TAG, null, cv);
            RssLabLog.debug("dao.insertTags: ", cv.toString());
        }
    }

    public void removeTags(List<Tag> list) {
        RssLabLog.debug("dao.removeTags: ", "" + list.size());

        for (Tag tag : list) {
            db.delete(AppConstant.TABLE_NAME_TAG, "id=?", new String[]{tag.getId()});
            RssLabLog.debug("dao.removeTags: ", tag.getId());
        }
    }

    public List<Feed> getFeeds() {
        RssLabLog.debug("dao.getFeeds: ", "start");
        List<Feed> list = new ArrayList<Feed>();
        String tableName = AppConstant.TABLE_NAME_FEED;
        Cursor result = db.query(tableName, new String[]{"id", "title", "sortId", "firstItemMSec, unReadCount, newestItemTimestamp"}, null, null, null, null, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            String id = result.getString(0);
            String title = result.getString(1);
            String sortId = result.getString(2);
            String firstItemMSec = result.getString(3);
            String unReadCount = result.getString(4);
            String newestItemTimestamp = result.getString(5);
            ArrayList<Tag> tagList = getTagsByFeedId(id);
            list.add(new Feed(id, title, tagList, sortId, firstItemMSec, unReadCount, newestItemTimestamp));
            result.moveToNext();
        }
        result.close();
        RssLabLog.debug("dao.getFeeds: ", "end");

        return list;
    }

    public void insertFeeds(List<Feed> feedList) {
        RssLabLog.debug("dao.insertFeeds: ", "start");

        for (Feed feed : feedList) {
            ContentValues cv = new ContentValues();
            cv.put("id", feed.getId());
            cv.put("title", feed.getTitle());
            cv.put("sortId", feed.getSortId());
            cv.put("firstItemMSec", feed.getFirstItemMSec());
            db.insert(AppConstant.TABLE_NAME_FEED, null, cv);
            RssLabLog.debug("dao.insertTags: ", cv.toString());
        }
        RssLabLog.debug("dao.insertFeeds: ", "start");

    }

    public void removeFeeds(List<Feed> feedList) {
        RssLabLog.debug("dao.removeFeeds: ", "start");

        String tableName = AppConstant.TABLE_NAME_FEED;
        for (Feed feed : feedList) {
            db.delete(tableName, "id=?", new String[]{feed.getTitle()});
        }
        RssLabLog.debug("dao.removeFeeds: ", "end");

    }

    public void insertFeedTags(String feedId, List<Tag> tags) {
        RssLabLog.debug("dao.insertFeedTags: ", "start");

        for (Tag tag : tags) {
            ContentValues cv = new ContentValues();
            cv.put("feedId", feedId);
            cv.put("tagId", tag.getId());
            db.insert(AppConstant.TABLE_NAME_FEED_TAGS, null, cv);
        }
        RssLabLog.debug("dao.insertFeedTags: ", "end");

    }


    public ArrayList<Tag> getTagsByFeedId(String feedId) {
        ArrayList<Tag> list = new ArrayList<Tag>();
        String tableName = AppConstant.TABLE_NAME_FEED_TAGS;
        Cursor result;
        //result = db.query(tableName, new String[]{"tagId"}, "feedId='" + feedId + "'", null, null, null, null);
        result = db.query(tableName, new String[]{"tagId"}, "feedId=?", new String[]{feedId}, null, null, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            String tagId = result.getString(0);
            list.add(new Tag(tagId));
            result.moveToNext();
        }
        result.close();
        return list;
    }

    public void removeFeedTag(String feedId, String tagId) {
        RssLabLog.debug("dao.removeFeedTag: ", "start");

        String tableName = AppConstant.TABLE_NAME_FEED_TAGS;

        db.delete(tableName, "feedId=? and tagId=?", new String[]{feedId, tagId});
        RssLabLog.debug("dao.removeFeedTag: ", "end");

    }

    public List<UnReadCountImpl> getUnReadCount() {
        RssLabLog.debug("dao.getUnReadCount");

        List<UnReadCountImpl> list = new ArrayList<UnReadCountImpl>();

        List<Tag> tagList = getTags();
        for (Tag tag : tagList) {
            list.add(new UnReadCountImpl(tag.getId(), tag.getUnReadCount(), tag.getNewestItemTimestamp()));
        }
        List<Feed> feedList = getFeeds();
        for (Feed feed : feedList) {
            list.add(new UnReadCountImpl(feed.getId(), feed.getUnReadCount(), feed.getNewestItemTimestamp()));
        }
        RssLabLog.debug("dao.getUnReadCount.return", "" + list.size());
        return list;
    }

    public void updateTagCount(UnReadCountImpl unReadCountImpl) {
        ContentValues cv = new ContentValues();
        cv.put(AppConstant.TABLE_COLUMN_UNREAD_COUNT, unReadCountImpl.getCount());
        cv.put(AppConstant.TABLE_COLUMN_NEWEST_ITEM_TIMESTAMP, unReadCountImpl.getNewestItemTimestamp());
        db.update(AppConstant.TABLE_NAME_TAG, cv, AppConstant.TABLE_COLUMN_ID + "=?", new String[]{unReadCountImpl.getId()});
        RssLabLog.debug("dao.updateTagCount.update: ", unReadCountImpl.getId() + "|" + unReadCountImpl.getCount());

    }

    public void updateFeedCount(UnReadCountImpl unReadCountImpl) {
        ContentValues cv = new ContentValues();
        cv.put(AppConstant.TABLE_COLUMN_UNREAD_COUNT, unReadCountImpl.getCount());
        cv.put(AppConstant.TABLE_COLUMN_NEWEST_ITEM_TIMESTAMP, unReadCountImpl.getNewestItemTimestamp());
        db.update(AppConstant.TABLE_NAME_FEED, cv, AppConstant.TABLE_COLUMN_ID + "=?", new String[]{unReadCountImpl.getId()});
        RssLabLog.debug("dao.updateFeedCount: ", unReadCountImpl.getId(), "count:", unReadCountImpl.getCount());

    }

    public List<Feed> getFeedsForView(String tagId) {
        RssLabLog.debug("dao.getFeedsForView: ", "start");
        List<Feed> list = new ArrayList<Feed>();

        Cursor result = db.rawQuery("select f.title,f.unReadCount, f.id from feed f join feedCategories fc on f.id = fc.feedId join tag t on fc.tagId=t.id where t.id=? and f.unReadCount>0", new String[]{tagId});
        //  result = db.rawQuery("SELECT id, tagSortId, label, unReadCount, newestItemTimestamp FROM tag", null);

        result.moveToFirst();
        while (!result.isAfterLast()) {

            String title = result.getString(0);
            String unReadCount = result.getString(1);
            String id = result.getString(2);
            list.add(BeanFactory.createFeedForView(id, title, unReadCount));
            result.moveToNext();
        }
        result.close();
        RssLabLog.debug("dao.getFeedsForView.return: ", "" + list.size());
        return list;
    }

    public String[] getFeedsByTabId(String tagId) {
        String[] rtn;
        int index = 0;
        Cursor result = db.rawQuery("select f.id from feed f join " + AppConstant.TABLE_NAME_FEED_TAGS + " fc on f.id = fc.feedId join tag t on fc.tagId=t.id where t.id=? and f.unReadCount>0", new String[]{tagId});
        //  result = db.rawQuery("SELECT id, tagSortId, label, unReadCount, newestItemTimestamp FROM tag", null);
        int resultCount = result.getCount();
        if (resultCount > 0) {
            rtn = new String[resultCount];
            result.moveToFirst();
            while (!result.isAfterLast()) {
                String id = result.getString(0);
                rtn[index++] = id;
                result.moveToNext();
            }
            result.close();
        } else {
            rtn = new String[0];
        }
        RssLabLog.debug("dao.getFeedsByTabId.return.size: ", "" + rtn.length);

        return rtn;
    }

    public List<Article> getArticles() {
//        RssLabLog.debug("dao.getArticles: ", "start");
//        List<Article> list = new ArrayList<Article>();
//        String tableName = AppConstant.TABLE_NAME_ARTICLE;
//        Cursor result = db.query(tableName, new String[]{"id", "title", "sortId", "firstItemMSec, unReadCount, newestItemTimestamp"}, null, null, null, null, null);
//        result.moveToFirst();
//        while (!result.isAfterLast()) {
//            String id = result.getString(0);
//            String title = result.getString(1);
//            String sortId = result.getString(2);
//            String firstItemMSec = result.getString(3);
//            String unReadCount = result.getString(4);
//            String newestItemTimestamp = result.getString(5);
//            ArrayList<Tag> tagList = getTagsByFeedId(id);
//            list.add(new Article((String href, String feedId, String title, String content) {
//            result.moveToNext();
//        }
//        result.close();
//        RssLabLog.debug("dao.getArticles: ", "end");
        return null;
    }

    public List<Article> getUnReadArticleIds() {
        RssLabLog.debug("dao.getUnReadArticleIds");

        List<Article> list = new ArrayList<Article>();
        String tableName = AppConstant.TABLE_NAME_ARTICLE;
        Cursor result = db.query(tableName, new String[]{"id", "timestampUsec"}, AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=?", new String[]{AppConstant.ARTICLE_STATUS_UNREAD}, null, null, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            String id = result.getString(0);
            String timestampUsec = result.getString(1);
            list.add(new Article(id, timestampUsec));
            result.moveToNext();
        }
        result.close();
        RssLabLog.debug("dao.getUnReadArticleIds", "" + list.size());
        return list;
    }

    public void insertArticles(List<Article> list) {
        RssLabLog.debug("dao.insert articles into DB table.list size", list.size());
        for (Article article : list) {
            ContentValues cv = new ContentValues();
            cv.put("id", article.getId());
            cv.put("timestampUsec", article.getTimestampUsec());
            cv.put("articleId", article.getArticleId());
            cv.put("href", article.getHref());
            cv.put("feedId", article.getFeedId());
            cv.put("title", article.getTitle());
            cv.put("content", article.getContent());
            cv.put(AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS, AppConstant.ARTICLE_STATUS_UNREAD);
            cv.put("onlineStatus", AppConstant.ARTICLE_ONLINE_STATUS_ONLINE);
            cv.put("imgSyncStatus", AppConstant.ARTICLE_IMAGE_STATUS_NEW);

            db.insert(AppConstant.TABLE_NAME_ARTICLE, null, cv);
        }
        RssLabLog.debug("dao.insert article into DB ", "done.");

    }

    public void removeArticles(List<Article> articleList) {
        RssLabLog.debug("dao.removeArticles: ", "" + articleList.size());

        for (Article article : articleList) {
            if (article != null) {
                String id = article.getId();
                if (id == null) {
                    RssLabLog.debug("dao.removeArticles", "article id is null");
                    continue;
                }
                db.delete(AppConstant.TABLE_NAME_ARTICLE, "id=?", new String[]{article.getId()});
                RssLabLog.debug("dao.removeArticles: ", article.getId(), article.getTitle());
            } else {
                RssLabLog.debug("dao.removeArticles", "article is null");
            }

        }
    }

    public List<Article> getArticlesForView(String feedId) {
        RssLabLog.debug("dao.getArticlesForView", "FeedId", feedId);
        List<Article> list = new ArrayList<Article>();

        Cursor result = db.query(AppConstant.TABLE_NAME_ARTICLE, new String[]{"articleId, title"}, "feedId=? and readStatus=?", new String[]{feedId, AppConstant.ARTICLE_STATUS_UNREAD}, null, null, null);

        result.moveToFirst();
        while (!result.isAfterLast()) {
            String articleId = result.getString(0);

            String title = result.getString(1);


            list.add(BeanFactory.createArticleForListView(articleId, title, feedId));
            result.moveToNext();
        }
        result.close();
        RssLabLog.debug("dao.getArticlesForView.return", "" + list.size());

        return list;

    }

    public ArticleForView getArticleById(String articleId) {
        RssLabLog.debug("dao.getArticleById", articleId);
        ArticleForView article = null;
        // Cursor result = db.query(AppConstant.TABLE_NAME_ARTICLE, new String[]{"title, content"}, "articleId=?", new String[]{articleId}, null, null, null);
        Cursor result = db.rawQuery("SELECT a.title, a.content, f.title, a.href FROM " + AppConstant.TABLE_NAME_ARTICLE + " a, " + AppConstant.TABLE_NAME_FEED + " f where a.feedId=f.id and a.articleId=? ", new String[]{articleId});

        result.moveToFirst();
        int resultCount = result.getCount();
        //RssLabLog.debug("dao.getArticleById.result.count", resultCount);
        if (resultCount > 0) {
            String title = result.getString(0);
            String content = result.getString(1);
            String feedLabel = result.getString(2);
            String href = result.getString(3);
            article = BeanFactory.createArticleForWebView(articleId, title, content, feedLabel, href);
            result.close();
            RssLabLog.debug("dao.getArticleForView: ", "Done");
        } else {
            RssLabLog.error("dao.getArticleById.error", "result count is 0.");
        }

        return article;
    }

    public List<String> getUnSyncImageArticleIds() {
        List<String> list = new ArrayList<String>();
        Cursor result = db.query(AppConstant.TABLE_NAME_ARTICLE, new String[]{"articleId"}, "imgSyncStatus=?", new String[]{AppConstant.ARTICLE_IMAGE_STATUS_NEW}, null, null, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {

            String articleId = result.getString(0);

            list.add(articleId);
            result.moveToNext();
        }
        result.close();
        return list;
    }

    public void updateArticleContent(String articleId, String content) {
        ContentValues cv = new ContentValues();
        cv.put("content", content);
        db.update(AppConstant.TABLE_NAME_ARTICLE, cv, "articleId=?", new String[]{articleId});
    }

    public void insertArticleImageMapping(List<ArticleImage> list) {
        RssLabLog.debug("dao.insert article image mapping into DB.list size", list.size());
        for (ArticleImage articleImage : list) {
            ContentValues cv = new ContentValues();
            cv.put("articleId", articleImage.getArticleId());
            cv.put("imgFilePath", articleImage.getImgFilePath());
            db.insert(AppConstant.TABLE_NAME_ARTICLE_IMAGE, null, cv);
        }
        RssLabLog.debug("dao.insert article image mapping into DB ", "done.");

    }

    public void updateArticleStatusToRead(String articleId) {
        RssLabLog.debug("dao.updateArticleStatusToRead", articleId);
        ContentValues cv = new ContentValues();
        cv.put(AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS, AppConstant.ARTICLE_STATUS_READ);
        db.update(AppConstant.TABLE_NAME_ARTICLE, cv, "articleId=?", new String[]{articleId});
        RssLabLog.debug("dao.updateArticleStatusToRead", "Done");
    }


    public List<Article> getReadArticle() {
        RssLabLog.debug("dao.getReadArticle");

        List<Article> articleList = new ArrayList<Article>();
        Cursor result = db.query(AppConstant.TABLE_NAME_ARTICLE, new String[]{"articleId", "feedId", "title"}, AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=?", new String[]{AppConstant.ARTICLE_STATUS_READ}, null, null, null);
        result.moveToFirst();
        while (!result.isAfterLast()) {
            String articleId = result.getString(0);
            String feedId = result.getString(1);
            String title = result.getString(2);
            articleList.add(BeanFactory.createArticleForDelete(articleId, feedId, title));
            result.moveToNext();
        }
        result.close();
        RssLabLog.debug("dao.getReadArticle.return", "" + articleList.size());

        return articleList;
    }

    public List<ArticleImage> getReadArticleImage() {

        List<ArticleImage> articleImageList = new ArrayList<ArticleImage>();

        // Cursor articleImageResult = db.query(AppConstant.TABLE_NAME_ARTICLE_IMAGE, new String[]{"imgFileName", "id"}, "articleId=?", new String[]{article.getArticleId()}, null, null, null);
        Cursor articleImageResult = db.rawQuery("select ai.id,ai.imgFilePath from articleImage ai, article a where ai.articleId = a.articleId and a.readStatus=?", new String[]{AppConstant.ARTICLE_STATUS_READ});

        articleImageResult.moveToFirst();
        while (!articleImageResult.isAfterLast()) {
            String id = articleImageResult.getString(0);
            String fileName = articleImageResult.getString(1);

            articleImageList.add(new ArticleImage(id, null, fileName));

            articleImageResult.moveToNext();
        }
        articleImageResult.close();

        RssLabLog.debug("dao.getReadArticleImage.return", "" + articleImageList.size());
        return articleImageList;
    }

    public void deleteArticleImageMapping(List<ArticleImage> articleImageList) {
        RssLabLog.debug("dao.deleteArticleImageMapping", articleImageList.size());
        for (ArticleImage articleImage : articleImageList) {
            RssLabLog.debug("dao.deleteArticleImageMapping", articleImage.getArticleId());
            db.delete(AppConstant.TABLE_NAME_ARTICLE_IMAGE, "id=?", new String[]{articleImage.getId()});
        }
        RssLabLog.debug("dao.deleteArticleImageMapping", "Done");

    }

    public void deleteArticle(List<Article> articleList) {
        RssLabLog.debug("dao.deleteArticle", "" + articleList.size());
        for (Article article : articleList) {
            RssLabLog.debug("dao.deleteArticle", article.getId());
            db.delete(AppConstant.TABLE_NAME_ARTICLE, "articleId=?", new String[]{article.getArticleId()});
        }
        RssLabLog.debug("dao.deleteArticle", "Done");
    }

    public ArticleForView getOneArticleByFeedId(String feedId) {
        ArticleForView article = null;
        Cursor result = db.rawQuery("SELECT a.articleId FROM " + AppConstant.TABLE_NAME_ARTICLE + " a, " + AppConstant.TABLE_NAME_FEED + " f where a.feedId=f.id and feedId=? and " + AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? limit 1 ", new String[]{feedId, AppConstant.ARTICLE_STATUS_UNREAD});
        result.moveToFirst();
        if (result.getCount() > 0) {
            String articleId = result.getString(0);
            article = getArticleById(articleId);

        }
        return article;
    }

    public int getArticleNumberByFeedId(String feedId) {
        Cursor result = db.rawQuery("SELECT count(*) as num FROM " + AppConstant.TABLE_NAME_ARTICLE + " where feedId=?", new String[]{feedId});
        result.moveToFirst();
        String num = null;
        if (result.getCount() > 0) {
            num = result.getString(0);
        }
        return Integer.parseInt(num);

    }

    public void markArticleImgStatusAsSync(String articleId) {
        ContentValues cv = new ContentValues();
        cv.put("imgSyncStatus", AppConstant.ARTICLE_IMAGE_STATUS_SYNC);
        db.update(AppConstant.TABLE_NAME_ARTICLE, cv, "articleId=?", new String[]{articleId});
        RssLabLog.debug("dao.markArticleImgStatusAsSync", articleId);


    }

    public List<Article> getArticlesUnderTag(String tagId) {
        List<Article> list = new ArrayList<Article>();
        Cursor result = db.rawQuery("SELECT a.articleId, a.title FROM " + AppConstant.TABLE_NAME_FEED_TAGS + " ft, " + AppConstant.TABLE_NAME_ARTICLE + " a where ft.feedId=a.feedId and ft.tagId=? and " + AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? ", new String[]{tagId, AppConstant.ARTICLE_STATUS_UNREAD});
        result.moveToFirst();
        while (!result.isAfterLast()) {
            String articleId = result.getString(0);

            String title = result.getString(1);

            list.add(BeanFactory.createArticleForListView(articleId, title, tagId));
            result.moveToNext();
        }
        result.close();
        return list;
    }

//    public Article getOneArticleByTagId(String tagId) {
//
//        Article article = null;
//        Cursor result = db.rawQuery("SELECT a.articleId, a.title, a.content, f.title FROM " + AppConstant.TABLE_NAME_FEED_TAGS + " ft, " + AppConstant.TABLE_NAME_ARTICLE + " a, " + AppConstant.TABLE_NAME_FEED + " f where ft.feedId=a.feedId and ft.feedId=f.id and ft.tagId=? and " + AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? limit 1", new String[]{tagId, AppConstant.ARTICLE_STATUS_UNREAD});
//        result.moveToFirst();
//        if (result.getCount() > 0) {
//            String articleId = result.getString(0);
//            String title = result.getString(1);
//            String content = result.getString(2);
//            String feedLabel = result.getString(3);
//            article = BeanFactory.createArticleForWebView(articleId, title, content, feedLabel);
//        }
//        result.close();
//        return article;
//
//    }

    public List<String> getArticleIdsByTagId(String tagId) {
        List<String> list = new ArrayList<String>();
        Cursor result = db.rawQuery("SELECT a.articleId FROM " + AppConstant.TABLE_NAME_FEED_TAGS + " ft, " + AppConstant.TABLE_NAME_ARTICLE + " a where ft.feedId=a.feedId and ft.tagId=? and " + AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? ", new String[]{tagId, AppConstant.ARTICLE_STATUS_UNREAD});
        result.moveToFirst();
        while (!result.isAfterLast()) {

            String articleId = result.getString(0);
            list.add(articleId);
            result.moveToNext();
        }
        result.close();
        return list;

    }

    public List<String> getAllArticleIds() {
        List<String> list = new ArrayList<String>();
        Cursor result = db.rawQuery("SELECT a.articleId FROM " + AppConstant.TABLE_NAME_ARTICLE + " a where  " + AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? ", new String[]{AppConstant.ARTICLE_STATUS_UNREAD});
        result.moveToFirst();
        while (!result.isAfterLast()) {

            String articleId = result.getString(0);
            list.add(articleId);
            result.moveToNext();
        }
        result.close();
        return list;

    }

    public ArticleForView getOneArticleByTagIdRandomly(String tagId) {
        //httpsGet article ids by tagId
        List<String> list = getArticleIdsByTagId(tagId);
        //httpsGet result count
        int count = list.size();
        //generate random number
        Random random = new Random();
        int index = random.nextInt(count);
        //httpsGet article id by the random number
        String atricleId = list.get(index);
        //httpsGet the article object by the article id
        ArticleForView article = getArticleById(atricleId);
        return article;
    }

    public ArticleForView getOneArticleRandomly() {
        //httpsGet article ids by tagId
        List<String> list = getAllArticleIds();
        //httpsGet result count
        int count = list.size();
        //generate random number
        Random random = new Random();
        int index = random.nextInt(count);
        //httpsGet article id by the random number
        String atricleId = list.get(index);
        //httpsGet the article object by the article id
        ArticleForView article = getArticleById(atricleId);
        return article;
    }

    public List<Article> getArticleURLsByFeedIds(String[] feedIds) {
        String ids = "''";
        for (String str : feedIds) {
            ids += ",'" + str + "'";
        }

        List<Article> list = new ArrayList<Article>();
        Cursor result = db.rawQuery(
                "SELECT a.articleId , a.href , a.title FROM " + AppConstant.TABLE_NAME_ARTICLE + " a  " +
                        "where a.feedId in (" + ids + ")  and " + AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? " +
                        "and " + AppConstant.TABLE_COLUM_ARTICLE_ONLINE_STATUS + "=?",
                new String[]{AppConstant.ARTICLE_STATUS_UNREAD, AppConstant.ARTICLE_ONLINE_STATUS_ONLINE});
        result.moveToFirst();
        while (!result.isAfterLast()) {

            String articleId = result.getString(0);
            String href = result.getString(1);
            String title = result.getString(2);
            list.add(BeanFactory.newOnlineArticle(articleId, href, title));
            result.moveToNext();
        }
        result.close();
        return list;

    }

    public void updateArticleAsOffline(Article article) {
        RssLabLog.debug("mark article as offline", article.getTitle());
        ContentValues cv = new ContentValues();
        cv.put(AppConstant.TABLE_COLUM_ARTICLE_ONLINE_STATUS, AppConstant.ARTICLE_ONLINE_STATUS_OFFLINE);
        db.update(AppConstant.TABLE_NAME_ARTICLE, cv, "articleId=?", new String[]{article.getArticleId()});

    }

    public List<Article> getAllArticles(String tagId) {
        List<Article> list = new ArrayList<Article>();
        Cursor result = db.rawQuery(
                "SELECT a.articleId, a.title FROM " + AppConstant.TABLE_NAME_ARTICLE + " a where " +
                AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? ", new String[]{AppConstant.ARTICLE_STATUS_UNREAD});
        result.moveToFirst();
        while (!result.isAfterLast()) {
            String articleId = result.getString(0);

            String title = result.getString(1);

            list.add(BeanFactory.createArticleForListView(articleId, title, tagId));
            result.moveToNext();
        }
        result.close();
        return list;

    }

    public int getarticleNumberByTagId(String tagId) {

        Cursor result = db.rawQuery("SELECT count(*) as num FROM " + AppConstant.TABLE_NAME_FEED_TAGS + " ft, " + AppConstant.TABLE_NAME_ARTICLE + " a where ft.feedId=a.feedId and ft.tagId=? and " + AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? ", new String[]{tagId, AppConstant.ARTICLE_STATUS_UNREAD});

        result.moveToFirst();
        String num = null;
        if (result.getCount() > 0) {
            num = result.getString(0);
        }
        return Integer.parseInt(num);
    }

    public int getarticleNum() {

        Cursor result = db.rawQuery("SELECT count(*) as num FROM " + AppConstant.TABLE_NAME_ARTICLE + " a where  " + AppConstant.TABLE_COLUM_ARTICLE_READ_STATUS + "=? ", new String[]{AppConstant.ARTICLE_STATUS_UNREAD});

        result.moveToFirst();
        String num = null;
        if (result.getCount() > 0) {
            num = result.getString(0);
        }
        return Integer.parseInt(num);

    }
}

