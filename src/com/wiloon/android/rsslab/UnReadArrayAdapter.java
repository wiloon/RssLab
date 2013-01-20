package com.wiloon.android.rsslab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.wiloon.android.rsslab.beans.UnReadCount;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/15/12
 * Time: 10:42 AM
 */
public class UnReadArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List values;

    public UnReadArrayAdapter(Context context, List values) {
        super(context, R.layout.unread_item, values);
        this.context = context;
        this.values = values;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.unread_item, parent, false);
        TextView labelView = (TextView) rowView.findViewById(R.id.ItemLabel);
        // labelView.setFocusableInTouchMode(true);
        TextView unReadCountView = (TextView) rowView.findViewById(R.id.UnReadCount);
        // unReadCountView.setFocusableInTouchMode(true);

        if (labelView != null && unReadCountView != null) {
            UnReadCount unReadCount = (UnReadCount) values.get(position);
            labelView.setText(unReadCount.getLabel());
            unReadCountView.setText(unReadCount.getUnReadCount());
            //RssLabLog.debug("unReadAdapter.setView.", unReadCount.getLabel(), "/", unReadCount.getUnReadCount());

        }


        return rowView;
    }
}
