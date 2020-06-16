package kz.school.grants.univer_menu;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kz.school.grants.R;
import kz.school.grants.univer_menu.activities.ProfessionsActivity;
import kz.school.grants.univer_menu.models.Univer;

public class UniversAdapter extends RecyclerView.Adapter<UniversAdapter.MyViewHolder> {
    Activity activity;
    Context mContext;
    ArrayList<Univer> mData;

    public UniversAdapter(Activity activity, Context mContext, ArrayList<Univer> mData) {
        this.activity = activity;
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_univer, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Univer item = mData.get(position);

        Glide.with(mContext.getApplicationContext())
                .load(item.getUniverImage())
                .placeholder(R.drawable.icon_univer)
                .into(holder.univerImage);

        holder.univerName.setText(item.getUniverName());
        holder.univerPhone.setText(item.getUniverPhone());
        holder.univerLocation.setText(item.getUniverLocation());
        holder.univerCode.setText(item.getUniverCode());

        holder.univerLayout.setOnClickListener(view -> onClick(holder, item));
    }

    public void onClick(MyViewHolder holder, Univer item){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            Intent intent = new Intent(mContext, ProfessionsActivity.class);
            Pair<View, String> p1 = Pair.create(holder.univerImage, "univerImage");
            Pair<View, String> p2 = Pair.create(holder.univerLocationIcon, "univerLocationIcon");
            Pair<View, String> p3 = Pair.create(holder.univerPhoneIcon, "univerPhoneIcon");
            Pair<View, String> p4 = Pair.create(holder.univerName, "univerName");
            Pair<View, String> p5 = Pair.create(holder.univerPhone, "univerPhone");
            Pair<View, String> p6 = Pair.create(holder.univerLocation, "univerLocation");
            Pair<View, String> p7 = Pair.create(holder.univerCode, "univerCode");

            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, p1, p2, p3, p4, p5, p6, p7);
            Bundle bundle = new Bundle();
            bundle.putSerializable("univer", item);
            intent.putExtras(bundle);

            mContext.startActivity(intent, options.toBundle());

        } else {

            Intent intent = new Intent(mContext, ProfessionsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("univer", item);
            intent.putExtras(bundle);
            mContext.startActivity(intent);
            // Swap without transition
        }
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView univerImage, univerLocationIcon, univerPhoneIcon;
        private TextView univerName, univerPhone, univerLocation, univerCode;
        LinearLayout univerLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

            univerImage = itemView.findViewById(R.id.univerImage);
            univerLocationIcon = itemView.findViewById(R.id.univerLocationIcon);
            univerPhoneIcon = itemView.findViewById(R.id.univerPhoneIcon);
            univerName = itemView.findViewById(R.id.univerName);
            univerPhone = itemView.findViewById(R.id.univerPhone);
            univerLocation = itemView.findViewById(R.id.univerLocation);
            univerCode = itemView.findViewById(R.id.univerCode);
            univerLayout = itemView.findViewById(R.id.univerLayout);

        }
    }
}

