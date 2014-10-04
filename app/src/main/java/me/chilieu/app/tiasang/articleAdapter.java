package me.chilieu.app.tiasang;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Thy on 9/24/2014.
 */
public class articleAdapter extends ArrayAdapter<ArticleDB> {
    Context context;
    int layoutResourceId;
    List<ArticleDB> article = null;
    public articleAdapter(Context context, int layoutResourceId, List<ArticleDB> article){
        super(context, layoutResourceId, article);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.article = article;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        ArticleHolder holder = null;
        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new ArticleHolder();
            holder.title = (TextView)row.findViewById(R.id.txtTitle);
            holder.headline = (TextView)row.findViewById(R.id.txtHeadline);
            holder.idArticle = (TextView)row.findViewById(R.id.idArticle);

            row.setTag(holder);
        } else {
            holder = (ArticleHolder)row.getTag();
        }
        ArticleDB articledb = article.get(position);
        holder.title.setText(articledb.getId());
        holder.headline.setText(articledb.getTitle());
        //holder.headline.setText(articledb.getHeadline());
        holder.idArticle.setText(articledb.getRealId());
        return row;
    }


    static class ArticleHolder
    {
        TextView title;
        TextView headline;
        TextView idArticle;
    }
}
