package com.example.raejin.mymovieapp;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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
        if(list.size() % 2 == 0) {
            return list.size() / 2;
        } else {
            return (list.size() / 2) + 1;
        }
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
        final int pos_l;
        final int pos_r;

        pos_l = (i * 2);
        pos_r = (i * 2) + 1;

        if(view == null) {
            view = layoutInflater.inflate(item_layout, viewGroup, false);
        }

        ImageView iv_thumb = (ImageView) view.findViewById(R.id.iv_thumb);
        iv_thumb.setImageResource(list.get(pos_l).getImg_id());

        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        tv_title.setText(list.get(pos_l).getTitle());

        TextView tv_date = (TextView)view.findViewById(R.id.tv_date);
        tv_date.setText(list.get(pos_l).getDate());

        ImageView iv_thumb2 = (ImageView) view.findViewById(R.id.iv_thumb2);
        TextView tv_title2 = (TextView)view.findViewById(R.id.tv_title2);
        TextView tv_date2 = (TextView)view.findViewById(R.id.tv_date2);

        if(pos_r < list.size()) {
            iv_thumb2.setImageResource(list.get(pos_r).getImg_id());
            tv_title2.setText(list.get(pos_r).getTitle());
            tv_date2.setText(list.get(pos_r).getDate());
        } else {
            iv_thumb2.setVisibility(View.GONE);
            tv_title2.setVisibility(View.GONE);
            tv_date2.setVisibility(View.GONE);
        }









        return view;
    }
}
