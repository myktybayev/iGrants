package kz.school.grants.granttar_menu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.granttar_menu.models.OneBallEseptuUniver;
import kz.school.grants.spec_menu.adapters.ItemClickListener;

public class AtauliBallEsepteuAdapter extends RecyclerView.Adapter<AtauliBallEsepteuAdapter.MyTViewHolder> {

    private Context context;
    private List<OneBallEseptuUniver> dataList;

    public class MyTViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        @BindView(R.id.maxScore) TextView maxScore;
        @BindView(R.id.minScore) TextView minScore;

        @BindView(R.id.univerImage)  ImageView univerImage;
        @BindView(R.id.univerName)  TextView univerName;
        @BindView(R.id.univerPhone)  TextView univerPhone;
        @BindView(R.id.univerLocation)  TextView univerLocation;
        @BindView(R.id.univerCode)  TextView univerCode;


        ItemClickListener clickListener;

        public MyTViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }


        @Override
        public void onClick(View view) {
            this.clickListener.onItemClick(view,getLayoutPosition());
        }

        public void setOnClick(ItemClickListener clickListener){
            this.clickListener = clickListener;
        }
    }

    public AtauliBallEsepteuAdapter(Context context, List<OneBallEseptuUniver> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_atauli_ball_esepteu, parent, false);

        return new MyTViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyTViewHolder holder, int position) {

        OneBallEseptuUniver oneBallEseptuUniver = dataList.get(position);

        Glide.with(context.getApplicationContext())
                .load(oneBallEseptuUniver.getUniver().getUniverImage())
                .placeholder(R.drawable.icon_univer)
                .into(holder.univerImage);

        holder.univerName.setText(oneBallEseptuUniver.getUniver().getUniverName());
        holder.univerPhone.setText(oneBallEseptuUniver.getUniver().getUniverPhone());
        holder.univerLocation.setText(oneBallEseptuUniver.getUniver().getUniverLocation());
        holder.univerCode.setText(oneBallEseptuUniver.getUniver().getUniverCode());

        holder.maxScore.setText(oneBallEseptuUniver.getMaxScore());
        holder.minScore.setText(oneBallEseptuUniver.getMinScore());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}