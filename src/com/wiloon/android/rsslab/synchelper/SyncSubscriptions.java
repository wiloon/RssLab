package com.wiloon.android.rsslab.synchelper;

import com.wiloon.android.rsslab.beans.Tag;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.synchelper.syncimage.SyncImageThreadPool;
import com.wiloon.android.rsslab.synchelper.synconlinefeeds.SyncOnlineFeeds;
import com.wiloon.android.rsslab.utils.GoogleReaderAPI;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 6/28/12
 * Time: 10:51 PM
 */
public class SyncSubscriptions extends Thread {
    private GoogleReaderAPI readerAPI;
    private RssLabDao dao;
    private List<Tag> lstReaderTags;
    String token;

    public SyncSubscriptions(String token) {
        readerAPI = new GoogleReaderAPI(token);
        dao = DaoFactory.getDaoForSync();
        this.token = token;
    }

    public void run() {
        try {
            // read item
            RssLabLog.info("sync read item", "start.");
            SyncReadItem syncReadItem = new SyncReadItem(token);
            syncReadItem.doSync();
            RssLabLog.info("sync read item", "done.");
            RssLabLog.info("=", "====================");

            // tags
            RssLabLog.info("sync tags", "start.");
            SyncTags syncTags = new SyncTags(token);
            boolean tagSync = syncTags.doSync();
            RssLabLog.info("sync tags", "done.");
            RssLabLog.info("=", "====================");

            //feeds
            RssLabLog.info("sync feeds", "start.");
            SyncFeeds syncFeeds = new SyncFeeds((token));
            boolean feedSync = syncFeeds.doSync();
            RssLabLog.info("sync feeds", "done.");
            RssLabLog.info("=", "====================");

            //unread count
            RssLabLog.info("sync unread count", "start.");
            SyncUnReadCount syncUnReadCount = new SyncUnReadCount(token);
            boolean unReadSync = syncUnReadCount.doSync();
            RssLabLog.info("sync unread count", "done.");
            RssLabLog.info("=", "====================");

            //unread articles
            RssLabLog.info("sync unread article", "start.");
            SyncUnReadArticle syncArticlesArticle = new SyncUnReadArticle(token);
            syncArticlesArticle.doSync();
            RssLabLog.info("sync unread article", "done.");
            RssLabLog.info("=", "====================");


            //download images
            SyncImageThreadPool syncImageThreadPool = new SyncImageThreadPool();
            syncImageThreadPool.execute();

            //sync online feeds
            RssLabLog.info("sync online articles", "start.");
            SyncOnlineFeeds syncOnlineFeeds = new SyncOnlineFeeds();
            syncOnlineFeeds.execute();
            RssLabLog.info("sync online articles", "Done.");
            RssLabLog.info("/", "***********************SYNC END******************************/");
            /* if (tagSync || feedSync || unReadSync) {
                List<Tag> tagList = dao.getTagsForView();
                RssLabLog.debug("wiloonRssLog.debugoSync.tagList", String.valueOf(tagList.size()));

                Looper mainLooper = Looper.getMainLooper();
                MyHandler handler = MyHandler.getInstance();
                Message m = handler.obtainMessage();

                m.obj = tagList;
                m.arg1 = 2;
                m.sendToTarget();
                //handler.sendMessage(m);
                RssLabLog.debug("message.sent", String.valueOf(tagList.size()));
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            RssLabLog.error("SyncSubscriptions", "error.");

        }


        RssLabLog.debug("SyncSubscriptions", "Done.");
    }


}
