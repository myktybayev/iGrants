package kz.school.grants.spec_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.spec_menu.adapters.SubjectPairAdapter;

import static kz.school.grants.MenuActivity.setTitle;

public class SpecFragment extends Fragment {

    @BindView(R.id.pairList)
    RecyclerView pairRecyclerView;
    RecyclerView.LayoutManager linearLayoutManager;
    List<SubjectPair> subjectPairList;
    SubjectPairAdapter subjectPairAdapter;
    View view;

    public SpecFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main, container, false);
        ButterKnife.bind(this, view);
        setTitle("Мамандықтар");

        subjectPairList = new ArrayList<>();

        subjectPairList.add(new SubjectPair("math_phys", "Математика-Физика", 32));
        subjectPairList.add(new SubjectPair("math_phys", "Биология-География", 33));
        subjectPairList.add(new SubjectPair("math_phys", "Химия-Биология", 15));
        subjectPairList.add(new SubjectPair("math_phys", " Биология-Химия", 52));

        linearLayoutManager = new LinearLayoutManager(getActivity());
        pairRecyclerView = view.findViewById(R.id.pairList);
        pairRecyclerView.setLayoutManager(linearLayoutManager);
        pairRecyclerView.setHasFixedSize(true);
        subjectPairAdapter = new SubjectPairAdapter(getActivity(), subjectPairList);

        pairRecyclerView.setAdapter(subjectPairAdapter);
        pairRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), pairRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {

                        Intent intent = new Intent(getActivity(), SpecialityActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("book", bookList.get(pos));
//                intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );


        return view;
    }
}
