package kz.school.grants.news_menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import kz.school.grants.R;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context mContext;
    List<NewsFeed> mData;

    public NewsAdapter(Context mContext, List<NewsFeed> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_news, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        NewsFeed item = mData.get(position);

        holder.news_title.setText(item.getNews_title());
        holder.news_date.setText(item.getNews_date());
        holder.news_desc.setText(item.getNews_desc());
        holder.likes.setText(""+item.getLikes());
        holder.dislikes.setText(""+item.getDislikes());

        Glide.with(mContext.getApplicationContext())
                .load(item.getNews_img())
                .placeholder(R.drawable.edu_news)
                .into(holder.news_img);

        holder.likesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int likesCount = Integer.parseInt(holder.likes.getText().toString());
                holder.likes.setText(""+(likesCount+1));
            }
        });

        holder.dislikesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int likesCount = Integer.parseInt(holder.dislikes.getText().toString());
                holder.dislikes.setText(""+(likesCount+1));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView news_title;
        private TextView news_date;
        private TextView news_desc;
        private ImageView news_img;

        private ImageView likesBtn;
        private TextView likes;

        private ImageView dislikesBtn;
        private TextView dislikes;


        public MyViewHolder(View itemView) {
            super(itemView);

            news_title = itemView.findViewById(R.id.news_title);
            news_date = itemView.findViewById(R.id.news_date);
            news_desc = itemView.findViewById(R.id.news_desc);
            news_img = itemView.findViewById(R.id.news_img);

            likesBtn = itemView.findViewById(R.id.likesBtn);
            likes = itemView.findViewById(R.id.likes);
            dislikesBtn = itemView.findViewById(R.id.dislikesBtn);
            dislikes = itemView.findViewById(R.id.dislikes);

        }
    }
}

