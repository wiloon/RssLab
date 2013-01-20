package com.wiloon.android.rsslab.utils;


import android.database.sqlite.SQLiteDatabase;
import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.beans.Feed;
import com.wiloon.android.rsslab.beans.Tag;
import com.wiloon.android.rsslab.beans.UnReadCountImpl;
import com.wiloon.android.rsslab.common.AppConstant;
import com.wiloon.android.rsslab.dao.DatabaseHelper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 6/28/12
 * Time: 10:08 PM
 */
public class GoogleReaderAPI {
    private HttpHelper HttpHelper;
    DatabaseHelper dbHelper;
    SQLiteDatabase db;

    public GoogleReaderAPI(String token) {
        HttpHelper = new HttpHelper(token);
        this.dbHelper = DatabaseHelper.getInstance(null, "RssLab", null, 1);
        db = dbHelper.getWritableDatabase();

    }


    public List<Tag> getTags() {
        RssLabLog.debug("reader.api.getTags", "get tags from server.");
        String url = AppConstant.GOOGLE_READER_URL_TAG;
        String response = HttpHelper.httpsGet(url);
        JSONTokener jsonParser = new JSONTokener(response);
        JSONObject obj = null;
        List<Tag> list = null;
        try {
            obj = (JSONObject) jsonParser.nextValue();
            JSONArray arr = obj.getJSONArray("tags");
            int size = arr.length();
            RssLabLog.debug("googleReaderAPI.getTags.tag.size ", size);
            list = new ArrayList<Tag>();
            for (int i = 0; i < size; i++) {

                JSONObject o = arr.getJSONObject(i);
                String id = o.getString("id");

                String tagSortId = o.getString("sortid");
                RssLabLog.debug("googleReaderAPI.getTags.tag ", id, tagSortId);
                list.add(new Tag(id, tagSortId));
            }


        } catch (Exception e) {
            RssLabLog.debug("catch", e.toString());
            e.printStackTrace();

        }
        return list;

    }


    public List<Feed> getFeeds() {
        List<Feed> list = null;
        String url = AppConstant.GOOGLE_READER_URL_SUBSCRIPTION;
        String response = HttpHelper.httpsGet(url);
        //RssLabLog.debug("googleReaderAPI.getFeeds.response ", response);
        try {
            JSONTokener jsonParser = new JSONTokener(response);
            JSONObject obj = null;


            obj = (JSONObject) jsonParser.nextValue();
            JSONArray arr = obj.getJSONArray("subscriptions");
            int length = arr.length();
            RssLabLog.debug("googleReaderAPI.getFeeds.Length ", String.valueOf(length));
            list = new ArrayList<Feed>();
            for (int i = 0; i < length; i++) {

                JSONObject o = arr.getJSONObject(i);

                //id
                String id = o.getString("id");
                //RssLabLog.debug("googleReaderAPI.getFeeds.id ", id);

                //title
                String title = o.getString("title");
                //RssLabLog.debug("googleReaderAPI.getFeeds.sortid ", title);


                //categories
                ArrayList<Tag> categories = new ArrayList<Tag>();
                JSONArray jsonArray = o.getJSONArray("categories");
                int categoriesLength = jsonArray.length();
                for (int j = 0; j < categoriesLength; j++) {
                    JSONObject co = jsonArray.getJSONObject(j);
                    //id
                    String tagId = co.getString("id");

                    //label
                    String label = co.getString("label");


                    Tag tag = new Tag(tagId, null, label);

                    categories.add(tag);
                }

                //sortId
                String sortId = o.getString("sortid");

                //firstitemmsec
                String firstItemMsec = o.getString("firstitemmsec");


                list.add(new Feed(id, title, categories, sortId, firstItemMsec));
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<UnReadCountImpl> getUnreadCount() {
        RssLabLog.debug("reader.api.getUnreadCount");
        List<UnReadCountImpl> list = new ArrayList<UnReadCountImpl>();

        String url = AppConstant.GOOGLE_READER_UNREAD;
        String response = HttpHelper.httpsGet(url);

        try {
            JSONTokener jsonParser = new JSONTokener(response);
            JSONObject obj = null;

            obj = (JSONObject) jsonParser.nextValue();
            JSONArray arr = obj.getJSONArray("unreadcounts");
            int length = arr.length();
            RssLabLog.debug("googleReaderAPI.getUnRead.Length ", String.valueOf(length));

            for (int i = 0; i < length; i++) {

                JSONObject o = arr.getJSONObject(i);

                //id
                String id = o.getString("id");

                //count
                String count = o.getString("count");
                RssLabLog.debug("googleReaderAPI.getUnRead ", id + "|" + count);

                //newestItemTimestampUsec
                String newestItemTimestamp = o.getString("newestItemTimestampUsec");

                list.add(new UnReadCountImpl(id, count, newestItemTimestamp));


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Article> getUnReadNews(Collection tmpList) {
        RssLabLog.debug("reader.api.getUnReadNews", "start");
        List<Article> list = new ArrayList<Article>();
        int requestSize = 200;
        List<Article> idList = (List<Article>) tmpList;
        RssLabLog.debug("reader.api.getUnReadNews.idList.size", idList.size());

        int listSize = idList.size();
        int left = listSize;
        int indexStart, indexEnd;
        indexStart = -1;
        indexEnd = -1;
        boolean exit = false;
        while (!exit) {
            indexStart = indexEnd + 1;
            if (left > requestSize) {
                indexEnd = indexStart + requestSize - 1;
                left = left - requestSize;
                RssLabLog.debug("reader.api.get unread articles.number", requestSize);
            } else {
                indexEnd = listSize - 1;
                exit = true;
                RssLabLog.debug("reader.api.get unread articles.number", left);
            }
            list.addAll(getUnreadArticleList(idList, indexStart, indexEnd));
        }
        RssLabLog.debug("reader.api.getUnReadNews.result.list.size", list.size());
        return list;
    }

    public List<Article> getUnreadArticleList(List<Article> tmpList, int indexStart, int indexEnd) {
        StringBuffer id = new StringBuffer();
        StringBuffer it = new StringBuffer();
        List<Article> list = new ArrayList<Article>();

        try {
            for (int i = indexStart; i <= indexEnd; i++) {
                Article article = tmpList.get(i);
                //id
                id.append("i=" + article.getId() + "&");

                //timestamp
                it.append("it=" + article.getTimestampUsec() + "&");
            }
            id.append(it);
            id.append(URLEncoder.encode("rs=user/06076212413827933855/state/com.google/reading-list&T=" + getToken(), "UTF-8"));

            String unReadItemUrl = "https://www.google.com/reader/api/0/stream/items/contents?ck=" + Utils.getTimestamp() + "&client=scroll";
            String requestPayload = id.toString();
            String unReadItemResponse = HttpHelper.post(unReadItemUrl, requestPayload);
            if (unReadItemResponse != null) {
                JSONTokener jsonParser = new JSONTokener(unReadItemResponse);
                JSONObject obj = (JSONObject) jsonParser.nextValue();
                JSONArray array = obj.getJSONArray("items");
                int size = array.length();
                RssLabLog.debug("reader.api.getUnReadNews.array.size", size);

                for (int i = 0; i < size; i++) {

                    JSONObject o = array.getJSONObject(i);
                    //id
                    String title = o.getString("title");

                    //articleId
                    String articleId = o.getString("id");

                    //RssLabLog.debug("reader.api.getArticle.content.size:", "" + content.length());
                    JSONObject origin = o.getJSONObject("origin");

                    String streamId = origin.getString("streamId");

                    JSONArray alternate = o.getJSONArray("alternate");
                    JSONObject altObj = alternate.getJSONObject(0);
                    String content = null;
                    //summary
                    if (o.has("summary")) {
                        JSONObject summary = o.getJSONObject("summary");
                        content = summary.getString("content");
                        //content
                    } else if (o.has("content")) {
                        JSONObject objContent = o.getJSONObject("content");
                        content = objContent.getString("content");
                    }


                    //href
                    String href = altObj.getString("href");
                    list.add(new Article(tmpList.get(indexStart + i).getId(), tmpList.get(indexStart + i).getTimestampUsec(), articleId, href, streamId, title, content));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RssLabLog.debug("reader.api.getUnReadArticleList.result.list.size", list.size());
        return list;
    }

    private String getToken() {
        String url = "https://www.google.com/reader/api/0/token?ck=" + Utils.getTimestamp() + "&client=Reader";
        String token;
        token = HttpHelper.httpsGet(url);
        RssLabLog.debug("reader.api.getToken", token);
        return token;
    }

    public List<Article> getUnReadArticleIds() {
        RssLabLog.debug("reader.api.getUnReadNews", "start");
        List<Article> list = new ArrayList<Article>();
        String url = "https://www.google.com/reader/api/0/stream/items/ids?s=user/06076212413827933855/state/com.google/reading-list&ot=1339743600&r=a&xt=user/06076212413827933855/state/com.google/read&n=1000&output=json&ck=" + Utils.getTimestamp() + "&client=scroll";
        String response = HttpHelper.httpsGet(url);
        RssLabLog.debug("reader.api.getUnReadNews.response", response);
        try {
            JSONTokener jsonParser = new JSONTokener(response);
            JSONObject obj = null;

            obj = (JSONObject) jsonParser.nextValue();
            JSONArray arr = obj.getJSONArray("itemRefs");
            int length = arr.length();
            RssLabLog.debug("reader.api.getUnReadNews.array.length", String.valueOf(length));

            for (int index = 0; index < length; index++) {

                JSONObject o = arr.getJSONObject(index);
                //id
                String id = o.getString("id");

                //count
                String timestamp = o.getString("timestampUsec");
                list.add(new Article(id, timestamp));

            }
        } catch (Exception e) {
            //do nothing
        }
        RssLabLog.debug("reader.api.getUnReadArticleIds.result.list.size", list.size());
        return list;
    }

    public byte[] getImgAsByteArray(String url) {
        return HttpHelper.getImgAsByteArray(url);
    }

    public void markArticlesAsRead(List<Article> articleList) {
        RssLabLog.debug("reader.api.markArticlesAsRead", "" + articleList.size());

        String url = "https://www.google.com/reader/api/0/edit-tag?client=scroll";
        String requestPayload = "";
        for (Article article : articleList) {
            String articleId = article.getArticleId();
            requestPayload = "a=user/06076212413827933855/state/com.google/read&" +
                    "async=true&" +
                    "s=" + article.getFeedId() + "&" +
                    "i=" + articleId + "&" +
                    "T=" + getToken();
            String response = HttpHelper.post(url, requestPayload);
            RssLabLog.debug("reader.api.markArticlesAsRead", articleId, response);
        }
    }
}

