package com.wiloon.android.rsslab.synchelper.syncimage;

import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 8/26/12
 * Time: 4:47 PM
 */
public class SyncImageThreadPool {
    private static ThreadPoolExecutor threadPoolExecutor;

    public void execute() {
        RssLabLog.debug("sync.image.thread pool.start");
        //TODO httpsGet x record one time...
        ArrayBlockingQueue queue = new ArrayBlockingQueue(1000);

        // ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 20, 3, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.AbortPolicy());
        threadPoolExecutor = new ThreadPoolExecutor(5, 15, 3, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.AbortPolicy());

        RssLabDao dao;
        dao = DaoFactory.getDaoForSync();
        List<String> list = dao.getUnSyncImageArticleIds();
        for (String articleId : list) {
            addExecutor(articleId);
        }
        RssLabLog.debug("sync.image.thread pool.end");

    }

    public static void addExecutor(String articleId) {
        threadPoolExecutor.execute(new ImgDownloadExecutor(articleId));
    }
}
