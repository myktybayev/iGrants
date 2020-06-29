package kz.school.grants.granttar_menu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import kz.school.grants.R;
import kz.school.grants.granttar_menu.activities.AtauliSpecListActivity;
import kz.school.grants.granttar_menu.activities.SerpinSpecListActivity;
import kz.school.grants.granttar_menu.adapters.GrantAdapter;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;

public class GranttarFragment extends Fragment implements View.OnClickListener {
    View v;
    private RecyclerView myrecyclerview;
    private ArrayList<Grant> lstContact;
    private DatabaseReference newsRef;
    GrantAdapter grantAdapter;
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
        progressLoading.setVisibility(View.GONE);
        lstContact.add(new Grant("1", "Жалпы грант", "Мемлекет тарапынан, республикалық бюджеттен беріледі."));
        lstContact.add(new Grant("1", "Атаулы грант", "Бұны да мемлекет республикалық бюджеттен береді, бірақ ЖОО на бекітілген."));
        lstContact.add(new Grant("1", "Ректор грант(ішкі грант)", "Бұл университеттің өзінің жеке гранты."));
        lstContact.add(new Grant("1", "Серпін", "Бұл да республикалық бюджеттен берілетін аймаққа бекітілген грант."));

        grantAdapter = new GrantAdapter(getContext(), lstContact);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(grantAdapter);

        newsRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();

        if (isAdmin()) {
            adminSigned = true;
            fabBtn.setVisibility(View.VISIBLE);
            fabBtn.setOnClickListener(this);
        }

        myrecyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), myrecyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {
                        if(pos == 1){

                            Intent intent = new Intent(getActivity(), AtauliSpecListActivity.class);
                            startActivity(intent);

                        }else if(pos == 3){

                            Intent intent = new Intent(getActivity(), SerpinSpecListActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {

                    }
                })
        );

//        newsRef.child("news").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                lstContact.clear();
//                for(DataSnapshot newsFeed: dataSnapshot.getChildren()){
//                    lstContact.add(newsFeed.getValue(NewsFeed.class));
//                }
//                progressLoading.setVisibility(View.GONE);
//                recyclerAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }

    public boolean isAdmin() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getEmail().contains("admin");
        }
        return false;
    }

    @Override
    public void onClick(View view) {
//        startActivity(new Intent(getActivity(), AddNews.class));
    }
}














