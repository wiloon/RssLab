package com.wiloon.android.rsslab.synchelper;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 11:38 AM
 */
public interface SyncAddDeleteItems {

    //httpsGet object/list from reader
    public Collection getObjectFromReader();

    //httpsGet object/list from DB
    public Collection getObjectFromDB();

    public void processReaderList(Collection readerObjCollection);

    public void processDBList(Collection DBObjCollection);
}
