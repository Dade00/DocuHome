package com.example.gestorehome.ui.Insert;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
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

import com.example.gestorehome.R;
import com.example.gestorehome.dbcontroller.DBcontroller;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutionException;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class InsertFragment extends Fragment implements View.OnClickListener {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private int imageButtonID = 0, buttonTag = 0;
    public boolean falgNew = false;
    private View root;
    private boolean CalOp = false;
    private LinearLayout viewCategoryNames;
    private ArrayList<Bitmap> myDocsImage = new ArrayList<>();
    private LinearLayout.LayoutParams layoutParamsPREVIEW = new LinearLayout.LayoutParams(768, 1024);

    //Funzione che crea un ImageButton
    private ImageButton addButton() {
        layoutParamsPREVIEW.setMargins(0, 10, 30, 10);
        final ImageButton btDoc = new ImageButton(getActivity());
        btDoc.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_add));
        btDoc.setOnClickListener(this);
        btDoc.setLayoutParams(layoutParamsPREVIEW);
        btDoc.setAdjustViewBounds(true);
        btDoc.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btDoc.setBackgroundResource(R.color.trans);
        btDoc.setTag(imageButtonID);
        imageButtonID++;
        return btDoc;
    }

    //Quando premo il bottone dal OnClickListener
    private void buttonController() {
        Context context = getContext();
        if (context.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_REQUEST);
        }
    }

    //Richiesta di utilizzo dei permessi
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    //Risposta da fotocamera, imposto il bitmap della preview
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle extras = data.getExtras();
        Bitmap a = (Bitmap) extras.get("data");
        try {
            myDocsImage.set(buttonTag, a);
        } catch (Exception ex) {
            myDocsImage.add(buttonTag, a);
        }
        ArrayList<ImageButton> imageButtons = new ArrayList<>();
        for (int i = 0; i < imageButtonID; i++) {
            imageButtons.add((ImageButton) root.findViewWithTag(i));
        }
        imageButtons.get(buttonTag).setImageBitmap(myDocsImage.get(buttonTag));
        if (falgNew)
            buildImageSection();
    }

    //Creazione del Fragment
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_insert, container, false);
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
        final EditText eText=(EditText) root.findViewById(R.id.expdate);
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
                if(!hasFocus)closeKeyboard(v);
            }
        });


        return root;
    }

    //Creo l'ImageButton iniziale
    private void buildImageSection() {
        viewCategoryNames.addView(addButton());
    }

    //OnClick del tasto
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.okButton) {
            try {
                okButtonActions();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            buttonTag = (int) v.getTag();
            if ((int) (v.getTag()) == imageButtonID - 1) //Nuovo
            {
                falgNew = true;
                buttonController();
            } else    //Modifica
            {
                falgNew = false;
                buttonController();
            }
        }
    }


    private void okButtonActions() throws ExecutionException, InterruptedException {
        //Updating actions
        //To DataBase

            DBcontroller dBcontroller = new DBcontroller(getContext());
            //dBcontroller.open();
            Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
            int index = spinner.getSelectedItemPosition();
            TextView textView = (TextView) root.findViewById(R.id.expdate);
            CheckBox checkBox = (CheckBox) root.findViewById(R.id.rememberyesno);
            TextView textView1 = (TextView) root.findViewById(R.id.titolaretext);
            if(textView1.getText().length()>0 && myDocsImage.size() > 0)
            {
                if(!dBcontroller.addDoc(index, textView.getText().toString(), checkBox.isChecked(), textView1.getText().toString())){
                    Toast.makeText(getContext(), "ERROR DOC", Toast.LENGTH_LONG).show();
                    return;
                }

                //Add picture
                int lastInsert = dBcontroller.getLastID();
                for (int i = 0; i < myDocsImage.size(); i++) {
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    myDocsImage.get(i).compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    final byte[] byteArray = stream.toByteArray();
                    if(!dBcontroller.addPic(byteArray, lastInsert)) {
                        Toast.makeText(getContext(), "Error IMAGE", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                //dBcontroller.close();
                clearFragment();
                Toast.makeText(getContext(), R.string.inserimentoaccepted, Toast.LENGTH_LONG).show();

            }
            else
                Toast.makeText(getContext(), R.string.inserimentonoaccepted, Toast.LENGTH_LONG).show();
    }

    public void clearFragment() {
        imageButtonID = 0;
        buttonTag = 0;
        falgNew = false;
        myDocsImage.clear();
        Context context;
        LinearLayout nnew = new LinearLayout(getContext());
        viewCategoryNames.removeAllViews();
        buildImageSection();
        CheckBox check =  root.findViewById(R.id.rememberyesno);
        check.setChecked(false);
        Switch s = (Switch) root.findViewById(R.id.expdateyesno);
        s.setChecked(true);
        TextView t =  root.findViewById(R.id.expdate);
        t.setText("");
        TextView te =  root.findViewById(R.id.titolaretext);
        te.setText("");
        Spinner spinner = (Spinner) root.findViewById(R.id.spinner);
        spinner.setSelection(0);
    }

    private void getCalendar(final EditText eText)
    {
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
                            CalOp=false;
                        }
                    }, year, month, day);
            picker.show();
    }

    private void closeKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
