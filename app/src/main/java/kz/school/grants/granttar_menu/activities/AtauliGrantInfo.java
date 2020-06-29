package kz.school.grants.granttar_menu.activities;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;

import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_KAZ_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_SPEC_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_18_19_KAZ_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_18_19_RUS_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_19_20_KAZ_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_19_20_RUS_COUNT;
import static kz.school.grants.database.StoreDatabase.TABLE_ATAULI_GRANTS;
import static kz.school.grants.database.StoreDatabase.TABLE_GRANTS;

public class AtauliGrantInfo extends AppCompatActivity {

    @BindView(R.id.y18_19_total)
    TextView y18_19_total;
    @BindView(R.id.y18_19_kaz)
    TextView y18_19_kaz;
    @BindView(R.id.y18_19_rus)
    TextView y18_19_rus;

    @BindView(R.id.y19_20_total)
    TextView y19_20_total;
    @BindView(R.id.y19_20kaz)
    TextView y19_20_kaz;
    @BindView(R.id.y19_20rus)
    TextView y19_20_rus;

    @BindView(R.id.yTotal)
    TextView yTotal;
    @BindView(R.id.yKaz)
    TextView yKaz;
    @BindView(R.id.yRus)
    TextView yRus;

    @BindView(R.id.kaz_max)
    TextView kaz_max;
    @BindView(R.id.kaz_min)
    TextView kaz_min;
    @BindView(R.id.kaz_ave)
    TextView kaz_ave;

    @BindView(R.id.rus_max)
    TextView rus_max;
    @BindView(R.id.rus_min)
    TextView rus_min;
    @BindView(R.id.rus_ave)
    TextView rus_ave;

    @BindView(R.id.auil_kaz_max)
    TextView auil_kaz_max;
    @BindView(R.id.auil_kaz_min)
    TextView auil_kaz_min;
    @BindView(R.id.auil_kaz_ave)
    TextView auil_kaz_ave;

    @BindView(R.id.auil_rus_max)
    TextView auil_rus_max;
    @BindView(R.id.auil_rus_min)
    TextView auil_rus_min;
    @BindView(R.id.auil_rus_ave)
    TextView auil_rus_ave;

    @BindView(R.id.auilLayout)
    LinearLayout auilLayout;

    String univerCode, specCode;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_info);
        ButterKnife.bind(this);

        setTitle("AtauliGrantInfo");
        initViews();

    }

    public void initViews() {
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            univerCode = bundle.getString("univerCode");
            specCode = bundle.getString("specCode");

            Log.i("AtauliGrantInfo", "univerCode: "+univerCode);

            Cursor cursor = storeDb.getCursorWhereEqualAndTo(sqdb, TABLE_ATAULI_GRANTS, "" + COLUMN_UNIVER_CODE, ""+ COLUMN_SPEC_CODE, ""+univerCode, ""+ specCode, "" + COLUMN_KAZ_MIN_POINT);
            Log.i("AtauliGrantInfo", "cursor: "+cursor.getCount());

            if (((cursor != null) && (cursor.getCount() > 0))) {
                cursor.moveToFirst();

                int y18_19_kazCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_18_19_KAZ_COUNT));
                int y18_19_rusCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_18_19_RUS_COUNT));
                int y18_19_TotalCount = y18_19_kazCount + y18_19_rusCount;

                int y19_20_kazCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_19_20_KAZ_COUNT));
                int y19_20_rusCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_19_20_RUS_COUNT));
                int y19_20_TotalCount = y19_20_kazCount + y19_20_rusCount;

                y18_19_total.setText("" + y18_19_TotalCount);
                y18_19_kaz.setText("" + y18_19_kazCount);
                y18_19_rus.setText("" + y18_19_rusCount);

                y19_20_total.setText("" + y19_20_TotalCount);
                y19_20_kaz.setText("" + y19_20_kazCount);
                y19_20_rus.setText("" + y19_20_rusCount);

                int yTotalInt = y19_20_TotalCount - y18_19_TotalCount;
                int yKazInt = y19_20_kazCount - y18_19_kazCount;
                int yRusInt = y19_20_rusCount - y18_19_rusCount;

                yTotal.setText("" + yTotalInt);
                yKaz.setText("" + yKazInt);
                yRus.setText("" + yRusInt);

                if (yTotalInt < 0) yTotal.setTextColor(getColor(R.color.red));
                if (yKazInt < 0)  yKaz.setTextColor(getColor(R.color.red));
                if (yRusInt < 0) yRus.setTextColor(getColor(R.color.red));

                kaz_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_MAX_POINT));
                kaz_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_MIN_POINT));
                kaz_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_AVE_POINT));

                rus_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_RUS_MAX_POINT));
                rus_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_RUS_MIN_POINT));
                rus_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_RUS_AVE_POINT));

                String [] auilStore = {
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_KAZ_MAX_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_KAZ_MIN_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_KAZ_AVE_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_MAX_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_MIN_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_AVE_POINT)};

                boolean auilKvota = false;
                for(String auilEnt: auilStore){
                    if(!auilEnt.equals("0")){
                        auilKvota = true;
                        break;
                    }
                }

                if(auilKvota) {
                    auil_kaz_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_KAZ_MAX_POINT));
                    auil_kaz_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_KAZ_MIN_POINT));
                    auil_kaz_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_KAZ_AVE_POINT));
                    auil_rus_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_MAX_POINT));
                    auil_rus_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_MIN_POINT));
                    auil_rus_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_AVE_POINT));
                }else{
                    auilLayout.setVisibility(View.GONE);
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_grants, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.grant_info:
                Dialog d = new Dialog(this);
                d.setCancelable(true);
                d.setContentView(R.layout.item_block_spec);
                d.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
