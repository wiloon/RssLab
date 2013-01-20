package com.wiloon.android.rsslab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.wiloon.android.rsslab.beans.Article;
import com.wiloon.android.rsslab.utils.RssLabLog;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: wiloon
 * Date: 7/15/12
 * Time: 10:42 AM
 */
public class ArticleListAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List values;

    public ArticleListAdapter(Context context, List values) {
        super(context, R.layout.article_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RssLabLog.debug("ArticleListAdapter.getView.", "" + position);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.article_list_item, parent, false);
        TextView labelView = (TextView) rowView.findViewById(R.id.ItemLabel);

        if (labelView != null) {
            Article article = (Article) values.get(position);
            labelView.setText(article.getTitle());
        }

        return rowView;
    }
}
