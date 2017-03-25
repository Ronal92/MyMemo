package com.jinwoo.android.mymemo;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.jinwoo.android.mymemo.data.DBHelper;
import com.jinwoo.android.mymemo.domain.Memo;
import com.jinwoo.android.mymemo.interfaces.MemoSaveInterface;

import java.sql.SQLException;


/**
 * A simple {@link Fragment} subclass.
 */
public class WriteFragment extends Fragment implements View.OnClickListener{

    private static final String ARG_POSITION= "100";
    private static final String ARG_TITLE= "101";
    private static final String ARG_CONTENTS= "102";

    View view;
    Context context;

    Memo memo;
    DBHelper dbHelper;
    Dao<Memo, Integer> memoDao;

    Button btnSave;
    EditText editTitle, editContents;


    MemoSaveInterface memoSaveInterface;

    private int position = 0;
    private String titleUpdate = null;
    private String contentsUpdate = null;

    public WriteFragment() {
        // Required empty public constructor
    }

    public static WriteFragment newInstance(int position, String upTitle, String upContents){
        WriteFragment wrtFragment = new WriteFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TITLE, upTitle);
        args.putString(ARG_CONTENTS, upContents);
        wrtFragment.setArguments(args);
        return wrtFragment;
    }




    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
        this.memoSaveInterface = (MemoSaveInterface) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            titleUpdate = getArguments().getString(ARG_TITLE);
            contentsUpdate = getArguments().getString(ARG_CONTENTS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_write, container, false);
            editTitle = (EditText)view.findViewById(R.id.editTitle);
            editContents = (EditText)view.findViewById(R.id.editContents);
            btnSave = (Button)view.findViewById(R.id.btnSave);
            btnSave.setOnClickListener(this);
        }
        if(titleUpdate != null && contentsUpdate != null) {
            editTitle.setText(titleUpdate);
            editContents.setText(contentsUpdate);
        }

        return view;
    }

    /**
     *  저장(save) / 수정(update) 구분해서 처리합니다.
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSave:
                if( titleUpdate == null && contentsUpdate == null ){
                    save();
                } else {
                    saveForUpdate();
                }
                memoSaveInterface.saveFromFragment();
                break;

        }
    }

    public void save(){
        memo = new Memo();
        String title = editTitle.getText().toString();
        String contents = editContents.getText().toString();
        memo.setTitle(title);
        memo.setContents(contents);
        dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        try {
            memoDao = dbHelper.getMemoDao();
            memoDao.create(memo);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveForUpdate(){
        String title = editTitle.getText().toString();
        String contents = editContents.getText().toString();
        dbHelper = OpenHelperManager.getHelper(context, DBHelper.class);
        try {
            memoDao = dbHelper.getMemoDao();
            memo = memoDao.queryForId(position);
            memo.setTitle(title);
            memo.setContents(contents);
            memoDao.update(memo);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
