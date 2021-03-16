package kz.school.grants.news_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import kz.school.grants.R;
import kz.school.grants.granttar_menu.activities.AtauliSpecListActivity;
import kz.school.grants.granttar_menu.activities.SerpinSpecListActivity;
import kz.school.grants.news_menu.activities.AddNews;
import kz.school.grants.news_menu.activities.NewsActivity;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;

public class NewsFragment extends Fragment implements View.OnClickListener {
    View v;
    private RecyclerView myrecyclerview;
    private List<NewsFeed> lstContact;
    private DatabaseReference newsRef;
    NewsAdapter recyclerAdapter;
    FirebaseAuth mAuth;
    boolean adminSigned = false;
    FloatingActionButton fabBtn;
    View progressLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_news, container, false);
        initViews();

        return v;
    }

    public void initViews(){

        lstContact = new ArrayList<>();
        progressLoading =  v.findViewById(R.id.llProgressBar);
        myrecyclerview =  v.findViewById(R.id.home_recyclerview);
        fabBtn =  v.findViewById(R.id.fabBtn);

        recyclerAdapter = new NewsAdapter(getContext(), lstContact);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(recyclerAdapter);

        newsRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        fabBtn.setOnClickListener(this);

        if (isAdmin()) {
            adminSigned = true;
            fabBtn.setVisibility(View.VISIBLE);
        }

        newsRef.child("news").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lstContact.clear();
                for(DataSnapshot newsFeed: dataSnapshot.getChildren()){
                    lstContact.add(newsFeed.getValue(NewsFeed.class));
                }
                progressLoading.setVisibility(View.GONE);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        myrecyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), myrecyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {
                        Intent intent = new Intent(getActivity(), NewsActivity.class);
                        intent.putExtra("news", lstContact.get(pos));
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {

                    }
                })
        );

    }

    private boolean isAdmin(){
        if(mAuth.getCurrentUser() != null){
            return Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).contains("admin");
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(getActivity(), AddNews.class));
    }
}














