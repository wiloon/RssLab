package com.wiloon.android.rsslab.synchelper;

import com.wiloon.android.rsslab.beans.GoogleReaderItem;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 11:36 AM
 */
public abstract class SyncAddDeleteItemsImpl extends SyncHelperImpl implements SyncAddDeleteItems {
    protected Collection readerObjCollection;
    protected Collection DBObjCollection;

    public SyncAddDeleteItemsImpl() {
        super();
        this.readerObjCollection = new CopyOnWriteArrayList();
        this.DBObjCollection = new CopyOnWriteArrayList();
    }

    public SyncAddDeleteItemsImpl(String token) {
        super(token);
        this.readerObjCollection = new CopyOnWriteArrayList();
        this.DBObjCollection = new CopyOnWriteArrayList();
    }

    public SyncAddDeleteItemsImpl(List<GoogleReaderItem> readerObjCollection, List<GoogleReaderItem> DBObjCollection) {
        this.readerObjCollection = new CopyOnWriteArrayList();
        this.DBObjCollection = new CopyOnWriteArrayList();
        this.readerObjCollection.addAll(readerObjCollection);
        this.DBObjCollection.addAll(DBObjCollection);
    }

    public void initial() {
        readerObjCollection.addAll(getObjectFromReader());
        DBObjCollection.addAll(getObjectFromDB());
    }

    @Override
    public boolean isSync() {
        //RssLabLog.debug("check if sync needed", "start.");
        boolean sync = false;
        for (GoogleReaderItem objReader : (List<GoogleReaderItem>) readerObjCollection) {
            //RssLabLog.debug("syncHelper.isSync.readerObj.id", objReader.getId());
            for (GoogleReaderItem objDB : (List<GoogleReaderItem>) DBObjCollection) {
                if (isItemEqual(objReader, objDB)) {
                    readerObjCollection.remove(objReader);
                    // RssLabLog.debug("syncHelper.isSync.removeFromList", objReader.getId());
                    // RssLabLog.debug("syncHelper.isSync.readerObjCollection.size", String.valueOf(readerObjCollection.size()));

                    DBObjCollection.remove(objDB);
                    //RssLabLog.debug("syncHelper.isSync.removeFromList", objDB.getId());
                    //RssLabLog.debug("syncHelper.isSync.DBObjCollection.size", String.valueOf(DBObjCollection.size()));
                    break;
                }
            }
        }
        int readerObjCollectionSize = readerObjCollection.size();
        int DBObjCollectionSize = DBObjCollection.size();
        if (readerObjCollectionSize > 0 || DBObjCollectionSize > 0) {
            RssLabLog.debug("sync helper.is sync.server collection size", readerObjCollectionSize);
            RssLabLog.debug("sync helper.is sync.DB collection size", DBObjCollectionSize);

            sync = true;
        }

        return sync;
    }

    public boolean process() {
        boolean sync = false;
        // if reader list size >0, new obj?, add to DB
        if (readerObjCollection.size() > 0) {
            sync = true;
            processReaderList(readerObjCollection);

        }

        //if db list size >0, obj deleted? remove obj from DB
        if (DBObjCollection.size() > 0) {
            sync = true;
            processDBList(DBObjCollection);
        }
        return sync;
    }

    protected boolean isItemEqual(GoogleReaderItem objReader, GoogleReaderItem objDB) {
        boolean equal = false;
        if (objReader.getId().equals(objDB.getId())) {
            equal = true;
        } else {
            equal = false;
        }

        return equal;
    }


}
