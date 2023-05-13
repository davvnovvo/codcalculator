package com.codcalculator.main.ui.honor;

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

public class honorPagerAdapter extends FragmentPagerAdapter {

    private int[] tabIcons = {
            R.drawable.honor_inventory,
            R.drawable.honor_fromto
    };

    private Context context;

    public honorPagerAdapter(Context context, FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new InventoryFragment();
            case 1:
                return new FromToFragment();
            default:
                throw new IllegalArgumentException("Invalid position: " + position);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String[] tabTitles = {
                context.getString(R.string.honor_inventory),
                context.getString(R.string.honor_fromto),
        };
        Drawable icon = ContextCompat.getDrawable(context, tabIcons[position]);
        icon.setBounds(0, 0, 50, 50);
        SpannableString spannableString = new SpannableString(" " + tabTitles[position]);
        ImageSpan imageSpan = new ImageSpan(icon, ImageSpan.ALIGN_BOTTOM);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}

