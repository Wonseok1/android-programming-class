package com.example.raejin.mymovieapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raejin.mymovieapp.R;
import com.example.raejin.mymovieapp.movie_list_item;

import java.util.ArrayList;

/**
 * Created by 501-00 on 2018-03-14.
 */

public class MyMovieAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<movie_list_item> arrayList;
    int item_layout_id;

    public MyMovieAdapter(Context context,
                          ArrayList<movie_list_item> arrayList,
                          int item_layout_id) {

        this.context = context;
        this.arrayList = arrayList;
        this.item_layout_id = item_layout_id;
        this.layoutInflater = (LayoutInflater)context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = layoutInflater.inflate(
                    item_layout_id,
                    viewGroup,
                    false);
        }
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView_item_poster);
        TextView textView = (TextView)view.findViewById(R.id.textView_item_title);

        imageView.setImageResource(arrayList.get(i).getMovie_poster_img());
        textView.setText(arrayList.get(i).getMovie_title());

        return view;
    }
}
