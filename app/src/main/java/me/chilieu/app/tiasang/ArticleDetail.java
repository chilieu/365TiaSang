package me.chilieu.app.tiasang;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class ArticleDetail extends Activity {

    private AdView adView;
    private String AD_UNIT_ID = "ca-app-pub-0760902944328301/8167414821";
    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        //display up button level
        getActionBar().setDisplayHomeAsUpEnabled(true);

        MySQLiteHelper db = new MySQLiteHelper(this);
        //getting id from previous activity
        Intent i = getIntent();
        String articleID = i.getStringExtra("article_id");
        ArticleDB article = new ArticleDB();
        article = db.getArticle(Integer.parseInt(articleID));
        //get and set headline
        TextView headline = (TextView) findViewById(R.id.ArticleHeadline);
        headline.setText(Html.fromHtml(article.getHeadline()));

        //get and set content
        TextView content = (TextView) findViewById(R.id.ArticleContent);
        content.setText(Html.fromHtml(article.getContent()));

        //set title bar text
        //getActionBar().setTitle("Tia Sáng " + article.getId());
        getActionBar().setTitle(article.getTitle());


        //GOOGLE ADS HERE
        //check google play services
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == resultCode) {
            Log.d("Location Updates", "Google Play services is available.");

            adView = new AdView(this);
            adView.setAdSize(AdSize.SMART_BANNER);
            adView.setAdUnitId(AD_UNIT_ID);

            // Add the AdView to the view hierarchy. The view will have no size
            // until the ad is loaded. This code assumes you have a LinearLayout with
            // attribute android:id="@+id/linear_layout" in your activity_main.xml.
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.Relative_Layout);
            layout.addView(adView);

            // Create an ad request. Check logcat output for the hashed device ID to
            // get test ads on a physical device.
            AdRequest adRequest = new AdRequest.Builder()
                    .addKeyword("youth christian").addKeyword("christian")
                    //.addTestDevice("1C0F88FD72B3E9290B8A5E1395EED2D9")
                    .build();

            // Start loading the ad in the background.
            adView.loadAd(adRequest);

        } else {
            //---Google Play services was not available for
            // some reason---
            Log.d("Location Updates", "Google Play services is not available.");
        }


    }

    public void showInterstitialAds(){

        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId(AD_UNIT_ID);

        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Begin loading your interstitial.
        interstitial.loadAd(adRequest);
        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // TODO Auto-generated method stub
                super.onAdLoaded();
                if(interstitial.isLoaded()) {
                    interstitial.show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        showInterstitialAds();
        //sending id to next activity
        Intent listArticle = new Intent(ArticleDetail.this, opening.class);
        startActivity(listArticle);
        overridePendingTransition(R.animator.push_right_in, R.animator.push_right_out);//back to home
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                // app icon in action bar clicked; goto parent activity.
                //overridePendingTransition(R.animator.push_right_in, R.animator.push_right_out);//back to home
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
