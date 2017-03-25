package com.jinwoo.android.mymemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.jinwoo.android.mymemo.data.DBHelper;
import com.jinwoo.android.mymemo.domain.Memo;
import com.jinwoo.android.mymemo.interfaces.DelnUpdInterface;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements DelnUpdInterface {
    private static final int REQUEST_ACT = 100;
    private static final String TAG = "FOREST";
    ListFragmentMemo listMemo;

    FrameLayout frameMemo;
    FragmentManager manager;

    List<Memo> datas = new ArrayList<>();


    DBHelper dbHelper;
    Dao<Memo, Integer> memoDao;

    Spinner spinnerSort;
    FloatingActionButton fab;

    Button btnReset;

    // 검색용
    EditText editSearch;
    Button btnSearch;


    // Spinner에서 사용할 어뎁터와 레이아웃 상태
    ArrayAdapter<String>  spinAdapter;
    List<String> spin = new ArrayList<>();
    int chooseLayout = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setWidget();
        setListner();

        frameMemo = (FrameLayout)findViewById(R.id.frameMemo);
        listMemo = ListFragmentMemo.newInstance(1);
        manager = getSupportFragmentManager();

        try{
            loadData();
        }catch(SQLException e){
            Log.e("TAG",e + "======================");
        }

        listMemo.setData(datas);
        setList();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
            switch(resultCode){
                case RESULT_OK:
                    try {
                         saveToList();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
            }
    }


    private void setWidget(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // 스피너 설정.
        spinnerSort = (Spinner)findViewById(R.id.spinnerSort);
        spin.add("Linear");
        spin.add("Grid");
        spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spin);
        spinnerSort.setAdapter(spinAdapter);
        // 플로팅 버튼 처리
        fab = (FloatingActionButton) findViewById(R.id.fab);
        // 검색 설정.
        editSearch = (EditText)findViewById(R.id.editSearch);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        // Reset
        btnReset = (Button)findViewById(R.id.btnReset);

    }

    private void setListner(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), DetailActivity.class);
                intent.putExtra("UPDATE",String.valueOf(0));
                startActivityForResult(intent, REQUEST_ACT );
            }
        });
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(spin.get(position).equals("Linear")){
                    chooseLayout = 1;
                }else if(spin.get(position).equals("Grid")){
                    chooseLayout = 2;
                }
                updateFragment(chooseLayout);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchStr = editSearch.getText().toString();
                try {
                    loadSpecificMemo(searchStr);
                    updateFragment(chooseLayout);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    loadData();
                    updateFragment(chooseLayout);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void updateFragment(int chooseLayout){
        listMemo = ListFragmentMemo.newInstance(chooseLayout);
        listMemo.setData(datas);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frameMemo, listMemo);
        transaction.commit();
    }


    private void loadSpecificMemo(String searchStr) throws SQLException{
        String query = "Select * From memo where title like '%" + searchStr + "%'";
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        memoDao = dbHelper.getMemoDao();
        GenericRawResults<Memo> rawResults = memoDao.queryRaw(query, memoDao.getRawRowMapper());
        datas = rawResults.getResults();
    }

    private void loadData() throws SQLException {
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        memoDao = dbHelper.getMemoDao();
        datas = memoDao.queryForAll();
    }

    private void setList() {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.frameMemo, listMemo);
        transaction.commit();
    }


    public void saveToList() throws SQLException {
        editSearch.setText("");
        loadData();
        listMemo.setData(datas);
        listMemo.refreshAdapter();
    }


    /*
     클릭한 메모를 리스트에서 지웁니다.
     */
    @Override
    public void deleteToList(int no) {
        dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        try {
            memoDao = dbHelper.getMemoDao();
            memoDao.deleteById(no);
            loadData();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        listMemo.setData(datas);
        listMemo.refreshAdapter();

    }

    /*
      클릭한 메모를 업데이트할 새 창을 띄웁니다.
     */
    @Override
    public void goToUpdate(int no) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("UPDATE",String.valueOf(no));
        startActivityForResult(intent, REQUEST_ACT);

    }
}
