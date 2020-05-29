package com.example.gestorehome.dbcontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DBcontroller {
    /*private static final String DATABASE_NOME = "myDoc";
    private static final int DATABASE_VERSIONE = 1;
    private static final String TABELLA_DOC =
            "CREATE TABLE " + DBmyDoc.docTable.TBL_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    DBmyDoc.docTable.FIELD_DOCTYPE + " INTEGER not null," +
                    DBmyDoc.docTable.FIELD_EXPDATE + " TEXT," +
                    DBmyDoc.docTable.FIELD_TITOLARE + " TEXT not null," +
                    DBmyDoc.docTable.FIELD_REMEMBERIT + " INTEGER)";
    private static final String TABELLA_PIC =
            "CREATE TABLE " + DBmyDoc.picsTable.TBL_NAME + " ( ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE," +
                    DBmyDoc.picsTable.FIELD_DOCID + " INTEGER not null," +
                    DBmyDoc.picsTable.FIELD_PIC + " BLOB)";*/

    final Context context;
    //private DatabaseHelper DBHelper;
    //private SQLiteDatabase db;

    public DBcontroller(Context ctx) {
        this.context = ctx;
    }

    /*private static class DatabaseHelper extends SQLiteOpenHelper {
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

    /*public DBcontroller open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }*/

    /*public void close() {
        DBHelper.close();
    }*/

    public boolean addDoc(int docType, String expDate, boolean remember, String titolare) throws ExecutionException, InterruptedException {
        InsertDoc insertDoc = new InsertDoc();
        int rem = BooleanUtils.toInteger(remember);
        return insertDoc.execute(Integer.toString(docType), expDate, titolare, Integer.toString(rem)).get();
    }

    public boolean addPic(byte[] picS, int doc) throws ExecutionException, InterruptedException {
        InsertPic insertPic = new InsertPic();
        String encodedImage = Base64.encodeToString(picS, Base64.DEFAULT);
        return insertPic.execute(encodedImage, Integer.toString(doc), null).get();
    }

    public boolean cancellaDoc(int ID) throws ExecutionException, InterruptedException {
        RemoveOneDoc removeOneDoc =new RemoveOneDoc();
        return removeOneDoc.execute(String.valueOf(ID)).get();
    }

    public int getLastID() throws ExecutionException, InterruptedException {
        int ID = 0;
        SelectLID selectLID = new SelectLID();
        ID = selectLID.execute().get();
        return ID;
    }

    public ArrayList<ArrayList<String>> getDataContentList() throws ExecutionException, InterruptedException {
        SelectAllDoc selectAllDoc = new SelectAllDoc();
        ArrayList<ArrayList<String>> ret = new ArrayList<>(selectAllDoc.execute().get());
        return ret ;
    }

    public Bitmap getFirstImageFromDocID(int ID) throws ExecutionException, InterruptedException {
        Bitmap bm = null;/*
        try {
            Cursor mCursore = db.query(DBmyDoc.picsTable.TBL_NAME, new String[]{DBmyDoc.picsTable.FIELD_PIC},
                    DBmyDoc.picsTable.FIELD_DOCID + "=" + ID, null, null, null, "ID ASC", "1");
            mCursore.moveToFirst();
            byte[] byteArray = mCursore.getBlob(0);
            bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception exp) {

        }*/
        SelectBitmapDocID selectBitmapDocID = new SelectBitmapDocID();

        return selectBitmapDocID.execute(String.valueOf(ID)).get();
    }

    public ArrayList<Bitmap> getAllBitmapForID(String ID) throws ExecutionException, InterruptedException {
        /*ArrayList<Bitmap> bitmaps = new ArrayList<>();
        try {
            Cursor mCursore = db.query(DBmyDoc.picsTable.TBL_NAME, new String[]{DBmyDoc.picsTable.FIELD_PIC},
                    DBmyDoc.picsTable.FIELD_DOCID + "=" + ID, null, null, null, "ID ASC", null);
            mCursore.moveToFirst();
            for (int i = 0; i < mCursore.getCount(); i++) {
                byte[] byteArray = mCursore.getBlob(0);
                bitmaps.add(BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length));
                mCursore.moveToNext();
            }
        } catch (Exception exp) {
        }*/
        SelectAllBitmapDocID selectBitmapDocID = new SelectAllBitmapDocID();
        return selectBitmapDocID.execute(String.valueOf(ID)).get();
    }

    public ArrayList<ArrayList<String>> getDataFromID(String ID) throws ExecutionException, InterruptedException {
        /*Cursor mCursore = db.query(DBmyDoc.docTable.TBL_NAME, new String[]{DBmyDoc.docTable.FIELD_EXPDATE, DBmyDoc.docTable.FIELD_DOCTYPE, DBmyDoc.docTable.FIELD_REMEMBERIT, DBmyDoc.docTable.FIELD_TITOLARE},
                DBmyDoc.docTable.FIELD_ID + " = " + ID, null, null, null, null, null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        ArrayList<ArrayList<String>> temp = new ArrayList<>();
        ArrayList<String> a = new ArrayList<>();
        a.add(mCursore.getString(1));
        ArrayList<String> b = new ArrayList<>();
        b.add(mCursore.getString(0));
        ArrayList<String> c = new ArrayList<>();
        c.add(mCursore.getString(2));
        ArrayList<String> d = new ArrayList<>();
        d.add(mCursore.getString(3));

        temp.add(a);
        temp.add(b);
        temp.add(c);
        temp.add(d);
        return temp;*/
        SelectOneDoc selectOneDoc = new SelectOneDoc();
        return selectOneDoc.execute(String.valueOf(ID)).get();
    }

}



