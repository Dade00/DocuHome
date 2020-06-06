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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gestorehome.R;
import com.example.gestorehome.ui.Login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    private View root;
    private String IDut = null;
    private File file = null;

    class LoginTask extends AsyncTask<String, String, String> {
        JSONObject jsonObject = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                View view;
                TextView textView = root.findViewById(R.id.nameAccount);
                textView.setText(jsonObject.getString("userName"));
                NavigationView navigationView = (NavigationView) requireActivity().findViewById(R.id.nav_view);
                View hView = navigationView.getHeaderView(0);
                TextView nav_user = (TextView) hView.findViewById(R.id.UserNamegg);
                nav_user.setText(jsonObject.getString("userName"));
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
                NavigationView navigationView = (NavigationView) requireActivity().findViewById(R.id.nav_view);
                View hView = navigationView.getHeaderView(0);
                ImageView nav_user = (ImageView) hView.findViewById(R.id.accountUserNav);
                nav_user.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

            } catch (Exception e) {
                e.printStackTrace();
            }
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);

        try {
            String fileName = "/mID.txt";
            file = new File(requireContext().getApplicationContext().getFilesDir() + fileName);
            if (!file.exists()) {
                file.createNewFile();
                Intent intent = null;
                intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent, 100, savedInstanceState);
            } else {
                if (file != null) {
                    FileInputStream inputStream = new FileInputStream(file);
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String receiveString = "";
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(receiveString);
                    }
                    inputStream.close();
                    IDut = stringBuilder.toString();
                    LoginTask loginTask = new LoginTask();
                    loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    Intent intent = null;
                    intent = new Intent(getContext(), LoginActivity.class);
                    startActivityForResult(intent, 100, savedInstanceState);
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        final Bundle sss = savedInstanceState;
        Button button = root.findViewById(R.id.exitBtt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintWriter writer = null;
                try {
                    writer = new PrintWriter(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                assert writer != null;
                writer.print("");
                writer.close();

                Intent intent = null;
                intent = new Intent(getContext(), LoginActivity.class);
                startActivityForResult(intent, 100, sss);
            }
        });


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            assert data != null;
            IDut = data.getStringExtra("result");
            try {
                String fileName = "/mID.txt";
                file = new File(requireContext().getApplicationContext().getFilesDir() + fileName);
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(IDut.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoginTask loginTask = new LoginTask();
            loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }


}
