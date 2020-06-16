package kz.school.grants.univer_menu.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.spec_menu.activities.GrantInfo;
import kz.school.grants.spec_menu.activities.SpecialityActivity;
import kz.school.grants.spec_menu.adapters.ExpandableListAdapter;
import kz.school.grants.univer_menu.models.Profession;
import kz.school.grants.univer_menu.models.Univer;

import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_TITLE;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROFESSIONS_LIST;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_TITLE;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_PAIR;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_ID;
import static kz.school.grants.database.StoreDatabase.TABLE_BLOCKS;
import static kz.school.grants.database.StoreDatabase.TABLE_PROFESSIONS;
import static kz.school.grants.database.StoreDatabase.TABLE_UNIVER_LIST;

public class ProfessionsActivity extends AppCompatActivity implements View.OnClickListener {

    private List<String> parent_title;
    private HashMap<String, List<String>> child_title;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    String subjectId;
    FloatingActionButton fabBtn;
    Button calcBtn;
    private FirebaseAuth mAuth;
    boolean adminSigned = false;
    Bundle bundle;
    Univer univer;
    TextView univerName, univerLocation, univerPhone, univerCode;
    HashMap<String, String> profParents;
    HashMap<String, Profession> profHashMap;
    ArrayList<Profession> profList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professions);
        setTitle("ProfessionsActivity");
        initViews();
    }

    public void initViews() {
        univerName = findViewById(R.id.univerName);
        univerLocation = findViewById(R.id.univerLocation);
        univerPhone = findViewById(R.id.univerPhone);
        univerCode = findViewById(R.id.univerCode);

        parent_title = new ArrayList<>();
        child_title = new HashMap<>();

        profHashMap = new HashMap<>();
        profParents = new HashMap<>();
        profList = new ArrayList<>();
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        bundle = getIntent().getExtras();
        initBundle(bundle);

        expandableListAdapter = new ExpandableListAdapter(parent_title, child_title, this);
        expandableListView = findViewById(R.id.expandable_list_view);


        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setGroupIndicator(null);

        addExpandableListeners();
        univerPhone.setOnClickListener(this);



    }

    public void initBundle(Bundle bundle) {
        if (bundle != null) {
            univer = (Univer) bundle.getSerializable("univer");
            univerName.setText(univer.getUniverName());
            univerLocation.setText(univer.getUniverLocation());
            univerPhone.setText(univer.getUniverPhone());
            univerCode.setText(univer.getUniverCode());

            Cursor cursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_UNIVER_LIST, COLUMN_UNIVER_ID, univer.getUniverId(), COLUMN_UNIVER_ID);

            if (((cursor != null) && (cursor.getCount() > 0))) {
                cursor.moveToFirst();
                if (storeDb.getStrFromColumn(cursor, COLUMN_PROFESSIONS_LIST) != null) {
                    String[] professions = storeDb.getStrFromColumn(cursor, COLUMN_PROFESSIONS_LIST).split(",");

                    for (String profCode : professions) {
                        Cursor childCursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_PROFESSIONS, COLUMN_PROF_CODE, profCode, COLUMN_PROF_CODE);

                        if (((childCursor != null) && (childCursor.getCount() > 0))) {
                            while (childCursor.moveToNext()) {
                                profList.add(new Profession(
                                        storeDb.getStrFromColumn(childCursor, COLUMN_BLOCK_CODE),
                                        storeDb.getStrFromColumn(childCursor, COLUMN_SUBJECTS_PAIR),
                                        storeDb.getStrFromColumn(childCursor, COLUMN_PROF_CODE),
                                        storeDb.getStrFromColumn(childCursor, COLUMN_PROF_TITLE)
                                ));

                                profParents.put(storeDb.getStrFromColumn(childCursor, COLUMN_SUBJECTS_PAIR), "value");
                            }
                        }
                    }
                    for (String subPair : profParents.keySet()) {
                        Log.i("ProfessionActivity", "subPair: " + subPair);
                        parent_title.add(subPair);

                        List<String> child = new ArrayList<>();
                        for (Profession profession : profList) {
                            if (profession.getSubjectPair().equals(subPair)) {
                                String pFull = profession.getProfCode() + " - " + profession.getProfTitle();
                                child.add(pFull);
                                profHashMap.put(pFull, profession);
                            }
                        }

                        child_title.put(subPair, child);
                    }


                /*
                String blockCode = storeDb.getStrFromColumn(cursor, COLUMN_BLOCK_CODE);
                String blockTitle = storeDb.getStrFromColumn(cursor, COLUMN_BLOCK_TITLE);
                String blockFull = blockCode + " - " + blockTitle;

                parent_title.add(blockFull);


                List<String> child = new ArrayList<>();
                Cursor childCursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_PROFESSIONS, COLUMN_BLOCK_CODE, blockCode, COLUMN_PROF_CODE);
                if (((childCursor != null) && (childCursor.getCount() > 0))) {
                    while (childCursor.moveToNext()) {

                        String profCode = storeDb.getStrFromColumn(childCursor, COLUMN_PROF_CODE);
                        String profTitle = storeDb.getStrFromColumn(childCursor, COLUMN_PROF_TITLE);
                        child.add(profCode + " - " + profTitle);
                    }
                }

                child_title.put(blockFull, child);
                */
                }
            }
        }

    }

    public void addExpandableListeners() {
        expandableListView.setOnGroupExpandListener(i -> {
//            Toast.makeText(SpecialityActivity.this, parent_title.get(i) + " expanded", Toast.LENGTH_SHORT).show();
        });

        expandableListView.setOnChildClickListener((expandableListView, view, pos, i1, l) -> {
            String profName = child_title.get(parent_title.get(pos)).get(i1);
            String bCode = profHashMap.get(profName).getBlockCode();

            Intent grantInfo = new Intent(ProfessionsActivity.this, GrantInfo.class);
            Bundle grantBundle = new Bundle();
            grantBundle.putString("blockCode", bCode);
            grantInfo.putExtras(grantBundle);
            startActivity(grantInfo);

            return false;
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.univerPhone:

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + univerPhone.getText()));
                startActivity(intent);

                break;
        }
    }

    private boolean isAdmin() {
        if (mAuth.getCurrentUser() != null) {
            return Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).contains("admin");
        }
        return false;
    }
}
