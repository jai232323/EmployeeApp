package com.example.employeeapp.Pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class EmployeeData implements Parcelable {


    private String employeeId;
    private String employeeName;
    private String employeeAddress;
    private String employeeAdharImg;

    public EmployeeData() {
    }

    public EmployeeData(String employeeId, String employeeName, String employeeAddress, String employeeAdharImg) {
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.employeeAddress = employeeAddress;
        this.employeeAdharImg = employeeAdharImg;
    }

    protected EmployeeData(Parcel in) {
        employeeId = in.readString();
        employeeName = in.readString();
        employeeAddress = in.readString();
        employeeAdharImg = in.readString();
    }

    public static final Creator<EmployeeData> CREATOR = new Creator<EmployeeData>() {
        @Override
        public EmployeeData createFromParcel(Parcel in) {
            return new EmployeeData(in);
        }

        @Override
        public EmployeeData[] newArray(int size) {
            return new EmployeeData[size];
        }
    };

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getEmployeeAddress() {
        return employeeAddress;
    }

    public void setEmployeeAddress(String employeeAddress) {
        this.employeeAddress = employeeAddress;
    }

    public String getEmployeeAdharImg() {
        return employeeAdharImg;
    }

    public void setEmployeeAdharImg(String employeeAdharImg) {
        this.employeeAdharImg = employeeAdharImg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(employeeId);
        parcel.writeString(employeeName);
        parcel.writeString(employeeAddress);
        parcel.writeString(employeeAdharImg);
    }
}
