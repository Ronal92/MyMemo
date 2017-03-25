package com.jinwoo.android.mymemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinwoo.android.mymemo.domain.Memo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JINWOO on 2017-02-15.
 */

public class ListFragmentMemo extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_LAYOUT = "layout";

    private int chooseLayout = 1;

    Context context;
    View view;
    RecyclerView recyclerView;
    ListAdapterMemo listAdapterMemo;

    List<Memo> datas =  new ArrayList<>();

    public ListFragmentMemo(){}

    public static ListFragmentMemo newInstance(int chooseLayout){
        ListFragmentMemo fragment = new ListFragmentMemo();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT, chooseLayout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        this.context = context;
    }
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        if(getArguments() != null){
            chooseLayout = getArguments().getInt(ARG_LAYOUT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        if(view != null){
            return view;
        }
        view = inflater.inflate(R.layout.list_memo, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);

        listAdapterMemo = new ListAdapterMemo(context, datas);
        recyclerView.setAdapter(listAdapterMemo);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        return view;

    }
    public void setData(List<Memo> datas) {
        this.datas = datas;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(chooseLayout == 1){
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else if( chooseLayout ==2 ){
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void refreshAdapter() {
        listAdapterMemo = new ListAdapterMemo(context, datas);
        recyclerView.setAdapter(listAdapterMemo);
        listAdapterMemo.notifyDataSetChanged();
    }


}
