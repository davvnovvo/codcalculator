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
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Locale;

public class GoldFragment extends Fragment {

    public GoldFragment() {
        // Constructor vacío requerido
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    int[] fieldIds;

    ImageView gold_image;
    TextView gold_image_label, gold_total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.resources_gold_fragment, container, false);

        gold_image = rootView.findViewById(R.id.gold_image_view);
        gold_image_label = rootView.findViewById(R.id.gold_image_label);
        ImageButton add_image = rootView.findViewById(R.id.gold_add_image_button);
        ImageButton reset = rootView.findViewById(R.id.gold_reset_fields);
        gold_total = rootView.findViewById(R.id.gold_total);
        ImageButton calculate = rootView.findViewById(R.id.gold_calculate);

        TextInputEditText[] fields = new TextInputEditText[] {
                rootView.findViewById(R.id.gold_field_1k),
                rootView.findViewById(R.id.gold_field_10k),
                rootView.findViewById(R.id.gold_field_50k),
                rootView.findViewById(R.id.gold_field_150k),
                rootView.findViewById(R.id.gold_field_500k),
                rootView.findViewById(R.id.gold_field_1_5M),
                rootView.findViewById(R.id.gold_field_5M)
        };
        fieldIds = new int[] { R.id.gold_field_1k, R.id.gold_field_10k, R.id.gold_field_50k, R.id.gold_field_150k, R.id.gold_field_500k, R.id.gold_field_1_5M, R.id.gold_field_5M};
        BigInteger[] values = new BigInteger[] {
                BigInteger.valueOf(1000),
                BigInteger.valueOf(10000),
                BigInteger.valueOf(50000),
                BigInteger.valueOf(150000),
                BigInteger.valueOf(500000),
                BigInteger.valueOf(1500000),
                BigInteger.valueOf(5000000)
        };

        resetFields(rootView);

        ImageButton remove_image = rootView.findViewById(R.id.gold_remove_image_button);
        remove_image.setOnClickListener(v -> {
            gold_image.setImageResource(0);
            gold_image_label.setVisibility(View.VISIBLE);
        });


        add_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        reset.setOnClickListener(v -> resetFields(rootView));

        calculate.setOnClickListener(v -> {
            BigInteger total = BigInteger.ZERO;
            for (int i = 0; i < fields.length; i++) {
                BigInteger quantity = TextUtils.isEmpty(fields[i].getText()) ? BigInteger.ZERO : new BigInteger(fields[i].getText().toString());
                total = total.add(quantity.multiply(values[i]));
            }
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            String formattedTotal = total.compareTo(BigInteger.ZERO) > 0 ? numberFormat.format(total) : "";
            gold_total.setText(formattedTotal);
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
                gold_image.setImageBitmap(bitmap);
                gold_image_label.setVisibility(View.GONE);
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
