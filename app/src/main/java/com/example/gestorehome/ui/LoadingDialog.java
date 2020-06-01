package com.example.gestorehome.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.gestorehome.R;

public class LoadingDialog {
private Activity context;
private AlertDialog dialog;

    public LoadingDialog(Activity CTX){
        context =CTX;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = context.getLayoutInflater();
        builder.setView(layoutInflater.inflate(R.layout.loadingalert, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }

}

