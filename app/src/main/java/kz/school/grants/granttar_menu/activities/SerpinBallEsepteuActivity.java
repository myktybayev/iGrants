package kz.school.grants.granttar_menu.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.granttar_menu.adapters.AtauliBallEsepteuAdapter;
import kz.school.grants.granttar_menu.models.OneBallEseptuUniver;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.univer_menu.models.Univer;

import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MIN_POINT;
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

public class SerpinBallEsepteuActivity extends AppCompatActivity implements View.OnClickListener {

    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    int step = 1;
    int max = 140;
    int min = 50;
    LinearLayoutManager linearLayoutManager;
    AtauliBallEsepteuAdapter ballEsepteuAdapter;
    ArrayList<OneBallEseptuUniver> specList;

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

    @BindView(R.id.univerNotFound)
    TextView univerNotFound;

    Bundle bundle;
    String specFull, specCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serpin_balmen_esepteu);
        ButterKnife.bind(this);
        setTitle("Серпін балл есептеу");

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
            specFull = bundle.getString("specFull");
            specCode = bundle.getString("specCode");
        }

        ballEsepteuAdapter = new AtauliBallEsepteuAdapter(this, specList);
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

                        Intent intent = new Intent(SerpinBallEsepteuActivity.this, SerpinGrantInfo.class);

                        Bundle bundle = new Bundle();
                        bundle.putString("specCode", specCode);
                        bundle.putString("specFull", specFull);
                        bundle.putString("univerName", specList.get(pos).getUniver().getUniverName());
                        bundle.putString("univerCode", specList.get(pos).getUniver().getUniverCode());
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
        specList.clear();
        llProgressBar.setVisibility(View.VISIBLE);
        String userScore = entBall.getText().toString();
        String columnSearchMax = COLUMN_KAZ_MAX_POINT;
        String columnSearchMin = COLUMN_KAZ_MIN_POINT;

        Cursor cursor = sqdb.rawQuery(
                "SELECT * FROM " + TABLE_SERPIN_GRANTS + " where " + COLUMN_SPEC_CODE + " = ? AND " + columnSearchMin + " <= ?" + " ORDER BY " + columnSearchMin + " DESC ",
                new String[]{specCode, userScore});

        if (((cursor != null) && (cursor.getCount() > 0) )) {
            while (cursor.moveToNext()) {
                String univerCode = storeDb.getStrFromColumn(cursor, COLUMN_UNIVER_CODE);
                String maxScore = storeDb.getStrFromColumn(cursor, columnSearchMax);
                String minScore = storeDb.getStrFromColumn(cursor, columnSearchMin);

                if(Integer.parseInt(minScore) > 0) {
                    Cursor cursorProf = storeDb.getCursorWhereEqualTo(sqdb, TABLE_UNIVER_LIST, "" + COLUMN_UNIVER_CODE, "" + univerCode, "" + COLUMN_UNIVER_CODE);
                    if (((cursorProf != null) && (cursorProf.getCount() > 0))) {
                        univerNotFound.setVisibility(View.GONE);

                        while (cursorProf.moveToNext()) {

                            Univer univer = new Univer(
                                    storeDb.getStrFromColumn(cursorProf, COLUMN_UNIVER_ID),
                                    storeDb.getStrFromColumn(cursorProf, COLUMN_UNIVER_IMAGE),
                                    storeDb.getStrFromColumn(cursorProf, COLUMN_UNIVER_NAME),
                                    storeDb.getStrFromColumn(cursorProf, COLUMN_UNIVER_PHON),
                                    storeDb.getStrFromColumn(cursorProf, COLUMN_UNIVER_LOCATION),
                                    storeDb.getStrFromColumn(cursorProf, COLUMN_UNIVER_CODE));

                            specList.add(new OneBallEseptuUniver(univer, maxScore, minScore));
                        }
                    }
                }else{
                    univerNotFound.setVisibility(View.VISIBLE);
                }
            }
        }else{
            univerNotFound.setVisibility(View.VISIBLE);
        }

        ballEsepteuAdapter.notifyDataSetChanged();
        llProgressBar.setVisibility(View.GONE);
    }
}
