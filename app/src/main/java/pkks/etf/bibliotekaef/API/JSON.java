package pkks.etf.bibliotekaef.API;

import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pkks.etf.bibliotekaef.types.BookEntry;

/**
 * Created by Adnan on 17.5.2015..
 */
public class JSON {
    //get api key from google developer console
    private static final String ISBN_API_KEY = "AIzaSyCJlp9J3pSVjJ1hr0IO5H6U5vE2mVzZ9Ko";

    public static BookEntry FetchBookInfo(String ISBN) {
        String url = String.format("https://www.googleapis.com/books/v1/volumes?q=isbn:%s&key=%s", ISBN, ISBN_API_KEY);
        try {
            JSONObject object = new JSONObject(downloadJSON(url));
            JSONArray array = object.getJSONArray("items");
            if ( array.length() == 0 ) return null;

            JSONObject bookItem = array.getJSONObject(0).getJSONObject("volumeInfo");
            BookEntry info = new BookEntry();

            if ( bookItem.has("title") )
                info.title = bookItem.getString("title");
            else
                info.title = "";

            if (bookItem.has("description") )
                info.description = bookItem.getString("description");
            else
                info.description = "";

            if ( bookItem.has("authors") ) {
                JSONArray authors = bookItem.getJSONArray("authors");
                info.author = "";
                for (int i = 0; i < authors.length(); i++)
                    info.author += ((i == 0) ? "" : ";") + authors.getString(i);
            } else {
                info.description = "";
            }

            if ( bookItem.has("pageCount") )
                info.pageCount = bookItem.getInt("pageCount");
            else
                info.pageCount = 0;

            return info;
        } catch ( Exception e ) {
            return null;
        }
    }

    private static String downloadJSON(String url) throws Exception {
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url);
        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
            } else {
                if ( statusCode == 401 ) throw new Exception("Not allowed!");

            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return builder.toString();
    }

    public static class UpdateInfo {
        public String changeLog;
        public String version;
        public String apkUrl;
        public String changeLogUrl;
    }
}