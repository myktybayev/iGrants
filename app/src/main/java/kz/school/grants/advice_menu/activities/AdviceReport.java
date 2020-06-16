package kz.school.grants.advice_menu.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.dk.view.folder.ResideMenu;
import com.google.firebase.database.annotations.Nullable;
import com.google.gson.Gson;

import java.net.URLEncoder;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.MenuActivity;
import kz.school.grants.R;
import kz.school.grants.advice_menu.Advice;
import kz.school.grants.database.StoreDatabase;

import static kz.school.grants.database.StoreDatabase.COLUMN_ADVICE_DATE;
import static kz.school.grants.database.StoreDatabase.COLUMN_ADVICE_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_GROUP_LIST;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_COUNT;
import static kz.school.grants.database.StoreDatabase.COLUMN_STUDENT_NAME;
import static kz.school.grants.database.StoreDatabase.COLUMN_STUDENT_PHONE;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_LIST;
import static kz.school.grants.database.StoreDatabase.TABLE_ADVICE_HISTORY;
import static kz.school.grants.database.StoreDatabase.TABLE_BLOCKS;

public class AdviceReport extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.univerList1)
    TextView univerTextView1;
    @BindView(R.id.univerList2)
    TextView univerTextView2;
    @BindView(R.id.univerList3)
    TextView univerTextView3;
    @BindView(R.id.univerList4)
    TextView univerTextView4;

    @BindView(R.id.groupList1)
    TextView groupTextView1;
    @BindView(R.id.groupList2)
    TextView groupTextView2;
    @BindView(R.id.groupList3)
    TextView groupTextView3;
    @BindView(R.id.groupList4)
    TextView groupTextView4;

    @BindView(R.id.submitBtn)
    Button submitBtn;

    @BindView(R.id.studentName)
    EditText studentName;
    @BindView(R.id.studentPhone)
    EditText studentPhone;

    @BindView(R.id.adviceDate)
    EditText adviceDate;

    Toolbar toolbar;
    Advice advice;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    Gson gson;
    String act;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice_report);

        ButterKnife.bind(this);
        initViews();

    }

    public void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.advice_report));

        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();
        gson = new Gson();

        toolbar.setNavigationIcon(R.drawable.ic_close_white);
        toolbar.setNavigationOnClickListener(view -> startActivity(new Intent(this, MenuActivity.class)));

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            advice = (Advice) bundle.getSerializable("advice");
            act = bundle.getString("act");

            assert advice != null;
            studentName.setText(advice.getStudentName());
            studentPhone.setText(advice.getStudentPhone());
            adviceDate.setText(advice.getAdviceDate());

            groupTextView1.setText(advice.getGroupList().get(0));
            groupTextView2.setText(advice.getGroupList().get(1));
            groupTextView3.setText(advice.getGroupList().get(2));
            groupTextView4.setText(advice.getGroupList().get(3));

            univerTextView1.setText(advice.getUniverList().get(0));
            univerTextView2.setText(advice.getUniverList().get(1));
            univerTextView3.setText(advice.getUniverList().get(2));
            univerTextView4.setText(advice.getUniverList().get(3));
        }

        submitBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submitBtn:
                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String message = String.format(
                        "Оқушы аты-жөні: %s\n" +
                                "Кеңес беру күні: %s\n" +
                                "Группа #1: %s\n" +
                                "Группа #2: %s\n" +
                                "Группа #3: %s\n" +
                                "Группа #4: %s\n\n" +
                                "Универ #1: %s\n" +
                                "Универ #2: %s\n" +
                                "Универ #3: %s\n" +
                                "Универ #4: %s\n" +
                                "",
                        advice.getStudentName(),
                        advice.getAdviceDate(),
                        advice.getGroupList().get(0),
                        advice.getGroupList().get(1),
                        advice.getGroupList().get(2),
                        advice.getGroupList().get(3),
                        advice.getUniverList().get(0),
                        advice.getUniverList().get(1),
                        advice.getUniverList().get(2),
                        advice.getUniverList().get(3));

                if (act.equals("addAdvice")) {
                    String groupList = gson.toJson(advice.getGroupList());
                    String univerList = gson.toJson(advice.getUniverList());

                    ContentValues value = new ContentValues();
                    value.put(COLUMN_ADVICE_ID, advice.getAdviceId());
                    value.put(COLUMN_STUDENT_NAME, advice.getStudentName());
                    value.put(COLUMN_STUDENT_PHONE, advice.getStudentPhone());
                    value.put(COLUMN_ADVICE_DATE, advice.getAdviceDate());
                    value.put(COLUMN_GROUP_LIST, groupList);
                    value.put(COLUMN_UNIVER_LIST, univerList);

                    sqdb.insert(TABLE_ADVICE_HISTORY, null, value);
                }

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + advice.getStudentPhone() + "&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
