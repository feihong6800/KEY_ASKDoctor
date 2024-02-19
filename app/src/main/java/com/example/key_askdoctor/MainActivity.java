package com.example.key_askdoctor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText doctorName, hospitalName,
            clinicLocation, clinicNumber, spokenLanguage,
            doctorQualifications;
    private Spinner category_Spinner;
    private AppCompatButton btn_monday_am, btn_monday_pm, btn_tuesday_am,
            btn_tuesday_pm, btn_wednesday_am, btn_wednesday_pm,
            btn_thursday_am, btn_thursday_pm, btn_friday_am, btn_friday_pm,
            btn_saturday_am, btn_saturday_pm, btn_sunday_am, btn_sunday_pm;
    private TextView btn_done;
    private ImageView btn_add_image;
    private Switch switchMonday, switchTuesday, switchWednesday,
            switchThursday, switchFriday, switchSaturday, switchSunday;
    private final int SELECT_LAUNDRY_IMAGE = 6;
    private Uri LaundryPicUri;
    private String laundryPicUriString, item;
    private DatabaseReference categoryRef;
    private List<String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add_image = findViewById(R.id.add_image);
        category_Spinner = findViewById(R.id.specialist_type);
        doctorName = findViewById(R.id.alei_doctor_name);
        hospitalName = findViewById(R.id.alei_hospital_name);
        clinicLocation = findViewById(R.id.alei_clinic_location);
        clinicNumber = findViewById(R.id.alei_phone_number);
        spokenLanguage = findViewById(R.id.alei_spoken_language);
        doctorQualifications = findViewById(R.id.alei_doctor_qualifications);
        btn_done = findViewById(R.id.tv_done);

        categoryRef = FirebaseDatabase.getInstance().getReference();
        categories = new ArrayList<String>();
        categoryRef.child("Specialties").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    item = dataSnapshot.getValue(String.class);
                    categories.add(item);

                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, categories);
                    // Drop down layout style - list view with radio button
                    dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                    // attaching data adapter to spinner
                    category_Spinner.setAdapter(dataAdapter);
                    category_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            item = adapterView.getItemAtPosition(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_LAUNDRY_IMAGE);
            }
        });

        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String doctor_name = doctorName.getText().toString();
                String hospital_name = hospitalName.getText().toString();
                String clinic_location = clinicLocation.getText().toString();
                String clinic_phone = clinicNumber.getText().toString();
                String spoken_language = spokenLanguage.getText().toString();
                String doctor_qualifications = doctorQualifications.getText().toString();

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_LAUNDRY_IMAGE && data != null && data.getData() != null) {
                //handle selected laundry image
                LaundryPicUri = data.getData();
                laundryPicUriString = String.valueOf(LaundryPicUri);
                //show what user select
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), LaundryPicUri);
                    btn_add_image.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}