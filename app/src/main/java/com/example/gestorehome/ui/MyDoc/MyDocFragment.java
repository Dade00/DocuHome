package com.example.gestorehome.ui.MyDoc;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gestorehome.GetUser;
import com.example.gestorehome.R;
import com.example.gestorehome.dbcontroller.DBcontroller;
import com.example.gestorehome.detail;
import com.example.gestorehome.ui.LoadingDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MyDocFragment extends Fragment {
    private ArrayList<ArrayList<String>> Data;
    private View root;
    final ArrayList<Bitmap> dImage = new ArrayList<>();



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        getActivity().setTitle(R.string.menu_mydoc);
        root = inflater.inflate(R.layout.fragment_mydoc, container, false);
        class StartAct extends AsyncTask<String, String, Void> {
            private LoadingDialog loadingDialog;
            @Override
            protected void onPreExecute() {
                loadingDialog = new LoadingDialog(getActivity());
                loadingDialog.startLoadingDialog();
            }

            @Override
            protected void onPostExecute(Void a)
            {
                ListView listView = (ListView) root.findViewById(R.id.listDoc);
                try {
                    MyArrayAdapter adapter = new MyArrayAdapter(root.getContext(), Data, dImage);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(getContext(), detail.class);
                                String ID = Data.get(0).get(position);
                                intent.putExtra("EXTRA_ID", ID);
                                startActivityForResult(intent, 10001);
                        }
                    });
                }catch (Exception e){
                    TextView textNoDoc = root.findViewById(R.id.noDoc);
                    textNoDoc.setVisibility(View.VISIBLE);
                }
                loadingDialog.dismissDialog();
            }

            @Override
            protected Void doInBackground(String... arg) {
                DBcontroller dBcontroller = new DBcontroller(getContext());
                GetUser getUser = new GetUser();
                try {
                    String IDut = getUser.getUserID(requireContext());
                    try {
                        Data = dBcontroller.getDataContentList(IDut);
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {
                        for (int i = 0; i < Data.get(0).size(); i++) {
                            dImage.add(dBcontroller.getFirstImageFromDocID(Integer.parseInt(Data.get(0).get(i).toString()), IDut));
                        }
                    } catch (Exception ignored) {
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        StartAct startAct = new StartAct();
        startAct.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return root;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == 10001) && (resultCode == Activity.RESULT_OK))
        {
            Data.clear();
            dImage.clear();
            assert getFragmentManager() != null;
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(this).attach(this).commit();
        }
    }
}
