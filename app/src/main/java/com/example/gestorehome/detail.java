package com.example.gestorehome;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestorehome.dbcontroller.DBcontroller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.apache.commons.lang3.BooleanUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.app.AppCompatActivity;

public class detail extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout.LayoutParams layoutParamsPREVIEW = new LinearLayout.LayoutParams(525, 700);
    private LinearLayout viewCategoryNames;
    private  String ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        ID = intent.getStringExtra("EXTRA_ID");

        DBcontroller dBcontroller = new DBcontroller(this);


        //dBcontroller.open();
        ArrayList<Bitmap> image = null;
        try {
            image = new ArrayList<>(dBcontroller.getAllBitmapForID(ID));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        viewCategoryNames = findViewById(R.id.viewDocsImage);
        for (int i = 0; i < image.size(); i++) {
            viewCategoryNames.addView(addButton(image.get(i)));
        }

        TextView docType = findViewById(R.id.dynamicDocType);
        TextView expDate = findViewById(R.id.dynamicExpDate);
        TextView titolare = findViewById(R.id.dynamicName);
        CheckBox rembembering = findViewById(R.id.dynamicRemberYesNo);

        ArrayList<ArrayList<String>> data = null;
        try {
            data = new ArrayList<>(dBcontroller.getDataFromID(ID));
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        Resources res = this.getResources();
        String[] tempVal = res.getStringArray(R.array.DocType);
        docType.setText(tempVal[Integer.parseInt(data.get(1).get(0))]);
        expDate.setText(data.get(2).get(0));
        titolare.setText(data.get(3).get(0));
        int myInt = Integer.parseInt(data.get(4).get(0));
        boolean myBool = BooleanUtils.toBoolean(myInt);
        rembembering.setChecked(myBool);
        //dBcontroller.close();

        FloatingActionButton button = findViewById(R.id.backButtone);
        button.setOnClickListener(this);

        FloatingActionButton button2 = findViewById(R.id.deleteButton);
        button2.setOnClickListener(this);
    }

    private ImageView addButton(Bitmap content) {
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
