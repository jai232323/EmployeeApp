package com.example.employeeapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.employeeapp.Pojo.EmployeeData;
import com.example.employeeapp.R;

import java.util.ArrayList;

public class EmployeeAdapter  extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {


    private Context context;
    private ArrayList<EmployeeData> items;

    public EmployeeAdapter(Context context, ArrayList<EmployeeData> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.employee_adapter,parent,false);

        return new EmployeeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        EmployeeData data = items.get(position);

        Glide.with(context)
                .load(data.getEmployeeAdharImg())
                .into(holder.AdharImage);

        holder.Employee_Name.setText(data.getEmployeeName());
        holder.Employee_Address.setText(data.getEmployeeAddress());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView AdharImage;
        TextView Employee_Name,Employee_Address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            AdharImage = itemView.findViewById(R.id.AdharImage);
            Employee_Name = itemView.findViewById(R.id.Employee_Name);
            Employee_Address = itemView.findViewById(R.id.Employee_Address);
        }
    }
}
