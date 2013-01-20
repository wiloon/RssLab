package com.wiloon.android.rsslab.synchelper;

import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 11:12 AM
 */
public interface SyncHelper {

    //initial, prepare data
    public void initial();

    // do sync
    public boolean doSync();


    //check if sync needed
    public boolean isSync();

    //process, update DB/reader, sync
    public boolean process();


}
