package com.codcalculator.main.ui.tokens;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.codcalculator.databinding.FragmentTokensBinding;

public class TokensFragment extends Fragment {

    private FragmentTokensBinding binding;
    private ViewPager viewPager;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragmento
        binding = FragmentTokensBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}