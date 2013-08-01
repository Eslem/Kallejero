package es.kallejero;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;



public class ServiceLlistener implements LocationListener {
	ArrayList<HashMap<String, String>> negocios;
	Context context;
	NotificationManager nmanager;
	
	
	public ServiceLlistener(ArrayList<HashMap<String, String>> data, Context context, NotificationManager nmanager){
		this.negocios=data;
		this.context=context;
		this.nmanager=nmanager;
		
	}
	
	@Override
		public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		for (int x=0; x<negocios.size(); x++){
			
			HashMap<String, String> negocio=negocios.get(x);
			String nombre=negocio.get("nombre");
	        String descripcion=negocio.get("descripcion");
	        String categoria="-"+negocio.get("categoria");
	        String url_imagen=negocio.get("imagen");
	        String posicion =negocio.get("posicion");
	        String[] parts = posicion.split(",");
	        double lat = Double.parseDouble(parts[0]);
	        double lng = Double.parseDouble(parts[1]);
			
			Location location=new Location("POINT_LOCATION");
			location.setLatitude(lat);
			location.setLongitude(lng);
			double distancia=arg0.distanceTo(location);
			
			if(distancia<=500){
				negocios.remove(x);
				if(negocios.size()==0){
					context.stopService(new Intent(context, ProximityService.class));
					//Toast.makeText(context, "servicio parado", Toast.LENGTH_SHORT).show();
				}
				createAlert(nombre, negocio);
				
			}
			
		}
	}
	
	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	public void createAlert(String nombre,  final HashMap<String, String> negocio){
		
		int icono= R.drawable.ic_launcher;
		Notification alerta= new Notification (icono," Negocio: "+nombre, System.currentTimeMillis());
		String titulo="Negocio Cerca";
		String texto="El negocio "+nombre+" esta cerca.";
		alerta.defaults |= Notification.DEFAULT_SOUND;
		alerta.defaults |= Notification.DEFAULT_VIBRATE;
		Intent alertIntent=new Intent(context, onClick.class);
		alertIntent.putExtra("negocio", negocio);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, alertIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alerta.setLatestEventInfo(context, titulo, texto, contentIntent);
		
		final int alert_id=1;
		nmanager.notify(alert_id, alerta);
		
		
	}
	

	

}
