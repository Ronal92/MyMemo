package com.jinwoo.android.mymemo.interfaces;

import com.jinwoo.android.mymemo.domain.Memo;

import java.sql.SQLException;

/**
 * Created by JINWOO on 2017-02-16.
 */

public interface DelnUpdInterface {
    public void deleteToList(int no);
    public void goToUpdate(int no);
}
