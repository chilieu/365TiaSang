package me.chilieu.app.tiasang;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;


public class opening extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        MySQLiteHelper db = new MySQLiteHelper(this);
        ArrayList<ArticleDB> articles = db.getAllArticle();
        ListView listArticle = (ListView)findViewById(R.id.listArticles);

        articleAdapter adapter = new articleAdapter(this, R.layout.listview_item_row, articles);
        listArticle.setAdapter(adapter);

        listArticle.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView idArticle = (TextView) view.findViewById(R.id.idArticle);
                String SendingId = idArticle.getText().toString();
                //Toast.makeText(getBaseContext(),"Your Listener Works! " + idArticle.getText().toString() + " id:" + id,Toast.LENGTH_SHORT).show();

                //sending id to next activity
                Intent detail = new Intent(opening.this, ArticleDetail.class);
                detail.putExtra("article_id", SendingId);
                startActivity(detail);
                overridePendingTransition(R.animator.push_left_in, R.animator.push_left_out);
                finish();
            }


        });

    }

}
