package es.kallejero.parser;

import android.os.AsyncTask;
import android.widget.Adapter;
import android.widget.SimpleAdapter;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Eslem on 16/07/13.
 */
public class Rest {

    private static final String BASE_URL = "http://kallejero.es/php/json.php";

    public String downloadData(String params) throws ClientProtocolException,
            IOException {
        final HttpClient client = new DefaultHttpClient();
        final HttpGet request = new HttpGet(BASE_URL+params);

        final HttpResponse response = client.execute(request);

        final InputStream contentStream = response.getEntity().getContent();
        final BufferedReader reader = new BufferedReader(
                new InputStreamReader(contentStream));

        final StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }

        return result.toString();
    }


}
