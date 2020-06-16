package kz.school.grants.univer_menu;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
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
import java.util.HashMap;
import java.util.Objects;

import kz.school.grants.R;
import kz.school.grants.advice_menu.AdviceFragment;
import kz.school.grants.advice_menu.activities.AdviceReport;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.univer_menu.activities.ProfessionsActivity;
import kz.school.grants.univer_menu.models.Univer;

import static kz.school.grants.database.StoreDatabase.COLUMN_PROFESSIONS_LIST;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_IMAGE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_LOCATION;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_NAME;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_PHON;
import static kz.school.grants.database.StoreDatabase.TABLE_UNIVER_LIST;
import static kz.school.grants.database.StoreDatabase.TABLE_VER;

public class UniversFragment extends Fragment implements View.OnClickListener {
    View v;
    private RecyclerView myRecyclerView;
    private ArrayList<Univer> lstUnivers;
    private ArrayList<Univer> lstUniversCopy;
    UniversAdapter universAdapter;
    private FirebaseAuth mAuth;
    private FloatingActionButton fabBtn;
    private View progressLoading;
    private SearchView searchView;
    Button ratingBtn;
    private DatabaseReference mDatabaseRef;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    String curVersion;
    HashMap<String, String> tempHashMap;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_univers, container, false);
        initViews();
        checkInternetConnection();
        checkVersion("univer_list_ver");

        return v;
    }

    public void initViews() {

        lstUnivers = new ArrayList<>();
        tempHashMap = new HashMap<>();
        progressLoading = v.findViewById(R.id.llProgressBar);
        myRecyclerView = v.findViewById(R.id.home_recyclerview);
        ratingBtn = v.findViewById(R.id.ratingBtn);
        fabBtn = v.findViewById(R.id.fabBtn);
        searchView = v.findViewById(R.id.searchView);
        storeDb = new StoreDatabase(getActivity());
        sqdb = storeDb.getWritableDatabase();


//        lstUnivers.add(new Univer("123", "url", "Университет имени Сулеймана Демиреля (УСД (СДУ))", "87571122334", "Алматы", "302"));
//        lstUnivers.add(new Univer("123", "url", "Кызылординский гуманитарный университет (КызГУ)","87571122334", "Кызылорда", "304"));
//        lstUnivers.add(new Univer("123", "url", "Центрально-Азиатский университет (ЦАУ)", "87571122334","Алматы", "096"));


        universAdapter = new UniversAdapter(getActivity(), getContext(), lstUnivers);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(universAdapter);
        myRecyclerViewAddListeners();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        fabBtn.setOnClickListener(this);
        ratingBtn.setOnClickListener(this);

        if (mAuth.getCurrentUser() != null) {
            fabBtn.setVisibility(View.VISIBLE);
        }
    }

    private void searchAddListener(){
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
    }

    private void myRecyclerViewAddListeners(){

        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabBtn.isShown())
                    fabBtn.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fabBtn.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        /*
        myRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), myRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {

//                        Intent univerCabinet = new Intent(getActivity(), ProfessionsActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("univer", lstUnivers.get(pos));
//                        univerCabinet.putExtras(bundle);
//                        startActivity(univerCabinet);
//
//                        Intent intent = new Intent(getActivity(), ProfessionsActivity.class);
//                        Pair<View, String> p1 = Pair.create((View)mObjectIV, "univerName");
//                        Pair<View, String> p2 = Pair.create((View)mObjectNameTV, "univerLocation");
//                        Pair<View, String> p2 = Pair.create((View)mObjectNameTV, "univerPhone");
//                        ActivityOptionsCompat options = ActivityOptionsCompat.
//                                makeSceneTransitionAnimation(getActivity(), p1, p2);
//
//                        startActivity(intent, options.toBundle());


                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {

                    }
                })
        );
        */
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ratingBtn:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://atameken.kz/kk/university_ratings?page=2"));
                startActivity(browserIntent);
                break;
        }
//        startActivity(new Intent(getActivity(), AddNews.class));
    }

    private void checkVersion(String versionName) {
        mDatabaseRef.child(versionName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String newVersion = dataSnapshot.getValue().toString();
                    if (!getCurrentVersion(versionName).equals(newVersion)) {
                        progressLoading.setVisibility(View.VISIBLE);
                        updateVersion(versionName, newVersion);
                        refreshUniverList();
                    } else {
                        fillUniversFromDB();
                    }
                } else {
                    Toast.makeText(getActivity(), "Can not find " + versionName, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fillUniversFromDB() {
        Cursor userCursor = storeDb.getCursorAll(sqdb, TABLE_UNIVER_LIST);
        lstUnivers.clear();

        Log.i("Univers", "fillUniversFromDB");

        if (((userCursor != null) && (userCursor.getCount() > 0))) {
            while (userCursor.moveToNext()) {

                Log.i("Univers", storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_NAME));

                lstUnivers.add(new Univer(
                        storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_ID),
                        storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_IMAGE),
                        storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_NAME),
                        storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_PHON),
                        storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_LOCATION),
                        storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_CODE),
                        storeDb.getStrFromColumn(userCursor, COLUMN_PROFESSIONS_LIST)
                ));
            }
            lstUniversCopy = (ArrayList<Univer>) lstUnivers.clone();
            searchAddListener();

            progressLoading.setVisibility(View.GONE);
            universAdapter.notifyDataSetChanged();
        }

    }

    private void refreshUniverList() {
        mDatabaseRef.child("univer_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lstUnivers.clear();
                    storeDb.cleanUnivers(sqdb);
                    for (DataSnapshot feed : dataSnapshot.getChildren()) {
                        Univer univer = feed.getValue(Univer.class);
                        lstUnivers.add(univer);

                        ContentValues uniValues = new ContentValues();
                        assert univer != null;

                        uniValues.put(COLUMN_UNIVER_ID, univer.getUniverId());
                        uniValues.put(COLUMN_UNIVER_IMAGE, univer.getUniverImage());
                        uniValues.put(COLUMN_UNIVER_NAME, univer.getUniverName());
                        uniValues.put(COLUMN_UNIVER_PHON, univer.getUniverPhone());
                        uniValues.put(COLUMN_UNIVER_LOCATION, univer.getUniverLocation());
                        uniValues.put(COLUMN_UNIVER_CODE, univer.getUniverCode());
                        uniValues.put(COLUMN_PROFESSIONS_LIST, univer.getProfessions());

                        sqdb.insert(TABLE_UNIVER_LIST, null, uniValues);

                    }

                    lstUniversCopy = (ArrayList<Univer>) lstUnivers.clone();
                    progressLoading.setVisibility(View.GONE);
                    universAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateVersion(String versionName, String version) {
        ContentValues versionValues = new ContentValues();
        versionValues.put(versionName, version);
        sqdb.update(TABLE_VER, versionValues, versionName + "=" + curVersion, null);

    }

    private String getCurrentVersion(String versionName) {
        Cursor res = sqdb.rawQuery("SELECT " + versionName + " FROM " + TABLE_VER, null);
        res.moveToNext();
        curVersion = res.getString(0);
        return res.getString(0);
    }

    private boolean checkInternetConnection() {
        if (!isNetworkAvailable()) {
            Toast.makeText(getActivity(), getString(R.string.check_inet), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void filter(String text) {
        if (lstUnivers.size() > 0) lstUnivers.clear();

        if (text.isEmpty()) {
            lstUnivers.addAll(lstUniversCopy);

        } else {
            text = text.toLowerCase();
            for (Univer item : lstUniversCopy) {

                if (item.getUniverName().toLowerCase().contains(text) || item.getUniverName().toUpperCase().contains(text) ||
                        item.getUniverLocation().toLowerCase().contains(text) || item.getUniverLocation().toUpperCase().contains(text) ||
                        item.getUniverCode().toLowerCase().contains(text) || item.getUniverCode().toUpperCase().contains(text)) {

                    lstUnivers.add(item);
                }
            }
        }

        universAdapter.notifyDataSetChanged();
    }

}














