package com.wiloon.android.rsslab;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.wiloon.android.rsslab.beans.Feed;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.utils.RssLabLog;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 10:36 PM
 */
public class FeedOnClickListener implements AdapterView.OnItemClickListener {
    private Activity activity;

    public FeedOnClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        RssLabLog.debug("FeedOnClickListener.onclick");
        ListView parent = (ListView) adapterView;
        Feed feed = (Feed) parent.getItemAtPosition(i);

        RssLabLog.debug("feed.click: ", "feedId:", feed.getId(), "feedTitle:", feed.getTitle());
        //to start feed activity
        Intent intent = new Intent(activity, ArticleListActivity.class);

        intent.putExtra(AppConstant.MESSAGE_TYPE_FEED_ID, feed.getId());

        activity.startActivity(intent);
        RssLabLog.debug("FeedOnClickListener.onclick: ", "article list activity started.");

    }
}
