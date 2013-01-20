package com.wiloon.android.rsslab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.beans.ArticleForView;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.utils.RssLabLog;
import com.wiloon.android.rsslab.utils.Utils;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 8/19/12
 * Time: 4:18 PM
 */
public class ArticleFlipActivity extends Activity {
    private RssLabDao dao;
    String feedId;
    int articleNumber;
    int readIndex;
    private ArrayList<ArticleView> viewsList;

    public ArticleFlipActivity() {
        dao = DaoFactory.getDaoForSync();
        viewsList = new ArrayList<ArticleView>();
        readIndex = -1;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RssLabLog.debug("ArticleFlipActivity.onCreate", ".");

        Intent intent = getIntent();
        String articleId = intent.getStringExtra(AppConstant.MESSAGE_TYPE_Article_ID);
        feedId = intent.getStringExtra(AppConstant.MESSAGE_TYPE_ARTICLE_FEED_ID);
        RssLabLog.debug("ArticleFlipActivity.article id: ", articleId);

        Article article = dao.getArticleById(articleId);
        //mark article as read
        //dao.updateArticleStatusToRead(articleId);


        LayoutInflater layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout articleFlipLayout = (LinearLayout) layoutInflater.inflate(R.layout.article_flip, null);

        ViewPager viewPager = (ViewPager) articleFlipLayout.findViewById(R.id.vPager);

        setContentView(articleFlipLayout);

        viewPager.setAdapter(new GuidePageAdapter());

    }

    public int getArticleNumber() {
        int num = 0;
        if (Utils.isTagId(feedId)) {
            num = dao.getarticleNumberByTagId(feedId);
        } else if (feedId.equals(AppConstant.TAG_ID_ALL_ITEMS)) {
            num = dao.getarticleNum();
        } else {
            num = dao.getArticleNumberByFeedId(feedId);
        }
        return num;
    }

    class GuidePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getArticleNumber();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            //RssLabLog.debug("activity.article flip.adapter.isViewFromObject", arg1.toString());

            return arg0 == arg1;
        }

        @Override
        public int getItemPosition(Object object) {
            //RssLabLog.debug("activity.article flip.adapter.getItemPosition", object.toString());

            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            RssLabLog.debug("activity.article flip.adapter.destroyItem", arg1);

            ((ViewPager) arg0).removeView(viewsList.get(arg1).getArticleView());
            //viewsList.set(arg1, null);
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            RssLabLog.debug("activity.article flip.adapter.instantiateItem", arg1);
            ArticleForView article = null;
            View viewRtn = null;
            //mark current reading article as read
            if (isMarkArticleAsRead(arg1)) {
                if (viewsList != null && viewsList.size() > 0) {
                    ArticleView view = null;
                    if (readIndex >= 0 && readIndex <= viewsList.size() - 1) {
                        view = viewsList.get(readIndex);
                    }

                    if (view != null) {
                        String articleId = viewsList.get(readIndex).getArticleId();
                        dao.updateArticleStatusToRead(articleId);
                    }

                }


            }

            //load new article
            //when flip from right to left
            if (isLoadNewArticle(arg1)) {
                if (Utils.isTagId(feedId)) {
                    article = dao.getOneArticleByTagIdRandomly(feedId);
                } else if (feedId.equals(AppConstant.TAG_ID_ALL_ITEMS)) {
                    article = dao.getOneArticleRandomly();
                } else {
                    article = dao.getOneArticleByFeedId(feedId);

                }


                if (article != null && article.getContent() != null) {
                    LayoutInflater layoutInflater = (LayoutInflater) ArticleFlipActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    LinearLayout layout = (LinearLayout) layoutInflater.inflate(R.layout.layout0, null);
                    WebView webView = (WebView) layout.findViewById(R.id.webView0);
                    String encoding = AppConstant.ENCODING_UTF8;
                    webView.getSettings().setDefaultTextEncodingName(encoding);
                    String content = article.getContentForView();

                    //content = "<img src='file:/sdcard/0_myDoc/image/bug.jpg' width='300px'/>";
                    RssLabLog.debug("article activity.content", content);
                    webView.loadDataWithBaseURL(null, content, AppConstant.MIME_TYPE, encoding, null);
                    viewsList.add(new ArticleView(article.getArticleId(), layout));
                    viewRtn = viewsList.get(arg1).getArticleView();
                    ((ViewPager) arg0).addView(viewRtn);
                }

            }


            return viewRtn;
        }


        private boolean isMarkArticleAsRead(int arg1) {
            boolean rtn = false;
            if (arg1 - 1 > readIndex) {
                rtn = true;
                readIndex = arg1 - 1;
            }
            return rtn;
        }

        private boolean isLoadNewArticle(int arg1) {
            boolean rtn = false;
            if (arg1 == viewsList.size()) {
                rtn = true;

            }
            return rtn;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }


    }

    class ArticleView {
        String articleId;
        View articleView;

        public ArticleView(String articleId, View articleView) {
            this.articleId = articleId;
            this.articleView = articleView;
        }

        public String getArticleId() {
            return articleId;
        }

        public void setArticleId(String articleId) {
            this.articleId = articleId;
        }

        public View getArticleView() {
            return articleView;
        }

        public void setArticleView(View articleView) {
            this.articleView = articleView;
        }
    }


}
