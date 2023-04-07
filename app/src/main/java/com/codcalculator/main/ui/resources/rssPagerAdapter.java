package com.codcalculator.main.ui.resources;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.codcalculator.R;

public class rssPagerAdapter extends FragmentPagerAdapter {

    private int[] tabIcons = {
            R.drawable.gold_general,
            R.drawable.wood_general,
            R.drawable.stone_general,
            R.drawable.mana_general
    };

    private Context context;

    public rssPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    private String[] tabTitles = {
            "Gold",
            "Wood",
            "Stone",
            "Mana"
    };

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new GoldFragment();
            case 1:
                return new WoodFragment();
            case 2:
                return new StoneFragment();
            case 3:
                return new ManaFragment();
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
        icon.setBounds(0, 0, 50, 50);
        SpannableString spannableString = new SpannableString(" " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(icon, ImageSpan.ALIGN_BOTTOM);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}

