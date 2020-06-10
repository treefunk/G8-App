package com.myoptimind.g8_app.features.login;

import com.google.gson.annotations.SerializedName;
import com.myoptimind.g8_app.api.Meta;
import com.myoptimind.g8_app.models.Announcement;
import com.myoptimind.g8_app.models.BaseEntity;
import com.myoptimind.g8_app.models.Store;

import java.util.ArrayList;
import java.util.List;

public class LoginResponse extends BaseEntity {

    @SerializedName("employee_number")
    private String employeeNumber;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("email_address")
    private String emailAddress;

    private String position;
    private String birthday;

    @SerializedName("store")
    private String storeLastSync;

    @SerializedName("sales")
    private String salesLastSync;

    @SerializedName("announcements")
    private String announcementsLastSync;

    @SerializedName("timeslips")
    private String timeSlipLastSync;

    @SerializedName("time_in_out")
    private String timeInOut;

    @SerializedName("unread_announcements")
    private ArrayList<Announcement> unreadAnnouncements;

    @SerializedName("tagged_stores")
    private List<Store> taggedStores;

    public String getEmployeeNumber() {
        return employeeNumber;
    }

    public void setEmployeeNumber(String employeeNumber) {
        this.employeeNumber = employeeNumber;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public ArrayList<Announcement> getUnreadAnnouncements() {
        return unreadAnnouncements;
    }

    public void setUnreadAnnouncements(ArrayList<Announcement> unreadAnnouncements) {
        this.unreadAnnouncements = unreadAnnouncements;
    }

    public String getStoreLastSync() {
        return storeLastSync;
    }

    public void setStoreLastSync(String storeLastSync) {
        this.storeLastSync = storeLastSync;
    }

    public String getSalesLastSync() {
        return salesLastSync;
    }

    public void setSalesLastSync(String salesLastSync) {
        this.salesLastSync = salesLastSync;
    }

    public String getAnnouncementsLastSync() {
        return announcementsLastSync;
    }

    public void setAnnouncementsLastSync(String announcementsLastSync) {
        this.announcementsLastSync = announcementsLastSync;
    }

    public String getTimeSlipLastSync() {
        return timeSlipLastSync;
    }

    public void setTimeSlipLastSync(String timeSlipLastSync) {
        this.timeSlipLastSync = timeSlipLastSync;
    }

    public List<Store> getTaggedStores() {
        return taggedStores;
    }

    public void setTaggedStores(ArrayList<Store> taggedStores) {
        this.taggedStores = taggedStores;
    }

    public String getTimeInOut() {
        return timeInOut;
    }

    public void setTimeInOut(String timeInOut) {
        this.timeInOut = timeInOut;
    }
}