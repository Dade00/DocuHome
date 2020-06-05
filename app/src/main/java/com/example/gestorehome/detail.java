package com.example.gestorehome;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestorehome.dbcontroller.DBcontroller;
import com.example.gestorehome.ui.LoadingDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

public class detail extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout viewCategoryNames;
    private  String ID;
    private  ArrayList<ArrayList<String>> data = null;
    private ArrayList<Bitmap> image = null;
    private Activity act = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        this.setTitle(R.string.details);
        Intent intent = getIntent();
        ID = intent.getStringExtra("EXTRA_ID");
        final DBcontroller dBcontroller = new DBcontroller(this);
        class UIworker extends AsyncTask<Activity, String, Void> {
            final LoadingDialog loadingDialog = new LoadingDialog(act);
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loadingDialog.startLoadingDialog();
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);


                TextView docType = findViewById(R.id.dynamicDocType);
                TextView expDate = findViewById(R.id.dynamicExpDate);
                TextView titolare = findViewById(R.id.dynamicName);
                CheckBox rembembering = findViewById(R.id.dynamicRemberYesNo);




                viewCategoryNames = findViewById(R.id.viewDocsImage);
                for (int i = 0; i < image.size(); i++) {
                    viewCategoryNames.addView(addButton(image.get(i)));
                }
                Resources res = act.getResources();
                String[] tempVal = res.getStringArray(R.array.DocType);
                docType.setText(tempVal[Integer.parseInt(data.get(1).get(0))]);
                expDate.setText(data.get(2).get(0));
                titolare.setText(data.get(3).get(0));
                int myInt = Integer.parseInt(data.get(4).get(0));
                boolean myBool = BooleanUtils.toBoolean(myInt);
                rembembering.setChecked(myBool);
                //dBcontroller.close();

                FloatingActionButton button = findViewById(R.id.backButtone);
                button.setOnClickListener((View.OnClickListener)act);

                FloatingActionButton button2 = findViewById(R.id.deleteButton);
                button2.setOnClickListener((View.OnClickListener)act);

                loadingDialog.dismissDialog();

            }

            @Override
            protected Void doInBackground(Activity... arg) {
                try {
                    data = new ArrayList<>(dBcontroller.getDataFromID(ID));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    image = new ArrayList<>(dBcontroller.getAllBitmapForID(ID));
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        UIworker uIworker = new UIworker();
        uIworker.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this);
        //dBcontroller.open();

    }

    private ImageView addButton(Bitmap content) {
        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        final LinearLayout.LayoutParams layoutParamsPREVIEW = new LinearLayout.LayoutParams((int)(metrics.widthPixels*0.80), (int)(metrics.heightPixels*0.80));
        layoutParamsPREVIEW.setMargins(0, 10, 30, 10);
        final ImageView btDoc = new ImageView(this);
        btDoc.setLayoutParams(layoutParamsPREVIEW);
        btDoc.setAdjustViewBounds(true);
        btDoc.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btDoc.setImageBitmap(content);
        return btDoc;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backButtone)
        this.finish();
        else if (v.getId() == R.id.deleteButton)
        {
            DBcontroller dBcontroller = new DBcontroller(this);
            long ii = Long.parseLong(ID);
            /*dBcontroller.open();*/
            boolean success = false;
            try {
                success = dBcontroller.cancellaDoc((int) ii);
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();

            }
            /*dBcontroller.close();*/
            if(success)
            {setResult(Activity.RESULT_OK);
            this.finish();}
            else
                Toast.makeText(this, "Errore nella cancellazione, riprovare",Toast.LENGTH_SHORT).show();
        }
    }
}
