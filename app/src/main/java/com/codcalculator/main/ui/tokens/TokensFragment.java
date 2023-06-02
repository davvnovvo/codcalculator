package com.codcalculator.main.ui.tokens;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.codcalculator.R;
import com.codcalculator.databinding.FragmentTokensBinding;
import com.codcalculator.utilities.CustomSpinnerAdapter;

import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.SAXParser;

public class TokensFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentTokensBinding binding;
    private ViewPager viewPager;
    private Spinner token_type_spinner;
    private Spinner token_from_spinner1;
    private Spinner token_from_spinner2;
    private Spinner token_from_spinner3;
    private Spinner token_from_spinner4;
    private Spinner token_to_spinner1;
    private Spinner token_to_spinner2;
    private Spinner token_to_spinner3;
    private Spinner token_to_spinner4;
    private TextView token_total;
    private ImageView tokens_icon;
    private int tokenType = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflar el layout del fragmento
        binding = FragmentTokensBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        token_type_spinner = root.findViewById(R.id.token_type_spinner);
        token_from_spinner1 = root.findViewById(R.id.token_from_spinner1);
        token_from_spinner2 = root.findViewById(R.id.token_from_spinner2);
        token_from_spinner3 = root.findViewById(R.id.token_from_spinner3);
        token_from_spinner4 = root.findViewById(R.id.token_from_spinner4);
        token_to_spinner1 = root.findViewById(R.id.token_to_spinner1);
        token_to_spinner1.setSelection(4);
        token_to_spinner2 = root.findViewById(R.id.token_to_spinner2);
        token_to_spinner2.setSelection(4);
        token_to_spinner3 = root.findViewById(R.id.token_to_spinner3);
        token_to_spinner3.setSelection(4);
        token_to_spinner4 = root.findViewById(R.id.token_to_spinner4);
        token_to_spinner4.setSelection(4);
        token_total = root.findViewById(R.id.token_total);
        tokens_icon = root.findViewById(R.id.tokens_icon);
        ImageButton tokens_info = root.findViewById(R.id.tokens_info);
        Spinner[] spinners = {
                token_from_spinner1, token_from_spinner2, token_from_spinner3,
                token_from_spinner4, token_to_spinner1, token_to_spinner2,
                token_to_spinner3, token_to_spinner4
        };

        for (Spinner spinner : spinners) {
            spinner.setOnItemSelectedListener(this);
        }

        tokens_info.setOnClickListener(v -> showTokenInfo());

        List<String> honorLevels = Arrays.asList(getResources().getStringArray(R.array.token_type));
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, honorLevels, Color.WHITE);
        token_type_spinner.setAdapter(adapter);
        token_type_spinner.setSelection(2);

        token_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tokenType = position;
                updateTokenTypeIcon(tokenType);
                updateTokenTotal(tokenType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No se requiere ninguna acción cuando no se selecciona ningún elemento
            }
        });

        tokens_info.setOnClickListener(v -> {
            LayoutInflater info_inflater = (LayoutInflater) requireContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popupView = info_inflater.inflate(R.layout.tokens_info_layout, null);
            PopupWindow popupWindow = new PopupWindow(popupView, 1000, 500);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, 0);
        });

        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        updateTokenTotal(tokenType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // No se requiere ninguna acción cuando no se selecciona ningún elemento
    }

    // Método para actualizar el campo token_total
    private void updateTokenTotal(int type) {
        String[] fromValues = {
                token_from_spinner1.getSelectedItem().toString(),
                token_from_spinner2.getSelectedItem().toString(),
                token_from_spinner3.getSelectedItem().toString(),
                token_from_spinner4.getSelectedItem().toString()
        };

        String[] toValues = {
                token_to_spinner1.getSelectedItem().toString(),
                token_to_spinner2.getSelectedItem().toString(),
                token_to_spinner3.getSelectedItem().toString(),
                token_to_spinner4.getSelectedItem().toString()
        };

        int total = calculateTokenTotal(type, fromValues, toValues);
        token_total.setText(String.valueOf(total));
    }

    private void updateTokenTypeIcon(int position) {
        // Actualizar el icono según la posición del tipo de token
        switch (position) {
            case 0:
                tokens_icon.setImageResource(R.drawable.ic_elite_tokens);
                break;
            case 1:
                tokens_icon.setImageResource(R.drawable.ic_epic_tokens);
                break;
            case 2:
                tokens_icon.setImageResource(R.drawable.ic_legendary_tokens);
                break;
            default:
                break;
        }
    }

    private int calculateTokenTotal(int type, String[] fromValues, String[] toValues) {
        int[][] sculptable = {{}, {}, {}, {},
                {0, 0, 0, 0},
                {10, 10, 10, 10},
                {10, 10, 10, 10},
                {15, 10, 10, 10},
                {15, 20, 10, 10},
                {30, 20, 10, 10},
                {30, 20, 20, 10},
                {40, 20, 20, 10},
                {40, 30, 20, 10},
                {45, 30, 20, 10},
                {45, 30, 20, 20},
                {50, 30, 30, 20},
                {50, 40, 30, 20},
                {75, 40, 30, 20},
                {75, 40, 30, 20},
                {80, 40, 30, 20},
                {80, 50, 40, 30}
        };

        int totalTokens = 0;
        int from = 0;
        int to = 0;

        for (String value : fromValues) {
            int intValue = Integer.parseInt(value);
            from += intValue;
        }

        for (String value : toValues) {
            int intValue = Integer.parseInt(value);
            to += intValue;
        }

        switch(type) {
            case 0:
                for (int z = from + 1; z <= to; z++) {
                    totalTokens += sculptable[z][2];
                }
                return totalTokens;

            case 1:
                for (int z = from + 1; z <= to; z++) {
                    totalTokens += sculptable[z][1];
                }
                return totalTokens;

            case 2:
                for (int z = from + 1; z <= to; z++) {
                    totalTokens += sculptable[z][0];
                }
                return totalTokens;

            default:
                System.out.println("Something went wrong with the tokens switch case. Value registered -> " + type);
                break;
        }

        return 0;
    }

    public void showTokenInfo() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}