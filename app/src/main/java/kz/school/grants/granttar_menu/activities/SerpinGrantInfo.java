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
import android.widget.EditText;
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

import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_AVE_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MAX_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_KAZ_MIN_POINT;
import static kz.school.grants.database.StoreDatabase.COLUMN_SPEC_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_PREVIOUS_YEAR_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_YEAR_CURRENT_YEAR_COUNT;
import static kz.school.grants.database.StoreDatabase.TABLE_SERPIN_GRANTS;

public class SerpinGrantInfo extends AppCompatActivity {

    @BindView(R.id.y18_19_kaz) EditText y18_19_kaz;
    @BindView(R.id.y19_20kaz) EditText y19_20_kaz;

    @BindView(R.id.yKaz)
    TextView yKaz;

    @BindView(R.id.kaz_max) EditText kaz_max;
    @BindView(R.id.kaz_min) EditText kaz_min;
    @BindView(R.id.kaz_ave) EditText kaz_ave;

    @BindView(R.id.univerName)
    TextView tv_univerName;
    @BindView(R.id.specFull)
    TextView tv_specFull;

    String univerName, univerCode, specFull, specCode;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    private DatabaseReference mDatabaseBlockRef, mDatabaseRef;
    private FirebaseAuth mAuth;
    EditText[] editTextArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serpin_spec_info);
        ButterKnife.bind(this);
        setTitle("Серпін");
        initViews();
        initIncreaseVersion();

    }

    public void initViews() {
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        editTextArray = new EditText[] {y18_19_kaz, y19_20_kaz, kaz_max, kaz_min, kaz_ave};

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            specFull = bundle.getString("specFull");
            univerCode = bundle.getString("univerCode");
            univerName = bundle.getString("univerName");
            specCode = bundle.getString("specCode");

            mDatabaseBlockRef = mDatabaseRef.child("serpin_grant_list").child(specCode).child("univers");

            tv_univerName.setText(univerName);
            tv_specFull.setText(specFull);

            Cursor cursor = storeDb.getCursorWhereEqualAndTo(sqdb, TABLE_SERPIN_GRANTS, "" + COLUMN_UNIVER_CODE,""+ COLUMN_SPEC_CODE, ""+univerCode, ""+ specCode, "" + COLUMN_UNIVER_CODE);

            if (((cursor != null) && (cursor.getCount() > 0))) {
                cursor.moveToFirst();

                int y18_19_kazCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_PREVIOUS_YEAR_COUNT));
                int y19_20_kazCount = Integer.parseInt(storeDb.getIntegerFromColumn(cursor, COLUMN_YEAR_CURRENT_YEAR_COUNT));

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
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initEditViews() {
        for(EditText editText: editTextArray){
            editText.setBackgroundDrawable(getDrawable(R.drawable.border_black));
            editText.setFocusableInTouchMode(true);
            editText.setFocusable(true);
            editText.setEnabled(true);
        }
        addTotalDiff(yKaz, y18_19_kaz, y19_20_kaz);
    }

    private void saveGrantInfo(){
        HashMap<String, Long> grant18_19 = new HashMap<>();
        grant18_19.put("kaz", toLong(y18_19_kaz));

        HashMap<String, Long> grant19_20 = new HashMap<>();
        grant19_20.put("kaz", toLong(y19_20_kaz));

        HashMap<String, Long> kazEnt = new HashMap<>();
        kazEnt.put("ave", toLong(kaz_ave));
        kazEnt.put("max", toLong(kaz_max));
        kazEnt.put("min", toLong(kaz_min));

        mDatabaseBlockRef.child(univerCode).child("grant18_19").setValue(grant18_19);
        mDatabaseBlockRef.child(univerCode).child("grant19_20").setValue(grant19_20);
        mDatabaseBlockRef.child(univerCode).child("jalpiEnt").child("kaz").setValue(kazEnt);

        mDatabaseBlockRef.child(univerCode).child("univerCode").setValue(univerCode).addOnCompleteListener(task -> {
            Toast.makeText(SerpinGrantInfo.this, "Серпін грант өзгертілді", Toast.LENGTH_SHORT).show();
            mDatabaseRef.child("serpin_ver").setValue(getIncreasedVersion());

            finish();
        });
    }

    String version;

    public void initIncreaseVersion() {
        mDatabaseRef.child("serpin_ver").addValueEventListener(new ValueEventListener() {
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
                }
            }
        });
    }

    public Long toLong(EditText editText) {
        if(checkEmpty(editText))
            return 0L;

        return Long.parseLong(editText.getText().toString().trim());
    }

    public boolean checkEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() == 0) {
            return true;
        }

        return false;
    }
    public String toStr(EditText editText) {
        if(checkEmpty(editText))
            return "0";

        return editText.getText().toString().replace(" ", "");
    }

    public boolean isAdmin() {
        if (mAuth.getCurrentUser() != null) {
            return mAuth.getCurrentUser().getEmail().contains("admin");
        }
        return false;
    }
}
