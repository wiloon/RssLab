package com.wiloon.android.rsslab.synchelper;

import com.wiloon.android.rsslab.utils.GoogleReaderAPI;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.utils.RssLabLog;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/11/12
 * Time: 9:52 PM
 */
public abstract class SyncHelperImpl implements SyncHelper {

    protected GoogleReaderAPI readerAPI;
    protected RssLabDao dao;

    protected SyncHelperImpl() {
        this.dao = DaoFactory.getDaoForSync();
    }

    public SyncHelperImpl(String token) {
        this();
        this.readerAPI = new GoogleReaderAPI(token);


    }


    public abstract void initial();

    public boolean doSync() {
        boolean sync = false;
        initial();

        sync = isSync();
        RssLabLog.info("syncHelper.isSync", sync);

        if (sync) {
            sync = process();
        }
        return sync;
    }


}
