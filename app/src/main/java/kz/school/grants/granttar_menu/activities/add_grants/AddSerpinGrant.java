package kz.school.grants.granttar_menu.activities.add_grants;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;

public class AddSerpinGrant extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ProgressBar progressBar;
    Button submitBtn;
    String version = "-1";
    private DatabaseReference mDatabaseBlockRef, mDatabaseRef;

    @BindView(R.id.univerCode)
    EditText univerCode;

    @BindView(R.id.y18_19_kaz_ed)
    EditText y18_19_kaz_ed;

    @BindView(R.id.y19_20kaz_ed)
    EditText y19_20kaz_ed;

    @BindView(R.id.yKaz_tx)
    TextView yKaz_tx;

    @BindView(R.id.kaz_max_ed)
    EditText kaz_max_ed;
    @BindView(R.id.kaz_min_ed)
    EditText kaz_min_ed;
    @BindView(R.id.kaz_ave_ed)
    EditText kaz_ave_ed;

    String fillErrorStr;
    HashMap<String, String> professionHashMap;
    Bundle bundle;
    String specCode;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_serpin_grant);
        ButterKnife.bind(this);

        initViews();
        initIncreaseVersion();
    }

    public void initViews() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseBlockRef = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Серпін грант енгізу");

        submitBtn = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progressBar);
        fillErrorStr = getString(R.string.fill_all);
        professionHashMap = new HashMap<>();

        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            specCode = bundle.getString("specCode");
        }

        mDatabaseBlockRef = mDatabaseBlockRef.child("serpin_grant_list").child(specCode).child("univers");
        addTotalDiff(yKaz_tx, y18_19_kaz_ed, y19_20kaz_ed);
        submitBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:

                submitBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);

                if (checkEmptyEditTexts(
                        univerCode,
                        y18_19_kaz_ed,  y19_20kaz_ed,
                        kaz_max_ed, kaz_min_ed, kaz_ave_ed)) {

                    submitBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    return;
                }

                String univerCodeStr = univerCode.getText().toString();

                HashMap<String, Long> grant18_19 = new HashMap<>();
                grant18_19.put("kaz", toLong(y18_19_kaz_ed));

                HashMap<String, Long> grant19_20 = new HashMap<>();
                grant19_20.put("kaz", toLong(y19_20kaz_ed));

                HashMap<String, Long> kazEnt = new HashMap<>();
                kazEnt.put("ave", toLong(kaz_ave_ed));
                kazEnt.put("max", toLong(kaz_max_ed));
                kazEnt.put("min", toLong(kaz_min_ed));

                mDatabaseBlockRef.child(univerCodeStr).child("grant18_19").setValue(grant18_19);
                mDatabaseBlockRef.child(univerCodeStr).child("grant19_20").setValue(grant19_20);
                mDatabaseBlockRef.child(univerCodeStr).child("jalpiEnt").child("kaz").setValue(kazEnt);

                mDatabaseBlockRef.child(univerCodeStr).child("univerCode").setValue(univerCodeStr).addOnCompleteListener(task -> {
                    Toast.makeText(AddSerpinGrant.this, "Серпін университет енгізілді", Toast.LENGTH_SHORT).show();
                    mDatabaseRef.child("serpin_ver").setValue(getIncreasedVersion());
                    Intent intent = getIntent();
                    intent.putExtra("univerCode", univerCodeStr);
                    setResult(RESULT_OK, intent);
                    finish();
                });

                break;
        }
    }

    public boolean checkEmpty(EditText editText) {
        if (editText.getText().toString().trim().length() == 0) {
            return true;
        }

        return false;
    }

    public boolean checkEmptyEditTexts(EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (checkEmpty(editText)) {
                editText.setError(fillErrorStr);
                return true;
            }
        }

        return false;
    }

    public Long toLong(EditText editText) {
        if(checkEmpty(editText))
            return 0L;

        return Long.parseLong(editText.getText().toString().trim());
    }

    public String toStr(EditText editText) {
        if(checkEmpty(editText))
            return "0";

        return editText.getText().toString().replace(" ", "");
    }

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

    public String getSId() {
        Date date = new Date();
        String idN = "s" + date.getTime();
        return idN;
    }
}