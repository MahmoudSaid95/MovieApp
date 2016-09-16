package com.example.android.movieapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.android.movieapp.onlineData.Data;
import com.example.android.movieapp.onlineData.Result;
import com.google.gson.Gson;

import java.util.List;

public class MainActivity extends AppCompatActivity implements Callback {
    WindowManager windowManager;
    Display display;
    TelephonyManager manager ;
    static String state = "";
    DatabaseAdapter databaseAdapter;
    Adapter adapter;
    GridView gridView;
    static String favStr="";
    static String LastState="";

    MainActivityFragment mainActivityFragment;
    View fragmentdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = (GridView) findViewById(R.id.maingrid);
        windowManager = getWindowManager();

        display = windowManager.getDefaultDisplay();
        manager = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (display.getWidth() > display.getHeight() || (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)) {

            fragmentdetail = findViewById(R.id.fragmentdetail);
            fragmentdetail.setVisibility(View.INVISIBLE);
            state = "land";

        } else {
            state = "normal";
        }


        if (LastState.equals("top_rated")){
            vollycall("http://api.themoviedb.org/3/movie/top_rated?api_key=9490ec35a6eea2efe32378982073f7a3");
        }
        else if (LastState.equals("favorite")){
            databaseAdapter = new DatabaseAdapter(getBaseContext());
            adapter = new Adapter(getBaseContext(), databaseAdapter.getAllData());
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    getData(databaseAdapter.getAllData().get(i));
                }
            });
            favStr = "true";
        }
        else {
            vollycall("http://api.themoviedb.org/3/movie/popular?api_key=9490ec35a6eea2efe32378982073f7a3");
        }

        mainActivityFragment = new MainActivityFragment();

    }

    @Override
    protected void onStart() {
        super.onStart();
        windowManager = getWindowManager();
        display = windowManager.getDefaultDisplay();
        manager = (TelephonyManager)getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);

        if (display.getWidth() > display.getHeight() || (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)) {

            fragmentdetail = findViewById(R.id.fragmentdetail);
            fragmentdetail.setVisibility(View.INVISIBLE);
            state = "land";

        } else {
            state = "normal";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.most_popular) {
            vollycall("http://api.themoviedb.org/3/movie/popular?api_key=9490ec35a6eea2efe32378982073f7a3");
            LastState = "";
        } else if (id == R.id.top_rated) {
            vollycall("http://api.themoviedb.org/3/movie/top_rated?api_key=9490ec35a6eea2efe32378982073f7a3");
            LastState = "top_rated";
        }else if (id == R.id.fav){
            databaseAdapter = new DatabaseAdapter(getBaseContext());
            adapter = new Adapter(getBaseContext(), databaseAdapter.getAllData());
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    getData(databaseAdapter.getAllData().get(i));
                }
            });
            favStr = "true";
            LastState = "favorite";
        }

        return super.onOptionsItemSelected(item);
    }

    public void vollycall(String url) {
        final RequestQueue queue = Volley.newRequestQueue(getBaseContext());

        final StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<Result> movie;
                        Data data;
                        Gson g = new Gson();
                        data = g.fromJson(response, Data.class);
                        movie = data.getResults();
                        adapter = new Adapter(getBaseContext(), movie);
                        gridView.setAdapter(adapter);
                        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                getData((Result) gridView.getAdapter().getItem(i));
                            }
                        });
                        favStr = "false";
                        //        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        queue.add(stringRequest);
    }
    @Override
    public void getData(Result moviedata) {
        if (display.getWidth() > display.getHeight()|| (manager.getPhoneType() == TelephonyManager.PHONE_TYPE_NONE)) {
            DetailsFragment.moviedata = moviedata;
            fragmentdetail.setVisibility(View.VISIBLE);
            DetailsFragment.setObjects();

        } else {
            Intent intent = new Intent(MainActivity.this, Details.class);
            DetailsFragment.moviedata = moviedata;

            startActivity(intent);



        }

   }
}
