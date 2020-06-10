package com.myoptimind.g8_app.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "announcements")
public class Announcement extends BaseEntity {

    @SerializedName("announcement_type")
    @ColumnInfo(name = "announcement_type")
    private String announcementType;

    private String title;

    private String excerpt;

    private String content;

    @SerializedName("featured_image")
    @ColumnInfo(name = "featured_image")
    private String featuredImage;

    @SerializedName("formatted_created")
    @ColumnInfo(name = "formatted_created")
    private String formattedCreated;

    @SerializedName("formatted_announcement_type")
    @ColumnInfo(name = "formatted_announcement_type")
    private String formattedAnnouncementType;

    @SerializedName("duration")
    private String duration;

    @ColumnInfo(name = "is_checked")
    private Boolean isChecked;

    public Announcement() {
        super();
    }

    public String getAnnouncementType() {
        return announcementType;
    }

    public void setAnnouncementType(String announcementType) {
        this.announcementType = announcementType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExcerpt() {
        return excerpt;
    }

    public void setExcerpt(String excerpt) {
        this.excerpt = excerpt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public String getFormattedCreated() {
        return formattedCreated;
    }

    public void setFormattedCreated(String formattedCreated) {
        this.formattedCreated = formattedCreated;
    }

    public String getFormattedAnnouncementType() {
        return formattedAnnouncementType;
    }

    public void setFormattedAnnouncementType(String formattedAnnouncementType) {
        this.formattedAnnouncementType = formattedAnnouncementType;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public Boolean getChecked() {
        return isChecked;
    }


    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
