package com.codcalculator.main.ui.speedups;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.codcalculator.R;
import com.codcalculator.databinding.FragmentSpeedupsBinding;
import com.google.android.material.tabs.TabLayout;

public class GalleryFragment extends Fragment {

    private FragmentSpeedupsBinding binding;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        int[] tabIcons = {
                R.drawable.speedups_building,
                R.drawable.speedups_training,
                R.drawable.speedups_research,
                R.drawable.mana_general
        };
        String[] tabTitles = {
                getContext().getString(R.string.speedups_building),
                getContext().getString(R.string.speedups_training),
                getContext().getString(R.string.speedups_research),
                getContext().getString(R.string.speedups_universal)
        };

        // Inflar el layout del fragmento
        binding = FragmentSpeedupsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Configurar ViewPager y TabLayout
        viewPager = root.findViewById(R.id.speedups_view_pager);
        tabLayout = root.findViewById(R.id.speedups_tab_layout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                SpannableString spannableString = new SpannableString(tabTitles[position]);
                spannableString.setSpan(new RelativeSizeSpan(0.7f), 0, tabTitles[position].length(), 0);
                tabLayout.getTabAt(position).setText(spannableString);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Drawable icon = ContextCompat.getDrawable(getContext(), tabIcons[position]);
                icon.setBounds(0, 0, 75, 75);
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                spannableStringBuilder.append(" "); // Agregar un espacio para separar los iconos
                spannableStringBuilder.setSpan(new ImageSpan(icon, ImageSpan.ALIGN_CENTER), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                tabLayout.getTabAt(position).setText(spannableStringBuilder);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });
        viewPager.setAdapter(new speedsPagerAdapter(getContext(), getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}