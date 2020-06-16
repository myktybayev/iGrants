package kz.school.grants.granttar_menu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kz.school.grants.R;
import kz.school.grants.granttar_menu.models.SpecItem;

public class SpecListAdapter extends RecyclerView.Adapter<SpecListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<SpecItem> mData;

    public SpecListAdapter(Context mContext, ArrayList<SpecItem> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_grant_spec, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        SpecItem item = mData.get(position);

        holder.specName.setText(item.getSpecCode()+" - "+item.getSpecName());
        holder.specSubjectPair.setText(item.getSpecSubjectPair());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView specName, specSubjectPair;

        public MyViewHolder(View itemView) {
            super(itemView);

            specName = itemView.findViewById(R.id.specName);
            specSubjectPair = itemView.findViewById(R.id.specSubjectPair);
        }
    }
}

