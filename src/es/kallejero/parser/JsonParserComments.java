package es.kallejero.parser;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import es.kallejero.database.BaseDatos;

/**
 * Created by Eslem on 16/07/13.
 */
public class JsonParserComments {
    //Uri uri = Uri.parse("content://es.kallejero.kallejero.bd/negocio");
  
    public ArrayList<HashMap<String, String>> parserArray(String jsonString){

    final ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

    try {
        JSONArray jsonArr = new JSONArray(jsonString);

        final int numRows = jsonArr.length();
        for (int x = 0; x <= numRows; x++) {
            final JSONObject row = jsonArr.getJSONObject(x);

            final HashMap<String, String> negocio = new HashMap<String, String>();
            negocio.put("id", ""+row.getInt("id"));
            negocio.put("negocio_ID", ""+row.getInt("negocio_ID"));
            negocio.put("nombre", row.getString("nombre"));
            negocio.put("fecha", row.getString("fecha"));
            negocio.put("comentario", row.getString("comentario"));
            negocio.put("likes", row.getString("likes"));
            negocio.put("dislikes", row.getString("dislikes"));
            
            result.add(negocio);
        }
    } catch (JSONException e) {
        e.printStackTrace();
        Log.e("Error", e.getMessage());
    }
    return result;

}


}