package com.wiloon.android.rsslab.synchelper.syncimage;

import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.beans.ArticleImage;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.dao.DaoFactory;
import com.wiloon.android.rsslab.dao.RssLabDao;
import com.wiloon.android.rsslab.utils.HttpHelper;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 8/26/12
 * Time: 5:03 PM
 */
public class ImgDownloadExecutor implements Runnable {
    private String articleId;
    private RssLabDao dao;
    protected HttpHelper httpHelper;
    private static int index;
    private boolean isWidthHeightRemoved;

    public ImgDownloadExecutor(String articleId) {
        this.articleId = articleId;
        this.dao = DaoFactory.getDaoForSync();
        httpHelper = new HttpHelper();
        isWidthHeightRemoved = false;
    }

    @Override
    public void run() {
        RssLabLog.debug("sync.image.progress", index);
        Article article = dao.getArticleById(articleId);

        if (article != null) {
            RssLabLog.debug("sync.image.executor.article", article.getArticleId());

            String content = article.getContent();
            content = parseAndStoreImg(content);

            dao.updateArticleContent(articleId, content);
            dao.markArticleImgStatusAsSync(articleId);
            //dao.updateArticleImageMappingByID(articleId);
        }
        index++;
    }

    private String removeWidthAndHeight(String content) {
        String pattern = "(width|height)(=|:)('|\"){0,1}[0-9]{1,4}(px){0,1}('|\"){0,1}";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(content);
        content = matcher.replaceAll("");
        return content;


    }

    private String parseAndStoreImg(String content) {
        if (content == null) {
            return "";
        }
        int length = content.length();
        int index = 0;
        List<ArticleImage> list = new ArrayList<ArticleImage>();
        while (true) {
            try {
                String imgUrl;

                String pattern = "img.*?src=\"(.+?)\"";
                Pattern p = Pattern.compile(pattern);
                Matcher matcher = p.matcher(content);
                boolean find = matcher.find(index);
                if (find) {
                    index = matcher.end();
                    imgUrl = matcher.group(1);
                    boolean download = isDownload(imgUrl);
                    if (download) {
                        if (!isWidthHeightRemoved) {
                            content = removeWidthAndHeight(content);
                            isWidthHeightRemoved = true;
                        }
                        RssLabLog.debug("downloading img: ", "...");
                        String extension = imgUrl.substring(imgUrl.length() - 3, imgUrl.length());
                        byte[] data = httpHelper.getImgAsByteArray(imgUrl);
                        String path = AppConstant.IMG_FILE_BASE_PATH;
                        String fileName = UUID.randomUUID() + "." + extension;
                        String fullPath = path + File.separator + fileName;
                        File file = new File(fullPath);
                        FileOutputStream outStream = null;

                        outStream = new FileOutputStream(file);
                        outStream.write(data);
                        outStream.close();
                        String localImgPath = "file:" + fullPath;

                        //replace url with local file path
                        content = content.replace(imgUrl, localImgPath);

                        content = content.replace(localImgPath + "\"", localImgPath + "\"" + " width='295px' ");

                        list.add(new ArticleImage(null, articleId, localImgPath));
                    }
                } else {
                    if (list != null && list.size() > 0) {
                        dao.insertArticleImageMapping(list);
                    }

                    return content;
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    private boolean isDownload(String url) {
        boolean download = false;

        String tmp = url.toLowerCase();
        String pattern = ".*(jpg|gif|png|jpeg)$";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(tmp);
        boolean match = matcher.find();

        if (match) {
            download = true;
        }


        int indexFacebook = url.indexOf("facebook_icon_large");
        int indexTweet = url.indexOf("twitter_icon_large");
        int indexGplus = url.indexOf("gplus-16");
        int indexMail = url.indexOf("emailthis2");
        int indexBookmark = url.indexOf("bookmark.gif");
        if (indexFacebook > -1 || indexTweet > -1 || indexGplus > -1 || indexMail > -1 || indexBookmark > -1) {
            download = false;
        }
        pattern = ".*(mf\\.gif)$";
        p = Pattern.compile(pattern);
        matcher = p.matcher(tmp);
        match = matcher.find();

        if (match) {
            download = false;
        }

        if (url.indexOf("/sdcard/0_") > -1) {
            RssLabLog.debug("sync.img.error", "url error", url);
            download = false;
        }
        return download;
    }
}
