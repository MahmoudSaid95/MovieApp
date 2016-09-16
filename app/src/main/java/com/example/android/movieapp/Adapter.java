package com.example.android.movieapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.android.movieapp.onlineData.Result;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by M.Sa3ed on 8/13/2016.
 */
public class Adapter extends BaseAdapter {
    LayoutInflater inflater;
    Context context;
  List<Result> movies;

    public Adapter(Context context, List<Result> movies) {
        this.context = context;
        this.movies = movies;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Object getItem(int i) {
        return movies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       View view_item =  inflater.inflate(R.layout.poster_item, null);
        ImageView imageView;
        imageView = (ImageView) view_item.findViewById(R.id.poster);
        Result moviedata = movies.get(i);

        Picasso.with(context).load("http://image.tmdb.org/t/p/w185"+ moviedata.getPosterPath()).into(imageView);
        return view_item;

    }
}