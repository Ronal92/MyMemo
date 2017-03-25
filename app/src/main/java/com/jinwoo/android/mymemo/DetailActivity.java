package com.jinwoo.android.mymemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.jinwoo.android.mymemo.data.DBHelper;
import com.jinwoo.android.mymemo.domain.Memo;
import com.jinwoo.android.mymemo.interfaces.MemoSaveInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by JINWOO on 2017-02-15.
 */

public class DetailActivity extends AppCompatActivity implements MemoSaveInterface {

    // 뷰페이저 참조변수 생성
    ViewPager viewPager;


    List<Memo> datas = new ArrayList<>();

    DBHelper dbHelper;
    Dao<Memo, Integer> memoDao;

    // 탭 및 페이저 속성을 정의
    final int TAB_COUNT = 2;
    WriteFragment writeFragment;
    WebViewFragment webViewFragment;

    // 업데이트할 때 넘겨줄 값
    int position;
    String upTitle, upContents;

    //현재 페이지
    private int page_position = 0;
    // 페이지 이동 경로를 저장하는 스택 변수
    private Stack<Integer> pageStack = new Stack<>();
    boolean backPress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = bundle.getString("UPDATE");
        int no = Integer.parseInt(str);

        try {
            loadDataForUpdate(no);
        } catch (SQLException e) {
            e.printStackTrace();
        }




        // 웹뷰 프래그먼트 초기화!
        writeFragment = WriteFragment.newInstance(position ,upTitle, upContents);
        webViewFragment = new WebViewFragment();
        // 탭 Layout 정의
        TabLayout tabLayout = (TabLayout)findViewById(R.id.tab);
        // 탭 생성 및 타이틀 입력
        tabLayout.addTab(tabLayout.newTab().setText("메모장"));
        tabLayout.addTab(tabLayout.newTab().setText("Google"));

        // 프래그먼트 페이저 작성
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // 페이저어뎁터 생성.
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        // 페이저어뎁터 세팅.
        viewPager.setAdapter(adapter);

        // 페이저 리스너.
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                if(!backPress){
                    pageStack.push(page_position);
                } else {
                    backPress = false;
                }
                // 이전페이지까지 정보 저장.
                page_position = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // 탭 리스너
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

    }

    /**
     *  업데이트할 메모의 번지수를 DB에서 찾는다.
     *  @param no
     *  @throws SQLException
     */
    private void loadDataForUpdate(int no) throws SQLException {
        if(no != 0) {
            dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
            memoDao = dbHelper.getMemoDao();
            datas = memoDao.queryForAll();
            Memo memo = memoDao.queryForId(no);
            position = memo.getId();
            upTitle = memo.getTitle();
            upContents = memo.getContents();
        }
    }


    @Override
    public void saveFromFragment() {
        Intent intent = new Intent(this.getIntent());
        setResult(RESULT_OK, intent);
        finish();
    }

    class PagerAdapter extends FragmentStatePagerAdapter{

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch(position){
                case 0 : fragment = writeFragment;
                    break;
                case 1 : fragment = webViewFragment;
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }

    @Override
    public void onBackPressed(){
        switch(page_position){
            case 1:
                if(webViewFragment.goBack()){
                }else{
                    goBackStack();
                }
                break;
            default:
                goBackStack();
                break;
        }
    }

    private void goBackStack() {
        if(pageStack.size() < 1){
            super.onBackPressed();
        } else {
            backPress = true;
            viewPager.setCurrentItem(pageStack.pop());
        }
    }


}
