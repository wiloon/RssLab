package com.wiloon.android.rsslab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.utils.RssLabLog;
import com.wiloon.android.rsslab.utils.Utils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/21/12
 * Time: 9:26 AM
 */
public class ArticleListActivity extends Activity {
    private RssLabDao dao;

    public ArticleListActivity() {
        dao = DaoFactory.getDaoForSync();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RssLabLog.debug("ArticleListActivity.onCreate");
        Intent intent = getIntent();
        String tagId = intent.getStringExtra(AppConstant.MESSAGE_TYPE_TAG_ID);
        String feedId = intent.getStringExtra(AppConstant.MESSAGE_TYPE_FEED_ID);
        RssLabLog.debug("ArticleListActivity.feedId: ", feedId);
        List<Article> articleList;
        if (tagId != null && tagId.equals(AppConstant.TAG_ID_ALL_ITEMS)) {
            articleList = dao.getAllArticles(tagId);
        } else if (feedId != null && Utils.isTagId(feedId)) {
            articleList = dao.getArticlesUnderTag(feedId);
        } else {
            articleList = dao.getArticlesForView(feedId);

        }
        RssLabLog.debug("ArticleListActivity.feedList: ", "" + articleList.size());

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout parent = (LinearLayout) layoutInflater.inflate(R.layout.article_list, null);

        ListView listView = (ListView) parent.findViewById(R.id.ArticleListView);


        ArticleListAdapter adapter = new ArticleListAdapter(this, articleList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ArticleListOnClickListener(this));
        parent.removeView(listView);
        this.setContentView(listView);


    }
}
