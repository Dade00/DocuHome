package com.example.gestorehome.ui.home;

import android.os.AsyncTask;

import com.example.gestorehome.dbcontroller.DBmyDoc;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class SelectOneUt extends AsyncTask<String, String, JSONObject> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(JSONObject arg)
    {

    }

    @Override
    protected JSONObject doInBackground(String... arg) {
        String ID = arg[0];
        String result = null;
        String IDUtente = null;
        try {
            String urlPHP = DBmyDoc.srvStart + "/Get/SelectmAccount.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            //SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String = URLEncoder.encode("idusr", "UTF-8") + "=" + URLEncoder.encode(ID, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                result = stringBuilder.toString();
                httpURLConnection.disconnect();
                inputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Parse json data into arrayString
            try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(0);
                return jsonObject;
            }
            catch (Exception e){
                e.printStackTrace();
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
