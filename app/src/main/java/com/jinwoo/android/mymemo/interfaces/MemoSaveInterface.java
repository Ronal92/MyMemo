package com.jinwoo.android.mymemo.interfaces;

/**
 *   DetailActivity에서 implements 합니다.
 *   메모 리스트가 보여질 MainActivity와 메모가 쓰여질 WriteFragment사이를 연결하는 통신입니다.
 *   Created by JINWOO on 2017-02-15.
 */

public interface MemoSaveInterface {
    public void saveFromFragment();
}
