package com.example.NewsApp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.NewsApp.Modles.NewsApiResponse;
import com.example.NewsApp.Modles.NewsHeadlines;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectListener, View.OnClickListener {
    RecyclerView recyclerView;
    CustomAdapter adapter;
    ProgressDialog dialog;
    Button b1,b2,b3,b4,b5,b6,b7;
    SearchView searchView;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        searchView= findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                dialog.setTitle("Fetching News articles of "+ query);
                dialog.show();
                RequestManager manager =new RequestManager(MainActivity.this);
                manager.getNewsHeadlines(listener,"general",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        dialog =new ProgressDialog(this);
        dialog.setTitle("Fetching News Articles...");
        dialog.show();

        b1=findViewById(R.id.btn_1);
        b2=findViewById(R.id.btn_2);
        b3=findViewById(R.id.btn_3);
        b4=findViewById(R.id.btn_4);
        b5=findViewById(R.id.btn_5);
        b6=findViewById(R.id.btn_6);
        b7=findViewById(R.id.btn_7);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);
        b5.setOnClickListener(this);
        b6.setOnClickListener(this);
        b7.setOnClickListener(this);

        RequestManager manager =new RequestManager(this);
        manager.getNewsHeadlines(listener,"general",null);

    }
    private final OnFetchDataListener<NewsApiResponse> listener = new OnFetchDataListener<NewsApiResponse>() {
        @Override
        public void onFetchData(List<NewsHeadlines> list, String message) {
            if(list.isEmpty()){
                Toast.makeText(MainActivity.this, "No Data Found!!", Toast.LENGTH_SHORT).show();
            }
            else{
                showNews(list);
                dialog.dismiss();

            }

        }

        @Override
        public void onError(String message) {
            Toast.makeText(MainActivity.this, "An Error Occurred!!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showNews(List<NewsHeadlines> list) {
        recyclerView= findViewById(R.id.recycler_main);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        adapter=new CustomAdapter(this,list,this);
        recyclerView.setAdapter((adapter));
    }

    @Override
    public void OnNewsClick(NewsHeadlines headlines) {
        startActivity(new Intent(MainActivity.this,DetailsActivity.class)
        .putExtra("data",headlines));
    }

    @Override
    public void onClick(View v) {
        Button button=(Button) v;
        String category =button.getText().toString();
        dialog.setTitle("Fetching News articles of "+ category);
        dialog.show();
        RequestManager manager =new RequestManager(this);
        manager.getNewsHeadlines(listener,category,null);
    }
}