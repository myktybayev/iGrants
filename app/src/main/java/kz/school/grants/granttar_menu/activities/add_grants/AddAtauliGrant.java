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

public class AddAtauliGrant extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    ProgressBar progressBar;
    Button submitBtn;
    String version = "-1";
    CheckBox auilKvotasi;
    boolean auilKvotasiChecked = false;
    LinearLayout auilKvotasiLayout;
    private DatabaseReference mDatabaseBlockRef, mDatabaseRef;

    @BindView(R.id.univerCode)
    EditText univerCode;

    @BindView(R.id.y18_19_total_tx)
    TextView y18_19_total_tx;
    @BindView(R.id.y19_20_total_tx)
    TextView y19_20_total_tx;

    @BindView(R.id.y18_19_kaz_ed)
    EditText y18_19_kaz_ed;
    @BindView(R.id.y18_19_rus_ed)
    EditText y18_19_rus_ed;

    @BindView(R.id.y19_20kaz_ed)
    EditText y19_20kaz_ed;
    @BindView(R.id.y19_20rus_ed)
    EditText y19_20rus_ed;

    @BindView(R.id.yTotal_tx)
    TextView yTotal_tx;
    @BindView(R.id.yKaz_tx)
    TextView yKaz_tx;
    @BindView(R.id.yRus_tx)
    TextView yRus_tx;

    @BindView(R.id.kaz_max_ed)
    EditText kaz_max_ed;
    @BindView(R.id.kaz_min_ed)
    EditText kaz_min_ed;
    @BindView(R.id.kaz_ave_ed)
    EditText kaz_ave_ed;
    @BindView(R.id.rus_max_ed)
    EditText rus_max_ed;
    @BindView(R.id.rus_min_ed)
    EditText rus_min_ed;
    @BindView(R.id.rus_ave_ed)
    EditText rus_ave_ed;

    @BindView(R.id.auil_kaz_max_ed) EditText auil_kaz_max_ed;
    @BindView(R.id.auil_kaz_min_ed) EditText auil_kaz_min_ed;
    @BindView(R.id.auil_kaz_ave_ed) EditText auil_kaz_ave_ed;
    @BindView(R.id.auil_rus_max_ed) EditText auil_rus_max_ed;
    @BindView(R.id.auil_rus_min_ed) EditText auil_rus_min_ed;
    @BindView(R.id.auil_rus_ave_ed) EditText auil_rus_ave_ed;

    String fillErrorStr;
    HashMap<String, String> professionHashMap;
    Bundle bundle;
    String specCode;
    int professionsCount = 0;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_atauli_grant);
        ButterKnife.bind(this);

        initViews();
        initIncreaseVersion();
    }

    public void initViews() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        mDatabaseBlockRef = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Атаулы грант енгізу");

        submitBtn = findViewById(R.id.submitBtn);
        progressBar = findViewById(R.id.progressBar);
        auilKvotasi = findViewById(R.id.auilKvotasi);
        auilKvotasiLayout = findViewById(R.id.auilKvotasiLayout);
        fillErrorStr = getString(R.string.fill_all);
        professionHashMap = new HashMap<>();

        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        bundle = getIntent().getExtras();
        if (bundle != null) {
            specCode = bundle.getString("specCode");
        }

        mDatabaseBlockRef = mDatabaseBlockRef.child("atauli_grant_list").child(specCode).child("univers");
        auilKvotasi.setOnCheckedChangeListener((compoundButton, b) -> {
            auilKvotasiChecked = b;

            if (b) {
                auilKvotasiLayout.setVisibility(View.VISIBLE);
            } else {
                auilKvotasiLayout.setVisibility(View.GONE);
            }
        });

        addTotalCalc(y18_19_total_tx, y18_19_kaz_ed, y18_19_rus_ed);
        addTotalCalc(y19_20_total_tx, y19_20kaz_ed, y19_20rus_ed);

        addTotalDiff(yKaz_tx, y18_19_kaz_ed, y19_20kaz_ed);
        addTotalDiff(yRus_tx, y18_19_rus_ed, y19_20rus_ed);

        submitBtn.setOnClickListener(this);
    }

    public void totalDiff() {
        int res = Integer.parseInt(y18_19_total_tx.getText().toString()) + Integer.parseInt(y19_20_total_tx.getText().toString());
        yTotal_tx.setText("" + (res));
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
                    totalDiff();
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
                        y18_19_kaz_ed, y18_19_rus_ed, y19_20kaz_ed, y19_20rus_ed,
                        kaz_max_ed, kaz_min_ed, kaz_ave_ed, rus_max_ed, rus_min_ed, rus_ave_ed)) {

                    submitBtn.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);

                    return;
                }

                String univerCodeStr = univerCode.getText().toString();

                HashMap<String, Long> grant18_19 = new HashMap<>();
                grant18_19.put("kaz", toLong(y18_19_kaz_ed));
                grant18_19.put("rus", toLong(y18_19_rus_ed));

                HashMap<String, Long> grant19_20 = new HashMap<>();
                grant19_20.put("kaz", toLong(y19_20kaz_ed));
                grant19_20.put("rus", toLong(y19_20rus_ed));

                HashMap<String, Long> kazEnt = new HashMap<>();
                kazEnt.put("ave", toLong(kaz_ave_ed));
                kazEnt.put("max", toLong(kaz_max_ed));
                kazEnt.put("min", toLong(kaz_min_ed));

                HashMap<String, Long> rusEnt = new HashMap<>();
                rusEnt.put("ave", toLong(rus_ave_ed));
                rusEnt.put("max", toLong(rus_max_ed));
                rusEnt.put("min", toLong(rus_min_ed));
//                grant18_19


                if(auilKvotasiChecked){
                    if (checkEmptyEditTexts(
                            auil_kaz_max_ed, auil_kaz_min_ed, auil_kaz_ave_ed,
                            auil_rus_max_ed, auil_rus_min_ed, auil_rus_ave_ed)){

                        submitBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);

                        return;
                    }

                    HashMap<String, Long> auilKaz = new HashMap<>();
                    auilKaz.put("ave", toLong(auil_kaz_ave_ed));
                    auilKaz.put("max", toLong(auil_kaz_max_ed));
                    auilKaz.put("min", toLong(auil_kaz_min_ed));

                    HashMap<String, Long> auilRus = new HashMap<>();
                    auilRus.put("ave", toLong(auil_rus_ave_ed));
                    auilRus.put("max", toLong(auil_rus_max_ed));
                    auilRus.put("min", toLong(auil_rus_min_ed));

                    mDatabaseBlockRef.child(univerCodeStr).child("jalpiEnt").child("auilKaz").setValue(auilKaz);
                    mDatabaseBlockRef.child(univerCodeStr).child("jalpiEnt").child("auilRus").setValue(auilRus);

                }



                mDatabaseBlockRef.child(univerCodeStr).child("grant18_19").setValue(grant18_19);
                mDatabaseBlockRef.child(univerCodeStr).child("grant19_20").setValue(grant19_20);
                mDatabaseBlockRef.child(univerCodeStr).child("jalpiEnt").child("kaz").setValue(kazEnt);
                mDatabaseBlockRef.child(univerCodeStr).child("jalpiEnt").child("rus").setValue(rusEnt);

                mDatabaseBlockRef.child(univerCodeStr).child("univerCode").setValue(univerCodeStr).addOnCompleteListener(task -> {
                    Toast.makeText(AddAtauliGrant.this, "Атаулы университет енгізілді", Toast.LENGTH_SHORT).show();
                    mDatabaseRef.child("atauli_grant_list_ver").setValue(getIncreasedVersion());
                    Intent intent = getIntent();
                    intent.putExtra("univerCode", univerCodeStr);
                    setResult(RESULT_OK, intent);
                    finish();
                });

                break;
        }


//        String sId = getSId();
//        SubjectPair subjectPair = new SubjectPair(sId, subject1 + "-" + subject2, 0, new HashMap<>());
//        mDatabaseRef.child("profile_subjects").child(sId).setValue(subjectPair).addOnCompleteListener(task -> {
//            mDatabaseRef.child("subjects_ver").setValue(getIncreasedVersion());
//            Toast.makeText(AddBlock.this, getString(R.string.subjects_added), Toast.LENGTH_SHORT).show();
//            finish();
//        });
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

    public String getSId() {
        Date date = new Date();
        String idN = "s" + date.getTime();
        return idN;
    }

    /*
    class AddSpecAdapter extends RecyclerView.Adapter<AddSpecAdapter.MyTViewHolder> {

        private Context context;

        public class MyTViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

            @BindView(R.id.specNumber)
            TextView specNumber;

            @BindView(R.id.specCode) EditText specCode;
            @BindView(R.id.specName) EditText specName;

            ItemClickListener clickListener;

            public MyTViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }


            @Override
            public void onClick(View view) {
                this.clickListener.onItemClick(view,getLayoutPosition());
            }

            public void setOnClick(ItemClickListener clickListener){
                this.clickListener = clickListener;
            }
        }

        public AddSpecAdapter(Context context) {
            this.context = context;
        }

        @Override
        public MyTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_spec, parent, false);

            return new MyTViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyTViewHolder holder, int position) {

            OneAddSpec item = oneAddSpecArrayList.get(position);

            holder.specNumber.setText(item.getNumber());
            holder.specCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    oneAddSpecArrayList.get(position).setCode(holder.specCode.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            holder.specName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    oneAddSpecArrayList.get(position).setTitle(holder.specName.getText().toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return oneAddSpecArrayList.size();
        }

    }
    */
}