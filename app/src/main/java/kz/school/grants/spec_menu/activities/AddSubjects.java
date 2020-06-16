package kz.school.grants.spec_menu.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import kz.school.grants.R;
import kz.school.grants.spec_menu.models.SubjectPair;

public class AddSubjects extends AppCompatActivity implements View.OnClickListener {
    Toolbar toolbar;
    Spinner spinnerSub1, spinnerSub2;
    ProgressBar progressBar;
    Button addSubjects;
    String subject1, subject2;
    private DatabaseReference mDatabaseRef;
    String version = "-1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_subjects);

        initViews();
        initIncreaseVersion();

    }

    public void initViews() {
        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.add_subjects));
        addSubjects = findViewById(R.id.addSubjects);
        progressBar = findViewById(R.id.progressBar);
        spinnerSub1 = findViewById(R.id.spinnerSub1);
        spinnerSub2 = findViewById(R.id.spinnerSub2);

        spinnerSub1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                subject1 = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinnerSub2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                subject2 = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addSubjects.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        addSubjects.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        String sId = getSId();
        SubjectPair subjectPair = new SubjectPair(sId, subject1 + "-" + subject2, 0, new HashMap<>());
        mDatabaseRef.child("profile_subjects").child(sId).setValue(subjectPair).addOnCompleteListener(task -> {
            mDatabaseRef.child("subjects_ver").setValue(getIncreasedVersion());
            Toast.makeText(AddSubjects.this, getString(R.string.subjects_added), Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    public void initIncreaseVersion() {
        mDatabaseRef.child("subjects_ver").addValueEventListener(new ValueEventListener() {
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