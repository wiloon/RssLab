package com.wiloon.android.rsslab;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.widget.*;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/8/12
 * Time: 9:14 AM
 */
public class MyHandler extends Handler {

    private static Activity activity;

    private MyHandler() {
        RssLabLog.debug("my_handler.constructor", "");
    }

    public static MyHandler instance;

    public static MyHandler getInstance(Looper looper, Activity act) {
        if (instance == null) {
            instance = new MyHandler(looper);
            activity = act;
        }
        return instance;
    }

    public static MyHandler getInstance() {
        if (instance != null) {
            return instance;
        }
        return null;
    }

    public MyHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(Message msg) {

        RssLabLog.debug("myHandler.handleMessage!!!!", msg.toString());

        List tagList = (List) msg.obj;
        RssLabLog.info("myHandler.list.size", "" + tagList.size());
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout parent = (LinearLayout) layoutInflater.inflate(R.layout.main, null);
        ListView listView = (ListView) parent.findViewById(R.id.ReaderListView);
        UnReadArrayAdapter adapter = new UnReadArrayAdapter(activity, tagList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new TagOnClickListener(activity));
        parent.removeView(listView);
        activity.setContentView(listView);
    }
}