package kz.school.grants.advice_menu.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.annotations.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.advice_menu.Advice;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.univer_menu.models.Univer;

import static kz.school.grants.database.StoreDatabase.COLUMN_PROFESSIONS_LIST;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_IMAGE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_LOCATION;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_NAME;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_PHON;
import static kz.school.grants.database.StoreDatabase.TABLE_UNIVER_LIST;

public class AddAdvice extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.univerList)
    AutoCompleteTextView univerAutoComplete;
    @BindView(R.id.univerList2)
    AutoCompleteTextView univerAutoComplete2;
    @BindView(R.id.univerList3)
    AutoCompleteTextView univerAutoComplete3;
    @BindView(R.id.univerList4)
    AutoCompleteTextView univerAutoComplete4;
    @BindView(R.id.btnUniverClear1)
    Button btnUniverClear1;
    @BindView(R.id.btnUniverClear2)
    Button btnUniverClear2;
    @BindView(R.id.btnUniverClear3)
    Button btnUniverClear3;
    @BindView(R.id.btnUniverClear4)
    Button btnUniverClear4;

    @BindView(R.id.groupList1)
    AutoCompleteTextView groupAutoComplete1;
    @BindView(R.id.groupList2)
    AutoCompleteTextView groupAutoComplete2;
    @BindView(R.id.groupList3)
    AutoCompleteTextView groupAutoComplete3;
    @BindView(R.id.groupList4)
    AutoCompleteTextView groupAutoComplete4;
    @BindView(R.id.btnGroupClear1)
    Button btnGroupClear1;
    @BindView(R.id.btnGroupClear2)
    Button btnGroupClear2;
    @BindView(R.id.btnGroupClear3)
    Button btnGroupClear3;
    @BindView(R.id.btnGroupClear4)
    Button btnGroupClear4;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.studentNameEditText)
    EditText studentNameEditText;
    @BindView(R.id.studentPhoneEditText)
    EditText studentPhoneEditText;

    @BindView(R.id.nextBtn)
    Button nextBtn;
    ArrayList<String> groupList;
    ArrayList<String> univerList;
    ArrayList<String> univerStore;

    String today;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_advice);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        ButterKnife.bind(this);
        initViews();

    }

    public void initViews() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_advice));
        getSupportActionBar().setHomeButtonEnabled(true);

        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

//        String[] univerStore = getResources().getStringArray(R.array.univers);
        String[] groupsStore = getResources().getStringArray(R.array.blockList);
        univerStore = new ArrayList<>();
        fillUniversFromDB();
        studentPhoneEditText.setSelection(studentPhoneEditText.getText().length());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.item_auto_complete, univerStore);
        ArrayAdapter<String> adapterGroup = new ArrayAdapter<>(this, R.layout.item_auto_complete, groupsStore);
        groupList = new ArrayList<>();
        univerList = new ArrayList<>();

        groupAutoComplete1.setThreshold(1);
        groupAutoComplete1.setAdapter(adapterGroup);

        groupAutoComplete2.setThreshold(1);
        groupAutoComplete2.setAdapter(adapterGroup);

        groupAutoComplete3.setThreshold(1);
        groupAutoComplete3.setAdapter(adapterGroup);

        groupAutoComplete4.setThreshold(1);
        groupAutoComplete4.setAdapter(adapterGroup);

        univerAutoComplete.setThreshold(1);
        univerAutoComplete.setAdapter(adapter);
        univerAutoComplete2.setThreshold(1);
        univerAutoComplete2.setAdapter(adapter);
        univerAutoComplete3.setThreshold(1);
        univerAutoComplete3.setAdapter(adapter);
        univerAutoComplete4.setThreshold(1);
        univerAutoComplete4.setAdapter(adapter);

        groupAutoComplete1.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        groupAutoComplete2.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        groupAutoComplete3.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        groupAutoComplete4.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        univerAutoComplete.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        univerAutoComplete2.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        univerAutoComplete3.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);
        univerAutoComplete4.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);

        addItemClickToHideKeyboard(groupAutoComplete1);
        addItemClickToHideKeyboard(groupAutoComplete2);
        addItemClickToHideKeyboard(groupAutoComplete3);
        addItemClickToHideKeyboard(groupAutoComplete4);

        addItemClickToHideKeyboard(univerAutoComplete);
        addItemClickToHideKeyboard(univerAutoComplete2);
        addItemClickToHideKeyboard(univerAutoComplete3);
        addItemClickToHideKeyboard(univerAutoComplete4);

        btnUniverClear1.setOnClickListener(this);
        btnUniverClear2.setOnClickListener(this);
        btnUniverClear3.setOnClickListener(this);
        btnUniverClear4.setOnClickListener(this);


        btnGroupClear1.setOnClickListener(this);
        btnGroupClear2.setOnClickListener(this);
        btnGroupClear3.setOnClickListener(this);
        btnGroupClear4.setOnClickListener(this);

        nextBtn.setOnClickListener(this);
    }

    private void fillUniversFromDB() {
        Cursor userCursor = storeDb.getCursorAll(sqdb, TABLE_UNIVER_LIST);

        Log.i("Univers", "fillUniversFromDB");

        if (((userCursor != null) && (userCursor.getCount() > 0))) {
            while (userCursor.moveToNext()) {

                Log.i("Univers", storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_NAME));

                univerStore.add(
                                storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_CODE) +
                                " - " +
                                storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_NAME)
                );
            }
        }else{
            Toast.makeText(this, "Университеттер табылмады, ақпаратты жүктеу үшін Университеттер менюға кіріңіз!", Toast.LENGTH_LONG).show();
        }
    }
    
    public void addItemClickToHideKeyboard(AutoCompleteTextView autoCompleteTextView) {
        autoCompleteTextView.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            assert in != null;
            in.hideSoftInputFromWindow(arg1.getApplicationWindowToken(), 0);

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.nextBtn:
                groupList.clear();
                univerList.clear();

                if (studentNameEditText.getText().toString().trim().equals("")) {
                    studentNameEditText.setError(getString(R.string.fill_all));
                    return;
                }

                if (studentPhoneEditText.getText().toString().trim().equals("")) {
                    studentPhoneEditText.setError(getString(R.string.fill_all));
                    return;
                }

                if (groupAutoComplete1.getText().toString().trim().equals("")) {
                    groupAutoComplete1.setError(getString(R.string.fill_all));
                    return;
                }
                if (groupAutoComplete2.getText().toString().trim().equals("")) {
                    groupAutoComplete2.setError(getString(R.string.fill_all));
                    return;
                }
                if (groupAutoComplete3.getText().toString().trim().equals("")) {
                    groupAutoComplete3.setError(getString(R.string.fill_all));
                    return;
                }
                if (groupAutoComplete4.getText().toString().trim().equals("")) {
                    groupAutoComplete4.setError(getString(R.string.fill_all));
                    return;
                }

                if (univerAutoComplete.getText().toString().trim().equals("")) {
                    univerAutoComplete.setError(getString(R.string.fill_all));
                    return;
                }
                if (univerAutoComplete2.getText().toString().trim().equals("")) {
                    univerAutoComplete2.setError(getString(R.string.fill_all));
                    return;
                }
                if (univerAutoComplete3.getText().toString().trim().equals("")) {
                    univerAutoComplete3.setError(getString(R.string.fill_all));
                    return;
                }
                if (univerAutoComplete4.getText().toString().trim().equals("")) {
                    univerAutoComplete4.setError(getString(R.string.fill_all));
                    return;
                }

                groupList.add(groupAutoComplete1.getText().toString());
                groupList.add(groupAutoComplete2.getText().toString());
                groupList.add(groupAutoComplete3.getText().toString());
                groupList.add(groupAutoComplete4.getText().toString());

                univerList.add(univerAutoComplete.getText().toString());
                univerList.add(univerAutoComplete2.getText().toString());
                univerList.add(univerAutoComplete3.getText().toString());
                univerList.add(univerAutoComplete4.getText().toString());

                Advice advice = new Advice(
                        getAdviceId(),
                        studentNameEditText.getText().toString(),
                        studentPhoneEditText.getText().toString(), today, groupList, univerList);

                Intent adviceReportIntent = new Intent(this, AdviceReport.class);
                Bundle bundle = new Bundle();
                bundle.putString("act", "addAdvice");
                bundle.putSerializable("advice", advice);
                adviceReportIntent.putExtras(bundle);

                startActivity(adviceReportIntent);

                break;

            case R.id.btnUniverClear1:
                univerAutoComplete.setText("");
                univerAutoComplete.setFocusableInTouchMode(true);
                break;

            case R.id.btnUniverClear2:
                univerAutoComplete2.setText("");
                univerAutoComplete2.setFocusableInTouchMode(true);
                break;

            case R.id.btnUniverClear3:
                univerAutoComplete3.setText("");
                univerAutoComplete3.setFocusableInTouchMode(true);
                break;

            case R.id.btnUniverClear4:
                univerAutoComplete4.setText("");
                univerAutoComplete4.setFocusableInTouchMode(true);
                break;

            case R.id.btnGroupClear1:
                groupAutoComplete1.setText("");
                groupAutoComplete1.setFocusableInTouchMode(true);
                break;

            case R.id.btnGroupClear2:
                groupAutoComplete2.setText("");
                groupAutoComplete2.setFocusableInTouchMode(true);
                break;

            case R.id.btnGroupClear3:
                groupAutoComplete3.setText("");
                groupAutoComplete3.setFocusableInTouchMode(true);
                break;

            case R.id.btnGroupClear4:
                groupAutoComplete4.setText("");
                groupAutoComplete4.setFocusableInTouchMode(true);
                break;
        }
    }

    public String getAdviceId() {
        Date date = new Date();

        DateFormat dateF = new SimpleDateFormat("dd.MM.yyyy");
        today = dateF.format(Calendar.getInstance().getTime());

        String idN = "a" + date.getTime();
        return idN;
    }

}
