package com.example.gestorehome.dbcontroller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.commons.lang3.BooleanUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DBcontroller {
    private static final String DATABASE_NOME = "myDoc";
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
                    DBmyDoc.picsTable.FIELD_PIC + " BLOB)";

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

    public boolean addDoc(int docType, String expDate, boolean remember, String titolare) throws ExecutionException, InterruptedException {
        InsertDoc callAPI = new InsertDoc();
        int rem = BooleanUtils.toInteger(remember);
        return callAPI.execute(Integer.toString(docType), expDate, titolare, Integer.toString(rem)).get();
    }

    public boolean addPic(byte[] picS, int doc) throws ExecutionException, InterruptedException {
        /*
        ContentValues initialValues = new ContentValues();
        initialValues.put(DBmyDoc.picsTable.FIELD_DOCID, doc);
        initialValues.put(DBmyDoc.picsTable.FIELD_PIC, picS);
        return db.insert(DBmyDoc.picsTable.TBL_NAME, null, initialValues);*/
        InsertPic callAPI = new InsertPic();
        String encodedImage = Base64.encodeToString(picS, Base64.DEFAULT);
        if(callAPI.execute(encodedImage,Integer.toString(doc), null).get())
        return true;
        else
            return false;
    }

    public boolean cancellaDoc(long ID) {
        boolean q = true;
        db.delete(DBmyDoc.docTable.TBL_NAME, DBmyDoc.docTable.FIELD_ID + " = " + ID, null);
        db.delete(DBmyDoc.picsTable.TBL_NAME, DBmyDoc.picsTable.FIELD_DOCID + " = " + ID, null);
        return q;
    }

    public int getLastID() throws SQLException {
        Cursor mCursore = db.query(DBmyDoc.docTable.TBL_NAME, new String[]{DBmyDoc.docTable.FIELD_ID}, null, null, null, null, "ID DESC", "1");
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        return Integer.parseInt(mCursore.getString(0));
    }

    public ArrayList<ArrayList<String>> getDataContentList() throws SQLException {
        Cursor mCursore = db.rawQuery("SELECT ID, docType, expDate, titolare FROM myDocs", null);
        if (mCursore != null) {
            mCursore.moveToFirst();
        }
        ArrayList<ArrayList<String>> strings = new ArrayList<>();
        ArrayList<String> strings1 = new ArrayList<>();
        ArrayList<String> strings2 = new ArrayList<>();
        ArrayList<String> strings3 = new ArrayList<>();
        ArrayList<String> strings4 = new ArrayList<>();
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
        mCursore.moveToFirst();
        for (int i = 0; i < mCursore.getCount(); i++) {
            strings4.add(mCursore.getString(3));
            mCursore.moveToNext();
        }
        strings.add(strings4);
        mCursore.close();
        return strings;
    }

    public Bitmap getFirstImageFromDocID(int ID) {
        Bitmap bm = null;
        try {
            Cursor mCursore = db.query(DBmyDoc.picsTable.TBL_NAME, new String[]{DBmyDoc.picsTable.FIELD_PIC},
                    DBmyDoc.picsTable.FIELD_DOCID + "=" + ID, null, null, null, "ID ASC", "1");
            mCursore.moveToFirst();
            byte[] byteArray = mCursore.getBlob(0);
            bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } catch (Exception exp) {

        }
        return bm;
    }

    public ArrayList<Bitmap> getAllBitmapForID(String ID) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
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
        }
        return bitmaps;
    }

    public ArrayList<ArrayList<String>> getDataFromID(String ID) throws SQLException {
        Cursor mCursore = db.query(DBmyDoc.docTable.TBL_NAME, new String[]{DBmyDoc.docTable.FIELD_EXPDATE, DBmyDoc.docTable.FIELD_DOCTYPE, DBmyDoc.docTable.FIELD_REMEMBERIT, DBmyDoc.docTable.FIELD_TITOLARE},
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
        return temp;
    }

}

class InsertPic extends AsyncTask<String, String, Boolean> {
    private String urlPHP ="http://raspberrypi/dbQuery/insertPic.php";
    private boolean fatto;
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Boolean arg)
    {

    }

    @Override
    protected Boolean doInBackground(String... arg) {
        String pic = arg[0];
        String doc = arg[1];
        try {
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("docid", "UTF-8") + "=" + URLEncoder.encode(doc, "UTF-8") + "&" +
                    URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(pic, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();
            fatto=true;
            return fatto;
        } catch (IOException e) {
            fatto=false;
            return fatto;
        }
    }
}

class InsertDoc extends AsyncTask<String, String, Boolean> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Boolean result) {


    }

    @Override
    protected Boolean doInBackground(String... arg) {
        String docType = arg[0];
        String expDate = arg[1];
        String titolare = arg[2];
        String reme = arg[3];
        try {
            String urlPHP = "http://raspberrypi/dbQuery/insertDoc.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("doctype", "UTF-8") + "=" + URLEncoder.encode(docType, "UTF-8") + "&" +
                    URLEncoder.encode("expdate", "UTF-8") + "=" + URLEncoder.encode(expDate, "UTF-8") + "&" +
                    URLEncoder.encode("titolare", "UTF-8") + "=" + URLEncoder.encode(titolare, "UTF-8")+ "&" +
                    URLEncoder.encode("remember", "UTF-8") + "=" + URLEncoder.encode(reme, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            httpURLConnection.disconnect();
            return true ;
        } catch (IOException e) {
            return false;
        }
    }
}