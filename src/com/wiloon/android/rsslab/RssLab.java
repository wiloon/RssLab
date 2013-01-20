package com.wiloon.android.rsslab;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.wiloon.android.rsslab.beans.Tag;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.List;

public class RssLab extends Activity {

    RssLabDao dao;
    private Handler mHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RssLabLog.debug("rssLab.onCreate");

        //initialize dao
        dao = DaoFactory.createDaoWithActivity(this);


        doSync();
    }

    public void onStart() {
        super.onStart();
        /* RssLabLog.debug("rssLab.onStart");
        setContentView(R.layout.main);

        //httpsGet all tags from DB
        List<Tag> tagList = dao.getTagsForView();

        //set message handler
        mHandler = MyHandler.getInstance(Looper.getMainLooper(), this);
        Message m = mHandler.obtainMessage();
        m.obj = tagList;
        m.sendToTarget();*/


        List<Tag> tagList = dao.getTagsForView();
        tagList.add(0, new Tag(AppConstant.TAG_ID_ALL_ITEMS, null, "All Items"));
        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) layoutInflater.inflate(R.layout.main, null);
        ListView listView = (ListView) parent.findViewById(R.id.ReaderListView);
        UnReadArrayAdapter adapter = new UnReadArrayAdapter(this, tagList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new TagOnClickListener(this));
        listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                RssLabLog.debug("rsslab.onFocus.");
            }
        });
        listView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                RssLabLog.debug("rsslab.onItemSelected");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                RssLabLog.debug("rsslab.onNothingSelected");
            }
        });

        parent.removeView(listView);
        this.setContentView(listView);

    }

    public void onResume() {
        super.onResume();
        RssLabLog.debug("rssLab.onResume");

    }

    public void onRestart() {
        super.onRestart();
        RssLabLog.debug("rssLab.onRestart");

    }

    public void onPostResume() {
        super.onPostResume();
        RssLabLog.debug("rssLab.onResume");

    }

    public void onPause() {
        super.onPause();
        RssLabLog.debug("rssLab.onPause");

    }

    public void onStop() {
        super.onStop();
        RssLabLog.debug("rssLab.onStop");

    }

    public void onDestroy() {
        super.onDestroy();
        RssLabLog.debug("rssLab.onDestroy");

    }

    private void doSync() {
        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccounts();

        for (Account account : accounts) {
            if (account.type.equals(this.getString(R.string.google_account_type)) && account.name.equalsIgnoreCase("wiloon.wy@gmail.com")) {
                Bundle options = new Bundle();
                am.getAuthToken(account, "Reader", options, this, new OnTokenAcquired(this), null);

            } else {
            }
        }
    }
}


