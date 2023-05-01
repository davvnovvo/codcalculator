package com.codcalculator.main.ui.speedups;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codcalculator.R;
import com.codcalculator.main.ui.resources.GoldFragment;
import com.codcalculator.main.ui.resources.ManaFragment;
import com.codcalculator.main.ui.resources.StoneFragment;
import com.codcalculator.main.ui.resources.WoodFragment;

public class speedsPagerAdapter extends FragmentPagerAdapter {

    private int[] tabIcons = {
            R.drawable.speedups_building,
            R.drawable.speedups_training,
            R.drawable.speedups_research,
            R.drawable.speedups_universal
    };

    private Context context;

    public speedsPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new BuildingFragment();
            case 1:
                return new TrainingFragment();
            case 2:
                return new ResearchFragment();
            case 3:
                return new UniversalFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable icon = ContextCompat.getDrawable(context, tabIcons[position]);
        icon.setBounds(0, 0, 75, 75);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append(" "); // Agregar un espacio para separar los iconos
        spannableStringBuilder.setSpan(new ImageSpan(icon, ImageSpan.ALIGN_CENTER), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableStringBuilder;
    }
}
