package kz.school.grants.spec_menu.adapters;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.spec_menu.models.OneBlockSpec;
import kz.school.grants.spec_menu.models.OneSpec;
import kz.school.grants.spec_menu.models.SubjectPair;

public class BallEsepteuAdapter extends RecyclerView.Adapter<BallEsepteuAdapter.MyTViewHolder> {

    private Context context;
    private List<OneSpec> dataList;

    public class MyTViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        @BindView(R.id.specTitle) TextView specTitle;
        @BindView(R.id.maxScore) TextView maxScore;
        @BindView(R.id.minScore) TextView minScore;

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

    public BallEsepteuAdapter(Context context, List<OneSpec> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_one_spec, parent, false);

        return new MyTViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyTViewHolder holder, int position) {

        OneSpec oneSpec = dataList.get(position);
//
        holder.specTitle.setText(oneSpec.getTitle());
        holder.maxScore.setText(oneSpec.getMaxScore());
        holder.minScore.setText(oneSpec.getMinScore());

//        holder.sCount.setText(""+subjectPair.getCount());
//
//        int gradientBack = gradientStore.getResourceId(position, 0);
//        int subjectImage = subjectStore.getResourceId(position, 0);
//
//        holder.gradientLayout.setBackground(context.getResources().getDrawable(gradientBack));
//        holder.groupPhoto.setBackground(context.getResources().getDrawable(subjectImage));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}