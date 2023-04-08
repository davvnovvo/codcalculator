package com.codcalculator.main.ui.speedups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.codcalculator.R;

public class BuildingFragment extends Fragment {

    public BuildingFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.speedups_building_fragment, container, false);

        return rootView;
    }

}
