package com.wiloon.android.rsslab.synchelper;

import com.wiloon.android.rsslab.beans.Feed;
import com.wiloon.android.rsslab.beans.GoogleReaderItem;
import com.wiloon.android.rsslab.beans.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/11/12
 * Time: 10:45 PM
 */
public class SyncFeeds extends SyncAddDeleteItemsImpl {
    public SyncFeeds(String token) {
        super(token);
    }

    @Override
    protected boolean isItemEqual(GoogleReaderItem objReader, GoogleReaderItem objDB) {
        Feed feedReader = (Feed) objReader;
        Feed feedDB = (Feed) objDB;
        boolean feedEqual = false;
        boolean tagEqual = false;
        //check id equal
        if (objReader.getId().equals(objDB.getId())) {
            feedEqual = true;
            //if id equal, then compare categories
            SyncTags syncTags = new SyncTags(feedReader.getCategories(), feedDB.getCategories());
            tagEqual = syncTags.isSync();


        }

        return feedEqual || tagEqual;
    }

    @Override
    public void processReaderList(Collection readerObjCollection) {
        //insert feed categories
        for (Feed feed : (List<Feed>) readerObjCollection) {
            dao.insertFeedTags(feed.getId(), feed.getCategories());
        }

        //insert feeds
        dao.insertFeeds((List<Feed>) readerObjCollection);
    }

    @Override
    public void processDBList(Collection DBObjCollection) {
        //remove feed categories
        for (Feed feed : (List<Feed>) DBObjCollection) {
            for (Tag tag : feed.getCategories()) {
                dao.removeFeedTag(feed.getId(), tag.getId());
            }
        }
        //remove feeds
        dao.removeFeeds((List<Feed>) DBObjCollection);

    }

    @Override
    public List getObjectFromReader() {
        return readerAPI.getFeeds();
    }

    @Override
    public List getObjectFromDB() {
        return dao.getFeeds();
    }


}
