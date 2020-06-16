package kz.school.grants.advice_menu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import kz.school.grants.R;
import kz.school.grants.advice_menu.activities.AddAdvice;
import kz.school.grants.advice_menu.activities.AdviceReport;
import kz.school.grants.database.StoreDatabase;
import kz.school.grants.spec_menu.adapters.RecyclerItemClickListener;

import static kz.school.grants.database.StoreDatabase.COLUMN_ADVICE_DATE;
import static kz.school.grants.database.StoreDatabase.COLUMN_ADVICE_ID;
import static kz.school.grants.database.StoreDatabase.COLUMN_GROUP_LIST;
import static kz.school.grants.database.StoreDatabase.COLUMN_STUDENT_NAME;
import static kz.school.grants.database.StoreDatabase.COLUMN_STUDENT_PHONE;
import static kz.school.grants.database.StoreDatabase.COLUMN_UNIVER_LIST;
import static kz.school.grants.database.StoreDatabase.TABLE_ADVICE_HISTORY;


public class AdviceFragment extends Fragment implements View.OnClickListener {
    View view;

    @BindView(R.id.historyRecyclerview)
    RecyclerView historyRecyclerview;
    @BindView(R.id.fabBtn)
    FloatingActionButton fabBtn;

    private StoreDatabase storeDb;
    private SQLiteDatabase sqdb;
    private LinearLayoutManager linearLayoutManager;
    private AdviceHistoryAdapter adviceHistoryAdapter;
    ArrayList<Advice> adviceList;
    Gson gson;
    Dialog d;
    int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_advice, container, false);

        ButterKnife.bind(this, view);
        initViews();
        fillAdvices();

        return view;
    }

    public void initViews() {

        storeDb = new StoreDatabase(getActivity());
        sqdb = storeDb.getWritableDatabase();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        historyRecyclerview.setLayoutManager(linearLayoutManager);
        historyRecyclerview.setHasFixedSize(true);

        gson = new Gson();
        adviceList = new ArrayList<>();

        adviceHistoryAdapter = new AdviceHistoryAdapter(getActivity(), adviceList);
        historyRecyclerview.setAdapter(adviceHistoryAdapter);
        historyRecyclerview.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), historyRecyclerview, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, final int pos) {


                        Intent adviceReportIntent = new Intent(getActivity(), AdviceReport.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("act", "viewAdvice");
                        bundle.putSerializable("advice", adviceList.get(pos));
                        adviceReportIntent.putExtras(bundle);

                        startActivity(adviceReportIntent);
                    }

                    @Override
                    public void onLongItemClick(View view, int pos) {
                        d = new Dialog(Objects.requireNonNull(getActivity()));
                        position = pos;
                        d.setContentView(R.layout.dialog_edit_subjects);
                        LinearLayout deleteLayout = d.findViewById(R.id.deleteLayout);
                        LinearLayout editLayout = d.findViewById(R.id.editLayout);
                        editLayout.setVisibility(View.GONE);
                        deleteLayout.setOnClickListener(AdviceFragment.this);
                        d.show();
                    }
                })
        );

        fabBtn.setOnClickListener(this);
    }

    private void fillAdvices() {
        Cursor userCursor = sqdb.rawQuery("SELECT * FROM " + TABLE_ADVICE_HISTORY + " ORDER BY " + COLUMN_ADVICE_DATE + " DESC", null);

        if (((userCursor != null) && (userCursor.getCount() > 0))) {
            while (userCursor.moveToNext()) {

                String adviceId = storeDb.getStrFromColumn(userCursor, COLUMN_ADVICE_ID);
                String studentName = storeDb.getStrFromColumn(userCursor, COLUMN_STUDENT_NAME);
                String studentPhone = storeDb.getStrFromColumn(userCursor, COLUMN_STUDENT_PHONE);
                String adviceDate = storeDb.getStrFromColumn(userCursor, COLUMN_ADVICE_DATE);
                String groupListJson = storeDb.getStrFromColumn(userCursor, COLUMN_GROUP_LIST);
                String univerListJson = storeDb.getStrFromColumn(userCursor, COLUMN_UNIVER_LIST);

                Type type = new TypeToken<ArrayList<String>>() {}.getType();
                ArrayList<String> groupList = gson.fromJson(groupListJson, type);
                ArrayList<String> univerList = gson.fromJson(univerListJson, type);
                Log.i("AdviceFragment","groupList: "+groupList);

                adviceList.add(new Advice(adviceId, studentName, studentPhone, adviceDate, groupList, univerList));
            }
        }
        adviceHistoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fabBtn:

                startActivity(new Intent(getActivity(), AddAdvice.class));
                break;

            case R.id.deleteLayout:

                new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme)
                        .setTitle("Өшіру")
                        .setMessage(adviceList.get(position).getStudentName()+"\n"+adviceList.get(position).getAdviceDate())
                        .setPositiveButton(getString(R.string.yes), (dialog, which) -> {
                            storeDb.deleteAdvice(sqdb, adviceList.get(position));
                            adviceList.remove(position);
                            adviceHistoryAdapter.notifyDataSetChanged();
                            d.dismiss();

                        })
                        .setNeutralButton(getString(R.string.no), (dialog, which) -> {
                            d.dismiss();
                        })
                        .show();

                break;
        }
    }
}
