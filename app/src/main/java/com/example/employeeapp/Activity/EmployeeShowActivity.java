package com.example.employeeapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.employeeapp.Adapter.EmployeeAdapter;
import com.example.employeeapp.Pojo.EmployeeData;
import com.example.employeeapp.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class EmployeeShowActivity extends AppCompatActivity {


    ExtendedFloatingActionButton fab;

    RecyclerView EmployeeRecyclerView;
    private ArrayList<EmployeeData> list;
    private EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_show);


        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(EmployeeShowActivity.this,EmployeeAddActivity.class));
            }
        });

        GridLayoutManager linearLayoutManager =  new GridLayoutManager(EmployeeShowActivity.this,2);

        EmployeeRecyclerView = findViewById(R.id.EmployeeRecyclerView);
        EmployeeRecyclerView.setLayoutManager(linearLayoutManager);
        list = new ArrayList<>();
        adapter = new EmployeeAdapter(EmployeeShowActivity.this,list);
        EmployeeRecyclerView.setAdapter(adapter);

        //getTenantData
        getEmployeeData();

    }

    private void getEmployeeData() {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Employees");
        reference1.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    EmployeeData data = dataSnapshot.getValue(EmployeeData.class);
                    list.add(0, data);

                }
//                Collections.reverse(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EmployeeShowActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}