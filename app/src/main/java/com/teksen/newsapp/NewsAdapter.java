package com.teksen.newsapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private boolean isButtonPressed = false;

    private final List<String> colorList = Arrays.asList(
            "#ee82ee",  // Mor
            "#3a5fcd",  // Mavi
            "#ffde66",  // Sarı
            "#FF0000",  // Kırmızı
            "#008000",  // Yeşil
            "#800080",  // Mor
            "#000080",  // Lacivert
            "#FFA500",  // Turuncu
            "#800000",  // Kahverengi
            "#000000"   // Siyah
    );
    private List<NewsDTO> newsList = new ArrayList<>();

    public void setNewsList(List<NewsDTO> newsList) {
        this.newsList = newsList;
        notifyDataSetChanged();
    }

    public void setButtonPressed() {
        isButtonPressed = !isButtonPressed;
        notifyDataSetChanged();
        notifyItemRangeChanged(0, newsList.size()); // Yeni satırı ekleyin
    }

    @Override
    public int getItemViewType(int position) {
        return isButtonPressed ? 1 : 0;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutResId;
        if (viewType == 1) {
            layoutResId = R.layout.item_news_v1; // Button basıldığında kullanılacak görünüm
        } else {
            layoutResId = R.layout.item_news; // Button basılmadığında kullanılacak görünüm
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {

        NewsDTO newsDTO = newsList.get(position);
        holder.titleTextView.setText(newsDTO.getTitle());
        if(!isButtonPressed){
            //holder.contentTextView.setText(newsDTO.getContent());
        }

        holder.sourceTextView.setText(newsDTO.getSourceName());

        String randomColor = colorList.get(position % colorList.size());
        Drawable roundedBackground = createRoundedBackground(randomColor);
        holder.sourceTextView.setBackground(roundedBackground);

        if (newsDTO.getImageUrl() != null) {
            Picasso.get()
                    .load(newsDTO.getImageUrl())
                    .into(holder.imageView);
        } else {
            Picasso.get()
                    .load(R.drawable.boyfight) // Varsayılan resim
                    .into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), NewsDetailsActivity.class);
                intent.putExtra("news", newsDTO); // Haberi NewsDetailActivity'ye iletmek için intent'e ekleyebilirsiniz
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }





    private Drawable createRoundedBackground(String color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(250);
        drawable.setColor(Color.parseColor(color));
        return drawable;
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView contentTextView;
        ImageView imageView;
        TextView sourceTextView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            imageView = itemView.findViewById(R.id.newsImageView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
        }
    }
}
