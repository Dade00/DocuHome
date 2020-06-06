package com.example.gestorehome.ui.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gestorehome.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;
import java.util.concurrent.ExecutionException;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText NAME;
    private TextInputEditText PSW;
    private Context context;
    private Bundle bnd;

    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(),R.string.login_before, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = this.getApplicationContext();
        bnd = savedInstanceState;
        NAME = findViewById(R.id.usernameT);
        PSW = findViewById(R.id.passwordT);
        Button button = findViewById(R.id.login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                class LoginTask extends AsyncTask<String, String, String> {
                    String IDut = null;
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (IDut == null) {
                            Toast.makeText(context, "Utente non trovato, controlla i paramentri o creane uno nuovo", Toast.LENGTH_SHORT).show();
                        } else {
                            Intent returnIntent = new Intent();
                            returnIntent.putExtra("result", IDut);
                            setResult(Activity.RESULT_OK, returnIntent);
                            finish();
                        }
                    }

                    @Override
                    protected String doInBackground(String... strings) {
                        SelectIDUtente selectIDUtente = new SelectIDUtente();
                        try {
                            IDut = selectIDUtente.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Objects.requireNonNull(NAME.getText()).toString(), Objects.requireNonNull(PSW.getText()).toString()).get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }
                LoginTask loginTask = new LoginTask();
                loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
        Button button2 = findViewById(R.id.registerR);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivityForResult(intent, 120, bnd);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 120 && resultCode == Activity.RESULT_OK) {
            assert data != null;
            NAME.setText(data.getStringExtra("USR"));
        }
    }
}
