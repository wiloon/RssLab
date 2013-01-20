package com.wiloon.android.rsslab.synchelper;

import com.wiloon.android.rsslab.beans.GoogleReaderItem;
import com.wiloon.android.rsslab.beans.UnReadCountImpl;
import com.wiloon.android.rsslab.utils.RssLabLog;
import com.wiloon.android.rsslab.utils.Utils;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 11:04 AM
 */
public class SyncUnReadCount extends SyncAddDeleteItemsImpl {


    public SyncUnReadCount(String token) {
        super(token);
    }


    protected boolean isItemEqual(GoogleReaderItem objReader, GoogleReaderItem objDB) {
        boolean equal = false;
        if (objReader.getId().equals(objDB.getId())) {
            // RssLabLog.debug("sync.unReadCount", objDB.getId());

            String readerCount = ((UnReadCountImpl) objReader).getCount();
            String DBCount = ((UnReadCountImpl) objDB).getCount();

            // RssLabLog.debug("sync.unReadCount", "readerCount", readerCount, "DBCount", DBCount);
            if (readerCount.equals(DBCount)) {
                equal = true;
            } else {
                //id equal, count not equal
                DBObjCollection.remove(objDB);
            }
            //RssLabLog.debug("sync.unReadCount.return", equal);
        }
        return equal;
    }


    @Override
    public List<UnReadCountImpl> getObjectFromReader() {
        //httpsGet un read count from google reader
        return readerAPI.getUnreadCount();
    }

    @Override
    public List<UnReadCountImpl> getObjectFromDB() {
        // httpsGet un read count from DB
        return dao.getUnReadCount();
    }

    @Override
    public void processReaderList(Collection readerObjCollection) {
        //if reader list size >0 store the count into db.
        for (UnReadCountImpl unReadCountImpl : (List<UnReadCountImpl>) readerObjCollection) {
            String id = unReadCountImpl.getId();
            if (Utils.isTagId(id)) {
                dao.updateTagCount(unReadCountImpl);
            } else if (Utils.isFeedId(id)) {
                dao.updateFeedCount(unReadCountImpl);
            }
        }
        RssLabLog.debug("sync.unReadCount.", "Done");
    }

    @Override
    public void processDBList(Collection DBObjCollection) {
        //if db list > 0, set count to 0,
        for (UnReadCountImpl unReadCountImpl : (List<UnReadCountImpl>) DBObjCollection) {
            String count = unReadCountImpl.getCount();
            if (!count.equals("0")) {
                String id = unReadCountImpl.getId();
                unReadCountImpl.setCount("0");
                RssLabLog.debug("sync.unReadCount.processDBList.setCountTo.0", id);
                if (Utils.isTagId(id)) {
                    dao.updateTagCount(unReadCountImpl);
                } else if (Utils.isFeedId(id)) {
                    dao.updateFeedCount(unReadCountImpl);
                }
            }

        }
    }
}
