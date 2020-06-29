package kz.school.grants.granttar_menu.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.granttar_menu.activities.add_grants.AddAtauliGrant;
import kz.school.grants.granttar_menu.activities.add_grants.AddSerpinGrant;
import kz.school.grants.granttar_menu.adapters.AtauliUniversAdapter;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.univer_menu.models.Univer;

import static kz.school.grants.database.StoreDatabase.COLUMN_PROFESSIONS_LIST;
import static kz.school.grants.database.StoreDatabase.COLUMN_SPEC_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_IMAGE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_LOCATION;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_NAME;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_PHON;
import static kz.school.grants.database.StoreDatabase.TABLE_ATAULI_GRANTS;
import static kz.school.grants.database.StoreDatabase.TABLE_SERPIN_GRANTS;
import static kz.school.grants.database.StoreDatabase.TABLE_UNIVER_LIST;
import static kz.school.grants.database.StoreDatabase.TABLE_VER;

public class AtauliGrantsUniversActivity extends AppCompatActivity implements View.OnClickListener {
    View v;
    private RecyclerView myRecyclerView;
    private ArrayList<Univer> lstUnivers;
    private ArrayList<Univer> lstUniversCopy;
    AtauliUniversAdapter universAdapter;
    private FirebaseAuth mAuth;
    private FloatingActionButton fabBtn;
    private View progressLoading;
    private SearchView searchView;
    private DatabaseReference mDatabaseRef;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    String curVersion;
    HashMap<String, String> tempHashMap;
    Bundle bundle;
    Button calcBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atauli_univers);
        setTitle("AtauliGrantsUniversActivity");
        initViews();
    }

    public void initViews() {

        lstUnivers = new ArrayList<>();
        tempHashMap = new HashMap<>();
        progressLoading = findViewById(R.id.llProgressBar);
        myRecyclerView = findViewById(R.id.home_recyclerview);
        fabBtn = findViewById(R.id.fabBtn);
        searchView = findViewById(R.id.searchView);
        calcBtn = findViewById(R.id.calcBtn);
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        universAdapter = new AtauliUniversAdapter(this, this, lstUnivers);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(universAdapter);
        myRecyclerViewAddListeners();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        fabBtn.setOnClickListener(this);
        calcBtn.setOnClickListener(this);


        bundle = getIntent().getExtras();
        initBundle(bundle);

        if (isAdmin()) {
            fabBtn.setVisibility(View.VISIBLE);
        }

        myRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, myRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {

                        Intent intent = new Intent(AtauliGrantsUniversActivity.this, AtauliGrantInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("specCode", specCode);
                        bundle.putString("univerCode", lstUnivers.get(pos).getUniverCode());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {

                    }
                })
        );
    }

    String specCode, specName;

    public void initBundle(Bundle bundle) {
        if (bundle != null) {
            specCode = bundle.getString("specCode");
            specName = bundle.getString("specName");

            Cursor cursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_ATAULI_GRANTS, COLUMN_SPEC_CODE, specCode, COLUMN_SPEC_CODE);

            if (((cursor != null) && (cursor.getCount() > 0))) {
                while (cursor.moveToNext()) {

                    String univerCode = storeDb.getStrFromColumn(cursor, COLUMN_UNIVER_CODE);
                    Cursor childCursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_UNIVER_LIST, COLUMN_UNIVER_CODE, univerCode, COLUMN_UNIVER_CODE);
                    if (((childCursor != null) && (childCursor.getCount() > 0))) {
                        childCursor.moveToFirst();

                        lstUnivers.add(new Univer(
                                storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_ID),
                                storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_IMAGE),
                                storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_NAME),
                                storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_PHON),
                                storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_LOCATION),
                                storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_CODE),
                                storeDb.getStrFromColumn(childCursor, COLUMN_PROFESSIONS_LIST)
                        ));
                    }

                    universAdapter.notifyDataSetChanged();
                }

            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.calcBtn:
                Intent ballIntent = new Intent(AtauliGrantsUniversActivity.this, AtauliBallEsepteuActivity.class);

                Bundle grantBundle = new Bundle();
                grantBundle.putString("specCode", specCode);

                ballIntent.putExtras(grantBundle);
                startActivity(ballIntent);

                break;

            case R.id.fabBtn:
                Intent addAtauliBlock = new Intent(AtauliGrantsUniversActivity.this, AddAtauliGrant.class);
                Bundle bundle = new Bundle();
                bundle.putString("specCode", specCode);
                addAtauliBlock.putExtras(bundle);

                startActivityForResult(addAtauliBlock, 12);

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 12  && resultCode  == RESULT_OK) {
                String univerCode = data.getStringExtra("univerCode");
                Cursor childCursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_UNIVER_LIST, COLUMN_UNIVER_CODE, univerCode, COLUMN_UNIVER_CODE);
                if (((childCursor != null) && (childCursor.getCount() > 0))) {
                    childCursor.moveToFirst();

                    lstUnivers.add(new Univer(
                            storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_ID),
                            storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_IMAGE),
                            storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_NAME),
                            storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_PHON),
                            storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_LOCATION),
                            storeDb.getStrFromColumn(childCursor, COLUMN_UNIVER_CODE),
                            storeDb.getStrFromColumn(childCursor, COLUMN_PROFESSIONS_LIST)
                    ));
                }

                universAdapter.notifyDataSetChanged();
            }
        } catch (Exception ex) {
            Toast.makeText(AtauliGrantsUniversActivity.this, ex.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean isAdmin() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getEmail().contains("admin");
        }
        return false;
    }

    private void searchAddListener() {
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

    private void myRecyclerViewAddListeners() {

        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabBtn.isShown()) fabBtn.hide();

                if (dy > 0 || dy < 0 && calcBtn.isShown()) calcBtn.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(isAdmin()) fabBtn.show();
                    calcBtn.setVisibility(View.VISIBLE);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
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
                    Toast.makeText(AtauliGrantsUniversActivity.this, "Can not find " + versionName, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, getString(R.string.check_inet), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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














