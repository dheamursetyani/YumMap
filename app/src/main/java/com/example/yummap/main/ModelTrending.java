package com.example.yummap.main;

public class ModelTrending implements java.io.Serializable {
    int imgThumb;
    String tvPlaceName;
    String tvVote;
    String category; // NEW

    public ModelTrending(int imgThumb, String tvPlaceName, String tvVote, String category) {
        this.imgThumb = imgThumb;
        this.tvPlaceName = tvPlaceName;
        this.tvVote = tvVote;
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getImgThumb() {
        return imgThumb;
    }

    public void setImgThumb(int imgThumb) {
        this.imgThumb = imgThumb;
    }

    public String getTvPlaceName() {
        return tvPlaceName;
    }

    public void setTvPlaceName(String tvPlaceName) {
        this.tvPlaceName = tvPlaceName;
    }

    public String getTvVote() {
        return tvVote;
    }

    public void setTvVote(String tvVote) {
        this.tvVote = tvVote;
    }
}
