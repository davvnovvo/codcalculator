package com.codcalculator.main.ui.tokens;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.codcalculator.R;
import com.codcalculator.databinding.FragmentTokensBinding;
import com.codcalculator.utilities.CustomSpinnerAdapter;

import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.SAXParser;

public class TokensFragment extends Fragment {

    private FragmentTokensBinding binding;
    private ViewPager viewPager;
    private Spinner token_type_spinner;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragmento
        binding = FragmentTokensBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        token_type_spinner = root.findViewById(R.id.token_type_spinner);

        List<String> honorLevels = Arrays.asList(getResources().getStringArray(R.array.token_type));
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, honorLevels, Color.WHITE);
        token_type_spinner.setAdapter(adapter);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}