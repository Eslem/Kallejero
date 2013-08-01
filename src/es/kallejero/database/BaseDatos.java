package es.kallejero.database;

import android.provider.BaseColumns;

public class BaseDatos {
	
	public static final String DB_NAME = "negocios.db";
	public static final int DB_VERSION = 1;
	
	
	private BaseDatos () {}

	
	public static final class Posts implements BaseColumns {
		
		private Posts() {}
		
	    public static final String DEFAULT_SORT_ORDER = "ID DESC";
    	
	    public static final String NOMBRE_TABLA = "negocios";
		
		public static final String ID = "id";
		public static final String NOMBRE = "nombre";
		public static final String DESCRIPCION = "descripcion";
        public static final String CIUDAD = "ciudad";
        public static final String CATEGORIA = "categoria";
        public static final String USUARIO = "usuario";
        public static final String DIRECCION = "direccion";
        public static final String TELEFONO = "telefono";
		public static final String LATITUD="latitud";
		public static final String LONGITUD="longitud";
		public static final String IMAGEN="imagen";
		
		
		
	}
}
