package com.example.gestorehome.dbcontroller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

public class DBcontroller {
    private static final String DATABASE_NOME = "myDoc";
    private static final int DATABASE_VERSIONE = 1;

    private static final String TABELLA_DOC =
            "CREATE TABLE " + DBmyDoc.docTable.TBL_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    DBmyDoc.docTable.FIELD_DOCTYPE + " INTEGER not null," +
                    DBmyDoc.docTable.FIELD_EXPDATE + " TEXT," +
                    DBmyDoc.docTable.FIELD_REMEMBERIT + " INTEGER)";
    private static final String TABELLA_PIC =
            "CREATE TABLE " + DBmyDoc.picsTable.TBL_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    DBmyDoc.picsTable.FIELD_DOCID + " INTEGER not null," +
                    DBmyDoc.picsTable.FIELD_PIC + " BLOB)" ;

    final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DBcontroller(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NOME, null, DATABASE_VERSIONE);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(TABELLA_DOC);
                db.execSQL(TABELLA_PIC);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DatabaseHelper.class.getName(), "Aggiornamento database dalla versione " + oldVersion + " alla "
                    + newVersion + ". I dati esistenti verranno eliminati.");
            db.execSQL("DROP TABLE IF EXISTS clienti");
            onCreate(db);
        }

    }


    public DBcontroller open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }


    public void close() {
        DBHelper.close();
    }


    public long addDoc(int docType, String expDate, boolean remember) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DBmyDoc.docTable.FIELD_DOCTYPE, docType);
        initialValues.put(DBmyDoc.docTable.FIELD_EXPDATE, expDate);
        int myInt = remember ? 1 : 0;
        initialValues.put(DBmyDoc.docTable.FIELD_REMEMBERIT, (myInt));
        return db.insert(DBmyDoc.docTable.TBL_NAME, null, initialValues);
    }

    public long addPic (byte[] picS, int doc)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(DBmyDoc.picsTable.FIELD_DOCID, doc);
        initialValues.put(DBmyDoc.picsTable.FIELD_PIC, picS);
        return db.insert(DBmyDoc.picsTable.TBL_NAME, null, initialValues);
    }

    /*
    public boolean cancellaBambino(long rigaId) {
        return db.delete(DBbambini.TBL_NAME, DBbambini.FIELD_ID + "=" + rigaId, null) > 0;
    }


    public Cursor ottieniTuttiBambini() {
        return db.query(DBbambini.TBL_NAME, new String[]{DBbambini.FIELD_ID, DBbambini.FIELD_NOME, DBbambini.FIELD_COGNOME}, null, null, null, null, null);
    }

*/
    public int getLastID() throws SQLException {
        Cursor mCursore = db.query(DBmyDoc.docTable.TBL_NAME, new String[]{DBmyDoc.docTable.FIELD_ID}, null, null, null, null, "ID DESC", "1");
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        return Integer.parseInt(mCursore.getString(0));
    }

    public ArrayList<ArrayList<String>> getDataContentList() throws SQLException {
        Cursor mCursore = db.rawQuery("SELECT ID, docType, expDate FROM myDocs", null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        ArrayList<ArrayList<String>> strings = new ArrayList<>();
        ArrayList<String> strings1 = new ArrayList<>();
        ArrayList<String> strings2 = new ArrayList<>();
        ArrayList<String> strings3 = new ArrayList<>();
        for (int i = 0; i < mCursore.getCount(); i++) {
            strings1.add(mCursore.getString(0));
            mCursore.moveToNext();
        }
        strings.add(strings1);
        mCursore.moveToFirst();
        for (int i = 0; i < mCursore.getCount(); i++) {
            strings2.add(mCursore.getString(1));
            mCursore.moveToNext();
        }
        strings.add(strings2);
        mCursore.moveToFirst();
        for (int i = 0; i < mCursore.getCount(); i++) {
            strings3.add(mCursore.getString(2));
            mCursore.moveToNext();
        }
        strings.add(strings3);
        mCursore.close();
        return strings;
    }

public Bitmap getFirstImageFromDocID(int ID)
{
    Bitmap bm = null;
    try{
        Cursor mCursore = db.query( DBmyDoc.picsTable.TBL_NAME, new String[]{DBmyDoc.picsTable.FIELD_PIC},
                DBmyDoc.picsTable.FIELD_DOCID + "=" + ID, null, null, null, "ID ASC", "1");
        mCursore.moveToFirst();
        byte[] byteArray = mCursore.getBlob(0);
        bm = BitmapFactory.decodeByteArray(byteArray, 0 ,byteArray.length);
    }
    catch (Exception exp){

    }
    return  bm;
}
/*

    public boolean aggiornaBambino(long rigaId, String name, String email) {
        ContentValues args = new ContentValues();
        args.put(DBbambini.FIELD_NOME, name);
        args.put(DBbambini.FIELD_COGNOME, email);
        return db.update(DBbambini.TBL_NAME, args, DBbambini.FIELD_ID + "=" + rigaId, null) > 0;
    }*/

}
