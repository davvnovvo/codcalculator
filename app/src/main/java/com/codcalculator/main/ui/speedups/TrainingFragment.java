package com.codcalculator.main.ui.speedups;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.codcalculator.main.utilities.SharedPreferencesUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.math.BigInteger;
import java.text.NumberFormat;

public class TrainingFragment extends Fragment {

    public TrainingFragment() {
        // Constructor vacío requerido
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    int[] fieldIds;

    ImageView training_image;
    TextView training_image_label, training_total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.speedups_training_fragment, container, false);

        training_image = rootView.findViewById(R.id.training_image_view);
        training_image_label = rootView.findViewById(R.id.training_image_label);
        ImageButton add_image = rootView.findViewById(R.id.training_add_image_button);
        ImageButton reset = rootView.findViewById(R.id.training_reset_fields);
        training_total = rootView.findViewById(R.id.training_total);
        ImageButton calculate = rootView.findViewById(R.id.training_calculate);

        TextInputEditText[] fields = new TextInputEditText[] {
                rootView.findViewById(R.id.training_field_1min),
                rootView.findViewById(R.id.training_field_5min),
                rootView.findViewById(R.id.training_field_10min),
                rootView.findViewById(R.id.training_field_15min),
                rootView.findViewById(R.id.training_field_30min),
                rootView.findViewById(R.id.training_field_60min),
                rootView.findViewById(R.id.training_field_3h),
                rootView.findViewById(R.id.training_field_8h),
                rootView.findViewById(R.id.training_field_15h),
                rootView.findViewById(R.id.training_field_24h),
                rootView.findViewById(R.id.training_field_3_days),
                rootView.findViewById(R.id.training_field_7_days),
                rootView.findViewById(R.id.training_field_30_days)
        };

        fieldIds = new int[] {R.id.training_field_1min, R.id.training_field_5min, R.id.training_field_10min, R.id.training_field_15min, R.id.training_field_30min, R.id.training_field_60min, R.id.training_field_3h, R.id.training_field_8h, R.id.training_field_15h, R.id.training_field_24h, R.id.training_field_3_days, R.id.training_field_7_days, R.id.training_field_30_days};

        BigInteger[] values = new BigInteger[] {BigInteger.ONE, BigInteger.valueOf(5), BigInteger.TEN, BigInteger.valueOf(15), BigInteger.valueOf(30),
                BigInteger.valueOf(60), BigInteger.valueOf(180), BigInteger.valueOf(480), BigInteger.valueOf(900),
                BigInteger.valueOf(1440), BigInteger.valueOf(4320), BigInteger.valueOf(10080), BigInteger.valueOf(43200)};


        resetFields(rootView);

        ImageButton remove_image = rootView.findViewById(R.id.training_remove_image_button);
        remove_image.setOnClickListener(v -> {
            training_image.setImageResource(0);
            training_image_label.setVisibility(View.VISIBLE);
        });


        add_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        reset.setOnClickListener(v -> resetFields(rootView));

        training_total.setText(SharedPreferencesUtil.getCalculation(getContext(), "training"));
        calculate.setOnClickListener(v -> {
            BigInteger total = BigInteger.ZERO;
            for (int i = 0; i < fields.length; i++) {
                BigInteger quantity = TextUtils.isEmpty(fields[i].getText()) ? BigInteger.ZERO : new BigInteger(fields[i].getText().toString());
                total = total.add(quantity.multiply(values[i]));
            }

            BigInteger minutes = total.mod(BigInteger.valueOf(60)); // Obtiene los minutos sobrantes
            BigInteger totalHours = total.divide(BigInteger.valueOf(60));
            BigInteger hours = totalHours.mod(BigInteger.valueOf(24)); // 24 horas en un día
            BigInteger totalDays = totalHours.divide(BigInteger.valueOf(24));

            // Obtener los recursos de cadena para los plurales y singulares de "día", "hora" y "minuto"
            Resources res = getResources();
            String day = res.getQuantityString(R.plurals.day, totalDays.intValue());
            String hour = res.getQuantityString(R.plurals.hour, hours.intValue());
            String minute = res.getQuantityString(R.plurals.minute, minutes.intValue());

            String formattedTotal;
            if (totalDays.compareTo(BigInteger.ZERO) > 0) {
                String dayStr = NumberFormat.getInstance().format(totalDays);
                String hourStr = "";
                String minuteStr = "";
                if (hours.compareTo(BigInteger.ZERO) > 0) {
                    hourStr = NumberFormat.getInstance().format(hours);
                }
                if (minutes.compareTo(BigInteger.ZERO) > 0) {
                    minuteStr = NumberFormat.getInstance().format(minutes);
                }
                if (hours.compareTo(BigInteger.ZERO) == 0 && minutes.compareTo(BigInteger.ZERO) == 0) {
                    formattedTotal = res.getString(R.string.time_format_days_only, dayStr, day);
                } else if (hours.compareTo(BigInteger.ZERO) == 0) {
                    formattedTotal = res.getString(R.string.time_format_days_and_minutes, dayStr, day, minuteStr, minute);
                } else {
                    formattedTotal = res.getString(R.string.time_format_days_hours_and_minutes, dayStr, day, hourStr, hour, minuteStr, minute);
                }
            } else if (hours.compareTo(BigInteger.ZERO) > 0) {
                String hourStr = NumberFormat.getInstance().format(hours);
                String minuteStr = "";
                if (minutes.compareTo(BigInteger.ZERO) > 0) {
                    minuteStr = NumberFormat.getInstance().format(minutes);
                }
                formattedTotal = minutes.compareTo(BigInteger.ZERO) == 0 ? res.getString(R.string.time_format_hours_only, hourStr, hour) : res.getString(R.string.time_format_hours_and_minutes, hourStr, hour, minuteStr, minute);
            } else {
                formattedTotal = minutes.compareTo(BigInteger.ZERO) > 0 ? res.getString(R.string.time_format_minutes_only, NumberFormat.getInstance().format(minutes), minute) : "";
            }

            SharedPreferencesUtil.saveCalculation(getContext(), "training", formattedTotal);
            training_total.setText(formattedTotal);
        });


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Comprobar si el resultado corresponde a la selección de una imagen
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // Obtener la URI de la imagen seleccionada
            Uri imageUri = data.getData();

            // Mostrar la imagen en el ImageView
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                training_image.setImageBitmap(bitmap);
                training_image_label.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void resetFields(View v) {
        for (int fieldId : fieldIds) {
            TextInputEditText field = v.findViewById(fieldId);
            field.setText("");
        }
    }

}
