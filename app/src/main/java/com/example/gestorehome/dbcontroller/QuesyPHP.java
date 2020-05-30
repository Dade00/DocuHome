package com.example.gestorehome.dbcontroller;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

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
import java.util.ArrayList;
class InsertDoc extends AsyncTask<String, String, Boolean> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Boolean result) {


    }

    @Override
    protected Boolean doInBackground(String... arg) {
        String docType = arg[0];
        String expDate = arg[1];
        String titolare = arg[2];
        String reme = arg[3];
        try {
            String urlPHP = DBmyDoc.srvStart + "/Insert/InsDoc.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String = URLEncoder.encode("doctype", "UTF-8") + "=" + URLEncoder.encode(docType, "UTF-8") + "&" +
                    URLEncoder.encode("expdate", "UTF-8") + "=" + URLEncoder.encode(expDate, "UTF-8") + "&" +
                    URLEncoder.encode("titolare", "UTF-8") + "=" + URLEncoder.encode(titolare, "UTF-8") + "&" +
                    URLEncoder.encode("remember", "UTF-8") + "=" + URLEncoder.encode(reme, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

class InsertPic extends AsyncTask<String, String, Boolean> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Boolean arg)
    {

    }

    @Override
    protected Boolean doInBackground(String... arg) {
        String pic = arg[0];
        String doc = arg[1];
        try {
            String urlPHP = DBmyDoc.srvStart + "/Insert/InsPic.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("docid", "UTF-8") + "=" + URLEncoder.encode(doc, "UTF-8") + "&" +
                    URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(pic, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            inputStream.close();
            httpURLConnection.disconnect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

class SelectLID extends AsyncTask<String, String, Integer> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Integer arg)
    {

    }

    @Override
    protected Integer doInBackground(String... arg) {
        String result = null;
        String res = null;
        try {
            String urlPHP = "http://raspberrypi:8080/dbQuery/Get/SelLastID.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            /*
            SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("docid", "UTF-8") + "=" + URLEncoder.encode(doc, "UTF-8") + "&" +
                    URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(pic, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();*/
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            httpURLConnection.disconnect();

            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }

                inputStream.close();
                result = stringBuilder.toString();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Parse json data
            try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;


                    jsonObject = jsonArray.getJSONObject(0);
                    res = jsonObject.getString("ID");
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            return -1;
        }
        assert res != null;
        return Integer.parseInt(res);
    }
}

class SelectAllDoc extends AsyncTask<String, String, ArrayList<ArrayList<String>>> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> arg)
    {

    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... arg) {
        String result = null;
        ArrayList<ArrayList<String>> ris = new ArrayList<>();
        try {
            String urlPHP = DBmyDoc.srvStart + "/Get/SelAllDoc.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            //httpURLConnection.setRequestMethod("POST");
            //httpURLConnection.setDoOutput(true);
            //OutputStream outputStream = httpURLConnection.getOutputStream();
            /*
            SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("docid", "UTF-8") + "=" + URLEncoder.encode(doc, "UTF-8") + "&" +
                    URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(pic, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();*/
            //outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            httpURLConnection.disconnect();
            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                result = stringBuilder.toString();
                inputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Parse json data into arrayString
            try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                ArrayList<String> ID = new ArrayList<>();
                ArrayList<String> docType = new ArrayList<>();
                ArrayList<String> expDate = new ArrayList<>();
                ArrayList<String> titolare = new ArrayList<>();
                ArrayList<String> rem = new ArrayList<>();
                for (int i = 0; i <  jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    ID.add(jsonObject.getString("ID"));
                    docType.add(jsonObject.getString("docType"));
                    expDate.add(jsonObject.getString("expDate"));
                    titolare.add(jsonObject.getString("titolare"));
                }
                ris.add(ID);
                ris.add(docType);
                ris.add(expDate);
                ris.add(titolare);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            return null;
        }
        return ris;
    }
}

class SelectOneDoc extends AsyncTask<String, String, ArrayList<ArrayList<String>>> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(ArrayList<ArrayList<String>> arg)
    {

    }

    @Override
    protected ArrayList<ArrayList<String>> doInBackground(String... arg) {
        String Id = arg[0];
        String result = null;
        ArrayList<ArrayList<String>> ris = new ArrayList<>();
        try {
            String urlPHP = DBmyDoc.srvStart + "/Get/SelectOneDoc.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            //SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("iddoc", "UTF-8") + "=" + URLEncoder.encode(Id, "UTF-8");
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
                ArrayList<String> ID = new ArrayList<>();
                ArrayList<String> docType = new ArrayList<>();
                ArrayList<String> expDate = new ArrayList<>();
                ArrayList<String> titolare = new ArrayList<>();
                ArrayList<String> rem = new ArrayList<>();
                for (int i = 0; i <  jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    ID.add(jsonObject.getString("ID"));
                    docType.add(jsonObject.getString("docType"));
                    expDate.add(jsonObject.getString("expDate"));
                    titolare.add(jsonObject.getString("titolare"));
                    rem.add(jsonObject.getString("remember"));
                }
                ris.add(ID);
                ris.add(docType);
                ris.add(expDate);
                ris.add(titolare);
                ris.add(rem);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return ris;
    }
}

class SelectBitmapDocID extends AsyncTask<String, String, Bitmap> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(Bitmap arg)
    {

    }

    @Override
    protected Bitmap doInBackground(String... arg) {
        String docID = arg[0];
        String result = null;
        String res = null;
        Bitmap bm = null;
        try {
            String urlPHP = DBmyDoc.srvStart + "/Get/SelBitmapFromDocID.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            //SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("docid", "UTF-8") + "=" + URLEncoder.encode(docID, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            //bm = BitmapFactory.decodeStream(inputStream);


            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                result = stringBuilder.toString();
                inputStream.close();
                httpURLConnection.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Parse json data
            try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                jsonObject = jsonArray.getJSONObject(0);
                res = jsonObject.getString("pic");
                byte[] bytes = Base64.decode(res, Base64.DEFAULT);
                bm = BitmapFactory.decodeByteArray(bytes, 0 ,bytes.length);

            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            return bm;
        }
        assert bm != null;
        return bm;
    }
}

class SelectAllBitmapDocID extends AsyncTask<String, String, ArrayList<Bitmap>> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute( ArrayList<Bitmap> arg)
    {

    }

    @Override
    protected ArrayList<Bitmap> doInBackground(String... arg) {
        String docID = arg[0];
        String result = null;
        String res = null;
        ArrayList<Bitmap> bm = new ArrayList<>();
        try {
            String urlPHP = DBmyDoc.srvStart + "/Get/SelBitmapFromDocID.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            //SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("docid", "UTF-8") + "=" + URLEncoder.encode(docID, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            //bm = BitmapFactory.decodeStream(inputStream);


            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                result = stringBuilder.toString();
                inputStream.close();
                httpURLConnection.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Parse json data
            try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                for (int i=0; i<jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    res = jsonObject.getString("pic");
                    byte[] bytes = Base64.decode(res, Base64.DEFAULT);
                    bm.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }
        } catch (IOException e) {
            return bm;
        }
        assert bm != null;
        return bm;
    }
}

class RemoveOneDoc extends AsyncTask<String, String, Boolean> {
    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute( Boolean arg)
    {

    }

    @Override
    protected Boolean doInBackground(String... arg) {
        String docID = arg[0];
        String result = null;
        String res = null;
        Boolean flag = null;
        try {
            String urlPHP = DBmyDoc.srvStart + "/Del/RemoveOneDoc.php";
            URL url = new URL(urlPHP);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            //SEND DATA WITH POST METHOD
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            String data_String =URLEncoder.encode("docid", "UTF-8") + "=" + URLEncoder.encode(docID, "UTF-8");
            bufferedWriter.write(data_String);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            //bm = BitmapFactory.decodeStream(inputStream);


            try{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line + "\n");
                }
                result = stringBuilder.toString();
                inputStream.close();
                httpURLConnection.disconnect();
                flag= result.equals("CMPL\n");
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            // Parse json data
           /* try{
                JSONArray jsonArray = new JSONArray(result);
                JSONObject jsonObject = null;
                for (int i=0; i<jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);
                    res = jsonObject.getString("pic");
                    byte[] bytes = Base64.decode(res, Base64.DEFAULT);
                    bm.add(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                }

            }
            catch (Exception e){
                e.printStackTrace();
            }*/
        } catch (IOException e) {
            return false;
        }

        assert false;
        return flag;
    }
}
