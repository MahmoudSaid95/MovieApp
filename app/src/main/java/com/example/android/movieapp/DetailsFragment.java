package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.movieapp.Review.Result;
import com.example.android.movieapp.Review.Review;
import com.example.android.movieapp.Trailers.Trailers;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsFragment extends Fragment {

    static ListView reviewlist;
    static ListView trailerlist;
    static TextView title;
    static ImageView poster;
    static TextView year;
    static TextView rate;
    static Context context;
    static Button favoriteButton;
    static List<String> trailerkey;
    static com.example.android.movieapp.onlineData.Result moviedata;
    View view;

    public DetailsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_details, container, false);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        title = (TextView) view.findViewById(R.id.movie_name);

        year = (TextView) view.findViewById(R.id.movie_year);

        rate = (TextView) view.findViewById(R.id.movie_rate);
        context = getContext();
        poster = (ImageView) view.findViewById(R.id.movie_poster);
        reviewlist = (ListView) view.findViewById(R.id.Movie_overview);
        trailerlist = (ListView) view.findViewById(R.id.Movie_trailers);
        favoriteButton = (Button) view.findViewById(R.id.favBtn);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseAdapter Adapter = new DatabaseAdapter(context);
                Adapter.getData(moviedata.getId() + "");
                if (MainActivity.favStr.equals("false")) {
                    if (!DatabaseAdapter._id) {
                        long insrt = Adapter.insertData(moviedata.getId() + "", moviedata.getPosterPath(), moviedata.getTitle(), moviedata.getReleaseDate(), moviedata.getVoteAverage() + "", "");
                        if (insrt != -1) {
                            Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(context, "Error In Insert Data", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "Favorite Already", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        trailerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(Intent.ACTION_VIEW);

                intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerkey.get(i)));

                startActivity(intent);
            }
        });
        if (MainActivity.state.equals("normal")) {
            setObjects();
        }
    }

    public static void setObjects() {
        getReview("http://api.themoviedb.org/3/movie/" + moviedata.getId() + "/reviews?api_key=9490ec35a6eea2efe32378982073f7a3");
        title.setText(moviedata.getTitle());
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185" + moviedata.getPosterPath()).into(poster);
        year.setText(moviedata.getReleaseDate().substring(0,4));

        rate.setText(moviedata.getVoteAverage() + "/10");
        getTrailer("http://api.themoviedb.org/3/movie/" + moviedata.getId() + "/videos?api_key=9490ec35a6eea2efe32378982073f7a3");

    }

    public static void getReview(final String url) {
        final RequestQueue queue = Volley.newRequestQueue(context);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        final List<Result> review;
                        Review reviews;
                        Gson g = new Gson();
                        reviews = g.fromJson(response, Review.class);
                        review = reviews.getResults();
                        List<String> reviewContent = new ArrayList<>();
                        for (int i = 0; i < review.size(); i++) {
                            reviewContent.add(review.get(i).getContent());

                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, reviewContent);
                        reviewlist.setAdapter(arrayAdapter);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    public static void getTrailer(final String url) {
        final RequestQueue queue = Volley.newRequestQueue(context);

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        final List<com.example.android.movieapp.Trailers.Result> trailer;
                        Trailers trailers;
                        Gson g = new Gson();
                        trailers = g.fromJson(response, Trailers.class);
                        trailer = trailers.getResults();

                        trailerkey = new ArrayList<>();
                        List<String> trailername = new ArrayList<>();
                        for (int i = 0; i < trailer.size(); i++) {
                            trailerkey.add(trailer.get(i).getKey());
                            trailername.add("Trailer " + (i+1));
                        }

                        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, trailername);
                        trailerlist.setAdapter(arrayAdapter);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        queue.add(stringRequest);
    }
}
