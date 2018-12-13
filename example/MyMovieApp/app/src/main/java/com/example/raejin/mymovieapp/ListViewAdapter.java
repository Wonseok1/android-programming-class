package com.example.raejin.mymovieapp;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.raejin.mymovieapp.Form.ListViewItem;

import java.util.ArrayList;

/**
 * Created by student on 2018-12-13.
 */

public class ListViewAdapter extends BaseAdapter {
    ArrayList<ListViewItem> list; // 자료를 저장하고 있는 ArrayList
    Context context;
    int item_layout;
    LayoutInflater layoutInflater;
    public ListViewAdapter(
            Context context,
            int item_layout,
            ArrayList<ListViewItem> list) {
        this.context = context;
        this.item_layout = item_layout;
        this.list = list;
        layoutInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final int pos = i;
        if(view == null) {
            view = layoutInflater.inflate(item_layout, viewGroup, false);
        }

        ImageView iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
        iv_thumb.setImageResource(list.get(pos).getImg_id());
        iv_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,
                        "그림을 선택했습니다.",
                        Toast.LENGTH_LONG).show();
            }
        });
        /**
         * 택스트뷰로 제목, 날짜 넣어보기
         */
        return view;
    }
}
