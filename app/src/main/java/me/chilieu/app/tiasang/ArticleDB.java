package me.chilieu.app.tiasang;

import static java.lang.String.format;

/**
 * Created by Thy on 9/23/2014.
 */
public class ArticleDB {

    private int id;
    private String title;
    private String content;
    private String headline;
    private int status;
    private String dom;

    public ArticleDB(){}

    public ArticleDB(int id, String title, String content, String headline, int status){
        super();
        this.id = id;
        this.title = title;
        this.content = content;
        this.headline = headline;
    }

    @Override
    public String toString(){
        return "Article [" + id + "]=" + title;
    }
    public ArticleDB getObjByID(int id){
        ArticleDB item = new ArticleDB();
        return item;
    }
    public String getId(){
        return format("%03d", this.id);
    }
    public String getRealId(){
        return String.valueOf(this.id);
    }
    public String getTitle(){
        return this.title;
    }
    public String getContent(){
        return this.content;
    }
    public String getHeadline(){
        return this.headline;
    }
    public String getDom(){return this.dom;}
    public void setDom(String dom){ this.dom = dom;}
}
