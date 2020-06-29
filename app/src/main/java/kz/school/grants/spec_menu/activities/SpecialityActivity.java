package kz.school.grants.spec_menu.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.spec_menu.adapters.ExpandableListAdapter;

import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_TITLE;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_TITLE;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_ID;
import static kz.school.grants.database.StoreDatabase.TABLE_BLOCKS;
import static kz.school.grants.database.StoreDatabase.TABLE_PROFESSIONS;

public class SpecialityActivity extends AppCompatActivity implements View.OnClickListener {

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
    int LAUNCH_ADD_BLOCK_ACTIVITY = 101;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speciality);
        setTitle("SpecialityActivity");
        initViews();
    }

    public void initViews() {
        parent_title = new ArrayList<>();
        child_title = new HashMap<>();
        fabBtn = findViewById(R.id.fabBtn);
        calcBtn = findViewById(R.id.calcBtn);
        fabBtn.setOnClickListener(this);
        calcBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();

        if (isAdmin()) {
            adminSigned = true;
            fabBtn.setVisibility(View.VISIBLE);
            fabBtn.setOnClickListener(this);
        }

        expandableListAdapter = new ExpandableListAdapter(parent_title, child_title, this);
        expandableListView = findViewById(R.id.expandable_list_view);
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        bundle = getIntent().getExtras();
        initBundle(bundle);

        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setGroupIndicator(null);
        expandableListView.setOnGroupExpandListener(i -> {
//            Toast.makeText(SpecialityActivity.this, parent_title.get(i) + " expanded", Toast.LENGTH_SHORT).show();
        });

        expandableListView.setOnChildClickListener((expandableListView, view, i, i1, l) -> {

            String blockTitle = parent_title.get(i);

            final String bCode = blockTitle.substring(0, blockTitle.indexOf("-")).trim();

            Log.i("SpecialityActivity", "bCode: "+bCode);

            Intent grantInfo = new Intent(SpecialityActivity.this, GrantInfo.class);

            Bundle grantBundle = new Bundle();
            grantBundle.putString("blockCode", bCode);
            grantInfo.putExtras(grantBundle);
            startActivity(grantInfo);
            return true;
        });

        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(SpecialityActivity.this, "Мамандық жайлы инфо шығады, жақында!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    public void initBundle(Bundle bundle){
        if (bundle != null) {
            subjectId = bundle.getString("subjectId");

            Cursor cursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_BLOCKS, COLUMN_SUBJECTS_ID, subjectId, COLUMN_BLOCK_CODE);

            if (((cursor != null) && (cursor.getCount() > 0))) {
                while (cursor.moveToNext()) {

                    String blockCode = storeDb.getStrFromColumn(cursor, COLUMN_BLOCK_CODE);
                    String blockTitle = storeDb.getStrFromColumn(cursor, COLUMN_BLOCK_TITLE);
                    String blockFull = blockCode + " - " + blockTitle;

                    Log.i("SpecialityActivity", "blockCode: "+blockCode);

                    if(blockCode.charAt(0) == 'B') {
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
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.calcBtn:

                Intent ballIntent = new Intent(SpecialityActivity.this, BallEsepteuActivity.class);

                Bundle grantBundle = new Bundle();
                grantBundle.putString("subjectId", subjectId);

                ballIntent.putExtras(grantBundle);
                startActivity(ballIntent);

                break;

            case R.id.fabBtn:

                Intent addBlock = new Intent(SpecialityActivity.this, AddBlock.class);

                Bundle blockBundle = new Bundle();
                blockBundle.putString("subjectId", subjectId);

                addBlock.putExtras(blockBundle);
                startActivity(addBlock);

                break;

        }
    }

    private boolean isAdmin(){
        if(mAuth.getCurrentUser() != null){
            return Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).contains("admin");
        }
        return false;
    }
}
