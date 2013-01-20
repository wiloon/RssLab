package com.wiloon.android.rsslab.synchelper;

import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.beans.ArticleImage;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.io.File;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/22/12
 * Time: 5:27 PM
 */
public class SyncReadItem extends SyncHelperImpl {
    List<Article> articleList;
    List<ArticleImage> articleImageList;

    public SyncReadItem(String token) {
        super(token);
    }

    @Override
    public void initial() {
        articleList = dao.getReadArticle();
        articleImageList = dao.getReadArticleImage();
        RssLabLog.debug("sync.read item.article list.size", articleList.size());
        RssLabLog.debug("sync.read item.articleImageList.size", articleImageList.size());

    }

    @Override
    public boolean isSync() {
        if (articleList.size() > 0) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public boolean process() {
        RssLabLog.info("sync.mark article as read, list size:", articleList.size());
        //send request to mark article as read
        readerAPI.markArticlesAsRead(articleList);

        //delete images on sd card
        for (ArticleImage articleImage : articleImageList) {
            String filePath = articleImage.getImgFilePath();
            if (filePath != null) {
                File file = new File(filePath);
                if (file.isFile() && file.exists()) {
                    file.delete();
                    RssLabLog.debug("sync.delete image", articleImage.getImgFilePath());
                }
            }
        }

        //delete image mapping
        dao.deleteArticleImageMapping(articleImageList);

        //delete article
        dao.deleteArticle(articleList);

        return true;
    }
}
