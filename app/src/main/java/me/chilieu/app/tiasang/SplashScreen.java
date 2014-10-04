package me.chilieu.app.tiasang;
import me.chilieu.app.tiasang.util.SystemUiHider;

import com.google.android.gms.common.GooglePlayServicesUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class SplashScreen extends Activity {

    private MySQLiteHelper db;

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //ALERT MESSAGE
        //Toast.makeText(getBaseContext(),"Please wait, Loading new articles...", Toast.LENGTH_LONG).show();

        TextView headlineStart = (TextView) findViewById(R.id.textHeadlineStart);
        headlineStart.setText(Html.fromHtml("Ngài đánh thức con mỗi buổi sớm mai, <br>đánh thức tai con để nghe lời Ngài dạy, <br> như học trò vậy.<br>Ê-sai 50:4b"));

        db = new MySQLiteHelper(this);
        new PrefetchData().execute();


    }

    /**
     * Async Task to make http call
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // before making http calls
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            /*
             * Will make http call here This call will download required data
             * before launching the app
             * example:
             * 1. Downloading and storing in SQLite
             * 2. Downloading images
             * 3. Fetching and parsing the xml / json
             * 4. Sending device information to server
             * 5. etc.,
             */
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String json;
            String jsonConfig;
            //db.deleteAllArticle();
            if(db.totalRows() <= 0){//if data already in database
                json = sh.makeServiceCall("http://app.chilieu.me/tiasang/data.php", ServiceHandler.GET);
            } else {
                try {
                    // Thread will sleep for 5 seconds
                    Thread.sleep(4*1000);
                } catch (Exception e) {

                }
                return null;
            }

            Log.e("Response: ", "> done");
            int id;
            String title;
            String content;
            String headline;
            int status;
            ArticleDB article;
            if (json != null) {
                try {
                    JSONArray jArr = new JSONArray(json);
                    db.deleteAllArticle();
                    for (int i = 0; i < jArr.length(); i++) {
                        JSONObject jObj = jArr.getJSONObject(i);
                        //title = "Tia Sáng " + jObj.getString("id");
                        title = jObj.getString("title");
                        content = jObj.getString("content");
                        headline = jObj.getString("headline");
                        status = Integer.parseInt(jObj.getString("status"));
                        article = new ArticleDB(1, title, content, headline, status);
                        db.addArticle(article);
                        //Log.e("JSON", "> " + title);
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else {
                //ALERT MESSAGE
                Toast.makeText(getBaseContext(),"Cannot connect to server ... ", Toast.LENGTH_LONG).show();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // After completing http call
            // will close this activity and lauch main activity
            //hide loadingPanel when loaded
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.loadingPanelText).setVisibility(View.GONE);

            Intent i = new Intent(SplashScreen.this, opening.class);
            startActivity(i);
            // close this activity
            finish();
        }



    }
}
