package com.codcalculator.main.ui.resources;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.codcalculator.R;
import com.codcalculator.databinding.FragmentResourcesBinding;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private FragmentResourcesBinding binding;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragmento
        binding = FragmentResourcesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar ViewPager y TabLayout
        viewPager = root.findViewById(R.id.view_pager);
        tabLayout = root.findViewById(R.id.tab_layout);
        viewPager.setAdapter(new rssPagerAdapter(getContext(), getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        // Configurar el texto en TextView
        //final TextView textView = binding.textHome;
        //HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}