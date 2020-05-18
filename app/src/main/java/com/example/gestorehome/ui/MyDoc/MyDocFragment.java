package com.example.gestorehome.ui.MyDoc;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.gestorehome.R;
import com.example.gestorehome.dbcontroller.DBcontroller;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class MyDocFragment extends Fragment {
    private View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_mydoc, container, false);
        ArrayList<Bitmap> dImage = new ArrayList<>();
        DBcontroller dBcontroller =new DBcontroller(getContext());
        dBcontroller.open();
        ArrayList<ArrayList<String>> Data = dBcontroller.getDataContentList();
        try {
            for (int i = 0; i < Data.get(0).size(); i++) {
                dImage.add(dBcontroller.getFirstImageFromDocID(Integer.parseInt(Data.get(0).get(i).toString())));
            }
        } catch (Exception ex){}

        ListView listView = (ListView) root.findViewById(R.id.listDoc);
        MyArrayAdapter adapter = new MyArrayAdapter(getContext(), Data, dImage);
        listView.setAdapter(adapter);
        dBcontroller.close();
        return root;
    }
}
