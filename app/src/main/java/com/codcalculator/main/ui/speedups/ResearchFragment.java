package com.codcalculator.main.ui.speedups;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.codcalculator.R;

public class ResearchFragment extends Fragment {

    public ResearchFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.speedups_research_fragment, container, false);

        return rootView;
    }

}

