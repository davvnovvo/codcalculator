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
import java.text.NumberFormat;
import java.util.Locale;

public class ManaFragment extends Fragment {

    public ManaFragment() {
        // Constructor vacío requerido
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    int[] fieldIds;

    ImageView mana_image;
    TextView mana_image_label, mana_total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.resources_mana_fragment, container, false);
        mana_image = rootView.findViewById(R.id.mana_image_view);
        mana_image_label = rootView.findViewById(R.id.mana_image_label);
        ImageButton add_image = rootView.findViewById(R.id.mana_add_image_button);
        ImageButton reset = rootView.findViewById(R.id.mana_reset_fields);
        mana_total = rootView.findViewById(R.id.mana_total);
        ImageButton calculate = rootView.findViewById(R.id.mana_calculate);

        TextInputEditText[] fields = new TextInputEditText[]{
                rootView.findViewById(R.id.mana_field_500),
                rootView.findViewById(R.id.mana_field_3k),
                rootView.findViewById(R.id.mana_field_15k),
                rootView.findViewById(R.id.mana_field_50k),
                rootView.findViewById(R.id.mana_field_200k),
                rootView.findViewById(R.id.mana_field_600k),
                rootView.findViewById(R.id.mana_field_2M)
        };
        fieldIds = new int[]{R.id.mana_field_500, R.id.mana_field_3k, R.id.mana_field_15k, R.id.mana_field_50k, R.id.mana_field_200k, R.id.mana_field_600k, R.id.mana_field_2M};
        int[] values = new int[]{500, 3000, 15000, 50000, 200000, 600000, 2000000};

        resetFields(rootView);

        ImageButton remove_image = rootView.findViewById(R.id.mana_remove_image_button);
        remove_image.setOnClickListener(v -> {
            mana_image.setImageResource(0);
            mana_image_label.setVisibility(View.VISIBLE);
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
            NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
            String formattedTotal = numberFormat.format(total);
            mana_total.setText(formattedTotal);
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
                mana_image.setImageBitmap(bitmap);
                mana_image_label.setVisibility(View.GONE);
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