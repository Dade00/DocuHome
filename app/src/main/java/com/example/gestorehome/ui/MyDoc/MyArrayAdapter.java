package com.example.gestorehome.ui.MyDoc;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestorehome.R;

import java.util.ArrayList;

public class MyArrayAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final ArrayList<ArrayList<String>>  values;
    private ArrayList<Bitmap> bitmaps;

    public MyArrayAdapter(Context context, ArrayList<ArrayList<String>> values, ArrayList<Bitmap> bitmaps) {
        super(context, -1, values.get(0));
        this.context = context;
        this.values = values;
        this.bitmaps = bitmaps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowlayoute, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.docTypeID);
        TextView textView2 = (TextView) rowView.findViewById(R.id.expDatevar);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        Resources res = context.getResources();
        String[] tempVal = res.getStringArray(R.array.DocType);
        textView.setText(tempVal[Integer.parseInt(values.get(1).get(position))]);
        textView2.setText(values.get(2).get(position));
        imageView.setImageBitmap(bitmaps.get(position));
        return rowView;
    }
}
