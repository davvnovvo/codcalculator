package com.codcalculator.main.ui.resources;

import android.app.Activity;
import android.content.Intent;
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
import java.util.Locale;

public class StoneFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    int[] fieldIds;

    ImageView stone_image;
    TextView stone_image_label, stone_total;

    public StoneFragment() {
        // Constructor vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.resources_stone_fragment, container, false);

        stone_image = rootView.findViewById(R.id.stone_image_view);
        stone_image_label = rootView.findViewById(R.id.stone_image_label);
        ImageButton add_image = rootView.findViewById(R.id.stone_add_image_button);
        ImageButton reset = rootView.findViewById(R.id.stone_reset_fields);
        stone_total = rootView.findViewById(R.id.stone_total);
        ImageButton calculate = rootView.findViewById(R.id.stone_calculate);

        TextInputEditText[] fields = new TextInputEditText[] {
                rootView.findViewById(R.id.stone_field_750),
                rootView.findViewById(R.id.stone_field_7_5k),
                rootView.findViewById(R.id.stone_field_37_5k),
                rootView.findViewById(R.id.stone_field_112_5k),
                rootView.findViewById(R.id.stone_field_375k),
                rootView.findViewById(R.id.stone_field_1_125M),
                rootView.findViewById(R.id.stone_field_3_75M)
        };
        fieldIds = new int[] { R.id.stone_field_750, R.id.stone_field_7_5k, R.id.stone_field_37_5k, R.id.stone_field_112_5k, R.id.stone_field_375k, R.id.stone_field_1_125M, R.id.stone_field_3_75M};
        BigInteger[] values = new BigInteger[] {
                BigInteger.valueOf(750),
                BigInteger.valueOf(7500),
                BigInteger.valueOf(37500),
                BigInteger.valueOf(112500),
                BigInteger.valueOf(375000),
                BigInteger.valueOf(1125000),
                BigInteger.valueOf(3750000)
        };

        resetFields(rootView);

        ImageButton remove_image = rootView.findViewById(R.id.stone_remove_image_button);
        remove_image.setOnClickListener(v -> {
            stone_image.setImageResource(0);
            stone_image_label.setVisibility(View.VISIBLE);
        });


        add_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        reset.setOnClickListener(v -> resetFields(rootView));

        stone_total.setText(SharedPreferencesUtil.getCalculation(getContext(), "stone"));
        calculate.setOnClickListener(v -> {
            BigInteger total = BigInteger.ZERO;
            for (int i = 0; i < fields.length; i++) {
                BigInteger quantity = TextUtils.isEmpty(fields[i].getText()) ? BigInteger.ZERO : new BigInteger(fields[i].getText().toString());
                total = total.add(quantity.multiply(values[i]));
            }
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            String formattedTotal = total.compareTo(BigInteger.ZERO) > 0 ? numberFormat.format(total) : "";
            SharedPreferencesUtil.saveCalculation(getContext(), "stone", formattedTotal);
            stone_total.setText(formattedTotal);
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
                stone_image.setImageBitmap(bitmap);
                stone_image_label.setVisibility(View.GONE);
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