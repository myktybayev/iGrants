package kz.school.grants.hostels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import kz.school.grants.R;
import kz.school.grants.news_menu.NewsFeed;

public class HostelAdapter extends RecyclerView.Adapter<HostelAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Hostel> mData;

    public HostelAdapter(Context mContext, ArrayList<Hostel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_hostel, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Hostel item = mData.get(position);

        Glide.with(mContext.getApplicationContext())
                .load(item.getHostelImage())
                .placeholder(R.drawable.hostel)
                .into(holder.hostelImage);

        holder.hostelRating.setRating(Float.parseFloat(item.getHostelRating()));
        holder.hostelName.setText(item.getHostelName());
        holder.hostelPrice.setText("KZT "+item.getHostelPrice());
        holder.hostelLocation.setText(item.getHostelLocation());
        holder.hostelGender.setText(item.getHostelGender());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView hostelImage;
        private RatingBar hostelRating;
        private TextView hostelName, hostelPrice, hostelLocation, hostelGender;

        public MyViewHolder(View itemView) {
            super(itemView);

            hostelImage = itemView.findViewById(R.id.hostelImage);
            hostelRating = itemView.findViewById(R.id.hostelRating);
            hostelName = itemView.findViewById(R.id.hostelName);
            hostelPrice = itemView.findViewById(R.id.hostelPrice);
            hostelLocation = itemView.findViewById(R.id.hostelLocation);
            hostelGender = itemView.findViewById(R.id.hostelGender);

        }
    }
}

