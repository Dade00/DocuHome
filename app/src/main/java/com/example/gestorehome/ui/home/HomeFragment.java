package com.example.gestorehome.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestorehome.R;
import com.example.gestorehome.ui.Login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        Intent intent = new Intent(getContext(), LoginActivity.class);
        try {
            startActivityForResult(intent, 100, savedInstanceState);
        }catch (Exception e){
            e.printStackTrace();}


       return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            class LoginTask extends AsyncTask<String, String, String> {
                String IDut = null;
                JSONObject jsonObject = null;
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    assert data != null;
                    IDut = data.getStringExtra("result");
                }
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);

                    try {
                        View view;
                        TextView textView = root.findViewById(R.id.nameAccount);
                         textView.setText(jsonObject.getString("userName"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String res = null;
                    try {
                        res = jsonObject.getString("imgUt");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        byte[] bytes = Base64.decode(res, Base64.DEFAULT);
                        ImageView imageView = root.findViewById(R.id.accountImage);
                        imageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                    }catch(Exception e) { e.printStackTrace();}
                }
                @Override
                protected String doInBackground(String... strings) {
                    SelectOneUt selectIDUt = new SelectOneUt();
                    try {
                        jsonObject = selectIDUt.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, IDut).get();
                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }
            LoginTask loginTask = new LoginTask();
            loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }
}
