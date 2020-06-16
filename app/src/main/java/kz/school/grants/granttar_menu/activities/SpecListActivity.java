package kz.school.grants.granttar_menu.activities;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.granttar_menu.adapters.SpecListAdapter;
import kz.school.grants.granttar_menu.models.SpecItem;
import kz.school.grants.spec_menu.activities.GrantInfo;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;
import kz.school.grants.univer_menu.models.Univer;
import kz.school.grants.univer_menu.UniversAdapter;

import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_PROF_TITLE;
import static kz.school.grants.database.StoreDatabase.COLUMN_SUBJECTS_PAIR;
import static kz.school.grants.database.StoreDatabase.TABLE_PROFESSIONS;
import static kz.school.grants.database.StoreDatabase.TABLE_PROFILE_SUBJECTS;

public class SpecListActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView myrecyclerview;
    private ArrayList<Univer> lstUnivers;
    private ArrayList<Univer> lstUniversCopy;
    UniversAdapter universAdapter;

    private ArrayList<SpecItem> lstSpecs;
    private ArrayList<SpecItem> lstSpecsCopy;
    private DatabaseReference newsRef;
    SpecListAdapter specListAdapter;
    private FirebaseAuth mAuth;
    private boolean adminSigned = false;
    private FloatingActionButton fabBtn;
    private View progressLoading;
    private SearchView searchView;
    boolean univerList = false;
    private ArrayList<String> lstProfs;
    private ArrayList<String> lstSubjectPair;
    Dialog addProfDialog;
    Spinner spinnerProfs, spinnerSubjectPair;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    Button addProf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_grants);
        setTitle("SpecListActivity");
        initViews();
    }

    public void initViews() {
        lstSpecs = new ArrayList<>();
        lstUnivers = new ArrayList<>();
        progressLoading = findViewById(R.id.llProgressBar);
        myrecyclerview = findViewById(R.id.home_recyclerview);
        fabBtn = findViewById(R.id.fabBtn);
        searchView = findViewById(R.id.searchView);
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();

        newsRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        if (isAdmin()) {
            adminSigned = true;
            fabBtn.setVisibility(View.VISIBLE);
        }

        getProfessionsFromDb();
        addSearchListener();
        loadProfessionToDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.calcBtn:

                break;

            case R.id.fabBtn:
                addProfDialog.show();
                break;

            case R.id.addProf:

                addProfDialog.dismiss();
                break;

        }
    }

    public void getProfessionsFromDb(){
        lstSpecs.add(new SpecItem("5В060200", "Информатика", "Математика - Физика"));
        lstSpecs.add(new SpecItem("5В070300", "Информационные системы", "Математика - Физика"));
        lstSpecs.add(new SpecItem("5В070300", "Информационные системы", "Математика - Физика"));
        lstSpecs.add(new SpecItem("5В011600", "География", "География - Всемирная история"));
        lstSpecs.add(new SpecItem("5В013000", "История-Религиоведение", "Всемирная история - География"));
        lstSpecs.add(new SpecItem("5В041200", "Операторское искусство", "Творческий экзамен - Творческий экзамен"));
        lstSpecs.add(new SpecItem("5В042200", "Издательское дело", "Творческий экзамен - Творческий экзамен"));
        lstSpecs.add(new SpecItem("5В072200", "Полиграфия", "Творческий экзамен - Творческий экзамен"));

        lstSpecsCopy = (ArrayList<SpecItem>) lstSpecs.clone();

        specListAdapter = new SpecListAdapter(this, lstSpecs);
        myrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        myrecyclerview.setAdapter(specListAdapter);


        lstUnivers.add(new Univer("123", "url", "Университет имени Сулеймана Демиреля (УСД (СДУ))", "87771199022", "Алматы", "302", ""));
        lstUnivers.add(new Univer("123", "url", "Кызылординский гуманитарный университет (КызГУ)", "87771199022", "Кызылорда", "304", ""));
        lstUnivers.add(new Univer("123", "url", "Центрально-Азиатский университет (ЦАУ)", "87771199022", "Алматы", "096", ""));

        lstUniversCopy = (ArrayList<Univer>) lstUnivers.clone();
        universAdapter = new UniversAdapter(this, this, lstUnivers);

        fabBtn.setOnClickListener(this);

        myrecyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(this, myrecyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {
                        if(univerList){

                            Intent grantInfo = new Intent(SpecListActivity.this, GrantInfo.class);

                            Bundle grantBundle = new Bundle();
                            grantBundle.putString("blockCode", "В057");
                            grantInfo.putExtras(grantBundle);
                            startActivity(grantInfo);

                        }else{
                            myrecyclerview.setAdapter(universAdapter);
                            univerList = true;
                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {

                    }
                })
        );
    }

    public void loadProfessionToDialog(){
        lstProfs = new ArrayList<>();
        lstSubjectPair = new ArrayList<>();
        addProfDialog = new Dialog(this);
        addProfDialog.setContentView(R.layout.add_prof_to_grants);

        spinnerProfs = addProfDialog.findViewById(R.id.spinnerProfs);
        spinnerSubjectPair = addProfDialog.findViewById(R.id.spinnerSubjectPair);

        addProf = addProfDialog.findViewById(R.id.addProf);

        Cursor cursor = storeDb.getCursorAll(sqdb, TABLE_PROFESSIONS);

        if (((cursor != null) && (cursor.getCount() > 0))) {
            while (cursor.moveToNext()) {
                lstProfs.add(storeDb.getStrFromColumn(cursor, COLUMN_PROF_CODE)+" - "+storeDb.getStrFromColumn(cursor, COLUMN_PROF_TITLE));
            }
        }

        Cursor cursorSubjectPair = storeDb.getCursorAll(sqdb, TABLE_PROFILE_SUBJECTS);

        if (((cursorSubjectPair != null) && (cursorSubjectPair.getCount() > 0))) {
            while (cursorSubjectPair.moveToNext()) {
                lstSubjectPair.add(storeDb.getStrFromColumn(cursorSubjectPair, COLUMN_SUBJECTS_PAIR));
            }
        }

        spinnerProfs.setAdapter(new ArrayAdapter<>(this, R.layout.item_subject_pair, lstProfs));
        spinnerSubjectPair.setAdapter(new ArrayAdapter<>(this, R.layout.item_subject_pair, lstSubjectPair));
        addProf.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        if(univerList) {
            myrecyclerview.setAdapter(specListAdapter);
            univerList = false;
        }else{
            super.onBackPressed();
        }
    }

    public void addSearchListener(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });
    }

    public void filter(String text) {

        lstSpecs.clear();

        if (text.isEmpty()) {
            lstSpecs.addAll(lstSpecsCopy);

        } else {
            text = text.toLowerCase();
            for (SpecItem item : lstSpecsCopy) {

                if (item.getSpecName().toLowerCase().contains(text) || item.getSpecName().toUpperCase().contains(text) ||
                        item.getSpecCode().toLowerCase().contains(text) || item.getSpecCode().toUpperCase().contains(text)||
                        item.getSpecSubjectPair().toLowerCase().contains(text) || item.getSpecSubjectPair().toUpperCase().contains(text)) {

                    lstSpecs.add(item);
                }
            }
        }

        specListAdapter.notifyDataSetChanged();
    }

    private boolean isAdmin(){
        if(mAuth.getCurrentUser() != null){
            return Objects.requireNonNull(mAuth.getCurrentUser().getEmail()).contains("admin");
        }
        return false;
    }
}

    /*
    class CustomAdapter extends ArrayAdapter<SpecItem> {

        LayoutInflater flater;

        public CustomAdapter(Activity context, int resouceId, List<SpecItem> list){
            super(context,resouceId, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView,position);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return rowview(convertView,position);
        }

        private View rowview(View convertView , int position){

            SpecItem rowItem = getItem(position);

            viewHolder holder ;
            View rowview = convertView;
            if (rowview==null) {

                holder = new viewHolder();
                flater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                assert flater != null;
                rowview = flater.inflate(R.layout.item_grant_dialog_spec, null, false);

                holder.specName = rowview.findViewById(R.id.specName);
                holder.specSubjectPair = rowview.findViewById(R.id.specSubjectPair);
                rowview.setTag(holder);

            }else{
                holder = (viewHolder) rowview.getTag();
            }

            holder.specName.setText(rowItem.getSpecCode()+" - "+rowItem.getSpecName());
            holder.specSubjectPair.setText(rowItem.getSpecSubjectPair());

            return rowview;
        }

        private class viewHolder{
            TextView specName, specSubjectPair;
        }
    }
    */
    /*
        spinnerProfs = addProfDialog.findViewById(R.id.spinnerProfs);
        addProf = addProfDialog.findViewById(R.id.addProf);
        addProf.setOnClickListener(this);

        Cursor cursor = storeDb.getCursorAll(sqdb, TABLE_PROFESSIONS);

        if (((cursor != null) && (cursor.getCount() > 0))) {
            while (cursor.moveToNext()) {
                String blockCode = storeDb.getStrFromColumn(cursor, COLUMN_BLOCK_CODE);
                String specCode = storeDb.getStrFromColumn(cursor, COLUMN_PROF_CODE);
                String specName = storeDb.getStrFromColumn(cursor, COLUMN_PROF_TITLE);

                Cursor cursorBlock = storeDb.getCursorWhereEqualTo(sqdb, TABLE_BLOCKS, COLUMN_BLOCK_CODE, blockCode, COLUMN_BLOCK_CODE);
                cursorBlock.moveToFirst();
                String subjectId = storeDb.getStrFromColumn(cursorBlock, COLUMN_SUBJECTS_ID);

                Cursor subjectCursor = storeDb.getCursorWhereEqualTo(sqdb, TABLE_PROFILE_SUBJECTS, COLUMN_SUBJECTS_ID, subjectId, COLUMN_SUBJECTS_ID);
                subjectCursor.moveToFirst();
                String subjectPair = storeDb.getStrFromColumn(subjectCursor, COLUMN_SUBJECTS_TITLE);

//                String blockCode =

                lstProfs.add(new SpecItem(specCode, specName, subjectPair));
            }

            spinnerProfs.setAdapter(new CustomAdapter(this, R.layout.item_grant_dialog_spec, lstProfs));
        }
        */