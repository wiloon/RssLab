package com.wiloon.android.rsslab.common;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/8/12
 * Time: 2:36 PM
 */
public class AppConstant {
    public final static String DB_NAME = "RssLab";
    public final static String TABLE_NAME_TAG = "tag";
    public final static String TABLE_NAME_FEED = "feed";
    public final static String TABLE_NAME_FEED_TAGS = "feedCategories";
    public final static String TABLE_NAME_ARTICLE = "article";
    public final static String TABLE_NAME_ARTICLE_IMAGE = "articleImage";
    public final static String TABLE_COLUM_ARTICLE_READ_STATUS = "readStatus";
    public final static String TABLE_COLUM_ARTICLE_ONLINE_STATUS = "onlineStatus";

    public final static String TABLE_NAME_UNREAD_COUNT = "unread_count";
    public final static String GOOGLE_READER_URL_SUBSCRIPTION = "https://www.google.com/reader/api/0/subscription/list?output=json";
    public final static String GOOGLE_READER_URL_TAG = "https://www.google.com/reader/api/0/tag/list?output=json";
    public final static String GOOGLE_READER_UNREAD = "https://www.google.com/reader/api/0/unread-count?output=json";

    public final static String GOOGLE_ITEM_TYPE_TAG = "TAG";
    public final static String GOOGLE_ITEM_TYPE_FEED = "FEED";

    public final static String TABLE_COLUMN_ID = "id";
    public final static String TABLE_COLUMN_UNREAD_COUNT = "unReadCount";
    public final static String TABLE_COLUMN_NEWEST_ITEM_TIMESTAMP = "newestItemTimestamp";

    public final static String LIST_VIEW_ID_ITEM_LABEL = "ItemLabel";
    public final static String LIST_VIEW_ID_UNREAD_COUNT = "UnReadCount";
    public final static String LIST_VIEW_feed_LABEL_ID = "FeedLabel";
    public final static String LIST_VIEW_feed_UNREAD_COUNT_ID = "FeedCount";
    public final static String MESSAGE_TYPE_TAG_ID = "com.wiloon.android.rsslab.message.tagId";
    public final static String MESSAGE_TYPE_FEED_ID = "com.wiloon.android.rsslab.message.feedId";
    public final static String MESSAGE_TYPE_Article_ID = "com.wiloon.android.rsslab.message.article.id";
    public final static String MESSAGE_TYPE_ARTICLE_FEED_ID = "com.wiloon.android.rsslab.message.article.feedId";


    public final static String ARTICLE_STATUS_READ = "READ";
    public final static String ARTICLE_STATUS_UNREAD = "UNREAD";

    public final static String MIME_TYPE = "text/html";
    public final static String ENCODING_UTF8 = "UTF-8";
    public final static String ENCODING_GBK = "gbk";

    public final static String ARTICLE_IMAGE_STATUS_NEW = "NEW";
    public final static String ARTICLE_IMAGE_STATUS_SYNC = "SYNC";
    public final static String ARTICLE_ONLINE_STATUS_ONLINE = "ONLINE";
    public final static String ARTICLE_ONLINE_STATUS_OFFLINE = "OFFLINE";


    public final static String IMG_FILE_BASE_PATH = "/sdcard/0_myDoc/image/rssLab";
    public final static String LOG_SEPARATOR = " ";
    public final static String LOG_PREFIX_DEBUG = "WILOON.DEBUG";
    public final static String LOG_PREFIX_INFO = "WILOON.INFO";
    public final static String ONLINE_TAG_ID = "user/06076212413827933855/label/Online";


    public final static String FILTER_PATTERN_INFOZM = "文章内容.*(<section id=\"articleContent\">.*/section>)";
    public final static String FILTER_PATTERN_FT = "id=\"bodytext\".*?((<p>.*?</p>){1,}(<div>.*</div>){0,1}(<p>.*?</p>){1,}).*?</div>";
    public final static String TAG_ID_ALL_ITEMS = "ALLITEMS";
}
