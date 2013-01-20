package com.wiloon.android.rsslab;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.os.Bundle;
import com.wiloon.android.rsslab.synchelper.SyncSubscriptions;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/7/12
 * Time: 7:13 PM
 */

public class OnTokenAcquired implements AccountManagerCallback<Bundle> {


    public OnTokenAcquired(Context context) {

    }

    @Override
    public void run(AccountManagerFuture<Bundle> result) {
         try {
            Bundle bundle = null;

            bundle = result.getResult();

            final String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

            SyncSubscriptions sync = new SyncSubscriptions(token);
            sync.start();


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }


    }
}