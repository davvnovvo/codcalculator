package com.codcalculator.main.ui.speedups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.codcalculator.R;

public class TrainingFragment extends Fragment {

    public TrainingFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.speedups_training_fragment, container, false);

        return rootView;
    }

}

