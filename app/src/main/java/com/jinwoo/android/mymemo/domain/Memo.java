package com.jinwoo.android.mymemo.domain;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by JINWOO on 2017-02-15.
 */

@DatabaseTable(tableName = "memo")
public class Memo {

    @DatabaseField(generatedId = true)
    int id;
    @DatabaseField
    String title;
    @DatabaseField
    String contents;
    @DatabaseField
    Date date;

    public Memo(){}

    public Memo(String title, String contents){
        this.title = title;
        this.contents = contents;
        this.date = new Date(System.currentTimeMillis());
    }

    public int getId() {
        return id;
    }

     public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public Date getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}
