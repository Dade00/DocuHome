package com.example.gestorehome.ui.Login;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.gestorehome.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import rx.functions.Action1;

public class RegisterActivity extends AppCompatActivity {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private TextInputEditText NAME = null;
    private ImageButton imageButton = null;
    private Context context;
    private boolean USR_OK = false, PSW_OK = false;
    InputStream is;
    BitmapFactory.Options opts;
    private Bitmap imaFinal = null;
    private  boolean Update = false;
    private Bundle savedInstanceStateG = null;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        context = this.getApplicationContext();
        savedInstanceStateG = savedInstanceState;
        final TextInputEditText UserName = findViewById(R.id.userIns);
        final TextInputLayout layoutUser = findViewById(R.id.nomeLayout);
        final TextInputLayout layoutPSW = findViewById(R.id.layoutPSW);
        final TextInputEditText PswIns = findViewById(R.id.pswIns);
        final TextInputEditText PswConf = findViewById(R.id.pswConf);

        class LoginTask extends AsyncTask<String, String, String> {
            String control = null;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (control.contains("Not")) {
                    layoutUser.setBoxStrokeColor(Color.GREEN);
                    USR_OK=true;

                } else {
                    layoutUser.setBoxStrokeColor(Color.RED);
                    USR_OK=false;
                }
            }

            @Override
            protected String doInBackground(String... strings) {
                UsrExist usrExist = new UsrExist();
                try {
                    control = usrExist.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Objects.requireNonNull(UserName.getText()).toString()).get();
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }

        Button regi = findViewById(R.id.registerConfirm);
        regi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginTask loginTask = new LoginTask();
                loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                pswControl(PswConf, PswIns, layoutPSW );
                if(Objects.requireNonNull(PswIns.getText()).length()>0) {
                    if (USR_OK && PSW_OK && imaFinal != null) {
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        imaFinal.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        final byte[] byteArray = stream.toByteArray();
                        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                        InsUsr insUsr = new InsUsr();
                        String exec = null;
                        try {
                            exec = insUsr.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, Objects.requireNonNull(UserName.getText()).toString(), PswIns.getText().toString(), encodedImage).get();
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                        assert exec != null;
                        if (exec.contains("EXEC")){
                            Toast.makeText(getApplicationContext(), R.string.InsertCmpl, Toast.LENGTH_LONG).show();
                            Intent da = new Intent();
                            da.putExtra("USR", UserName.getText().toString());
                            setResult(RESULT_OK, da);
                            finish();
                        }
                    }
                }
            }
        });


        imageButton = (ImageButton) findViewById(R.id.imageUsr);
        /*Bitmap bm = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_baseline_photo_camera_24);
        Bitmap resized = Bitmap.createScaledBitmap(bm, 200,200 , true);
        Bitmap conv_bm = getRoundedRectBitmap(resized, 200);
        imageButton.setImageBitmap(conv_bm);
        resized.recycle();
        bm.recycle();*/
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                }
                else{
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, MY_CAMERA_PERMISSION_CODE, savedInstanceState);
                }
            }
        });


        RxTextView.textChanges(UserName).debounce(1500, TimeUnit.MILLISECONDS).subscribe((new Action1<CharSequence>(){
            @Override
            public void call(CharSequence charSequence) {
                if (Objects.requireNonNull(UserName.getText()).length() > 0) {
                    LoginTask loginTask = new LoginTask();
                    loginTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                } else {
                    layoutUser.setBoxStrokeColor(Color.RED);
                }
            }
        }));

        RxTextView.textChanges(PswConf).debounce(1500, TimeUnit.MILLISECONDS).subscribe((new Action1<CharSequence>() {
            @Override
            public void call(CharSequence charSequence) {
               pswControl(PswConf, PswIns, layoutPSW );
            }
        }));
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MY_CAMERA_PERMISSION_CODE && resultCode == Activity.RESULT_OK)
        {
            assert data != null;
            Bundle extras = data.getExtras();
            assert extras != null;
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            assert imageBitmap != null;
            //Bitmap resized = Bitmap.createScaledBitmap(imageBitmap, 200,200 , true);
            Bitmap conv_bm = getRoundedRectBitmap(imageBitmap, 150);
            imageButton.setImageBitmap(conv_bm);
            Bitmap.Config config;
            imaFinal = conv_bm;
            //resized.recycle();
        }
    }

    public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
        int widthLight = bitmap.getWidth();
        int heightLight = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Paint paintColor = new Paint();
        paintColor.setFlags(Paint.ANTI_ALIAS_FLAG);

        RectF rectF = new RectF(new Rect(0, 0, 150, 150));

        canvas.drawRoundRect(rectF, 75 ,75, paintColor);

        Paint paintImage = new Paint();
        paintImage.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        int sp = (heightLight-150)-(((heightLight-150))/2);
        canvas.drawBitmap(bitmap, 0, (sp*-1), paintImage);
        return output;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "permission granted", Toast.LENGTH_LONG).show();
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                startActivityForResult(intent, MY_CAMERA_PERMISSION_CODE, savedInstanceStateG);
            } else {
                Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void pswControl(TextInputEditText PswConf, TextInputEditText PswIns, TextInputLayout layoutPSW){
        if (Objects.requireNonNull(PswConf.getText()).length() > 0) {
            if (!Objects.requireNonNull(PswIns.getText()).toString().equals(Objects.requireNonNull(PswConf.getText()).toString())) {
                layoutPSW.setBoxStrokeColor(Color.RED);
                PSW_OK=false;
            }
            else
            {
                layoutPSW.setBoxStrokeColor(Color.GREEN);
                PSW_OK=true;
            }
        } else {
            layoutPSW.setBoxStrokeColor(Color.RED);
            PSW_OK=false;
        }
    }

}