package com.wiloon.android.rsslab;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.utils.RssLabLog;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/14/12
 * Time: 10:36 PM
 */
public class ArticleListOnClickListener implements AdapterView.OnItemClickListener {
    private Activity activity;

    public ArticleListOnClickListener(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        RssLabLog.debug("ArticleListOnClickListener.onclick", "" + i);
        ListView parent = (ListView) adapterView;
        Article article = (Article) parent.getItemAtPosition(i);

        RssLabLog.debug("ArticleListOnClickListener.click: ", "article id:", article.getArticleId(), "Title:", article.getTitle());

        //to start article activity
        //Intent intent = new Intent(activity, ArticleActivity.class);
        Intent intent = new Intent(activity, ArticleFlipActivity.class);

        intent.putExtra(AppConstant.MESSAGE_TYPE_Article_ID, article.getArticleId());
        intent.putExtra(AppConstant.MESSAGE_TYPE_ARTICLE_FEED_ID, article.getFeedId());

        activity.startActivity(intent);
        RssLabLog.debug("ArticleListOnClickListener.onclick", "start article activity");

    }
}
