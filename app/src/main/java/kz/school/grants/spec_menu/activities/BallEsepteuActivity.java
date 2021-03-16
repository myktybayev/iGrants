package kz.school.grants.spec_menu.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.spec_menu.adapters.BallEsepteuAdapter;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.spec_menu.adapters.SubjectPairAdapter;
import kz.school.grants.spec_menu.models.OneSpec;
import kz.school.grants.univer_menu.activities.ProfessionsActivity;

import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_TITLE;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_ID;
import static kz.school.grants.database.StoreDatabase.TABLE_BLOCKS;
import static kz.school.grants.database.StoreDatabase.TABLE_GRANTS;
import static kz.school.grants.database.StoreDatabase.TABLE_PROFESSIONS;

public class BallEsepteuActivity extends AppCompatActivity implements View.OnClickListener {

    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    int step = 1;
    int max = 140;
    int min = 50;
    LinearLayoutManager linearLayoutManager;
    BallEsepteuAdapter ballEsepteuAdapter;
    ArrayList<OneSpec> specList;

    @BindView(R.id.resultRecyclerView)
    RecyclerView resultRecyclerView;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.entBall)
    EditText entBall;
    @BindView(R.id.llProgressBar)
    View llProgressBar;
    @BindView(R.id.calcBtn)
    Button calcBtn;
    @BindView(R.id.auilGroup)
    CheckBox auilGroup;
    @BindView(R.id.kazGroup)
    RadioButton kazGroup;
    @BindView(R.id.rusGroup)
    RadioButton rusGroup;

    Bundle bundle;
    String subjectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balmen_esepteu);
        ButterKnife.bind(this);
        setTitle("BallEsepteuActivity");
        initViews();
    }

    public void initViews() {
        seekBar.setMax((max - min) / step);

        linearLayoutManager = new LinearLayoutManager(this);
        resultRecyclerView.setLayoutManager(linearLayoutManager);
        resultRecyclerView.setHasFixedSize(true);
        specList = new ArrayList<>();
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            subjectId = bundle.getString("subjectId");
        }

        ballEsepteuAdapter = new BallEsepteuAdapter(this, specList);
        resultRecyclerView.setAdapter(ballEsepteuAdapter);
        calcBtn.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                int value = min + (progress * step);
                entBall.setText("" + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        resultRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, resultRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {
                        String[] profSlit = specList.get(pos).getTitle().split("-");
                        String profCode = profSlit[0].trim();

//                        Toast.makeText(BallEsepteuActivity.this, profCode[0].trim()+"!!!", Toast.LENGTH_SHORT).show();

                        Intent grantInfo = new Intent(BallEsepteuActivity.this, SpecBarUniverlerActivity.class);

                        Bundle grantBundle = new Bundle();
                        grantBundle.putString("profFull", specList.get(pos).getTitle());
                        grantBundle.putString("profCode", profCode);
                        grantInfo.putExtras(grantBundle);
                        startActivity(grantInfo);

                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {

                    }
                })
        );
    }

    //        Cursor cursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_GRANTS, ""+COLUMN_SUBJECTS_ID, ""+subjectId, ""+COLUMN_KAZ_MIN_POINT);

    @Override
    public void onClick(View view) {
        specList.clear();
        llProgressBar.setVisibility(View.VISIBLE);
        String userScore = entBall.getText().toString();
        String columnSearchMax = COLUMN_KAZ_MAX_POINT;
        String columnSearchMin = COLUMN_KAZ_MIN_POINT;

        if (kazGroup.isChecked()) {
            if (auilGroup.isChecked()) {
                columnSearchMax = COLUMN_AUIL_KAZ_MAX_POINT;
                columnSearchMin = COLUMN_AUIL_KAZ_MIN_POINT;
            } else {
                columnSearchMax = COLUMN_KAZ_MAX_POINT;
                columnSearchMin = COLUMN_KAZ_MIN_POINT;
            }
        } else if (rusGroup.isChecked()) {
            if (auilGroup.isChecked()) {
                columnSearchMax = COLUMN_AUIL_RUS_MAX_POINT;
                columnSearchMin = COLUMN_AUIL_RUS_MIN_POINT;
            } else {
                columnSearchMax = COLUMN_RUS_MAX_POINT;
                columnSearchMin = COLUMN_RUS_MIN_POINT;
            }
        }

        Cursor cursor = sqdb.rawQuery(
                "SELECT * FROM " + TABLE_GRANTS + " where " + COLUMN_SUBJECTS_ID + " = ? AND " + columnSearchMin + " <= ?" + " ORDER BY " + columnSearchMin + " DESC ",
                new String[]{subjectId, userScore});

        if (((cursor != null) && (cursor.getCount() > 0))) {
            while (cursor.moveToNext()) {
                String blockCode = storeDb.getStrFromColumn(cursor, COLUMN_BLOCK_CODE);
                String maxScore = storeDb.getStrFromColumn(cursor, columnSearchMax);
                String minScore = storeDb.getStrFromColumn(cursor, columnSearchMin);
                Log.i("BallEsepteu", "blockCode: " + blockCode);

                if (blockCode.charAt(0) == 'B' && Integer.parseInt(minScore) > 0) {
                    Cursor cursorProf = storeDb.getCursorWhereEqualTo(sqdb, TABLE_PROFESSIONS, "" + COLUMN_BLOCK_CODE, "" + blockCode, "" + COLUMN_BLOCK_CODE);
                    if (((cursorProf != null) && (cursorProf.getCount() > 0))) {
                        while (cursorProf.moveToNext()) {
                            String profCode = storeDb.getStrFromColumn(cursorProf, COLUMN_PROF_CODE);
                            String profTitle = storeDb.getStrFromColumn(cursorProf, COLUMN_PROF_TITLE);

                            specList.add(new OneSpec(profCode + " - " + profTitle, maxScore, minScore));
                        }
                    }
                }
            }
        }

        ballEsepteuAdapter.notifyDataSetChanged();
        llProgressBar.setVisibility(View.GONE);
    }
}
