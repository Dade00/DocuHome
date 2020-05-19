package com.example.gestorehome.ui.MyDoc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.gestorehome.R;
import com.example.gestorehome.dbcontroller.DBcontroller;
import com.example.gestorehome.detail;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MyDocFragment extends Fragment implements AdapterView.OnItemClickListener {
    private ArrayList<ArrayList<String>> Data;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.menu_mydoc);
        final View root = inflater.inflate(R.layout.fragment_mydoc, container, false);
        final ArrayList<Bitmap> dImage = new ArrayList<>();
        DBcontroller dBcontroller = new DBcontroller(getContext());
        dBcontroller.open();
        Data = dBcontroller.getDataContentList();
        try {
            for (int i = 0; i < Data.get(0).size(); i++) {
                dImage.add(dBcontroller.getFirstImageFromDocID(Integer.parseInt(Data.get(0).get(i).toString())));
            }
        } catch (Exception ex) {
        }

        ListView listView = (ListView) root.findViewById(R.id.listDoc);
        MyArrayAdapter adapter = new MyArrayAdapter(getContext(), Data, dImage);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((AdapterView.OnItemClickListener) this);
        dBcontroller.close();
        root.setOnFocusChangeListener(new View.OnFocusChangeListener(){


            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                DBcontroller dBcontroller = new DBcontroller(getContext());
                dBcontroller.open();
                Data = dBcontroller.getDataContentList();
                try {
                    for (int i = 0; i < Data.get(0).size(); i++) {
                        dImage.add(dBcontroller.getFirstImageFromDocID(Integer.parseInt(Data.get(0).get(i).toString())));
                    }
                } catch (Exception ex) {
                }

                ListView listView = (ListView) root.findViewById(R.id.listDoc);
                MyArrayAdapter adapter = new MyArrayAdapter(getContext(), Data, dImage);
                listView.setAdapter(adapter);
                dBcontroller.close();
            }
        });
        return root;
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(), detail.class);
        String ID = Data.get(0).get(position);
        intent.putExtra("EXTRA_ID", ID);
        startActivityForResult(intent, 10001);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
        {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}
