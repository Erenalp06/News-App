package com.teksen.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.teksen.newsapp.service.ApiManager;
import com.teksen.newsapp.service.NewsApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private NewsApiService theNewsApiService;
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;

    private DrawerLayout drawerLayout;

    FirebaseAuth auth;
    FirebaseUser user;

    private SearchView searchView;




    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            email = user.getEmail();
        }

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);
        Menu menu = navigationView.getMenu();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.inflateMenu(R.menu.menu_drawer);





        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        MenuItem emailMenuItem = navigationView.getMenu().findItem(R.id.menu_email);
        emailMenuItem.setTitle(email);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter();
        recyclerView.setAdapter(newsAdapter);

        Retrofit retrofit = ApiManager.getRetrofitInstance();

        theNewsApiService = retrofit.create(NewsApiService.class);

        Call<List<NewsDTO>> call = theNewsApiService.getAllNews();
        call.enqueue(new Callback<List<NewsDTO>>() {
            @Override
            public void onResponse(Call<List<NewsDTO>> call, Response<List<NewsDTO>> response) {
                if(response.isSuccessful()){
                    List<NewsDTO> newsList = response.body();
                    //newsAdapter.setNewsList(newsList);
                    System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                }else{
                    System.out.println("Hataaaaaaaaaaaa response");
                }
            }

            @Override
            public void onFailure(Call<List<NewsDTO>> call, Throwable t) {
                t.printStackTrace();
                System.out.println("Hataaaaaaaaaaaaaaaaa failure");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.searchItem);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search anything...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });




        return true;
    }

    private void performSearch(String query) {
        System.out.println("ARAMA KUTUSUNA YAZILDI : " + query);
        Call<List<NewsDTO>> call = theNewsApiService.findNewsBySearchQuery(query);
        callNews(call, "success sports", "sports response", "sports failure");
        Toast.makeText(this, query + " News", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int itemId = item.getItemId();


        switch (itemId) {
            case R.id.menu_sports:
                // Spor haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call = theNewsApiService.findNewsByCategoryName("sports");
                callNews(call, "success sports", "sports response", "sports failure");
                Toast.makeText(this, "Sports News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_health:
                // Sağlık haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call1 = theNewsApiService.findNewsByCategoryName("health");
                callNews(call1, "success health", "health response", "health failure");
                Toast.makeText(this, "Health News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_education:
                // Eğitim haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call2 = theNewsApiService.findNewsByCategoryName("education");
                callNews(call2, "success sports", "sports response", "sports failure");
                Toast.makeText(this, "Education News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_science:
                // Bilim haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call3 = theNewsApiService.findNewsByCategoryName("science");
                callNews(call3, "success sports", "sports response", "sports failure");
                Toast.makeText(this, "Science News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_technology:
                // Teknoloji haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call4 = theNewsApiService.findNewsByCategoryName("technology");
                callNews(call4, "success sports", "sports response", "sports failure");
                Toast.makeText(this, "Tecnology News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_finance:
                // Finans haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call5 = theNewsApiService.findNewsByCategoryName("finance");
                callNews(call5, "success sports", "sports response", "sports failure");
                Toast.makeText(this, "Finance News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_travel:
                // Seyahat haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call6 = theNewsApiService.findNewsByCategoryName("travel");
                callNews(call6, "success sports", "sports response", "sports failure");
                Toast.makeText(this, "Travel News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_game:
                // Oyun haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call7 = theNewsApiService.findNewsByCategoryName("game");
                callNews(call7, "success sports", "sports response", "sports failure");
                Toast.makeText(this, "Game News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_automobile:
                // Otomobil haberlerine yönlendirme işlemleri
                Call<List<NewsDTO>> call8 = theNewsApiService.findNewsByCategoryName("automobile");
                callNews(call8, "success sports", "sports response", "sports failure");
                Toast.makeText(this, "Automobile News", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_logout:;
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                break;


        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void callNews(Call<List<NewsDTO>> call, String success_sports, String sports_response, String sports_failure) {
        call.enqueue(new Callback<List<NewsDTO>>() {
            @Override
            public void onResponse(Call<List<NewsDTO>> call, Response<List<NewsDTO>> response) {
                if(response.isSuccessful()){
                    List<NewsDTO> newsList = response.body();
                    newsAdapter.setNewsList(newsList);
                    System.out.println(success_sports);
                }else{
                    System.out.println(sports_response);
                }
            }

            @Override
            public void onFailure(Call<List<NewsDTO>> call, Throwable t) {
                t.printStackTrace();
                System.out.println(sports_failure);

            }
        });
    }




    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private Boolean changeView = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }else if(item.getItemId() == R.id.changeView){
            if(changeView){
                item.setIcon(R.drawable.icon1);
            }else{
                item.setIcon(R.drawable.icon2);
            }
            changeView = !changeView;
            newsAdapter.setButtonPressed();
            Toast.makeText(this, "buttona basıldı", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }






}











