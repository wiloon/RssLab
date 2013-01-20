package com.wiloon.android.rsslab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.utils.RssLabLog;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/21/12
 * Time: 11:38 AM
 */
public class ArticleActivity extends Activity {
    private RssLabDao dao;

    public ArticleActivity() {
        dao = DaoFactory.getDaoForSync();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RssLabLog.debug("ArticleActivity.onCreate");

        Intent intent = getIntent();
        String articleId = intent.getStringExtra(AppConstant.MESSAGE_TYPE_Article_ID);
        RssLabLog.debug("ArticleActivity.article id: ", articleId);

        Article article = dao.getArticleById(articleId);

        //mark article as read
        dao.updateArticleStatusToRead(articleId);

        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        LinearLayout parent = (LinearLayout) layoutInflater.inflate(R.layout.article, null);

        WebView webView = (WebView) parent.findViewById(R.id.ArticleView);
        String encoding = AppConstant.ENCODING_UTF8;
        webView.getSettings().setDefaultTextEncodingName(encoding);

        if (article.getContent() != null) {
            String content;
            content = article.getContent();
            //content = "<img src='file:/sdcard/0_myDoc/image/bug.jpg' width='300px'/>";
            RssLabLog.debug("article activity.content", content);
            webView.loadDataWithBaseURL(null, content, AppConstant.MIME_TYPE, encoding, null);
        }


        parent.removeView(webView);
        this.setContentView(webView);
    }
}
