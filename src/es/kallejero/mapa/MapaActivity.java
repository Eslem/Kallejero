package es.kallejero.mapa;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import es.kallejero.MainActivity;
import es.kallejero.R;
import es.kallejero.onClick;
import es.kallejero.R.id;
import es.kallejero.R.layout;
import es.kallejero.parser.JsonParser;
import es.kallejero.parser.Rest;
import es.kallejero.parser.downloadImage;
		
/**
 * Created by Eslem on 17/07/13.
 */
	public class MapaActivity extends Activity {
		public String opciones;
		static double mylat;
		Location location;
		static double lat;
		static double lng;
		ArrayList<HashMap<String, String>> mData = new ArrayList<HashMap<String, String>>();
		
		class DownloadTask extends AsyncTask<String, Void, String> {


	        @Override
	        protected String doInBackground(String... params) {
	            try {
	                String jsonString = new Rest().downloadData(params[0]);
	                return jsonString;
	            } catch (Exception e) {
	                return null;
	            }
	        }


	        @Override
	        protected void onPostExecute(String jsonString) {
	            if (jsonString == null) {
	                return;
	            }
	            mData.clear();
	            mData.addAll(new JsonParser().parserArray(jsonString));
	            Mmap.clear();
	            //Toast.makeText(getApplicationContext(), "testing changes", Toast.LENGTH_LONG).show();
	            setMarkers(mData);
	        }

	    }
		private GoogleMap Mmap;
		GoogleMapOptions options = new GoogleMapOptions();

		
		@SuppressLint("NewApi")
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			
			//Compuebo q este el array para parsearlo
			if(mData == null){
				opciones=MainActivity.opciones;
		    	new DownloadTask().execute(opciones);
			}
			
			
			setContentView(R.layout.activity_map);
			Mmap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
					
			//Asigno Mylocation para que muestre el lugar donde esta
			Mmap.setMyLocationEnabled(true);
			
			//Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 16));
			 
			final LocationManager lm= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			
			double longitude;
			double latitude;
			Intent intent=getIntent();
			
			if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

				//Comperuebo posicion por internet
				if(lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
					location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
					LocationListener mlistener=  new MiLocationListener(this, Mmap);
					
					
					if(!intent.hasExtra("lat")){
						lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500000, 0, mlistener);
					}
				}
				if(location != null){
					longitude = location.getLongitude();
					latitude = location.getLatitude();
				}
				
				//Alerta para activar GPS
			    AlertDialog.Builder builder = new AlertDialog.Builder(this);
			    builder.setTitle("Location Manager");
			    builder.setMessage("Quieres iniciar el Gps para mayor eficiencia?");
			    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            //Launch settings, allowing user to make a change
			            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			            startActivity(i);
			            
			        }
			    });
			    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			        @Override
			        public void onClick(DialogInterface dialog, int which) {
			            //No location service, no Activity
			        	
			        }
			    });
			    builder.create().show();
			}
			else{
				LocationListener mlistener=  new MiLocationListener(this, Mmap);
				if(!intent.hasExtra("lat")){
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500000, 0, mlistener);
				}
			}
			
			
			
			
		    
			//Al mantener pulsado en cualquier sitio del mapa muestra las coordenadas
			Mmap.setOnMapLongClickListener(new OnMapLongClickListener(){
										
				@Override
				public void onMapLongClick(LatLng point) {
					// TODO Auto-generated method stub
					Projection proj = Mmap.getProjection();
			        Point coord = proj.toScreenLocation(point);
			        
			        
			        Toast.makeText(
			                MapaActivity.this,
			                "Click\n" +
			                "Lat: " + point.latitude + "\n" +
			                "Lng: " + point.longitude + "\n" +
			                "X: " + coord.x + " - Y: " + coord.y,
			                Toast.LENGTH_LONG).show();
				}
			});
			opciones=es.kallejero.MainActivity.opciones;
			if(opciones != null){
				new DownloadTask().execute(opciones);
			}
			
			if(intent.hasExtra("lat")){
				String lat=intent.getStringExtra("lat");
				String lng=intent.getStringExtra("lng");
				double latitud=Double.parseDouble(lat);
				double longitud=Double.parseDouble(lng);
						
				Log.d("datos", latitud+" "+longitud );
				
				Mmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitud, longitud), 16));
			}
			
			Mmap.setInfoWindowAdapter(new InfoWindowAdapter() {
 
            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }
 
            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {
 
                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.info_window, null);
                Log.d("id", arg0.getSnippet());

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();
 
                // Getting reference to the TextView to set latitude
                TextView Titulo = (TextView) v.findViewById(R.id.titulo_info);
 
                // Getting reference to the TextView to set longitude
                TextView Descripcion = (TextView) v.findViewById(R.id.descripcion_info);
                TextView Categoria = (TextView) v.findViewById(R.id.categoria_info);
                ImageView imageView=(ImageView) v.findViewById(R.id.image_map);
                // Setting the latitude
                Titulo.setText(arg0.getTitle());
                
                
                String position=arg0.getSnippet();
                HashMap<String, String>negocio=null;
              
                negocio = (HashMap<String, String>) mData.get(Integer.parseInt(position));
	            String descripcion=negocio.get("descripcion");
	            final String categoria="-"+negocio.get("categoria");
	            Descripcion.setText(descripcion);
	            Categoria.setText(categoria);
	            String id=negocio.get("id");
	            
	            //Imagen
	            File dir=new File(Environment.getExternalStorageDirectory()
	  	                + File.separator +"kallejero"+File.separator+"images");
	  	        if(!dir.exists()){
	  	        	dir.mkdirs();
	  	        }
	  	        File file = new File(dir.getPath().toString()+File.separator + id+".png");
	  	        if(file.exists()){
	  	        	 
	  	        	Bitmap imagen = BitmapFactory.decodeFile(file.getAbsolutePath());
	  	        	imageView.setImageBitmap(imagen);
	  	        }

                      
                return v;
 
            }
        });
			
			Mmap.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

				@Override
				public void onInfoWindowClick(Marker arg0) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(MapaActivity.this, onClick.class);
					 String id=arg0.getSnippet();
		                
		            HashMap<String, String>negocio = (HashMap<String, String>) mData.get(Integer.parseInt(id));
	               
	                intent.putExtra("negocio", negocio);
	                 startActivity(intent);
				}
				
				
	                
				
			});
		}
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			//getMenuInflater().inflate(R.menu.activity_map, menu);
			return true;
		}
		
		public void setMarkers(ArrayList<HashMap<String, String>> negocios){
			int x=negocios.size();
			
			for(int y=0; y<x; y++){
				HashMap<String, String> negocio=negocios.get(y);
				String nombre=negocio.get("nombre");
				
		        String posicion =negocio.get("posicion");
		        String[] parts = posicion.split(",");
		        String icon=negocio.get("categoria").toLowerCase();
		        double lat = Double.parseDouble(parts[0]);
		        double lng = Double.parseDouble(parts[1]);
		       	//Posicion en el array
		        String position = ""+y;
		        
		        String uri = "drawable/"+icon;

		        // int imageResource = R.drawable.icon;
		        int imageResource = getResources().getIdentifier(uri, null, getPackageName());
		        
		        Mmap.addMarker(new MarkerOptions()
		        .position(new LatLng(lat, lng))
		        .title(nombre)
		        .snippet(position)
		        .icon(BitmapDescriptorFactory.fromResource(imageResource))
		        );
				
			}
			
		}

		//carga los marcadores
		
		@Override
	    protected void onResume() { 
			super.onResume();
	    
	    	opciones=MainActivity.opciones;
	    	new DownloadTask().execute(opciones);
	    	
	    
	    
	   }
		

		}
