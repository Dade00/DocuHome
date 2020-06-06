package com.example.gestorehome.ui.Insert;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gestorehome.GetUser;
import com.example.gestorehome.R;
import com.example.gestorehome.dbcontroller.DBcontroller;
import com.example.gestorehome.ui.LoadingDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import static android.app.Activity.RESULT_OK;


public class InsertFragment extends Fragment implements View.OnClickListener {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100, MY_WRITE_CODE = 101, MY_READ_CODE = 102;
    private int imageButtonID = 0, buttonTag = 0;
    public boolean fagNew = false;
    private View root;
    private boolean CalOp = false;
    private LinearLayout viewCategoryNames;
    private Uri mImageUri;
    private File photo;
    private  ArrayList<Uri> mUriImm = new ArrayList<>();
    private ArrayList<Bitmap> myDocsImage = new ArrayList<>();
    private Bundle bundle;


    /*@Override
    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < mUriImm.size(); i++) {
            File file = new File(mUriImm.get(i));
        }
    }*/

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_insert, container, false);
        bundle = savedInstanceState;
        getActivity().setTitle(R.string.Inserisci);
        //Font
        ArrayAdapter adapter = ArrayAdapter.createFromResource(requireContext(), R.array.DocType, R.layout.myspinnertext);// where array_name consists of the items to show in Spinner
        adapter.setDropDownViewResource(R.layout.myspinnertext); // where custom-spinner is mycustom xml file.
        Spinner mySpinner = root.findViewById(R.id.spinner);
        mySpinner.setAdapter(adapter);

        final Switch mySwitch = root.findViewById(R.id.expdateyesno);
        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    root.findViewById(R.id.expdate).setEnabled(false);
                    root.findViewById(R.id.rememberyesno).setEnabled(false);
                } else {
                    root.findViewById(R.id.expdate).setEnabled(true);
                    root.findViewById(R.id.rememberyesno).setEnabled(true);
                }
            }
        });

        viewCategoryNames = root.findViewById(R.id.viewImageButton);
        buildImageSection();
        FloatingActionButton ok = root.findViewById(R.id.okButton);
        ok.setOnClickListener(this);
        //OpenDatePicker
        final EditText eText = (EditText) root.findViewById(R.id.expdate);
        eText.setInputType(InputType.TYPE_NULL);
        eText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    if (!CalOp)
                        getCalendar(eText);
            }
        });
        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!CalOp)
                    getCalendar(eText);
            }
        });
        //Chiusura tastiere
        EditText editText = root.findViewById(R.id.titolaretext);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) closeKeyboard(v);
            }
        });

        //Chiedo permessi di Foto, Write and Read Storage
        if (requireContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else if (requireContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_CODE);
        } else if (requireContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_CODE);
        }

        return root;
    }

    //Richiesta di utilizzo dei permessi fotografica
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_CAMERA_PERMISSION_CODE && resultCode == RESULT_OK) {
            Uri mUri = grabImage();
            ContentResolver cr = requireContext().getContentResolver();
            Bitmap a = null;
            try {
                a = ThumbnailUtils.extractThumbnail((MediaStore.Images.Media.getBitmap(cr, mImageUri)),
                        384, 512 );
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                myDocsImage.set(buttonTag, a);
                mUriImm.set(buttonTag, mUri);
            } catch (Exception ex) {
                myDocsImage.add(buttonTag, a);
                mUriImm.add(mUri);

                ArrayList<ImageButton> imageButtons = new ArrayList<>();
                for (int i = 0; i < imageButtonID; i++) {
                    imageButtons.add((ImageButton) root.findViewWithTag(i));
                }
                imageButtons.get(buttonTag).setImageBitmap(myDocsImage.get(buttonTag));
                if (fagNew)
                    buildImageSection();
            }
        }
    }

    @Override
    //OnClick dei vari button
    public void onClick(View v) {
        if (v.getId() == R.id.okButton) {
            try {
                okButtonActions();
            } catch (ExecutionException | InterruptedException | IOException e) {
                e.printStackTrace();
            }
        } else {
            buttonTag = (int) v.getTag();
            //Nuovo
            //Modifica
            fagNew = (int) (v.getTag()) == imageButtonID - 1;
            buttonController();
        }
    }

    //Creo l'ImageButton iniziale
    private void buildImageSection() {
        viewCategoryNames.addView(addButton());
    }

    //Inserimento nel DB dei dati con il bottone floating
    private void okButtonActions() throws ExecutionException, InterruptedException, IOException {
        //Updating actions
        //To DataBase

        DBcontroller dBcontroller = new DBcontroller(getContext());
        //dBcontroller.open();
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        int index = spinner.getSelectedItemPosition();
        TextView textView = (TextView) root.findViewById(R.id.expdate);
        CheckBox checkBox = (CheckBox) root.findViewById(R.id.rememberyesno);
        TextView textView1 = (TextView) root.findViewById(R.id.titolaretext);
        if (textView1.getText().length() > 0 && myDocsImage.size() > 0) {
            LoadingDialog loadingDialog = new LoadingDialog(getActivity());
            loadingDialog.startLoadingDialog();
            GetUser getUser =new GetUser();
            String IDut =  getUser.getUserID(requireContext());
            Switch s = (Switch) root.findViewById(R.id.expdateyesno);

            if (s.isChecked()) {
                if (textView.getText().length() > 0) {
                    if (!dBcontroller.addDoc(index, textView.getText().toString(), checkBox.isChecked(), textView1.getText().toString(), IDut)) {
                        Toast.makeText(getContext(), "ERROR DOC", Toast.LENGTH_LONG).show();
                        loadingDialog.dismissDialog();
                        return;
                    }
                } else {
                    Toast.makeText(getContext(), R.string.Nodate, Toast.LENGTH_LONG).show();
                    loadingDialog.dismissDialog();
                    return;
                }
            } else {
                if (!dBcontroller.addDoc(index, "NOEXP", false, textView1.getText().toString(), IDut)) {
                    Toast.makeText(getContext(), "ERROR DOC", Toast.LENGTH_LONG).show();
                    loadingDialog.dismissDialog();
                    return;
                }
            }

            //Add picture
            int lastInsert = dBcontroller.getLastID(IDut);
            for (int i = 0; i < mUriImm.size(); i++) {
                ContentResolver cr = requireContext().getContentResolver();
                Bitmap tr = android.provider.MediaStore.Images.Media.getBitmap(cr, mUriImm.get(i));
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                tr.compress(Bitmap.CompressFormat.JPEG, 85, stream);
                final byte[] byteArray = stream.toByteArray();
                if (!dBcontroller.addPic(byteArray, lastInsert, getContext(), IDut)) {
                    loadingDialog.dismissDialog();
                    Toast.makeText(getContext(), "Error IMAGE", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            //dBcontroller.close();
            clearFragment();
            loadingDialog.dismissDialog();
            Toast.makeText(getContext(), R.string.inserimentoaccepted, Toast.LENGTH_LONG).show();

        } else
            Toast.makeText(getContext(), R.string.inserimentonoaccepted, Toast.LENGTH_LONG).show();
    }

    //Reset il fragment
    public void clearFragment() {
        imageButtonID = 0;
        CalOp = false;
        buttonTag = 0;
        fagNew = false;
        myDocsImage.clear();
        Context context;
        LinearLayout nnew = new LinearLayout(getContext());
        viewCategoryNames.removeAllViews();
        buildImageSection();
        CheckBox check = root.findViewById(R.id.rememberyesno);
        check.setChecked(false);
        Switch s = (Switch) root.findViewById(R.id.expdateyesno);
        s.setChecked(true);
        TextView t = root.findViewById(R.id.expdate);
        t.setText("");
        TextView te = root.findViewById(R.id.titolaretext);
        te.setText("");
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        spinner.setSelection(0);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    //Apre l'activity calendario
    private void getCalendar(final EditText eText) {
        CalOp = true;
        final Calendar cldr = Calendar.getInstance();
        final int day = cldr.get(Calendar.DAY_OF_MONTH);
        int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        // date picker dialog
        DatePickerDialog picker = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Date data = new GregorianCalendar(year, monthOfYear, dayOfMonth).getTime();
                        Date now = new Date();
                        now.getTime();
                        if (data.after(now))
                            eText.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                        else {
                            Toast.makeText(getContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
                        }
                        CalOp = false;
                    }
                }, year, month, day);
        picker.show();
    }

    //Quando premo il bottone dal OnClickListener
    private void buttonController() {
        Context context = getContext();
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else if (context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_READ_CODE);
        } else if (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_WRITE_CODE);
        } else {
            IntentCamera();
        }
    }

    //Chiusura soft key
    private void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    //Funzione che crea un ImageButton
    private ImageButton addButton() {
        DisplayMetrics metrics = requireContext().getResources().getDisplayMetrics();
        final LinearLayout.LayoutParams layoutParamsPREVIEW = new LinearLayout.LayoutParams((int) (metrics.widthPixels * 0.60), (int) (metrics.heightPixels * 0.60));
        layoutParamsPREVIEW.setMargins(0, 10, 30, 10);
        final ImageButton btDoc = new ImageButton(getActivity());
        btDoc.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_add));
        btDoc.setOnClickListener(this);
        btDoc.setLayoutParams(layoutParamsPREVIEW);
        btDoc.setAdjustViewBounds(true);
        btDoc.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btDoc.setBackgroundResource(R.color.trans);
        btDoc.setTag(imageButtonID);
        imageButtonID++;
        return btDoc;
    }

    private void IntentCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            // place where to store camera taken picture
            photo = this.createTemporaryFile("picture", ".jpg");
            //photo.delete();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Please check SD card! Image shot is impossible!", Toast.LENGTH_SHORT);
        }
        mImageUri = FileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
        //start camera intent
        startActivityForResult(intent, MY_CAMERA_PERMISSION_CODE, bundle);
    }

    private File createTemporaryFile(String picture, String s) throws Exception {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return File.createTempFile(picture, s, tempDir);
    }

    public Uri grabImage() {
        requireContext().getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = requireContext().getContentResolver();
        try {
            return mImageUri;
        } catch (Exception e) {
            Toast.makeText(requireContext(), "Failed to load", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
