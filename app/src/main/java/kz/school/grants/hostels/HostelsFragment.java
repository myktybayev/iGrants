package kz.school.grants.hostels;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
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

import kz.school.grants.R;
import kz.school.grants.news_menu.NewsAdapter;
import kz.school.grants.news_menu.NewsFeed;
import kz.school.grants.news_menu.activities.AddNews;

public class HostelsFragment extends Fragment implements View.OnClickListener {
    View v;
    private RecyclerView myrecyclerview;
    ArrayList<Hostel> lstHostels;
    ArrayList<Hostel> lstHostelsCopy;
    private DatabaseReference newsRef;
    HostelAdapter hostelAdapter;
    FirebaseAuth mAuth;
    boolean adminSigned = false;
    FloatingActionButton fabBtn;
    View progressLoading;
    SearchView searchView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_hostels, container, false);
        initViews();

        return v;
    }

    public void initViews() {

        lstHostels = new ArrayList<>();
        progressLoading = v.findViewById(R.id.llProgressBar);
        myrecyclerview = v.findViewById(R.id.home_recyclerview);
        fabBtn = v.findViewById(R.id.fabBtn);
        searchView = v.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

//public Hostel(String hostelId, String hostelImage, String hostelRating, String hostelName, String hostelPrice, String hostelLocation, String hostelGender) {

        lstHostels.add(new Hostel("123", "url", "4", "Mountain", "5500", "Almaty", "Ұл/Қыз"));
        lstHostels.add(new Hostel("123", "url", "3", "Dostyk Hostel", "3500", "Almaty", "Ұл"));
        lstHostels.add(new Hostel("123", "url", "5", "European", "7000", "Astana", "Қыз"));
        lstHostels.add(new Hostel("123", "url", "3", "Шымкент Хотел", "3000", "Shymkent", "Ұл"));
        lstHostels.add(new Hostel("123", "url", "5", "Атырау Сити", "4000", "Atyrau", "Қыз"));
        lstHostels.add(new Hostel("123", "url", "4", "Тараз шахары", "5500", "Taraz", "Ұл/Қыз"));

        lstHostelsCopy = (ArrayList<Hostel>) lstHostels.clone();

        hostelAdapter = new HostelAdapter(getContext(), lstHostels);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        myrecyclerview.setAdapter(hostelAdapter);

        newsRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        fabBtn.setOnClickListener(this);

        if (mAuth.getCurrentUser() != null) {
            adminSigned = true;
            fabBtn.setVisibility(View.VISIBLE);
        }

        newsRef.child("hostels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lstHostels.clear();
                    for (DataSnapshot hostelFeed : dataSnapshot.getChildren()) {
                        lstHostels.add(hostelFeed.getValue(Hostel.class));
                    }

                    lstHostelsCopy = (ArrayList<Hostel>) lstHostels.clone();
                    progressLoading.setVisibility(View.GONE);
                    hostelAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
//        startActivity(new Intent(getActivity(), AddNews.class));
    }

    public void filter(String text) {

        lstHostels.clear();

        if (text.isEmpty()) {
            lstHostels.addAll(lstHostelsCopy);

        } else {
            text = text.toLowerCase();
            for (Hostel item : lstHostelsCopy) {

                if (item.getHostelName().toLowerCase().contains(text) || item.getHostelName().toUpperCase().contains(text) ||
                        item.getHostelLocation().toLowerCase().contains(text) || item.getHostelLocation().toUpperCase().contains(text)||
                        item.getHostelGender().toLowerCase().contains(text) || item.getHostelGender().toUpperCase().contains(text)||
                        item.getHostelPrice().toLowerCase().contains(text) || item.getHostelPrice().toUpperCase().contains(text)) {

                    lstHostels.add(item);
                }
            }
        }

        hostelAdapter.notifyDataSetChanged();
    }

    private boolean isAdmin(){
        if(mAuth.getCurrentUser() != null){
            return Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).contains("admin");
        }
        return false;
    }


}














