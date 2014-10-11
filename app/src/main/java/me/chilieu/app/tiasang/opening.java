package me.chilieu.app.tiasang;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class opening extends Activity {

    private String start_date;
    private MySQLiteHelper db;
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<ArticleDB>> listDataChild;
    private int lastExpandedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_opening);
        setContentView(R.layout.expandable_listview);
        db = new MySQLiteHelper(opening.this);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                ArticleDB child = (ArticleDB) parent.getExpandableListAdapter().getChild(groupPosition, childPosition);
                //TextView idArticle = (TextView) findViewById(R.id.idArticle);
                //String SendingId = idArticle.getText().toString();
                String SendingId = child.getRealId();
                //Log.d("ID", SendingId);

                //sending id to next activity
                Intent detail = new Intent(opening.this, ArticleDetail.class);
                detail.putExtra("article_id", SendingId);

                startActivity(detail);
                overridePendingTransition(R.animator.push_left_in, R.animator.push_left_out);
                //finish();
                return false;
            }
        });

        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if(groupPosition != previousItem ) expListView.collapseGroup(previousItem);
                previousItem = groupPosition;
            }

        });
    }

    /*
         * Preparing the list data
         */
    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<ArticleDB>>();

        //get start_date
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(opening.this);
        start_date = preferences.getString("start_date","");

        SimpleDateFormat sdfDOM = new SimpleDateFormat("dd");
        SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM-yyyy");
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        Calendar nextYear = Calendar.getInstance();
        nextYear.setTimeZone(TimeZone.getTimeZone("GMT"));
        nextYear.add(Calendar.MONTH, 13);
        Date nextYearDate = nextYear.getTime();

        Date currentDate = new Date();
        try {
            currentDate = sdf.parse(start_date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        long days = printDifferenceDay(currentDate, nextYearDate);
        //Log.d("Days_between", String.valueOf(days));

        Calendar loopMonth = Calendar.getInstance();
        loopMonth.add(Calendar.MONTH,0);
        currentDate = loopMonth.getTime();
        String tmpBefore = sdfMonth.format(currentDate);
        String tmpAfter = sdfMonth.format(currentDate);

        int countDay = 0, countMonth=0;
        ArticleDB item = new ArticleDB();
        List<ArticleDB> artTmp = new ArrayList<ArticleDB>();
        boolean flgAdd = false;

        while(countDay <= 365){//while next year date coming yet!

            if( !tmpBefore.equals(tmpAfter) || countDay == 365 ){//if not current month and total month <= 12 months and last day
                listDataHeader.add(tmpBefore);
                listDataChild.put(listDataHeader.get(countMonth), artTmp);//add to child list

                //reset data
                artTmp = new ArrayList<ArticleDB>();//reset tmp array
                countMonth++;

                //Log.d("Time", tmpBefore + "/" + tmpAfter);
                //Log.d("Add", String.valueOf(countMonth) );
            }

            String dom = sdfDOM.format(currentDate);
            //Log.d("DOM / Month", dom + "/" + tmpBefore );
            try{
                item = db.getArticle(countDay + 1);
                item.setDom(dom);
                artTmp.add(item);
            } catch (Exception e){
                //do nothing
            }

            loopMonth.add(Calendar.HOUR, 24);
            currentDate = loopMonth.getTime();
            tmpBefore = tmpAfter;
            tmpAfter = sdfMonth.format(currentDate);
            //Log.d("Count Day", String.valueOf(countDay));
            //Log.d("Day of Month", dom);
            countDay++;//count up 1 more day
        }

    }

    /**
     *
     * @return current Date from Calendar in dd/MM/yyyy format
     * adding 1 into month because Calendar month starts from zero
     */
    public static String getDate(Calendar cal){
        return "" + cal.get(Calendar.DATE) +"/" +
                (cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.YEAR);
    }


    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public long printDifferenceDay(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        double elapsedDays = (double) Math.ceil((different / daysInMilli) + 0.5);
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        return (long) elapsedDays;
        //System.out.printf("%d days, %d hours, %d minutes, %d seconds%n",elapsedDays,elapsedHours, elapsedMinutes, elapsedSeconds);

    }
}
