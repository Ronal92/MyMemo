package com.jinwoo.android.mymemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinwoo.android.mymemo.domain.Memo;
import com.jinwoo.android.mymemo.interfaces.DelnUpdInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JINWOO on 2017-02-15.
 */

public class ListAdapterMemo extends RecyclerView.Adapter<ListAdapterMemo.ViewHolder> {

    Context context;
    DelnUpdInterface deleteInterface;
    DelnUpdInterface updateInterface;
    List<Memo> datas = new ArrayList<>();



    public ListAdapterMemo(Context context, List<Memo> datas){
        this.context = context;
        this.deleteInterface  = (DelnUpdInterface)context;
        this.updateInterface = (DelnUpdInterface)context;
        this.datas = datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_memo_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Memo memo = datas.get(position);
        holder.txtMemo.setText(memo.getTitle());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView txtMemo;
        public ViewHolder(View view) {
            super(view);
            txtMemo = (TextView)view.findViewById(R.id.txtMemo);

            txtMemo.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int no = 0;
                    String deleteStr = txtMemo.getText().toString();
                    for(Memo item : datas){
                        if(item.getTitle().equals(deleteStr)){
                            no = item.getId();
                        }
                    }
                    deleteInterface.deleteToList(no);
                    return true;
                }
            });

            txtMemo.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int no = 0;
                    String deleteStr = txtMemo.getText().toString();
                    for(Memo item : datas){
                        if(item.getTitle().equals(deleteStr)){
                            no = item.getId();
                        }
                    }
                    updateInterface.goToUpdate(no);
                }
            });
        }
    }
}
