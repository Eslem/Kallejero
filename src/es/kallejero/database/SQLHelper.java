package es.kallejero.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper {

	/**
	 * Constructor
	 * @param context
	 */
	public SQLHelper(Context context) {
		super(context, BaseDatos.DB_NAME, null, BaseDatos.DB_VERSION );
	}

	/**
	 * Creaci�n de la base de datos
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		if(db.isReadOnly()) { db=getWritableDatabase(); }

		db.execSQL("CREATE TABLE " + BaseDatos.Posts.NOMBRE_TABLA + " (" +
        		BaseDatos.Posts.ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
        		BaseDatos.Posts.NOMBRE + " TEXT," +
        		BaseDatos.Posts.DESCRIPCION + " TEXT," +
                BaseDatos.Posts.CATEGORIA + " TEXT" +
              //  BaseDatos.Posts.LATITUD + " TEXT," +
        		//BaseDatos.Posts.LONGITUD + " TEXT," +
               // BaseDatos.Posts.CIUDAD + " TEXT," +
               // BaseDatos.Posts.DIRECCION + " TEXT," +
               // BaseDatos.Posts.TELEFONO + " TEXT," +
               // BaseDatos.Posts.USUARIO + " TEXT," +
               // BaseDatos.Posts.IMAGEN + " TEXT" +
        		      		 ")"
        		);
        		




}

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }
}