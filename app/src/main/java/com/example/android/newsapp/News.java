package com.example.android.newsapp;

public class News {
    private String mTitle;
    private String mAuthor;
    String mDate;
    String mSection;
    private String mUrl;

    public News(String  title,String authorName, String date, String section,  String url) {
        mTitle = title;
        mAuthor = authorName;
        mDate = date;
        mSection = section;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor(){return mAuthor;}

    public String getDate() {
        return mDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getUrl(){return mUrl;}
}

