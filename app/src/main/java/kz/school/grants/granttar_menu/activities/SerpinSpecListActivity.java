package kz.school.grants.granttar_menu.activities;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.Objects;

import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.granttar_menu.adapters.AtauliSpecListAdapter;
import kz.school.grants.granttar_menu.models.OneUniver;
import kz.school.grants.granttar_menu.models.SpecItem;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.spec_menu.models.GrantCounts;

import static kz.school.grants.database.StoreDatabase.COLUMN_GRANT_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_SPEC_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_SPEC_NAME;
import static kz.school.grants.database.StoreDatabase.COLUMN_SPEC_SUBJECTS_PAIR;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_PAIR;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_18_19_KAZ_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_19_20_KAZ_COUNT;
import static kz.school.grants.database.StoreDatabase.TABLE_PROFILE_SUBJECTS;
import static kz.school.grants.database.StoreDatabase.TABLE_SERPIN_GRANTS;
import static kz.school.grants.database.StoreDatabase.TABLE_SERPIN_SPECS;
import static kz.school.grants.database.StoreDatabase.TABLE_VER;

public class SerpinSpecListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView profRecyclerview;
    private ArrayList<SpecItem> lstSpecs;
    private ArrayList<SpecItem> lstSpecsCopy;
    AtauliSpecListAdapter specListAdapter;
    private FirebaseAuth mAuth;
    private boolean adminSigned = false;
    private FloatingActionButton fabBtn;
    private View progressLoading;
    private SearchView searchView;
    boolean univerList = false;
    private ArrayList<String> lstProfs;
    private ArrayList<String> lstSubjectPair;
    Dialog addProfDialog;
    Spinner spinnerSubjectPair;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    Button addProf;
    private DatabaseReference mDatabaseRef;
    String curVersion;
    AutoCompleteTextView groupProfsAutoComplete;
    String subjectPair;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serpin_grants);
        setTitle("Серпін бағдарламалары");
        initViews();
        checkInternetConnection();
        checkVersion("serpin_ver");
    }

    public void initViews() {
        lstSpecs = new ArrayList<>();
        progressLoading = findViewById(R.id.llProgressBar);
        profRecyclerview = findViewById(R.id.profRecyclerview);
        fabBtn = findViewById(R.id.fabBtn);
        searchView = findViewById(R.id.searchView);
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        specListAdapter = new AtauliSpecListAdapter(this, lstSpecs);
        profRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        profRecyclerview.setAdapter(specListAdapter);

        if (isAdmin()) {
            adminSigned = true;
            fabBtn.setVisibility(View.VISIBLE);
            fabBtn.setOnClickListener(this);
        }

        addSearchListener();
        loadProfessionToDialog();

        profRecyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(this, profRecyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {

                        Intent intent = new Intent(SerpinSpecListActivity.this, SerpinGrantsUniversActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("specCode", lstSpecs.get(pos).getSpecCode());
                        bundle.putString("specName", lstSpecs.get(pos).getSpecName());
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {

                    }
                })
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calcBtn:

                break;

            case R.id.fabBtn:
                addProfDialog.show();
                break;

            case R.id.addProf:

                String specCode = groupProfsAutoComplete.getText().toString().split("-")[0].trim();
                String specName = groupProfsAutoComplete.getText().toString().split("-")[1].trim();
                SpecItem specItem = new SpecItem(specCode, specName, subjectPair);
                lstSpecs.add(specItem);
                specListAdapter.notifyDataSetChanged();

                mDatabaseRef.child("serpin_grant_list").child(specCode).setValue(specItem).addOnCompleteListener(task -> {
                    long inceradedVer = getIncreasedVersion();
                    mDatabaseRef.child("serpin_ver").setValue(inceradedVer);
                    Toast.makeText(SerpinSpecListActivity.this, "Серпін бағдарлама енгізілді"+inceradedVer, Toast.LENGTH_SHORT).show();

                    groupProfsAutoComplete.setText("");
                });

                addProfDialog.dismiss();
                break;

        }
    }

    private void fillGrantFromDB() {
        Cursor userCursor = storeDb.getCursorAll(sqdb, TABLE_SERPIN_SPECS);
        lstSpecs.clear();

        if (((userCursor != null) && (userCursor.getCount() > 0))) {
            while (userCursor.moveToNext()) {

                lstSpecs.add(new SpecItem(
                        storeDb.getStrFromColumn(userCursor, COLUMN_SPEC_CODE),
                        storeDb.getStrFromColumn(userCursor, COLUMN_SPEC_NAME),
                        storeDb.getStrFromColumn(userCursor, COLUMN_SPEC_SUBJECTS_PAIR)
                ));
            }
            lstSpecsCopy = (ArrayList<SpecItem>) lstSpecs.clone();
            progressLoading.setVisibility(View.GONE);
            specListAdapter.notifyDataSetChanged();
        }
    }

    private void refreshSerpinList() {
        mDatabaseRef.child("serpin_grant_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    lstSpecs.clear();
                    storeDb.cleanSerpin(sqdb);
                    for (DataSnapshot feed : dataSnapshot.getChildren()) {

                        SpecItem specItem = feed.getValue(SpecItem.class);
                        lstSpecs.add(specItem);

                        String grant_code = feed.getKey();

                        ContentValues profValues = new ContentValues();
                        profValues.put(COLUMN_SPEC_CODE, specItem.getSpecCode());
                        profValues.put(COLUMN_SPEC_NAME, specItem.getSpecName());
                        profValues.put(COLUMN_SPEC_SUBJECTS_PAIR, specItem.getSpecSubjectPair());
                        profValues.put(COLUMN_GRANT_CODE, grant_code);

                        sqdb.insert(TABLE_SERPIN_SPECS, null, profValues);

                        HashMap<String, OneUniver> universHashMap = specItem.getUnivers();
                        if (universHashMap != null) {
                            for (String univerCode : universHashMap.keySet()) {

                                ContentValues grantsValues = new ContentValues();
                                grantsValues.put(COLUMN_SPEC_CODE, specItem.getSpecCode());
                                grantsValues.put(COLUMN_UNIVER_CODE, universHashMap.get(univerCode).getUniverCode());
                                grantsValues.put(COLUMN_YEAR_18_19_KAZ_COUNT, Integer.parseInt("" + universHashMap.get(univerCode).getGrant18_19().get("kaz")));
                                grantsValues.put(COLUMN_YEAR_19_20_KAZ_COUNT, Integer.parseInt("" + universHashMap.get(univerCode).getGrant19_20().get("kaz")));

                                HashMap<String, GrantCounts> entBalldari = universHashMap.get(univerCode).getJalpiEnt();
                                grantsValues.put(COLUMN_KAZ_MAX_POINT, entBalldari.get("kaz").getMax());
                                grantsValues.put(COLUMN_KAZ_MIN_POINT, entBalldari.get("kaz").getMin());
                                grantsValues.put(COLUMN_KAZ_AVE_POINT, entBalldari.get("kaz").getAve());

                                sqdb.insert(TABLE_SERPIN_GRANTS, null, grantsValues);

                            }
                        }

                    }

                    lstSpecsCopy = (ArrayList<SpecItem>) lstSpecs.clone();
                    progressLoading.setVisibility(View.GONE);
                    specListAdapter.notifyDataSetChanged();
                }else{
                    lstSpecs.clear();
                    storeDb.cleanSerpin(sqdb);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                        refreshSerpinList();
                    } else {
                        fillGrantFromDB();
                    }
                } else {
                    Toast.makeText(SerpinSpecListActivity.this, "Can not find " + versionName, Toast.LENGTH_SHORT).show();
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

    public long getIncreasedVersion() {
        long ver = Long.parseLong(curVersion);
        ver += 1;
        return ver;
    }

    public void loadProfessionToDialog() {
        String[] groupsStore = getResources().getStringArray(R.array.blockList);
        lstSubjectPair = new ArrayList<>();
        addProfDialog = new Dialog(this);
        addProfDialog.setContentView(R.layout.add_prof_to_grants);

        groupProfsAutoComplete = addProfDialog.findViewById(R.id.groupProfsAutoComplete);
        spinnerSubjectPair = addProfDialog.findViewById(R.id.spinnerSubjectPair);
        addProf = addProfDialog.findViewById(R.id.addProf);

        Cursor cursorSubjectPair = storeDb.getCursorAll(sqdb, TABLE_PROFILE_SUBJECTS);

        if (((cursorSubjectPair != null) && (cursorSubjectPair.getCount() > 0))) {
            while (cursorSubjectPair.moveToNext()) {
                lstSubjectPair.add(storeDb.getStrFromColumn(cursorSubjectPair, COLUMN_SUBJECTS_PAIR));
            }
        }

        groupProfsAutoComplete.setThreshold(2);
        groupProfsAutoComplete.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        addItemClickToHideKeyboard(groupProfsAutoComplete);

        groupProfsAutoComplete.setAdapter(new ArrayAdapter<>(this, R.layout.item_subject_pair, groupsStore));
        spinnerSubjectPair.setAdapter(new ArrayAdapter<>(this, R.layout.item_subject_pair, lstSubjectPair));
        subjectPair = lstSubjectPair.get(0);

        spinnerSubjectPair.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                subjectPair = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addProf.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if (univerList) {
            profRecyclerview.setAdapter(specListAdapter);
            univerList = false;
        } else {
            super.onBackPressed();
        }
    }

    public void addSearchListener() {
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

    public void filter(String text) {

        lstSpecs.clear();

        if (text.isEmpty()) {
            lstSpecs.addAll(lstSpecsCopy);

        } else {
            text = text.toLowerCase();
            for (SpecItem item : lstSpecsCopy) {

                if (item.getSpecName().toLowerCase().contains(text) || item.getSpecName().toUpperCase().contains(text) ||
                        item.getSpecCode().toLowerCase().contains(text) || item.getSpecCode().toUpperCase().contains(text) ||
                        item.getSpecSubjectPair().toLowerCase().contains(text) || item.getSpecSubjectPair().toUpperCase().contains(text)) {

                    lstSpecs.add(item);
                }
            }
        }

        specListAdapter.notifyDataSetChanged();
    }

    private boolean isAdmin() {
        if (mAuth.getCurrentUser() != null) {
            return Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).contains("admin");
        }
        return false;
    }

    private boolean checkInternetConnection() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.check_inet), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void addItemClickToHideKeyboard(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}