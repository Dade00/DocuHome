package com.example.gestorehome.ui.Login;

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

class SelectIDUtente extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String arg)
    {

    }

    @Override
    protected String doInBackground(String... arg) {
        String Nome = arg[0];
        String psw = arg[1];
        String result = null;
        String IDUtente = null;
        try {
            String urlPHP = DBmyDoc.srvStart + "/Get/SelectUtente.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            //SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String = URLEncoder.encode("nomeutente", "UTF-8") + "=" + URLEncoder.encode(Nome, "UTF-8") + "&" +
                    URLEncoder.encode("crpw", "UTF-8") + "=" + URLEncoder.encode(psw, "UTF-8");
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
                IDUtente = jsonObject.getString("ID");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return IDUtente;
    }
}

class UsrExist extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result)
    {
    }

    @Override
    protected String doInBackground(String... arg) {
        String Nome = arg[0];
        String result = null;
        try {
            String urlPHP = DBmyDoc.srvStart + "/Get/CtrlUsrExist.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            //SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String = URLEncoder.encode("idusr", "UTF-8") + "=" + URLEncoder.encode(Nome, "UTF-8");
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
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}

class InsUsr extends AsyncTask<String, String, String> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String result)
    {
    }

    @Override
    protected String doInBackground(String... arg) {
        String Nome = arg[0];
        String psw = arg[1];
        String pics = arg[2];
        String result = null;
        try {
            String urlPHP = DBmyDoc.srvStart + "/Insert/InsUser.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            //SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String = URLEncoder.encode("userName", "UTF-8") + "=" + URLEncoder.encode(Nome, "UTF-8")+ "&" +
                    URLEncoder.encode("passw", "UTF-8") + "=" + URLEncoder.encode(psw, "UTF-8")+ "&" +
                    URLEncoder.encode("img", "UTF-8") + "=" + URLEncoder.encode(pics, "UTF-8");
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
                return result;
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }
}


