package kz.school.grants.news_menu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import kz.school.grants.R;
import kz.school.grants.news_menu.NewsFeed;

public class NewsActivity extends AppCompatActivity {

    TextView news_title;
    TextView news_date;
    TextView news_desc;
    ImageView news_img;
    ImageView likesBtn;
    TextView likes;
    ImageView dislikesBtn;
    TextView dislikes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        Intent intent = getIntent();
        NewsFeed item = (NewsFeed)intent.getSerializableExtra("news");

        news_title = findViewById(R.id.news_title);
        news_date = findViewById(R.id.news_date);

        news_desc = findViewById(R.id.news_desc);
        news_img = findViewById(R.id.news_img);
        likesBtn = findViewById(R.id.likesBtn);
        likes = findViewById(R.id.likes);
        dislikesBtn = findViewById(R.id.dislikesBtn);
        dislikes = findViewById(R.id.dislikes);

        news_title.setText(item.getNews_title());
        news_date.setText(item.getNews_date());
        news_desc.setText(item.getNews_desc());
        likes.setText(""+item.getLikes());
        dislikes.setText(""+item.getDislikes());

        Glide.with(getApplicationContext())
                .load(item.getNews_img())
                .placeholder(R.drawable.edu_news)
                .into(news_img);

        likesBtn.setOnClickListener(view -> {
            int likesCount = Integer.parseInt(likes.getText().toString());
            likes.setText(""+(likesCount+1));
        });

        dislikesBtn.setOnClickListener(view -> {
            int likesCount = Integer.parseInt(dislikes.getText().toString());
            dislikes.setText(""+(likesCount+1));
        });

    }
}