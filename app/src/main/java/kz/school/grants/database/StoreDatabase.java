package kz.school.grants.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import kz.school.grants.advice_menu.Advice;
import kz.school.grants.spec_menu.models.SubjectPair;

public class StoreDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "igrants.db";
    private static final int DATABASE_VERSION = 20;

    public static final String TABLE_PROFILE_SUBJECTS = "profile_subjects";
    public static final String TABLE_BLOCKS = "blocks";
    public static final String TABLE_PROFESSIONS = "professions";
    public static final String TABLE_GRANTS = "grants";
    public static final String TABLE_ADVICE_HISTORY = "advice_history";
    public static final String TABLE_UNIVER_LIST = "univer_list";
    public static final String TABLE_ATAULI_SPECS = "atauli_specs";
    public static final String TABLE_ATAULI_GRANTS = "atauli_grants";

    public static final String TABLE_SERPIN_SPECS = "serpin_specs";
    public static final String TABLE_SERPIN_GRANTS = "serpin_grants";
    public static final String TABLE_VER = "versions";

    // TABLE_PROFILE_SUBJECTS columns
    public static final String COLUMN_SUBJECTS_ID = "s_id";
    public static final String COLUMN_SUBJECTS_PAIR = "subjects_title";
    public static final String COLUMN_PROF_COUNT = "prof_count";

    // TABLE_BLOCKS columns
    // COLUMN_S_ID
    public static final String COLUMN_BLOCK_CODE = "block_code";
    public static final String COLUMN_BLOCK_TITLE = "block_title";


    // TABLE_PROFESSIONS columns
    // COLUMN_BLOCK_CODE
    public static final String COLUMN_PROF_CODE = "prof_code";
    public static final String COLUMN_PROF_TITLE = "prof_title";


    // TABLE_GRANTS columns
    // COLUMN_S_ID
    // COLUMN_BLOCK_CODE
    public static final String COLUMN_YEAR_18_19_KAZ_COUNT = "y18_19_kaz";
    public static final String COLUMN_YEAR_18_19_RUS_COUNT = "y18_19_rus";
    public static final String COLUMN_YEAR_19_20_KAZ_COUNT = "y19_20_kaz";
    public static final String COLUMN_YEAR_19_20_RUS_COUNT = "y19_20_rus";
    public static final String COLUMN_AUIL_KAZ_MAX_POINT = "auil_kaz_max";
    public static final String COLUMN_AUIL_KAZ_MIN_POINT = "auil_kaz_min";
    public static final String COLUMN_AUIL_KAZ_AVE_POINT = "auil_kaz_ave";
    public static final String COLUMN_AUIL_RUS_MAX_POINT = "auil_rus_max";
    public static final String COLUMN_AUIL_RUS_MIN_POINT = "auil_rus_min";
    public static final String COLUMN_AUIL_RUS_AVE_POINT = "auil_rus_ave";
    public static final String COLUMN_KAZ_MAX_POINT = "kaz_max";
    public static final String COLUMN_KAZ_MIN_POINT = "kaz_min";
    public static final String COLUMN_KAZ_AVE_POINT = "kaz_ave";
    public static final String COLUMN_RUS_MAX_POINT = "rus_max";
    public static final String COLUMN_RUS_MIN_POINT = "rus_min";
    public static final String COLUMN_RUS_AVE_POINT = "rus_ave";

    //TABLE_ADVICE_HISTORY columns
    public static final String COLUMN_ADVICE_ID = "a_id";
    public static final String COLUMN_STUDENT_NAME = "student_name";
    public static final String COLUMN_STUDENT_PHONE = "student_phone";
    public static final String COLUMN_ADVICE_DATE = "advice_date";
    public static final String COLUMN_GROUP_LIST = "group_list";
    public static final String COLUMN_UNIVER_LIST = "univer_list";

    //TABLE_UNIVER_LIST columns
    public static final String COLUMN_UNIVER_ID = "univer_id";
    public static final String COLUMN_UNIVER_IMAGE = "univer_image";
    public static final String COLUMN_UNIVER_NAME = "univer_name";
    public static final String COLUMN_UNIVER_PHON = "univer_phone";
    public static final String COLUMN_UNIVER_LOCATION = "univer_location";
    public static final String COLUMN_UNIVER_CODE = "univer_code";
    public static final String COLUMN_PROFESSIONS_LIST = "professions_list";

    //TABLE_ATAULI_GRANT_LIST columns
    public static final String COLUMN_SPEC_CODE = "spec_code";
    public static final String COLUMN_SPEC_NAME = "spec_name";
    public static final String COLUMN_SPEC_SUBJECTS_PAIR = "spec_subjects_pair";
    public static final String COLUMN_GRANT_CODE = "spec_grant_code";

    //TABLE_VER columns
    public static final String COLUMN_SUBJECT_VER = "subject_ver";
    public static final String COLUMN_UNIVER_LIST_VER = "univer_list_ver";
    public static final String COLUMN_ATAULI_GRANTS_LIST_VER = "atauli_grant_list_ver";
    public static final String COLUMN_SERPIN_VER = "serpin_ver";
    Context context;

    public StoreDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_PROFILE_SUBJECTS + "(" +
                COLUMN_SUBJECTS_ID + " TEXT, " +
                COLUMN_SUBJECTS_PAIR + " TEXT , " +
                COLUMN_PROF_COUNT + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_BLOCKS + "(" +
                COLUMN_SUBJECTS_ID + " TEXT, " +
                COLUMN_BLOCK_CODE + " TEXT , " +
                COLUMN_BLOCK_TITLE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_PROFESSIONS + "(" +
                COLUMN_BLOCK_CODE + " TEXT , " +
                COLUMN_SUBJECTS_PAIR + " TEXT , " +
                COLUMN_PROF_CODE + " TEXT , " +
                COLUMN_PROF_TITLE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ATAULI_SPECS + "(" +
                COLUMN_SPEC_CODE + " TEXT , " +
                COLUMN_SPEC_NAME + " TEXT , " +
                COLUMN_SPEC_SUBJECTS_PAIR + " TEXT , " +
                COLUMN_GRANT_CODE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_ATAULI_GRANTS + "(" +
                COLUMN_SPEC_CODE + " TEXT, " +
                COLUMN_UNIVER_CODE + " TEXT , " +
                COLUMN_YEAR_18_19_KAZ_COUNT + " INTEGER , " +
                COLUMN_YEAR_18_19_RUS_COUNT + " INTEGER , " +
                COLUMN_YEAR_19_20_KAZ_COUNT + " INTEGER , " +
                COLUMN_YEAR_19_20_RUS_COUNT + " INTEGER , " +
                COLUMN_AUIL_KAZ_MAX_POINT + " INTEGER , " +
                COLUMN_AUIL_KAZ_MIN_POINT + " INTEGER , " +
                COLUMN_AUIL_KAZ_AVE_POINT + " INTEGER , " +
                COLUMN_AUIL_RUS_MAX_POINT + " INTEGER , " +
                COLUMN_AUIL_RUS_MIN_POINT + " INTEGER , " +
                COLUMN_AUIL_RUS_AVE_POINT + " INTEGER , " +
                COLUMN_KAZ_MAX_POINT + " INTEGER , " +
                COLUMN_KAZ_MIN_POINT + " INTEGER , " +
                COLUMN_KAZ_AVE_POINT + " INTEGER , " +
                COLUMN_RUS_MAX_POINT + " INTEGER , " +
                COLUMN_RUS_MIN_POINT + " INTEGER , " +
                COLUMN_RUS_AVE_POINT + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_SERPIN_SPECS + "(" +
                COLUMN_SPEC_CODE + " TEXT , " +
                COLUMN_SPEC_NAME + " TEXT , " +
                COLUMN_SPEC_SUBJECTS_PAIR + " TEXT , " +
                COLUMN_GRANT_CODE + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_SERPIN_GRANTS + "(" +
                COLUMN_SPEC_CODE + " TEXT, " +
                COLUMN_UNIVER_CODE + " TEXT , " +
                COLUMN_YEAR_18_19_KAZ_COUNT + " INTEGER , " +
                COLUMN_YEAR_19_20_KAZ_COUNT + " INTEGER , " +
                COLUMN_KAZ_MAX_POINT + " INTEGER , " +
                COLUMN_KAZ_MIN_POINT + " INTEGER , " +
                COLUMN_KAZ_AVE_POINT + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_GRANTS + "(" +
                COLUMN_SUBJECTS_ID + " TEXT, " +
                COLUMN_BLOCK_CODE + " TEXT , " +
                COLUMN_YEAR_18_19_KAZ_COUNT + " INTEGER , " +
                COLUMN_YEAR_18_19_RUS_COUNT + " INTEGER , " +
                COLUMN_YEAR_19_20_KAZ_COUNT + " INTEGER , " +
                COLUMN_YEAR_19_20_RUS_COUNT + " INTEGER , " +
                COLUMN_AUIL_KAZ_MAX_POINT + " INTEGER , " +
                COLUMN_AUIL_KAZ_MIN_POINT + " INTEGER , " +
                COLUMN_AUIL_KAZ_AVE_POINT + " INTEGER , " +
                COLUMN_AUIL_RUS_MAX_POINT + " INTEGER , " +
                COLUMN_AUIL_RUS_MIN_POINT + " INTEGER , " +
                COLUMN_AUIL_RUS_AVE_POINT + " INTEGER , " +
                COLUMN_KAZ_MAX_POINT + " INTEGER , " +
                COLUMN_KAZ_MIN_POINT + " INTEGER , " +
                COLUMN_KAZ_AVE_POINT + " INTEGER , " +
                COLUMN_RUS_MAX_POINT + " INTEGER , " +
                COLUMN_RUS_MIN_POINT + " INTEGER , " +
                COLUMN_RUS_AVE_POINT + " INTEGER)");

        db.execSQL("CREATE TABLE " + TABLE_ADVICE_HISTORY + "(" +
                COLUMN_ADVICE_ID + " TEXT , " +
                COLUMN_STUDENT_NAME + " TEXT , " +
                COLUMN_STUDENT_PHONE + " TEXT , " +
                COLUMN_ADVICE_DATE + " TEXT , " +
                COLUMN_GROUP_LIST + " TEXT , " +
                COLUMN_UNIVER_LIST + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_UNIVER_LIST + "(" +
                COLUMN_UNIVER_ID + " TEXT , " +
                COLUMN_UNIVER_IMAGE + " TEXT , " +
                COLUMN_UNIVER_NAME + " TEXT , " +
                COLUMN_UNIVER_PHON + " TEXT , " +
                COLUMN_UNIVER_LOCATION + " TEXT , " +
                COLUMN_UNIVER_CODE + " TEXT , " +
                COLUMN_PROFESSIONS_LIST + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_VER + "(" +
                COLUMN_UNIVER_LIST_VER + " TEXT , " +
                COLUMN_ATAULI_GRANTS_LIST_VER + " TEXT , " +
                COLUMN_SERPIN_VER + " TEXT , " +
                COLUMN_SUBJECT_VER + " TEXT)");

        addVersions(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFILE_SUBJECTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BLOCKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROFESSIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GRANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVICE_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UNIVER_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATAULI_SPECS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATAULI_GRANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERPIN_SPECS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERPIN_GRANTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VER);

        onCreate(db);
    }

    public void cleanSubjects(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_PROFILE_SUBJECTS);
    }

    public void cleanBlocks(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_BLOCKS);
        db.execSQL("delete from " + TABLE_PROFESSIONS);
        db.execSQL("delete from " + TABLE_GRANTS);
    }

    public void cleanVersions(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_VER);

    }

    public void cleanUnivers(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_UNIVER_LIST);
    }

    public void cleanGrants(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_ATAULI_SPECS);
        db.execSQL("delete from " + TABLE_ATAULI_GRANTS);
    }

    public void cleanSerpin(SQLiteDatabase db) {
        db.execSQL("delete from " + TABLE_SERPIN_SPECS);
        db.execSQL("delete from " + TABLE_SERPIN_GRANTS);
    }

    public Cursor getCursorWhereGreaterThan(SQLiteDatabase db, String tableName, String columnName, String greaterValue, String orderColumn) {
        return db.rawQuery("SELECT * FROM " + tableName + " WHERE " + columnName + ">?" + " ORDER BY " + orderColumn, new String[]{greaterValue});
    }

    public Cursor getCursorWhereEqualTo(SQLiteDatabase db, String tableName, String columnName, String equalValue, String orderColumn) {
        return db.rawQuery("SELECT * FROM " + tableName + " WHERE " + columnName + "=?" + " ORDER BY " + orderColumn, new String[]{equalValue});
    }

    public Cursor getCursorWhereLikeTo(SQLiteDatabase db, String tableName, String columnName, String equalValue, String orderColumn) {
        return db.rawQuery("SELECT * FROM " + tableName + " WHERE " + columnName + " LIKE ?" + " ORDER BY " + orderColumn, new String[]{"%"+ equalValue+ "%" });
    }

    public Cursor getCursorAll(SQLiteDatabase db, String tableName) {
        return db.rawQuery("SELECT * FROM " + tableName,null);
    }

    public String getStrFromColumn(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));

    }

    public String getIntegerFromColumn(Cursor cursor, String columnName) {
        if (cursor.getColumnIndex(columnName) != -1)
            return "" + cursor.getInt(cursor.getColumnIndex(columnName));

        return "no data";
    }

//    public Cursor getBookByFKey(String fkey) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " WHERE " +
//                COLUMN_FKEY + "=?", new String[]{fkey});
//        return res;
//
//    }

    public void addVersions(SQLiteDatabase db) {
        ContentValues versionValues = new ContentValues();
        versionValues.put(COLUMN_SUBJECT_VER, "0");
        versionValues.put(COLUMN_UNIVER_LIST_VER, "0");
        versionValues.put(COLUMN_ATAULI_GRANTS_LIST_VER, "0");
        versionValues.put(COLUMN_SERPIN_VER, "0");

        db.insert(TABLE_VER, null, versionValues);
    }

//    public void updateBook(SQLiteDatabase db, Book book) {
//        ContentValues updateValues = new ContentValues();
//
//        updateValues.put(COLUMN_BNAME, book.getName());
//        updateValues.put(COLUMN_BAUTHOR, book.getAuthor());
//        updateValues.put(COLUMN_BDESC, book.getDesc());
//        updateValues.put(COLUMN_BPAGE_NUMBER, book.getPage_number());
//        updateValues.put(COLUMN_BRATING, book.getRating());
//        updateValues.put(COLUMN_PHOTO, book.getPhoto());
//        updateValues.put(COLUMN_BRESERVED, book.getReserved());
//        updateValues.put(COLUMN_QR_CODE, book.getQr_code());
//        updateValues.put(COLUMN_IMG_STORAGE_NAME, book.getImgStorageName());
//
//        db.update(TABLE_BOOKS, updateValues, COLUMN_FKEY + "='" + book.getFirebaseKey()+"'", null);
//        Log.i("child", "db: "+book.getName());
//    }

    public void deleteSubject(SQLiteDatabase db, SubjectPair subjectPair) {
        db.delete(TABLE_PROFILE_SUBJECTS, COLUMN_SUBJECTS_ID + "='" + subjectPair.getId() + "'", null);
    }

    public void deleteAdvice(SQLiteDatabase db, Advice advice) {
        db.delete(TABLE_ADVICE_HISTORY, COLUMN_ADVICE_ID + "='" + advice.getAdviceId() + "'", null);
    }
}