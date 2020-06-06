package com.example.gestorehome.dbcontroller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class DBcontroller {

    final Context context;

    public DBcontroller(Context ctx) {
        this.context = ctx;
    }

    public boolean addDoc(int docType, String expDate, boolean remember, String titolare, String IDut) throws ExecutionException, InterruptedException {
        InsertDoc insertDoc = new InsertDoc();
        int rem = BooleanUtils.toInteger(remember);
        return insertDoc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,Integer.toString(docType), expDate, titolare, Integer.toString(rem), IDut).get();
    }

    public boolean addPic(byte[] picS, int doc, Context context, String id) throws ExecutionException, InterruptedException {
        InsertPic insertPic = new InsertPic();
        String encodedImage = Base64.encodeToString(picS, Base64.DEFAULT);
        insertPic.CTX = context;
        return insertPic.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR ,encodedImage, Integer.toString(doc), id).get();
    }

    public boolean cancellaDoc(int ID) throws ExecutionException, InterruptedException {
        RemoveOneDoc removeOneDoc =new RemoveOneDoc();
        return removeOneDoc.execute(String.valueOf(ID)).get();
    }

    public int getLastID(String id) throws ExecutionException, InterruptedException {
        int ID = 0;
        SelectLID selectLID = new SelectLID();
        ID = selectLID.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, id).get();
        return ID;
    }

    public ArrayList<ArrayList<String>> getDataContentList(String Idut) throws ExecutionException, InterruptedException {
        SelectAllDoc selectAllDoc = new SelectAllDoc();
        ArrayList<ArrayList<String>> ret = new ArrayList<>(selectAllDoc.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Idut).get());
        return ret ;
    }

    public Bitmap getFirstImageFromDocID(int ID, String isAcc) throws ExecutionException, InterruptedException {
        Bitmap bm = null;
        SelectBitmapDocID selectBitmapDocID = new SelectBitmapDocID();

        return selectBitmapDocID.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, String.valueOf(ID), isAcc).get();
    }

    public ArrayList<Bitmap> getAllBitmapForID(String ID) throws ExecutionException, InterruptedException {
        SelectAllBitmapDocID selectBitmapDocID = new SelectAllBitmapDocID();
        return selectBitmapDocID.execute(String.valueOf(ID)).get();
    }

    public ArrayList<ArrayList<String>> getDataFromID(String ID) throws ExecutionException, InterruptedException {
        SelectOneDoc selectOneDoc = new SelectOneDoc();
        return selectOneDoc.execute(String.valueOf(ID)).get();
    }

}



