package com.myoptimind.g8_app.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class User extends BaseEntity {

    public static final int USER_TYPE_PROMODISER    = 3; // same value from api
    public static final int USER_TYPE_COORDINATOR = 1;

    @ColumnInfo(name = "employee_number")
    private String employeeNumber;

    private String email;

    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    private int position;

    private String birthday;

    @ColumnInfo(name = "display_picture")
    private String displayPicture;

    @ColumnInfo(name = "mobile_num")
    private String mobileNum;

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getDisplayPicture() {
        return displayPicture;
    }

    public void setDisplayPicture(String displayPicture) {
        this.displayPicture = displayPicture;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getFullname(){
        return firstName + " " + lastName;
    }

    public Boolean isPromodiser(){
        return getPosition() == USER_TYPE_PROMODISER;
    }

    public Boolean isCoordinator(){
        return getPosition() == USER_TYPE_COORDINATOR;
    }

    public int getTimeInLimit(){
        return -1; // -1 = unli timein
/*        switch(getPosition()){
            case User.USER_TYPE_COORDINATOR: {
                return 3;
            }
            case User.USER_TYPE_PROMODISER: {
                return -1; // -1 for unlimited time ins outs
            }
            default: {
                return  1;
            }
        }*/
    }
}

