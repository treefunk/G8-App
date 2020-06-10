package com.myoptimind.g8_app.features.salesreport;

import org.joda.time.DateTime;

public class SalesDay {

    public SalesDay() {
        isDisabled = false;
        mSalesValue = "";
    }

    private String mDay, mSalesValue;

    private String salesReportId;

    public String dayCreated;

    private DateTime dateTime;

    private Boolean isDisabled;

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        this.mDay = day;
    }

    public String getSalesValue() {
        return mSalesValue;
    }

    public void setSalesValue(String salesValue) {
        this.mSalesValue = salesValue;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getDisabled() {
        return isDisabled;
    }

    public void setDisabled(Boolean disabled) {
        isDisabled = disabled;
    }

    public String getSalesReportId() {
        return salesReportId;
    }

    public void setSalesReportId(String salesReportId) {
        this.salesReportId = salesReportId;
    }

    public String getDayCreated() {
        return dayCreated;
    }

    public void setDayCreated(String dayCreated) {
        this.dayCreated = dayCreated;
    }
}
