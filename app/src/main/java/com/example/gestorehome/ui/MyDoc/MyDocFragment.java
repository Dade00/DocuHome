package com.example.gestorehome.ui.MyDoc;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import com.example.gestorehome.R;

public class MyDocFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //slideshowViewModel =
         //       ViewModelProviders.of(this).get(SlideshowViewModel.class);
        return inflater.inflate(R.layout.fragment_mydoc, container, false);
    }
}
