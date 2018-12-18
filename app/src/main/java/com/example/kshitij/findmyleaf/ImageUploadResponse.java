package com.example.kshitij.findmyleaf;

import com.google.gson.annotations.SerializedName;

class ImageUploadResponse {  /// change the fields for the type of response you will get from your api
    @SerializedName("_id")
    private String id;
    @SerializedName("message")
    private String message;
    @SerializedName("date")
    private String date;
    @SerializedName("epoch")
    private Long epoch;
    @SerializedName("image")
    private String image;
    @SerializedName("__v")
    private Integer v;
    @SerializedName("title")
    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getEpoch() {
        return epoch;
    }

    public void setEpoch(Long epoch) {
        this.epoch = epoch;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getV() {
        return v;
    }

    public void setV(Integer v) {
        this.v = v;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
