package kz.school.grants.spec_menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kz.school.grants.R;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.spec_menu.adapters.SpecAdapter;

public class SpecialityActivity extends AppCompatActivity {

    @BindView(R.id.pairList)
    RecyclerView pairRecyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    List<SubjectPair> subjectPairList;
    SpecAdapter subjectPairAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        subjectPairList = new ArrayList<>();

        subjectPairList.add(new SubjectPair("math_phys", "5В060200-Информатика", 32));
        subjectPairList.add(new SubjectPair("math_phys", "5В070300-Информационные системы", 33));
        subjectPairList.add(new SubjectPair("math_phys", "5В070400-Вычислительная техника и программное обеспечение", 15));

        linearLayoutManager = new LinearLayoutManager(this);
        pairRecyclerView = findViewById(R.id.pairList);
        pairRecyclerView.setLayoutManager(linearLayoutManager);
        pairRecyclerView.setHasFixedSize(true);
        subjectPairAdapter = new SpecAdapter(this, subjectPairList);

        pairRecyclerView.setAdapter(subjectPairAdapter);
        pairRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(SpecialityActivity.this, pairRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {

                        Intent intent = new Intent(SpecialityActivity.this, GrantInfo.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


    }
}
