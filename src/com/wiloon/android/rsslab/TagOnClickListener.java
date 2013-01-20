package com.wiloon.android.rsslab;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.wiloon.android.rsslab.beans.Tag;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.utils.RssLabLog;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 10:36 PM
 */
public class TagOnClickListener implements AdapterView.OnItemClickListener {
    private Activity activity;

    public TagOnClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        RssLabLog.debug("tag.onclick: ", "start");
        ListView parent = (ListView) adapterView;


        int index = i;
        if ((parent).getTag() != null) {
            ((View) (parent).getTag()).setBackground(null);
        }
        (parent).setTag(view);
        view.setBackgroundColor(Color.RED);

        Tag tag = (Tag) parent.getItemAtPosition(index);
        String tagId = tag.getId();
        RssLabLog.debug("tag.onclick: ", index + "|" + tagId);
        Intent intent;
        if (tagId.equals(AppConstant.TAG_ID_ALL_ITEMS)) {
            //to start article activity
            intent = new Intent(activity, ArticleListActivity.class);

        } else {
            //to start feed activity
            intent = new Intent(activity, FeedActivity.class);

        }

        intent.putExtra(AppConstant.MESSAGE_TYPE_TAG_ID, tagId);

        activity.startActivity(intent);
        RssLabLog.debug("tag.onclick", "start feed activity");
        RssLabLog.debug("tag.onclick: ", "end");


    }
}
