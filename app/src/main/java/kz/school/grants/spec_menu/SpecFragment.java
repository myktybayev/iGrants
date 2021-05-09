package kz.school.grants.spec_menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.spec_menu.activities.AddSubjects;
import kz.school.grants.spec_menu.activities.SpecialityActivity;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.spec_menu.adapters.SubjectPairAdapter;
import kz.school.grants.spec_menu.models.GrantCounts;
import kz.school.grants.spec_menu.models.OneBlockSpec;
import kz.school.grants.spec_menu.models.SubjectPair;

import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_TITLE;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_TITLE;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_PAIR;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECT_VER;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_PREVIOUS_YEAR_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_CURRENT_YEAR_COUNT;
import static kz.school.grants.database.StoreDatabase.TABLE_BLOCKS;
import static kz.school.grants.database.StoreDatabase.TABLE_GRANTS;
import static kz.school.grants.database.StoreDatabase.TABLE_PROFESSIONS;
import static kz.school.grants.database.StoreDatabase.TABLE_PROFILE_SUBJECTS;
import static kz.school.grants.database.StoreDatabase.TABLE_VER;

public class SpecFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.pairList)
    RecyclerView pairRecyclerView;
    private RecyclerView.LayoutManager linearLayoutManager;
    private List<SubjectPair> subjectPairList;
    private SubjectPairAdapter subjectPairAdapter;
    private View view;
    private FirebaseAuth mAuth;
    private boolean adminSigned = false;
    private FloatingActionButton fabBtn;
    private DatabaseReference mDatabaseRef;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    private String currentSubjectVersion;
    private View progressLoading;
    private String subjectsId;
    private int position;
    Dialog d;

    public SpecFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_spec, container, false);
        ButterKnife.bind(this, view);

        initViews();
        checkVersion();
        fillSubjects();
        onItemTouch();
        addListeners();

        return view;
    }

    private void initViews() {

        fabBtn = view.findViewById(R.id.fabBtn);
        progressLoading = view.findViewById(R.id.llProgressBar);
        mAuth = FirebaseAuth.getInstance();
        storeDb = new StoreDatabase(getActivity());
        sqdb = storeDb.getWritableDatabase();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        if (isAdmin()) {
            adminSigned = true;
            fabBtn.setVisibility(View.VISIBLE);
            fabBtn.setOnClickListener(this);
        }

        subjectPairList = new ArrayList<>();

//        subjectPairList.add(new SubjectPair("math_phys", "Математика-Физика", 32));
//        subjectPairList.add(new SubjectPair("math_phys", "Биология-География", 33));
//        subjectPairList.add(new SubjectPair("math_phys", "Химия-Биология", 15));
//        subjectPairList.add(new SubjectPair("math_phys", " Биология-Химия", 52));

        linearLayoutManager = new LinearLayoutManager(getActivity());
        pairRecyclerView = view.findViewById(R.id.pairList);
        pairRecyclerView.setLayoutManager(linearLayoutManager);
        pairRecyclerView.setHasFixedSize(true);
        subjectPairAdapter = new SubjectPairAdapter(Objects.requireNonNull(getActivity()), subjectPairList);

        pairRecyclerView.setAdapter(subjectPairAdapter);

    }

    public boolean isAdmin(){
        if(mAuth.getCurrentUser() != null){
            return mAuth.getCurrentUser().getEmail().contains("admin");
        }
        return false;
    }

    public void onItemTouch() {
        pairRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), pairRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {

                        Intent intent = new Intent(getActivity(), SpecialityActivity.class);

                        Bundle bundle = new Bundle();

                        bundle.putString("subjectPair", subjectPairList.get(pos).getPair());
                        bundle.putString("subjectId", subjectPairList.get(pos).getId());
                        intent.putExtras(bundle);

                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {
                        if (adminSigned) {
                            d = new Dialog(Objects.requireNonNull(getActivity()));
                            position = pos;

                            d.setContentView(R.layout.dialog_edit_subjects);
                            LinearLayout deleteLayout = d.findViewById(R.id.deleteLayout);
                            LinearLayout editLayout = d.findViewById(R.id.editLayout);
                            deleteLayout.setOnClickListener(SpecFragment.this);
                            editLayout.setOnClickListener(SpecFragment.this);
                            d.show();
                        }
                    }
                })
        );
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fabBtn:
                if (checkInternetConnection())
                    startActivity(new Intent(getActivity(), AddSubjects.class));

                break;
            case R.id.deleteLayout:
                new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                        .setTitle(subjectPairList.get(position).getPair())
                        .setMessage(getString(R.string.del_profile_subjects))

                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            subjectsId = subjectPairList.get(position).getId();
                            mDatabaseRef.child("profile_subjects").child(subjectsId).removeValue();
                            SubjectPair subjectPair = subjectPairList.get(position);
                            storeDb.deleteSubject(sqdb, subjectPair);
                            Toast.makeText(getActivity(), getString(R.string.profile_subjects_deleted), Toast.LENGTH_SHORT).show();
                            d.dismiss();

                            subjectPairList.remove(position);
                            subjectPairAdapter.notifyDataSetChanged();
                        })
                        .setNeutralButton(getString(R.string.no), (dialog, which) -> {

                        })
                        .show();
                break;

            case R.id.editLayout:

                break;
        }
    }

    private void addListeners() {
        mDatabaseRef.child("profile_subjects").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                SubjectPair subjectPair = dataSnapshot.getValue(SubjectPair.class);

//                Log.d("SpecFragment", "getGrant18_19: " + (hashMap.get("B057") != null ? hashMap.get("B057").getProfessions().get("5B060200") : "no data"));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                /*
                SubjectPair subjectPair = dataSnapshot.getValue(SubjectPair.class);
                storeDb.cleanBlocks(sqdb);

                String subjectId = dataSnapshot.getKey();
                HashMap<String, OneBlockSpec> blocksHashMap = subjectPair.getBlocks();

                if (blocksHashMap != null) {
                    for (String blockCode : blocksHashMap.keySet()) {
                        String blockTitle = blocksHashMap.get(blockCode).getTitle();

                        ContentValues blockValues = new ContentValues();
                        blockValues.put(COLUMN_SUBJECTS_ID, subjectId);
                        blockValues.put(COLUMN_BLOCK_CODE, blockCode);
                        blockValues.put(COLUMN_BLOCK_TITLE, blockTitle);

                        sqdb.insert(TABLE_BLOCKS, null, blockValues);

                        HashMap<String, String> profHaspMap = blocksHashMap.get(blockCode).getProfessions();
                        for (String pCode : profHaspMap.keySet()) {

                            String pTitle = profHaspMap.get(pCode);

                            ContentValues profValues = new ContentValues();
                            profValues.put(COLUMN_BLOCK_CODE, blockCode);
                            profValues.put(COLUMN_PROF_CODE, pCode);
                            profValues.put(COLUMN_PROF_TITLE, pTitle);

                            sqdb.insert(TABLE_PROFESSIONS, null, profValues);

                        }

                        HashMap<String, GrantCounts> entBalldari = blocksHashMap.get(blockCode).getJalpiEnt();

                        ContentValues grantsValues = new ContentValues();
                        grantsValues.put(COLUMN_SUBJECTS_ID, subjectId);
                        grantsValues.put(COLUMN_BLOCK_CODE, blockCode);
                        grantsValues.put(COLUMN_YEAR_18_19_KAZ_COUNT, Integer.parseInt("" + blocksHashMap.get(blockCode).getGrant18_19().get("kaz")));
                        grantsValues.put(COLUMN_YEAR_18_19_RUS_COUNT, Integer.parseInt("" + blocksHashMap.get(blockCode).getGrant18_19().get("rus")));

                        grantsValues.put(COLUMN_YEAR_19_20_KAZ_COUNT, Integer.parseInt("" + blocksHashMap.get(blockCode).getGrant19_20().get("kaz")));
                        grantsValues.put(COLUMN_YEAR_19_20_RUS_COUNT, Integer.parseInt("" + blocksHashMap.get(blockCode).getGrant19_20().get("rus")));

                        for (String entType : entBalldari.keySet()) {
                            if (entType.equals("auilKaz")) {
                                grantsValues.put(COLUMN_AUIL_KAZ_MAX_POINT, entBalldari.get(entType).getMax());
                                grantsValues.put(COLUMN_AUIL_KAZ_MIN_POINT, entBalldari.get(entType).getMin());
                                grantsValues.put(COLUMN_AUIL_KAZ_AVE_POINT, entBalldari.get(entType).getAve());

                            } else if (entType.equals("auilRus")) {
                                grantsValues.put(COLUMN_AUIL_RUS_MAX_POINT, entBalldari.get(entType).getMax());
                                grantsValues.put(COLUMN_AUIL_RUS_MIN_POINT, entBalldari.get(entType).getMin());
                                grantsValues.put(COLUMN_AUIL_RUS_AVE_POINT, entBalldari.get(entType).getAve());

                            } else if (entType.equals("kaz")) {
                                grantsValues.put(COLUMN_KAZ_MAX_POINT, entBalldari.get(entType).getMax());
                                grantsValues.put(COLUMN_KAZ_MIN_POINT, entBalldari.get(entType).getMin());
                                grantsValues.put(COLUMN_KAZ_AVE_POINT, entBalldari.get(entType).getAve());

                            } else if (entType.equals("rus")) {
                                grantsValues.put(COLUMN_RUS_MAX_POINT, entBalldari.get(entType).getMax());
                                grantsValues.put(COLUMN_RUS_MIN_POINT, entBalldari.get(entType).getMin());
                                grantsValues.put(COLUMN_RUS_AVE_POINT, entBalldari.get(entType).getAve());
                            }
                        }

                        sqdb.insert(TABLE_GRANTS, null, grantsValues);
                    }
                }
                */
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                SubjectPair subjectPair = dataSnapshot.getValue(SubjectPair.class);
                for (int i = 0; i < subjectPairList.size(); i++) {
                    if (subjectPairList.get(i).getId().equals(subjectPair.getId())) {
                        subjectPairList.remove(i);
                        subjectPairAdapter.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void fillSubjects() {
        Cursor userCursor = sqdb.rawQuery("SELECT * FROM " + TABLE_PROFILE_SUBJECTS + " ORDER BY " + COLUMN_PROF_COUNT + " DESC", null);
        subjectPairList.clear();

        if (((userCursor != null) && (userCursor.getCount() > 0))) {
            while (userCursor.moveToNext()) {
                subjectPairList.add(new SubjectPair(
                        userCursor.getString(userCursor.getColumnIndex(COLUMN_SUBJECTS_ID)),
                        userCursor.getString(userCursor.getColumnIndex(COLUMN_SUBJECTS_PAIR)),
                        userCursor.getInt(userCursor.getColumnIndex(COLUMN_PROF_COUNT)),
                        new HashMap<>()
                ));
            }
        }

        progressLoading.setVisibility(View.GONE);
        subjectPairAdapter.notifyDataSetChanged();
    }

    private void checkVersion() {
        mDatabaseRef.child("subjects_ver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String newVersion = dataSnapshot.getValue().toString();
                    if (!getCurrentSubjectVersion().equals(newVersion)) {

                        progressLoading.setVisibility(View.VISIBLE);
                        updateVersion(newVersion);
                        refreshSubjects();

                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.can_not_find_ver), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void refreshSubjects() {
        mDatabaseRef.child("profile_subjects").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    subjectPairList.clear();
                    storeDb.cleanSubjects(sqdb);
                    storeDb.cleanBlocks(sqdb);

                    for (DataSnapshot subjectSnapshot : dataSnapshot.getChildren()) {

                        SubjectPair subjectPair = subjectSnapshot.getValue(SubjectPair.class);
                        assert subjectPair != null;

                        String id = subjectPair.getId();
                        String pairTitle = subjectPair.getPair();
                        int count = subjectPair.getCount();

                        ContentValues value = new ContentValues();
                        value.put(COLUMN_SUBJECTS_ID, id);
                        value.put(COLUMN_SUBJECTS_PAIR, pairTitle);
                        value.put(COLUMN_PROF_COUNT, count);

                        sqdb.insert(TABLE_PROFILE_SUBJECTS, null, value);
                        subjectPairList.add(subjectPair);

                        String subjectId = subjectSnapshot.getKey();
                        HashMap<String, OneBlockSpec> blocksHashMap = subjectPair.getBlocks();

                        if (blocksHashMap != null) {
                            for (String blockCode : blocksHashMap.keySet()) {
                                String blockTitle = blocksHashMap.get(blockCode).getTitle();

                                ContentValues blockValues = new ContentValues();
                                blockValues.put(COLUMN_SUBJECTS_ID, subjectId);
                                blockValues.put(COLUMN_BLOCK_CODE, blockCode);
                                blockValues.put(COLUMN_BLOCK_TITLE, blockTitle);

                                sqdb.insert(TABLE_BLOCKS, null, blockValues);

                                HashMap<String, String> profHaspMap = blocksHashMap.get(blockCode).getProfessions();
                                for (String pCode : profHaspMap.keySet()) {

                                    String pTitle = profHaspMap.get(pCode);

                                    ContentValues profValues = new ContentValues();
                                    profValues.put(COLUMN_BLOCK_CODE, blockCode);
                                    profValues.put(COLUMN_SUBJECTS_PAIR, pairTitle);
                                    profValues.put(COLUMN_PROF_CODE, pCode);
                                    profValues.put(COLUMN_PROF_TITLE, pTitle);

                                    sqdb.insert(TABLE_PROFESSIONS, null, profValues);

                                }


                                ContentValues grantsValues = new ContentValues();
                                grantsValues.put(COLUMN_SUBJECTS_ID, subjectId);
                                grantsValues.put(COLUMN_BLOCK_CODE, blockCode);
                                grantsValues.put(COLUMN_YEAR_PREVIOUS_YEAR_COUNT, Integer.parseInt("" + blocksHashMap.get(blockCode).getGrant18_19().get("kaz")));
                                grantsValues.put(COLUMN_YEAR_CURRENT_YEAR_COUNT, Integer.parseInt("" + blocksHashMap.get(blockCode).getGrant19_20().get("kaz")));

                                HashMap<String, GrantCounts> entBalldari = blocksHashMap.get(blockCode).getJalpiEnt();
                                for (String entType : entBalldari.keySet()) {
                                    if (entType.equals("auilKaz")) {
                                        grantsValues.put(COLUMN_AUIL_ENT_MAX_POINT, entBalldari.get(entType).getMax());
                                        grantsValues.put(COLUMN_AUIL_ENT_MIN_POINT, entBalldari.get(entType).getMin());
                                        grantsValues.put(COLUMN_AUIL_ENT_AVE_POINT, entBalldari.get(entType).getAve());

                                    } else if (entType.equals("auilRus")) {
                                        grantsValues.put(COLUMN_AUIL_RUS_MAX_POINT, entBalldari.get(entType).getMax());
                                        grantsValues.put(COLUMN_AUIL_RUS_MIN_POINT, entBalldari.get(entType).getMin());
                                        grantsValues.put(COLUMN_AUIL_RUS_AVE_POINT, entBalldari.get(entType).getAve());

                                    } else if (entType.equals("kaz")) {
                                        grantsValues.put(COLUMN_KAZ_MAX_POINT, entBalldari.get(entType).getMax());
                                        grantsValues.put(COLUMN_KAZ_MIN_POINT, entBalldari.get(entType).getMin());
                                        grantsValues.put(COLUMN_KAZ_AVE_POINT, entBalldari.get(entType).getAve());

                                    } else if (entType.equals("rus")) {
                                        grantsValues.put(COLUMN_RUS_MAX_POINT, entBalldari.get(entType).getMax());
                                        grantsValues.put(COLUMN_RUS_MIN_POINT, entBalldari.get(entType).getMin());
                                        grantsValues.put(COLUMN_RUS_AVE_POINT, entBalldari.get(entType).getAve());
                                    }
                                }

                                sqdb.insert(TABLE_GRANTS, null, grantsValues);
                            }
                        }
                    }

                    progressLoading.setVisibility(View.GONE);
                    subjectPairAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void updateVersion(String version) {
        Cursor res = sqdb.rawQuery("SELECT " + COLUMN_SUBJECT_VER + " FROM " + TABLE_VER, null);
        res.moveToNext();
        String verStr = res.getString(0);

        ContentValues versionValues = new ContentValues();
        versionValues.put(COLUMN_SUBJECT_VER, version);
        sqdb.update(TABLE_VER, versionValues, COLUMN_SUBJECT_VER + "=" + verStr, null);

    }

    private String getCurrentSubjectVersion() {
        Cursor res = sqdb.rawQuery("SELECT " + COLUMN_SUBJECT_VER + " FROM " + TABLE_VER, null);
        res.moveToNext();
        currentSubjectVersion = res.getString(0);
        return currentSubjectVersion;
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

}
