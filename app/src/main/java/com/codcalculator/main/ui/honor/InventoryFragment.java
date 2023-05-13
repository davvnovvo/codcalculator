package com.codcalculator.main.ui.honor;

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

public class InventoryFragment extends Fragment {

    public InventoryFragment() {
        // Constructor vacío requerido
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    int[] fieldIds;

    ImageView honor_image;
    TextView honor_image_label, honor_total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.honor_inventory_fragment, container, false);

        honor_image = rootView.findViewById(R.id.honor_image_view);
        honor_image_label = rootView.findViewById(R.id.honor_image_label);
        ImageButton add_image = rootView.findViewById(R.id.honor_add_image_button);
        ImageButton reset = rootView.findViewById(R.id.honor_reset_fields);
        honor_total = rootView.findViewById(R.id.honor_total);
        ImageButton calculate = rootView.findViewById(R.id.honor_calculate);

        TextInputEditText[] fields = new TextInputEditText[] {
                rootView.findViewById(R.id.honor_field_5),
                rootView.findViewById(R.id.honor_field_10),
                rootView.findViewById(R.id.honor_field_20),
                rootView.findViewById(R.id.honor_field_50),
                rootView.findViewById(R.id.honor_field_100),
                rootView.findViewById(R.id.honor_field_200),
                rootView.findViewById(R.id.honor_field_500),
                rootView.findViewById(R.id.honor_field_1000),
                rootView.findViewById(R.id.honor_field_2000),
                rootView.findViewById(R.id.honor_field_5000)
        };
        fieldIds = new int[] { R.id.honor_field_5, R.id.honor_field_10, R.id.honor_field_20, R.id.honor_field_50, R.id.honor_field_100, R.id.honor_field_200, R.id.honor_field_500, R.id.honor_field_1000, R.id.honor_field_2000, R.id.honor_field_5000};
        BigInteger[] values = new BigInteger[] {
                BigInteger.valueOf(5),
                BigInteger.valueOf(10),
                BigInteger.valueOf(20),
                BigInteger.valueOf(50),
                BigInteger.valueOf(100),
                BigInteger.valueOf(200),
                BigInteger.valueOf(500),
                BigInteger.valueOf(1000),
                BigInteger.valueOf(2000),
                BigInteger.valueOf(5000),
        };

        resetFields(rootView);

        ImageButton remove_image = rootView.findViewById(R.id.honor_remove_image_button);
        remove_image.setOnClickListener(v -> {
            honor_image.setImageResource(0);
            honor_image_label.setVisibility(View.VISIBLE);
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
            honor_total.setText(formattedTotal);
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
                honor_image.setImageBitmap(bitmap);
                honor_image_label.setVisibility(View.GONE);
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