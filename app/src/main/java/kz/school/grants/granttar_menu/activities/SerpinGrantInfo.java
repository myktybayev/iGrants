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
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_18_19_KAZ_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_18_19_RUS_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_19_20_KAZ_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_19_20_RUS_COUNT;
import static kz.school.grants.database.StoreDatabase.TABLE_ATAULI_GRANTS;
import static kz.school.grants.database.StoreDatabase.TABLE_SERPIN_GRANTS;

public class SerpinGrantInfo extends AppCompatActivity {

    @BindView(R.id.y18_19_kaz)
    TextView y18_19_kaz;
    @BindView(R.id.y19_20kaz)
    TextView y19_20_kaz;

    @BindView(R.id.yKaz)
    TextView yKaz;

    @BindView(R.id.kaz_max)
    TextView kaz_max;
    @BindView(R.id.kaz_min)
    TextView kaz_min;
    @BindView(R.id.kaz_ave)
    TextView kaz_ave;

    String univerCode;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serpin_spec_info);
        ButterKnife.bind(this);

        setTitle("SerpinGrantInfo");
        initViews();

    }

    public void initViews() {
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            univerCode = bundle.getString("univerCode");

            Cursor cursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_SERPIN_GRANTS, "" + COLUMN_UNIVER_CODE, ""+univerCode, "" + COLUMN_UNIVER_CODE);
            Log.i("SerpinGrantInfo", "cursor: "+cursor.getCount());

            if (((cursor != null) && (cursor.getCount() > 0))) {
                cursor.moveToFirst();

                int y18_19_kazCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_18_19_KAZ_COUNT));
                int y19_20_kazCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_19_20_KAZ_COUNT));

                y18_19_kaz.setText("" + y18_19_kazCount);
                y19_20_kaz.setText("" + y19_20_kazCount);

                int yKazInt = y19_20_kazCount - y18_19_kazCount;

                yKaz.setText("" + yKazInt);

                if (yKazInt < 0)  yKaz.setTextColor(getColor(R.color.red));

                kaz_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_MAX_POINT));
                kaz_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_MIN_POINT));
                kaz_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_AVE_POINT));
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
