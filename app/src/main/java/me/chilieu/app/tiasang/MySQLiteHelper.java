package me.chilieu.app.tiasang;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Thy on 9/23/2014.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "tiasang";

    // Books table name
    private static final String TABLE_NAME = "articles";

    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_HEADLINE= "headline";
    private static final String KEY_STATUS= "status";

    private static final String[] COLUMNS = {KEY_ID, KEY_TITLE, KEY_CONTENT, KEY_HEADLINE, KEY_STATUS};

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create book table
        String CREATE_BOOK_TABLE = "CREATE TABLE "+TABLE_NAME+" ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, "+
                "content TEXT, "+
                "headline TEXT, "+
                "status INTEGER )";

        // create books table
        db.execSQL(CREATE_BOOK_TABLE);
        //for logging
        Log.d("Create Table", "CREATE TABLE articles");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older books table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        //for logging
        Log.d("Drop Table", "DROP TABLE IF EXISTS " + TABLE_NAME);
        // create fresh books table
        this.onCreate(db);
        //db.close();//close database connection
    }

    public int totalRows(){
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        db.close();//close database connection
        return cnt;
    }

    public void deleteAllArticle(){
        SQLiteDatabase db= this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        Log.d("deleteAllArticle", "TRUNCATE " + TABLE_NAME);
        db.close();//close database connection
    }

    public void addArticle(ArticleDB article){
        //for logging
        //Log.d("addArticle", article.toString());

        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, article.getTitle()); // get title
        values.put(KEY_CONTENT, article.getContent()); // get content
        values.put(KEY_HEADLINE, article.getHeadline()); // get headline
        values.put(KEY_STATUS, 0); // get status

        // 3. insert
        db.insert(TABLE_NAME, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        db.close();
    }

    public ArticleDB getArticle(int id){

        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. build query
        Cursor cursor =
                db.query(TABLE_NAME, // a. table
                        COLUMNS, // b. column names
                        " id = ?", // c. selections
                        new String[] { String.valueOf(id) }, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();

        // 4. build book object
        ArticleDB article = new ArticleDB(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),Integer.parseInt(cursor.getString(4)));

        //log
        //Log.d("getArticle("+id+")", article.toString());
        db.close();
        // 5. return book
        return article;
    }

    public ArrayList<ArticleDB> getAllArticle() {
        ArrayList<ArticleDB> articles = new ArrayList<ArticleDB>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_NAME;

        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // 3. go over each row, build book and add it to list
        ArticleDB article = null;
        if (cursor.moveToFirst()) {
            do {
                article = new ArticleDB(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3),Integer.parseInt(cursor.getString(4)));

                // Add article to articles
                articles.add(article);
            } while (cursor.moveToNext());
        }

        //Log.d("getAllArticles()", articles.toString());
        db.close();
        // return books
        return articles;
    }

}
