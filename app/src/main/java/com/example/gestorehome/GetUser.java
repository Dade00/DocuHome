package com.example.gestorehome;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetUser {
    public String getUserID(Context context) throws IOException {
        String fileName = "/mID.txt";
        File file = new File(context.getApplicationContext().getFilesDir() + fileName);
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String receiveString = "";
        StringBuilder stringBuilder = new StringBuilder();
        while ( (receiveString = bufferedReader.readLine()) != null ) {
            stringBuilder.append(receiveString);
        }
        inputStream.close();
        return stringBuilder.toString();
    }
}
