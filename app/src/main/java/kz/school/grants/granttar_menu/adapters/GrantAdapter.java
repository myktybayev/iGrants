package kz.school.grants.granttar_menu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kz.school.grants.R;
import kz.school.grants.granttar_menu.Grant;

public class GrantAdapter extends RecyclerView.Adapter<GrantAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<Grant> mData;

    public GrantAdapter(Context mContext, ArrayList<Grant> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_grant, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Grant item = mData.get(position);

        holder.grantTitle.setText(item.getGrant_title());
        holder.grantDesc.setText(item.getGrant_desc());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView grantTitle, grantDesc;

        public MyViewHolder(View itemView) {
            super(itemView);

            grantTitle = itemView.findViewById(R.id.grantTitle);
            grantDesc = itemView.findViewById(R.id.grantDesc);

        }
    }
}

