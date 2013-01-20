package com.wiloon.android.rsslab.synchelper;

import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/19/12
 * Time: 9:47 PM
 */
public class SyncUnReadArticle extends SyncAddDeleteItemsImpl {
    public SyncUnReadArticle(String token) {
        super(token);
    }

    @Override
    public Collection getObjectFromReader() {
        return readerAPI.getUnReadArticleIds();
    }

    @Override
    public Collection getObjectFromDB() {
        return dao.getUnReadArticleIds();
    }

    @Override
    public void processReaderList(Collection readerObjCollection) {
        RssLabLog.info("sync.unread article, server.id.list.size:", readerObjCollection.size());

        RssLabLog.debug("httpsGet unread articles from server");
        //pull articles and insert into DB
        List<Article> list = readerAPI.getUnReadNews(readerObjCollection);
        RssLabLog.debug("sync.unread article, server.article.list.size:", list.size());

        if (list.size() > 0) {
            dao.insertArticles(list);
           // dao.insertArticleImageMapping(list);
        }
    }

    @Override
    public void processDBList(Collection DbObjCollection) {
        RssLabLog.info("sync.unread article remove from DB, list size:", DbObjCollection.size());
        if (DbObjCollection.size() > 0) {
            //delete from DB
            dao.removeArticles((List<Article>) DbObjCollection);
           // dao.removeImages(List < Article > DbObjCollection);
        }
    }



}
