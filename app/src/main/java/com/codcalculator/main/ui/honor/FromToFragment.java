package com.codcalculator.main.ui.honor;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.codcalculator.R;
import com.codcalculator.utilities.CustomSpinnerAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FromToFragment extends Fragment {

    public FromToFragment() {
        // Constructor vacío requerido
    }

    int[] honorTable;

    TextView honor_total;
    TextInputEditText honor_points;
    Spinner honor_lvl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.honor_fromto_fragment, container, false);

        ImageButton reset = rootView.findViewById(R.id.honor_reset_fields);
        honor_total = rootView.findViewById(R.id.honor_result_text);
        ImageButton calculate = rootView.findViewById(R.id.honor_calculate);
        honor_points = rootView.findViewById(R.id.honor_points_input);
        honor_lvl = rootView.findViewById(R.id.honor_level_spinner);

        String[] honorLevels = getResources().getStringArray(R.array.honor_levels);
        String[] honorLevelsWithPrompt = new String[honorLevels.length + 1];
        honorLevelsWithPrompt[0] = getString(R.string.honor_level_prompt);
        System.arraycopy(honorLevels, 0, honorLevelsWithPrompt, 1, honorLevels.length);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, honorLevelsWithPrompt);
        honor_lvl.setAdapter(adapter);

        honorTable = new int[]{200, 400, 1200, 3500, 6000, 11500, 17500, 35000, 75000, 150000, 250000, 350000, 500000, 750000, 1000000};

        List<String> honorLevels2 = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.honor_levels)));
        honorLevels2.add(0, getString(R.string.honor_level_prompt));
        CustomSpinnerAdapter adapter2 = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, honorLevels2, Color.WHITE);
        honor_lvl.setAdapter(adapter2);

        resetFields();
        reset.setOnClickListener(v -> resetFields());

        calculate.setOnClickListener(v -> calculateRemainingPoints());

        return rootView;
    }

    private void resetFields() {
        honor_points.setText("");
        honor_lvl.setSelection(0);
    }

    private void calculateRemainingPoints() {
        String honorPointsText = honor_points.getText().toString();
        if (honorPointsText.isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.honor_points_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        int honorPoints = Integer.parseInt(honorPointsText);

        int selectedPosition = honor_lvl.getSelectedItemPosition();
        if (selectedPosition == 0) {
            Toast.makeText(getContext(), getString(R.string.honor_level_prompt_error), Toast.LENGTH_SHORT).show();
            return;
        }

        int targetLevel = selectedPosition;
        int currentLevel = calculateCurrentLevel(honorPoints);
        if (targetLevel <= currentLevel) {
            Toast.makeText(getContext(), getString(R.string.honor_points_wrong_level), Toast.LENGTH_SHORT).show();
            return;
        }

        int remainingPoints = honorTable[targetLevel - 1] - honorPoints;
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        String formattedTotal = numberFormat.format(remainingPoints);
        honor_total.setText(formattedTotal);

    }

    private int calculateCurrentLevel(int honorPoints) {
        for (int level = 0; level < honorTable.length; level++) {
            if (honorPoints < honorTable[level]) {
                return level;
            }
        }
        return honorTable.length;
    }

}
