package es.kallejero.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class Provider extends ContentProvider {

	
	public static final Uri CONTENT_URI = Uri.parse("content://es.kallejero.kallejero.bd");

	private static final int NEGOCIO = 1;
	private static final int NEGOCIO_ID = 2;

	private static final UriMatcher uriMatcher;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI("es.kallejero.kallejero.bd", "negocio", NEGOCIO);
		uriMatcher.addURI("es.kallejero.kallejero.bd", "negocio/#", NEGOCIO_ID);
	}

	private SQLiteDatabase BaseDatos;
	
	
	@Override
	public boolean onCreate() {
		Context context = getContext();
		SQLHelper dbHelper = new SQLHelper(context);
		BaseDatos = dbHelper.getWritableDatabase();
		return (BaseDatos == null) ? false : true;
	}
	
	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
		
		case NEGOCIO:
			return "vnd.android.cursor.dir/vnd.kallejero.kallejero.bd.negocio";
			
		case NEGOCIO_ID:
			return "vnd.android.cursor.item/vnd.kallejero.kallejero.bd.negocio";
		default:
			throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String where, String[] whereargs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case NEGOCIO:
			count = BaseDatos.delete(es.kallejero.database.BaseDatos.Posts.NOMBRE_TABLA, where, whereargs);
			break;
		case NEGOCIO_ID:
			String id = uri.getPathSegments().get(1);
			count = BaseDatos.delete(es.kallejero.database.BaseDatos.Posts.NOMBRE_TABLA, es.kallejero.database.BaseDatos.Posts.ID + " = " + id
					+ (!TextUtils.isEmpty(where) ? " AND (" + where + ')' : ""),
					whereargs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	
	/**
	 * insert
	 */
	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long rowID = BaseDatos.replace(es.kallejero.database.BaseDatos.Posts.NOMBRE_TABLA, "", values);
		
		// si todo ha ido ok devolvemos su Uri
		if (rowID > 0) {
			Uri baseUri = Uri.parse("content://es.kallejero.kallejero.bd/negocio");
			Uri _uri = ContentUris.withAppendedId(baseUri, rowID);
			
			getContext().getContentResolver().notifyChange(_uri, null);
			getContext().getContentResolver().notifyChange(baseUri, null);
			
			return _uri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}



	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder sqlBuilder = new SQLiteQueryBuilder();
		sqlBuilder.setTables(es.kallejero.database.BaseDatos.Posts.NOMBRE_TABLA);

		if (uriMatcher.match(uri) == NEGOCIO_ID) {
			sqlBuilder.appendWhere(es.kallejero.database.BaseDatos.Posts.ID + " = " + uri.getPathSegments().get(1));
		}

		if (sortOrder == null || sortOrder == "") {
			sortOrder =es.kallejero.database.BaseDatos.Posts.DEFAULT_SORT_ORDER;
		}

		Cursor c = sqlBuilder.query(BaseDatos, projection, selection,
				selectionArgs, null, null, sortOrder);

		// Registramos los cambios para que se enteren nuestros observers
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int count = 0;
		switch (uriMatcher.match(uri)) {
		case NEGOCIO:
			count = BaseDatos.update(es.kallejero.database.BaseDatos.Posts.NOMBRE_TABLA, values, selection,
					selectionArgs);
			break;
		case NEGOCIO_ID:
			count = BaseDatos.update(es.kallejero.database.BaseDatos.Posts.NOMBRE_TABLA, values, es.kallejero.database.BaseDatos.Posts.ID
					+ " = "
					+ uri.getPathSegments().get(1)
					+ (!TextUtils.isEmpty(selection) ? " AND (" + selection
					+ ')' : ""), selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

}
