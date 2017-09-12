package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    private static String formatDate(String dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        Date myDate = null;
        try {
            myDate = dateFormat.parse(dateObject);
        } catch (ParseException e) {
            e.printStackTrace();
        }
            SimpleDateFormat currentDateFormat = new SimpleDateFormat("MMM d, yyy", Locale.getDefault());
            return currentDateFormat.format(myDate);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News news = getItem(position);

        TextView titleText = listItemView.findViewById(R.id.title);
        titleText.setText(news.getTitle());

        TextView authorText = listItemView.findViewById(R.id.author);
        authorText.setText(news.getAuthor());

        TextView sectionText = listItemView.findViewById(R.id.section);
        sectionText.setText(news.getSection());

        String dateObject = new String(news.getDate());
        TextView dateText = listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        dateText.setText(formattedDate);

        return listItemView;

    } }
