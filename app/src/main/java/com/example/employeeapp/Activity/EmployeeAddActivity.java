package com.example.employeeapp.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.employeeapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class EmployeeAddActivity extends AppCompatActivity {


    //Initialize Variable
    MaterialCardView MC_AdharImg;;
    ImageView AdharImage;
    TextView AdharText;
    EditText Employee_Name,Employee_Address;
    Button Save;


    DatabaseReference reference;

    String employeeId,employeeAdharImg,employeeName,employeeAddress;


    Bitmap bitmap=null;
    private final int REQ = 1;
    StorageReference storageReference;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Employees");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        pd = new ProgressDialog(this);

        //Assign Variable

        MC_AdharImg = findViewById(R.id.MC_AdharImg);
        AdharImage = findViewById(R.id.AdharImage);
        AdharText = findViewById(R.id.AdharText);
        Employee_Name = findViewById(R.id.Employee_Name);
        Employee_Address = findViewById(R.id.Employee_Address);
        Save = findViewById(R.id.Save);

        MC_AdharImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });


        reference = FirebaseDatabase.getInstance().getReference("Employees");
        storageReference= FirebaseStorage.getInstance().getReference();



        employeeId = reference.push().getKey();


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                employeeName = Employee_Name.getText().toString();
                employeeAddress = Employee_Address.getText().toString();


                if (bitmap==null){
                    // UploadImage();
                    Toast.makeText(EmployeeAddActivity.this, "Upload Adhar Image", Toast.LENGTH_SHORT).show();
                }else if (employeeName.isEmpty())
                {
                    Employee_Name.requestFocus();
                    Employee_Name.setError("Required Name");
                }else if (employeeAddress.isEmpty())
                {
                    Employee_Address.requestFocus();
                    Employee_Address.setError("Required Address");
                }
                else {
                    UploadAdharImage();
                }




            }
        });


    }

    private void UploadAdharImage() {
        pd.setMessage("Please Wait");
        pd.show();


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();

        final StorageReference filePath;
        filePath=storageReference.child("Employees").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(EmployeeAddActivity.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    employeeAdharImg = String.valueOf(uri);
                                    //insertData(email, password);
                                    UploadDataToFirebase(employeeId,employeeName,employeeAddress,employeeAdharImg);
                                }
                            });
                        }
                    });
                }else {
                    pd.dismiss();
                    Toast.makeText(EmployeeAddActivity.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UploadDataToFirebase(String employeeId, String employeeName,
                                      String employeeAddress, String employeeAdharImg) {


        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("employeeId",employeeId);
        hashMap.put("employeeAdharImg",employeeAdharImg);
        hashMap.put("employeeName",employeeName);
        hashMap.put("employeeAddress",employeeAddress);


        reference.child(employeeId).setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                Toast.makeText(EmployeeAddActivity.this, "Employee Added Successfully", Toast.LENGTH_SHORT).show();
                pd.dismiss();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EmployeeAddActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent picImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picImage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {

            Uri uri = data.getData();


            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            AdharImage.setImageBitmap(bitmap);
            AdharText.setVisibility(View.INVISIBLE);
            AdharText.setText("Set Successfully");
        }
    }
}