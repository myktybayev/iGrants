package kz.school.grants.granttar_menu.activities;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;

import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_ENT_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_AUIL_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_RUS_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_SPEC_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_PREVIOUS_YEAR_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_CURRENT_YEAR_COUNT;
import static kz.school.grants.database.StoreDatabase.TABLE_ATAULI_GRANTS;

public class AtauliGrantInfo extends AppCompatActivity {

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

    @BindView(R.id.univerName)
    TextView tv_univerName;
    @BindView(R.id.specFull)
    TextView tv_specFull;

    String specFull, univerCode, univerName, specCode;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    boolean auilKvota = false;
    private DatabaseReference mDatabaseBlockRef, mDatabaseRef;
    String version = "-1";
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atauli_spec_info);
        ButterKnife.bind(this);

        setTitle("Атаулы грант");
        initViews();
        initIncreaseVersion();

    }

    public void initViews() {
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

            specCode = bundle.getString("specCode");
            specFull = bundle.getString("specFull");
            univerCode = bundle.getString("univerCode");
            univerName = bundle.getString("univerName");

            mDatabaseBlockRef = mDatabaseRef.child("atauli_grant_list").child(specCode).child("univers");

            tv_univerName.setText(univerName);
            tv_specFull.setText(specFull);

            Cursor cursor = storeDb.getCursorWhereEqualAndTo(sqdb, TABLE_ATAULI_GRANTS, "" + COLUMN_UNIVER_CODE, "" + COLUMN_SPEC_CODE, "" + univerCode, "" + specCode, "" + COLUMN_KAZ_MIN_POINT);

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

                String[] auilStore = {
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_MAX_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_MIN_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_AVE_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_MAX_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_MIN_POINT),
                        storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_AVE_POINT)};

                auilKvota = false;
                for (String auilEnt : auilStore) {
                    if (!auilEnt.equals("0")) {
                        auilKvota = true;
                        break;
                    }
                }

                if (auilKvota) {
                    auil_kaz_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_MAX_POINT));
                    auil_kaz_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_MIN_POINT));
                    auil_kaz_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_ENT_AVE_POINT));
                    auil_rus_max.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_MAX_POINT));
                    auil_rus_min.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_MIN_POINT));
                    auil_rus_ave.setText(storeDb.getIntegerFromColumn(cursor, COLUMN_AUIL_RUS_AVE_POINT));
                } else {
                    auilLayout.setVisibility(View.GONE);
                }
            }
        }
    }

    private MenuItem itemEdit;
    private MenuItem itemSave;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isAdmin()) {

            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.edit_grants, menu);

            itemEdit = menu.findItem(R.id.edit);
            itemSave = menu.findItem(R.id.save);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.edit:

                initEditViews();
                itemEdit.setVisible(false);
                itemSave.setVisible(true);


                break;

            case R.id.save:

                new AlertDialog.Builder(this, R.style.AlertDialogTheme)
                        .setTitle("Сақтау")
                        .setMessage("Өзгерістерді сақтауды қалайсыз ба?")

                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            saveGrantInfo();
                        })
                        .setNeutralButton(getString(R.string.no), (dialog, which) -> {

                        })
                        .show();

//                Toast.makeText(this, "Бағдарлама код: "+specCode, Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "Универ код: "+univerCode, Toast.LENGTH_SHORT).show();
                //increment atauli_grant_list_ver versions
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveGrantInfo() {


        HashMap<String, Long> grant18_19 = new HashMap<>();
        grant18_19.put("kaz", toLong(ed_y18_19_kaz));
        grant18_19.put("rus", toLong(ed_y18_19_rus));

        HashMap<String, Long> grant19_20 = new HashMap<>();
        grant19_20.put("kaz", toLong(ed_y19_20_kaz));
        grant19_20.put("rus", toLong(ed_y19_20_rus));

        HashMap<String, Long> kazEnt = new HashMap<>();
        kazEnt.put("ave", toLong(ed_kaz_ave));
        kazEnt.put("max", toLong(ed_kaz_max));
        kazEnt.put("min", toLong(ed_kaz_min));

        HashMap<String, Long> rusEnt = new HashMap<>();
        rusEnt.put("ave", toLong(ed_rus_ave));
        rusEnt.put("max", toLong(ed_rus_max));
        rusEnt.put("min", toLong(ed_rus_min));

        if (auilKvota) {

            HashMap<String, Long> auilKaz = new HashMap<>();
            auilKaz.put("ave", toLong(ed_auil_kaz_ave));
            auilKaz.put("max", toLong(ed_auil_kaz_max));
            auilKaz.put("min", toLong(ed_auil_kaz_min));

            HashMap<String, Long> auilRus = new HashMap<>();
            auilRus.put("ave", toLong(ed_auil_rus_ave));
            auilRus.put("max", toLong(ed_auil_rus_max));
            auilRus.put("min", toLong(ed_auil_rus_min));

            mDatabaseBlockRef.child(univerCode).child("jalpiEnt").child("auilKaz").setValue(auilKaz);
            mDatabaseBlockRef.child(univerCode).child("jalpiEnt").child("auilRus").setValue(auilRus);

        }


        mDatabaseBlockRef.child(univerCode).child("grant18_19").setValue(grant18_19);
        mDatabaseBlockRef.child(univerCode).child("grant19_20").setValue(grant19_20);
        mDatabaseBlockRef.child(univerCode).child("jalpiEnt").child("kaz").setValue(kazEnt);
        mDatabaseBlockRef.child(univerCode).child("jalpiEnt").child("rus").setValue(rusEnt);

        mDatabaseBlockRef.child(univerCode).child("univerCode").setValue(univerCode).addOnCompleteListener(task -> {
            Toast.makeText(AtauliGrantInfo.this, "Атаулы грант өзгертілді", Toast.LENGTH_LONG).show();
            mDatabaseRef.child("atauli_grant_list_ver").setValue(getIncreasedVersion());
            finish();
        });
    }

    EditText ed_y18_19_kaz;
    EditText ed_y18_19_rus;
    EditText ed_y19_20_kaz;
    EditText ed_y19_20_rus;
    EditText ed_kaz_max;
    EditText ed_kaz_min;
    EditText ed_kaz_ave;
    EditText ed_rus_max;
    EditText ed_rus_min;
    EditText ed_rus_ave;
    EditText ed_auil_kaz_max;
    EditText ed_auil_kaz_min;
    EditText ed_auil_kaz_ave;
    EditText ed_auil_rus_max;
    EditText ed_auil_rus_min;
    EditText ed_auil_rus_ave;


    private void initEditViews() {
        setContentView(R.layout.activity_edit_atauli_spec_info);

        tv_univerName = findViewById(R.id.univerName);
        tv_specFull = findViewById(R.id.specFull);

        tv_univerName.setText(univerName);
        tv_specFull.setText(specFull);

        yTotal = findViewById(R.id.yTotal);

        y18_19_total = findViewById(R.id.y18_19_total);
        y19_20_total = findViewById(R.id.y19_20_total);

        ed_y18_19_kaz = findViewById(R.id.y18_19_kaz);
        ed_y18_19_rus = findViewById(R.id.y18_19_rus);
        ed_y19_20_kaz = findViewById(R.id.y19_20kaz);
        ed_y19_20_rus = findViewById(R.id.y19_20rus);
        ed_kaz_max = findViewById(R.id.kaz_max);
        ed_kaz_min = findViewById(R.id.kaz_min);
        ed_kaz_ave = findViewById(R.id.kaz_ave);
        ed_rus_max = findViewById(R.id.rus_max);
        ed_rus_min = findViewById(R.id.rus_min);
        ed_rus_ave = findViewById(R.id.rus_ave);

        ed_kaz_max.setText(ent_max.getText().toString());
        ed_kaz_min.setText(ent_min.getText().toString());
        ed_kaz_ave.setText(ent_ave.getText().toString());

        auilLayout = findViewById(R.id.auilLayout);
        if (auilKvota) {
            ed_auil_kaz_max = findViewById(R.id.auil_kaz_max);
            ed_auil_kaz_min = findViewById(R.id.auil_kaz_min);
            ed_auil_kaz_ave = findViewById(R.id.auil_kaz_ave);
            ed_auil_rus_max = findViewById(R.id.auil_rus_max);
            ed_auil_rus_min = findViewById(R.id.auil_rus_min);
            ed_auil_rus_ave = findViewById(R.id.auil_rus_ave);

            ed_auil_kaz_max.setText(auil_kaz_max.getText().toString());
            ed_auil_kaz_min.setText(auil_kaz_min.getText().toString());
            ed_auil_kaz_ave.setText(auil_kaz_ave.getText().toString());
            ed_auil_rus_max.setText(auil_rus_max.getText().toString());
            ed_auil_rus_min.setText(auil_rus_min.getText().toString());
            ed_auil_rus_ave.setText(auil_rus_ave.getText().toString());
        } else {
            auilLayout.setVisibility(View.GONE);
        }

        addTotalCalc(y18_19_total, ed_y18_19_kaz, ed_y18_19_rus);
        addTotalCalc(y19_20_total, ed_y19_20_kaz, ed_y19_20_rus);

        addTotalDiff(yKaz, ed_y18_19_kaz, ed_y19_20_kaz);
        addTotalDiff(yRus, ed_y18_19_rus, ed_y19_20_rus);
    }

    public void totalDiff() {
        int res = Integer.parseInt(yKaz.getText().toString()) + Integer.parseInt(yRus.getText().toString());
        yTotal.setText("" + (res));
    }

    public void addTotalCalc(TextView resTextView, EditText editText1, EditText editText2) {
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!checkEmpty(editText1) && !checkEmpty(editText2)) {
                    int res = Integer.parseInt(toStr(editText1)) + Integer.parseInt(toStr(editText2));
                    resTextView.setText("" + (res));
                }
            }
        });
    }

    public void addTotalDiff(TextView resTextView, EditText y18_19, EditText y19_20) {
        y19_20.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!checkEmpty(y18_19) && !checkEmpty(y19_20)) {
                    int res = Integer.parseInt(toStr(y19_20)) - Integer.parseInt(toStr(y18_19));
                    if (res < 0) {
                        resTextView.setTextColor(getColor(R.color.red));
                    } else {
                        resTextView.setTextColor(getColor(R.color.green));
                    }
                    resTextView.setText("" + (res));
                    totalDiff();
                }
            }
        });
    }

    public boolean checkEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() == 0) {
            return true;
        }

        return false;
    }

    public String toStr(EditText editText) {
        if (checkEmpty(editText))
            return "0";

        return editText.getText().toString().replace(" ", "");
    }

    public Long toLong(EditText editText) {
        if (checkEmpty(editText))
            return 0L;

        return Long.parseLong(editText.getText().toString().trim());
    }

    public void initIncreaseVersion() {
        mDatabaseRef.child("atauli_grant_list_ver").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    version = dataSnapshot.getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public long getIncreasedVersion() {
        if (version.equals("-1")) version = "" + (new Date().getTime() % 1000);

        long ver = Long.parseLong(version);
        ver += 1;
        return ver;
    }

    public boolean isAdmin() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getEmail().contains("admin");
        }
        return false;
    }

}
