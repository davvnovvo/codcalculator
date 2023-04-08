package com.codcalculator.main.ui.speedups;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.codcalculator.R;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

public class BuildingFragment extends Fragment {

    public BuildingFragment() {
        // Constructor vacío requerido
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    int[] fieldIds;

    ImageView building_image;
    TextView building_image_label, building_total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.speedups_building_fragment, container, false);

        building_image = rootView.findViewById(R.id.building_image_view);
        building_image_label = rootView.findViewById(R.id.building_image_label);
        ImageButton add_image = rootView.findViewById(R.id.building_add_image_button);
        ImageButton reset = rootView.findViewById(R.id.building_reset_fields);
        building_total = rootView.findViewById(R.id.building_total);
        ImageButton calculate = rootView.findViewById(R.id.building_calculate);

        TextInputEditText[] fields = new TextInputEditText[] {
                rootView.findViewById(R.id.building_field_1min),
                rootView.findViewById(R.id.building_field_5min),
                rootView.findViewById(R.id.building_field_10min),
                rootView.findViewById(R.id.building_field_15min),
                rootView.findViewById(R.id.building_field_30min),
                rootView.findViewById(R.id.building_field_60min),
                rootView.findViewById(R.id.building_field_3h),
                rootView.findViewById(R.id.building_field_8h),
                rootView.findViewById(R.id.building_field_15h),
                rootView.findViewById(R.id.building_field_24h),
                rootView.findViewById(R.id.building_field_3_days),
                rootView.findViewById(R.id.building_field_7_days),
                rootView.findViewById(R.id.building_field_30_days)
        };
        fieldIds = new int[] {R.id.building_field_1min, R.id.building_field_5min, R.id.building_field_10min, R.id.building_field_15min, R.id.building_field_30min, R.id.building_field_60min, R.id.building_field_3h, R.id.building_field_8h, R.id.building_field_15h, R.id.building_field_24h, R.id.building_field_3_days, R.id.building_field_7_days, R.id.building_field_30_days};
        int[] values = new int[] {1, 5, 10, 15, 30, 60, 180, 480, 900, 1440, 4320, 10080, 43200};

        resetFields(rootView);

        ImageButton remove_image = rootView.findViewById(R.id.building_remove_image_button);
        remove_image.setOnClickListener(v -> {
            building_image.setImageResource(0);
            building_image_label.setVisibility(View.VISIBLE);
        });


        add_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        reset.setOnClickListener(v -> resetFields(rootView));

        calculate.setOnClickListener(v -> {
            int total = 0;
            for (int i = 0; i < fields.length; i++) {
                int quantity = TextUtils.isEmpty(fields[i].getText()) ? 0 : Integer.parseInt(fields[i].getText().toString());
                total += quantity * values[i];
            }

            int days = total / 1440; // 1440 minutos en un día
            int hours = (total % 1440) / 60; // 60 minutos en una hora
            int minutes = total % 60;

            // Obtener los recursos de cadena para los plurales y singulares de "día", "hora" y "minuto"
            Resources res = getResources();
            String day = res.getQuantityString(R.plurals.day, days);
            String hour = res.getQuantityString(R.plurals.hour, hours);
            String minute = res.getQuantityString(R.plurals.minute, minutes);

            String formattedTotal;
            if (days > 0) {
                formattedTotal = res.getString(R.string.time_format_days, days, day, hours, hour, minutes, minute);
            } else if (hours > 0) {
                formattedTotal = res.getString(R.string.time_format_hours, hours, hour, minutes, minute);
            } else {
                formattedTotal = res.getString(R.string.time_format_minutes, minutes, minute);
            }

            building_total.setText(formattedTotal);
        });

        return rootView;
    }

    private void resetFields(View v) {
        for (int fieldId : fieldIds) {
            TextInputEditText field = v.findViewById(fieldId);
            field.setText("");
        }
    }

}
