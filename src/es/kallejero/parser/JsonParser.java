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
public class JsonParser {
    Uri uri = Uri.parse("content://es.kallejero.kallejero.bd/negocio");
   /* public ContentValues values = new ContentValues();
    private ContentResolver contentResolver;
    public JsonParser(ContentResolver contentR) {
        this.contentResolver=contentR;
    }*/

    public ArrayList<HashMap<String, String>> parserArray(String jsonString){

    final ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();

    try {
        JSONArray jsonArr = new JSONArray(jsonString);

        final int numRows = jsonArr.length();
        for (int x = 0; x <= numRows; x++) {
            final JSONObject row = jsonArr.getJSONObject(x);

            final HashMap<String, String> negocio = new HashMap<String, String>();
            negocio.put("id", ""+row.getInt("id"));
            negocio.put("categoria", row.getString("categoria"));
            negocio.put("nombre", row.getString("nombre"));
            negocio.put("descripcion", row.getString("descripcion"));
            negocio.put("ciudad", row.getString("ciudad"));
            negocio.put("direccion", row.getString("direccion"));
            negocio.put("telefono", row.getString("telefono"));
            negocio.put("visitas", row.getString("visitas"));
            negocio.put("puntuacion", row.getString("puntuacion"));

            String imagen="http://kallejero.es/negocios/"+row.getString("imagen");
            negocio.put("imagen", imagen );
            negocio.put("posicion", row.getString("posicion"));


           /* values.put(BaseDatos.Posts.NOMBRE, row.getString("nombre"));
            values.put(BaseDatos.Posts.DESCRIPCION, row.getString("descripcion"));
            values.put(BaseDatos.Posts.CATEGORIA, row.getString("categoria"));

            contentResolver.insert(uri, values);*/
            result.add(negocio);
        }
    } catch (JSONException e) {
        e.printStackTrace();
        Log.e("Error", e.getMessage());
    }
    return result;

}


}