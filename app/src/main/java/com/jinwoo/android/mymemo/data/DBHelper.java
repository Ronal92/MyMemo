package com.jinwoo.android.mymemo.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jinwoo.android.mymemo.domain.Memo;

import java.sql.SQLException;


/**
 * Created by JINWOO on 2017-02-15.
 */

public class DBHelper extends OrmLiteSqliteOpenHelper{

    public static final String DB_NAME = "mymemo.db";
    public static final int DB_VERSION = 2;

    private Dao<Memo, Integer> memoDao = null;

    public DBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Memo.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Memo.class, false);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Memo, Integer> getMemoDao() throws SQLException{
        if(memoDao == null){
            memoDao = getDao(Memo.class);
        }
        return memoDao;
    }

}
