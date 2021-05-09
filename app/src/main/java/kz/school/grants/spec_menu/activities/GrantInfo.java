package kz.school.grants.spec_menu.activities;

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

import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_BLOCK_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_PREVIOUS_YEAR_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_CURRENT_YEAR_COUNT;
import static kz.school.grants.database.StoreDatabase.TABLE_GRANTS;

public class GrantInfo extends AppCompatActivity {

    @BindView(R.id.y18_19_total)
    TextView y18_19_total;

    @BindView(R.id.y19_20_total)
    TextView y19_20_total;

    @BindView(R.id.yTotal)
    TextView yTotal;


    @BindView(R.id.ent_max)
    TextView ent_max;
    @BindView(R.id.ent_min)
    TextView ent_min;
    @BindView(R.id.ent_ave)
    TextView ent_ave;

    @BindView(R.id.auil_ent_max)
    TextView auil_ent_max;
    @BindView(R.id.auil_ent_min)
    TextView auil_ent_min;
    @BindView(R.id.auil_ent_ave)
    TextView auil_ent_ave;

    @BindView(R.id.auilLayout)
    LinearLayout auilLayout;

    String profName, blockCode;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_info);
        ButterKnife.bind(this);

        initViews();

    }

    public void initViews() {
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            profName = bundle.getString("profName");
            blockCode = bundle.getString("blockCode");

            setTitle(profName);
            Log.i("GrantInfo", "bCode: "+blockCode);

            Cursor cursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_GRANTS, "" + COLUMN_BLOCK_CODE, ""+blockCode, "" + COLUMN_BLOCK_CODE);
            Log.i("GrantInfo", "cursor: "+cursor.getCount());

            if (((cursor != null) && (cursor.getCount() > 0))) {
                cursor.moveToFirst();

                int y18_19_TotalCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_PREVIOUS_YEAR_COUNT));
                int y19_20_TotalCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_CURRENT_YEAR_COUNT));

                y18_19_total.setText("" + y18_19_TotalCount);
                y19_20_total.setText("" + y19_20_TotalCount);

                int yTotalInt = y19_20_TotalCount - y18_19_TotalCount;

                yTotal.setText("" + yTotalInt);

                if (yTotalInt < 0) yTotal.setTextColor(getColor(R.color.red));

                ent_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_MAX_POINT));
                ent_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_MIN_POINT));
                ent_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_KAZ_AVE_POINT));

                String [] auilStore = {
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_MAX_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_MIN_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_AVE_POINT)};

                boolean auilKvota = false;
                for(String auilEnt: auilStore){
                    if(!auilEnt.equals("0")){
                        auilKvota = true;
                        break;
                    }
                }

                if(auilKvota) {
                    auil_ent_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_MAX_POINT));
                    auil_ent_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_MIN_POINT));
                    auil_ent_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_AVE_POINT));

                }else{
                    auilLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    /*

    COLUMN_YEAR_18_19_KAZ_COUNT
    COLUMN_YEAR_18_19_RUS_COUNT
    COLUMN_YEAR_19_20_KAZ_COUNT
    COLUMN_YEAR_19_20_RUS_COUNT
    COLUMN_KAZ_MAX_POINT
    COLUMN_KAZ_MIN_POINT
    COLUMN_KAZ_AVE_POINT
    COLUMN_RUS_MAX_POINT
    COLUMN_RUS_MIN_POINT
    COLUMN_RUS_AVE_POINT

    COLUMN_AUIL_KAZ_MAX_POINT
    COLUMN_AUIL_KAZ_MIN_POINT
    COLUMN_AUIL_KAZ_AVE_POINT
    COLUMN_AUIL_RUS_MAX_POINT
    COLUMN_AUIL_RUS_MIN_POINT
    COLUMN_AUIL_RUS_AVE_POINT

     */
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
