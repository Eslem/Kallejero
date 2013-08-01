package es.kallejero.mapa;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.widget.Toast;



public class MiLocationListener implements LocationListener {
		private Context context;
		private GoogleMap map;
		
	
	public MiLocationListener(Context context, GoogleMap mmap){
		this.context=context;
		this.map=mmap;
		
	}
	
	@Override
		public void onLocationChanged(Location arg0) {
		// TODO Auto-generated method stub
		map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getAltitude(), arg0.getLatitude()), 16));
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

	

}
