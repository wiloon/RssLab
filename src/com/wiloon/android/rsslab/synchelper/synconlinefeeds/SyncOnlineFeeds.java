package com.wiloon.android.rsslab.synchelper.synconlinefeeds;

import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.synchelper.syncimage.SyncImageThreadPool;
import com.wiloon.android.rsslab.utils.HttpHelper;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 9/2/12
 * Time: 9:55 AM
 */
public class SyncOnlineFeeds {
    RssLabDao dao;

    public void execute() {

        dao = DaoFactory.getDaoForSync();
        ArrayBlockingQueue queue = new ArrayBlockingQueue(1000);
        ThreadPoolExecutor tpe = new ThreadPoolExecutor(5, 10, 3, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.AbortPolicy());

        //httpsGet online feeds configuration
        String[] feedIds = dao.getFeedsByTabId(AppConstant.ONLINE_TAG_ID);

        //get URLs of online feeds
        List<Article> articleUrls = dao.getArticleURLsByFeedIds(feedIds);

        for (Article article : articleUrls) {
            tpe.execute(new OnlineFeedsExecutor(article));
        }
    }
}

class OnlineFeedsExecutor implements Runnable {
    private Article article;
    HttpHelper httpHelper;
    RssLabDao dao;

    public OnlineFeedsExecutor(Article article) {
        this.article = article;
        httpHelper = new HttpHelper();
        dao = DaoFactory.getDaoForSync();
    }


    @Override
    public void run() {
        String content = "";
        try {
            RssLabLog.debug("Download online article", article.getTitle());
            OnlineArticleFilter filter = null;

            //download full article by url
            String articleUrl = article.getHref();
            String html = httpHelper.httpGet(articleUrl);
            if (!html.isEmpty()) {
                if (articleUrl.indexOf("infzm.com") > -1) {
                    filter = new OnlineArticleFilterImpl(AppConstant.FILTER_PATTERN_INFOZM);
                    content = filter.getArticleContent(html);
                    //replace article content with full article in DB table

                } else if (articleUrl.indexOf("ftchinese.com") > -1) {
                    filter = new OnlineArticleFilterImpl(AppConstant.FILTER_PATTERN_FT);
                    content = filter.getArticleContent(html);
                } else {
                    content = html;
                }
                if (content.isEmpty()) {
                    RssLabLog.error("content id empty!!!", "!!!");
                }
                dao.updateArticleContent(article.getArticleId(), content);
            } else {

            }
            dao.updateArticleAsOffline(article);
            SyncImageThreadPool.addExecutor(article.getArticleId());
        } catch (Exception e) {
            RssLabLog.error("online article download error", article.getArticleId(), e.toString());
        }


    }
}