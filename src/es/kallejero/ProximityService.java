package es.kallejero;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.widget.Toast;

public class ProximityService extends Service{
	static ArrayList<HashMap<String, String>> negocios = new ArrayList<HashMap<String, String>>();
	
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
    public void onCreate() {
          Toast.makeText(this,"Servicio creado",   Toast.LENGTH_SHORT).show();
         
          // getting attached intent data
         
         
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
          Toast.makeText(this,"Servicio arrancado "+ idArranque,   Toast.LENGTH_SHORT).show();
          final HashMap<String, String> negocio=(HashMap<String, String>) intenc.getSerializableExtra("negocio");
          negocios.add(negocio);
          
          final LocationManager lm= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
          String ns=Context.NOTIFICATION_SERVICE;
  		
  		NotificationManager nmanager= (NotificationManager) getSystemService(ns);
          LocationListener mlistener=  new ServiceLlistener(negocios, this, nmanager);
			
			//lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlistener);
			lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mlistener);
          return START_STICKY;
    }

    @Override
    public void onDestroy() {
          Toast.makeText(this,"Servicio detenido", Toast.LENGTH_SHORT).show();
          
    }
    
    public void removeNegocio(int posicion){
    	negocios.remove(posicion);
    }

}
