package kz.school.grants.advice_menu;

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
import kz.school.grants.spec_menu.adapters.ItemClickListener;
import kz.school.grants.spec_menu.models.SubjectPair;

public class AdviceHistoryAdapter extends RecyclerView.Adapter<AdviceHistoryAdapter.MyTViewHolder> {

    private Context context;
    private List<Advice> dataList;

    public class MyTViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

        @BindView(R.id.studentName)
        TextView studentName;
        @BindView(R.id.studentPhone)
        TextView studentPhone;
        @BindView(R.id.adviceDate)
        TextView adviceDate;

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

    public AdviceHistoryAdapter(Context context, List<Advice> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public MyTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_advice, parent, false);

        return new MyTViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyTViewHolder holder, int position) {

        Advice advice = dataList.get(position);

        holder.studentName.setText(advice.getStudentName());
        holder.studentPhone.setText(advice.getStudentPhone());
        holder.adviceDate.setText(advice.getAdviceDate());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

}