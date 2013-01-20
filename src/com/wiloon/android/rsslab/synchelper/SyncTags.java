package com.wiloon.android.rsslab.synchelper;

import com.wiloon.android.rsslab.beans.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/11/12
 * Time: 10:38 PM
 */
public class SyncTags extends SyncAddDeleteItemsImpl {


    public SyncTags(String token) {
        super(token);

    }

    public SyncTags(List listReader, List listDB) {
        super(listReader, listDB);
    }

    @Override
    public List getObjectFromReader() {
        return readerAPI.getTags();
    }

    @Override
    public List getObjectFromDB() {
        return dao.getTags();
    }

    @Override
    public void processReaderList(Collection readerObjCollection) {
        dao.insertTags((List<Tag>) readerObjCollection);
    }

    @Override
    public void processDBList(Collection DBObjCollection) {
        dao.removeTags((List<Tag>) DBObjCollection);
    }


}
