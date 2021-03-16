package kz.school.grants.spec_menu.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.univer_menu.UniversAdapter;
import kz.school.grants.univer_menu.models.Univer;

import static kz.school.grants.database.StoreDatabase.COLUMN_PROFESSIONS_LIST;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_CODE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_IMAGE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_LOCATION;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_NAME;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_PHON;
import static kz.school.grants.database.StoreDatabase.TABLE_UNIVER_LIST;
import static kz.school.grants.database.StoreDatabase.TABLE_VER;

public class SpecBarUniverlerActivity extends AppCompatActivity implements View.OnClickListener {
    View v;
    private RecyclerView myRecyclerView;
    private ArrayList<Univer> lstUnivers;
    private ArrayList<Univer> lstUniversCopy;
    UniversAdapter universAdapter;
    private FirebaseAuth mAuth;
    private FloatingActionButton fabBtn;
    private View progressLoading;
    private SearchView searchView;
    Button ratingBtn;
    private DatabaseReference mDatabaseRef;
    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    String curVersion;
    HashMap<String, String> tempHashMap;
    Bundle bundle;
    String profFull, profCode;
    TextView univerNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_univerler);
        initViews();
        checkInternetConnection();
        fillUniversFromDB();
    }

    public void initViews() {
        lstUnivers = new ArrayList<>();
        tempHashMap = new HashMap<>();
        progressLoading = findViewById(R.id.llProgressBar);
        myRecyclerView = findViewById(R.id.home_recyclerview);
        ratingBtn = findViewById(R.id.ratingBtn);
        fabBtn = findViewById(R.id.fabBtn);
        searchView = findViewById(R.id.searchView);
        univerNotFound = findViewById(R.id.univerNotFound);
        storeDb = new StoreDatabase(this);
        sqdb = storeDb.getWritableDatabase();


        bundle = getIntent().getExtras();
        if (bundle != null) {
            profFull = bundle.getString("profFull");
            profCode = bundle.getString("profCode");
            setTitle(profFull);
        }

        universAdapter = new UniversAdapter(this, this, lstUnivers);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.setAdapter(universAdapter);
        myRecyclerViewAddListeners();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        fabBtn.setOnClickListener(this);
        ratingBtn.setOnClickListener(this);

        fabBtn.setVisibility(View.GONE);
    }

    private void searchAddListener() {
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

    private void myRecyclerViewAddListeners() {

        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && fabBtn.isShown())
                    fabBtn.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE)
                    fabBtn.show();
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ratingBtn:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://atameken.kz/kk/university_ratings?page=2"));
                startActivity(browserIntent);
                break;
        }
    }

    private void fillUniversFromDB() {
        Cursor uCursor = storeDb.getCursorAll(sqdb, TABLE_UNIVER_LIST);
        if (((uCursor != null) && (uCursor.getCount() > 0))) {

            Cursor userCursor = storeDb.getCursorWhereLikeTo(sqdb, TABLE_UNIVER_LIST, COLUMN_PROFESSIONS_LIST, profCode, COLUMN_UNIVER_ID);
            lstUnivers.clear();

            if (((userCursor != null) && (userCursor.getCount() > 0))) {
                while (userCursor.moveToNext()) {
                    String univerCode =  storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_CODE);

                    if(!univerCode.equals("190") && !univerCode.equals("421") && !univerCode.equals("522") && (!univerCode.equals("026") || profCode.equals("5B074800"))){
                        lstUnivers.add(new Univer(
                                storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_ID),
                                storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_IMAGE),
                                storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_NAME),
                                storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_PHON),
                                storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_LOCATION),
                                storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_CODE),
                                storeDb.getStrFromColumn(userCursor, COLUMN_PROFESSIONS_LIST)
                        ));
                    }else{
                        univerNotFound.setVisibility(View.VISIBLE);
                    }
                }
                lstUniversCopy = (ArrayList<Univer>) lstUnivers.clone();
                searchAddListener();

                progressLoading.setVisibility(View.GONE);
                universAdapter.notifyDataSetChanged();
            }else{
                progressLoading.setVisibility(View.GONE);
                univerNotFound.setVisibility(View.VISIBLE);
            }
        }else{
            Toast.makeText(this, "Университеттер тізімі табылмады, бірінші Университет менюға кіріп шықсаңыз", Toast.LENGTH_LONG).show();
        }

    }

    private boolean checkInternetConnection() {
        if (!isNetworkAvailable()) {
            Toast.makeText(this, getString(R.string.check_inet), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void filter(String text) {
        if (lstUnivers.size() > 0) lstUnivers.clear();

        if (text.isEmpty()) {
            lstUnivers.addAll(lstUniversCopy);

        } else {
            text = text.toLowerCase();
            for (Univer item : lstUniversCopy) {

                if (item.getUniverName().toLowerCase().contains(text) || item.getUniverName().toUpperCase().contains(text) ||
                        item.getUniverLocation().toLowerCase().contains(text) || item.getUniverLocation().toUpperCase().contains(text) ||
                        item.getUniverCode().toLowerCase().contains(text) || item.getUniverCode().toUpperCase().contains(text)) {

                    lstUnivers.add(item);
                }
            }
        }

        universAdapter.notifyDataSetChanged();
    }

}














