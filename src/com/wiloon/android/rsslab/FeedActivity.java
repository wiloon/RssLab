package com.wiloon.android.rsslab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;
import com.wiloon.android.rsslab.beans.BeanFactory;
import com.wiloon.android.rsslab.beans.Feed;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 10:07 PM
 */
public class FeedActivity extends Activity {
    private RssLabDao dao;

    public FeedActivity() {
        RssLabLog.debug("feedActivity: ", "constructor");

        dao = DaoFactory.getDaoForSync();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RssLabLog.debug("feedActivity.onCreate: ", "start");
        Intent intent = getIntent();
        String tagId = intent.getStringExtra(AppConstant.MESSAGE_TYPE_TAG_ID);
        RssLabLog.debug("feedActivity.onCreate.tagLabel: ", tagId);

        List<Feed> feedList = dao.getFeedsForView(tagId);
        Feed feed = BeanFactory.createFeedForView(tagId, "All Items", "");
        feedList.add(0, feed);
        RssLabLog.debug("feedActivity.onCreate.feedList: ", "" + feedList.size());

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout parent = (LinearLayout) layoutInflater.inflate(R.layout.feed, null);

        ListView listView = (ListView) parent.findViewById(R.id.FeedListView);

        UnReadArrayAdapter adapter = new UnReadArrayAdapter(this, feedList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new FeedOnClickListener(this));
        parent.removeView(listView);
        this.setContentView(listView);


    }
}
