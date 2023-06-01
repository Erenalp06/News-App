package com.teksen.newsapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.teksen.newsapp.dto.FavoriteDTO;
import com.teksen.newsapp.service.ApiManager;
import com.teksen.newsapp.service.ApiManagerFavorite;
import com.teksen.newsapp.service.FavoriteApiService;
import com.teksen.newsapp.service.NewsApiService;
import com.teksen.newsapp.service.TTSService;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NewsDetailsActivity extends AppCompatActivity {

    private FavoriteApiService theFavoriteApiService;

    private TextView titleTextView;
    private TextView contentTextView;
    private ImageView imageView;
    private TextView sourceTextView;

    private TextView urlTextView;

    private TTSService ttsService;

    private float pitch = 1.0f;
    private float speechRate = 1.0f;

    private String newsDTOContent;

    private NewsDTO newsDTOFavorite;

    FirebaseAuth auth;
    FirebaseUser user;

    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details2);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if(user == null){
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            email = user.getEmail();
        }

        ttsService = new TTSService(this);

        // ActionBar'ı al
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Geri tuşunu göster

        }

        // XML dosyasında yer alan bileşenleri eşleştirin
        titleTextView = findViewById(R.id.titleTextView);
        contentTextView = findViewById(R.id.contentTextView);
        imageView = findViewById(R.id.newsImageView);
        sourceTextView = findViewById(R.id.sourceTextView);
        urlTextView = findViewById(R.id.urlTextView);



        // Intent aracılığıyla haber verilerini al
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("news")) {
            NewsDTO newsDTO = intent.getParcelableExtra("news");
            if (newsDTO != null) {
                newsDTOFavorite = newsDTO;
                // Haber verilerini ilgili bileşenlere ayarla
                titleTextView.setText(newsDTO.getTitle());
                contentTextView.setText(newsDTO.getContent());
                sourceTextView.setText(newsDTO.getSourceName());
                urlTextView.setText(newsDTO.getUrl());

                newsDTOContent = newsDTO.getContent();

                // URL'yi tıklanabilir hale getir
                urlTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String url = newsDTO.getUrl();
                        if (url != null && !url.isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(intent);
                        }
                    }
                });

                if (newsDTO.getImageUrl() != null) {
                    Picasso.get()
                            .load(newsDTO.getImageUrl())
                            .into(imageView);
                } else {
                    Picasso.get()
                            .load(R.drawable.boyfight)
                            .into(imageView);
                }

                try {
                    String articleUrl = newsDTO.getUrl();
                    System.out.println("URL : " + articleUrl);
                    getArticleContent(articleUrl);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        Retrofit retrofit = ApiManagerFavorite.getRetrofitInstance();

        theFavoriteApiService = retrofit.create(FavoriteApiService.class);

        Button favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteDTO favoriteDTO = new FavoriteDTO(
                        newsDTOFavorite.getId(),
                        email,
                        newsDTOFavorite.getTitle(),
                        newsDTOFavorite.getContent(),
                        newsDTOFavorite.getSourceId(),
                        newsDTOFavorite.getSourceName(),
                        newsDTOFavorite.getAuthor(),
                        newsDTOFavorite.getDescription(),
                        newsDTOFavorite.getUrl(),
                        newsDTOFavorite.getImageUrl(),
                        newsDTOFavorite.getPublishedAt()
                );

                Call<Boolean> call = theFavoriteApiService.checkNewsForFavorite(favoriteDTO);
                call.enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        boolean isExist = response.body();
                        if(!isExist){
                            Call<FavoriteDTO> call1 = theFavoriteApiService.createFavoriteForUser(favoriteDTO);
                            call1.enqueue(new Callback<FavoriteDTO>() {
                                @Override
                                public void onResponse(Call<FavoriteDTO> call, Response<FavoriteDTO> response) {
                                    Toast.makeText(getApplicationContext(), "Added to favourites", Toast.LENGTH_SHORT).show();
                                    favoriteButton.setEnabled(false);
                                }

                                @Override
                                public void onFailure(Call<FavoriteDTO> call, Throwable t) {
                                    Toast.makeText(getApplicationContext(), "Something is wrong!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(), "Already added to favourites!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });



            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_micro, menu);
        this.menu = menu;
        return true;
    }
    String articleContext;
    private void getArticleContent(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document doc = Jsoup.connect(url).get();
                    Element articleElement = doc.select("article").first();
                    if (articleElement != null) {
                        String articleContent = articleElement.text();
                        articleContent = articleContent.replaceAll("\\s+", " ");


                        // Makale içeriğiyle ilgili işlemleri yapabilirsiniz
                        String finalArticleContent = articleContent;
                        System.out.println("FinalArticleContent : " + articleContent);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                System.out.println("ArticleTest: " + finalArticleContent);



                                int maxLength = 2000;
                                int length = finalArticleContent.length();

                                int startIndex = 0;
                                int endIndex = maxLength;

                                while (startIndex < length) {
                                    if (endIndex >= length) {
                                        endIndex = length;
                                    } else {

                                        endIndex = finalArticleContent.lastIndexOf(" ", endIndex);
                                        int punctuationIndex = finalArticleContent.indexOf(".", endIndex);
                                        if (punctuationIndex != -1 && punctuationIndex <= endIndex) {
                                            endIndex = punctuationIndex + 1;
                                        }
                                    }

                                    String subText = finalArticleContent.substring(startIndex, endIndex);
                                    subText += "...";
                                    contentTextView.setText(subText);
                                    articleContext = subText;

                                    startIndex = endIndex + 1;
                                    endIndex = startIndex + maxLength;
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private Menu menu;
    boolean isClicked = false;
    private String lastText = "";
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.settingsButton) {
            // Ayarlar düğmesine tıklandığında yapılmasını istediğiniz işlemleri buraya yazın
            openSettingsDialog();
            return true;
        }

        if (id == R.id.startButton) {
            item.setVisible(false); // Başlat düğmesini gizle
            MenuItem stopButton = menu.findItem(R.id.stopButton);
            stopButton.setVisible(true); // Durdur düğmesini göster
            System.out.println("Article Context : " + articleContext);
            if(articleContext == null){
                System.out.println("NewsDTOContent : " + newsDTOContent);
                String truncatedContent = truncateText(newsDTOContent, "[");
                ttsService.speakText(truncatedContent, Locale.ENGLISH, speechRate, pitch);

            }else{
                ttsService.speakText(articleContext, Locale.ENGLISH, speechRate, pitch);
            }

            // Konuşma işlemini başlat

            return true;
        } else if (id == R.id.stopButton) {
            item.setVisible(false); // Durdur düğmesini gizle
            MenuItem startButton = menu.findItem(R.id.startButton);
            startButton.setVisible(true); // Başlat düğmesini göster

            ttsService.stopSpeaking();
            // Konuşma işlemini durdur






    }
        return super.onOptionsItemSelected(item);
    }

    private String truncateText(String text, String delimiter) {
        int delimiterIndex = text.indexOf(delimiter);
        if (delimiterIndex != -1) {
            return text.substring(0, delimiterIndex);
        } else {
            return text;
        }
    }

    private void openSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Speech Settings");


        View view = getLayoutInflater().inflate(R.layout.dialog_speech_settings, null);
        builder.setView(view);


        SeekBar pitchSeekBar = view.findViewById(R.id.pitchSeekBar);
        SeekBar speechRateSeekBar = view.findViewById(R.id.speechRateSeekBar);

        // Tamam düğmesine tıklandığında ayarları uygulayın
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Pitch ve speech rate değerlerini alın
                pitch = pitchSeekBar.getProgress() / 10.0f;
                speechRate = speechRateSeekBar.getProgress() / 10.0f;

            }
        });

        // Dialog penceresini gösterin
        builder.show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ttsService.stopSpeaking();
    }



}