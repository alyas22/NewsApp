package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;



public class QueryUtils {
    public static String LOG_TAG = QueryUtils.class.getSimpleName();

    private static ArrayList<News> extractFromJson(String NewsJSON) {
        ArrayList<News> news_list = new ArrayList<>();

        if (TextUtils.isEmpty(NewsJSON)) {
            return null;
        }

        try {
            JSONObject jsonResponse = new JSONObject(NewsJSON);
            JSONObject jsonResults = jsonResponse.getJSONObject("response");
            JSONArray resultsArray = jsonResults.getJSONArray("results");

            for (int i = 0; i < resultsArray.length(); i++){
                JSONObject newsItem = resultsArray.getJSONObject(i);
                String section = newsItem.getString("sectionName");
                String date = newsItem.getString("webPublicationDate");
                String title = newsItem.getString("webTitle");
                String url = newsItem.getString("webUrl");
                JSONArray tagsArray = newsItem.getJSONArray("tags");
                String allAuthors = allAuthors(tagsArray);

                News news = new News(title,allAuthors,date,section,url );
                news_list.add(news);
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the News JSON results", e);
        }
        return news_list;
    }

    public static ArrayList<News> fetchNewsData(String textEntered) {

        URL url = createURL(textEntered);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        ArrayList<News> news = extractFromJson(jsonResponse);
        return news;
    }

    private static URL createURL(String stringUrl) {

        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the News JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
    private static String allAuthors(JSONArray authorsArray) throws JSONException {

        String authorsList = "";
        if (authorsArray.length() == 0)
            authorsList = "";
        for (int i = 0; i < authorsArray.length(); i++) {
            JSONObject tagsObject = authorsArray.getJSONObject(i);
            String firstName = tagsObject.optString("firstName");
            String lastName = tagsObject.optString("lastName");
            String authorName = firstName + " " + lastName;
            if (i == 0)
                authorsList = authorName;
            else
                authorsList = authorsList + ", " + authorName;
        }
        return authorsList;
    }

}
