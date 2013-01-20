package com.wiloon.android.rsslab.dao;

import android.app.Activity;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/8/12
 * Time: 9:01 PM
 */
public class DaoFactory {
    public static RssLabDao getDaoForSync() {
        return RssLabDao.getInstance();
    }

    public static RssLabDao createDaoWithActivity(Activity act) {
        return new RssLabDao(act);
    }
}
