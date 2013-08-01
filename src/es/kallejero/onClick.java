package es.kallejero;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import es.kallejero.mapa.MapaActivity;
import es.kallejero.parser.Bitmap_fromURL;

import  es.kallejero.R;

/**
 * Created by Eslem on 17/07/13.
 */
public class onClick extends Activity {

	private String id;
    @SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.onclick_negocio);


        Intent i = getIntent();
        // getting attached intent data
        final HashMap<String, String> negocio=(HashMap<String, String>) i.getSerializableExtra("negocio");
      
        id=negocio.get("id");
        final String nombre=negocio.get("nombre");
        String descripcion=negocio.get("descripcion");
        String categoria="-"+negocio.get("categoria");
        String url_imagen=negocio.get("imagen");
        String posicion =negocio.get("posicion");
        String Svisitas =negocio.get("visitas");
        String Spuntuacion =negocio.get("puntuacuio");
        String[] parts = posicion.split(",");
        final String lat = parts[0];
        final String lng = parts[1];


        TextView titulo=(TextView) findViewById(R.id.titulo_onClick);
        TextView about=(TextView) findViewById(R.id.descripcion_OnClick);
        TextView category=(TextView) findViewById(R.id.categoria_OnCLick);
        ImageView imageView=(ImageView) findViewById(R.id.image_onClick);
        TextView visitas=(TextView) findViewById(R.id.visitas_OnClick);
        TextView puntuacion=(TextView) findViewById(R.id.puntuacion_OnCLick);

        visitas.setText("Visitas:" +Svisitas);
        puntuacion.setText("Ranking: "+Spuntuacion);
        
        File dir=new File(Environment.getExternalStorageDirectory()
                + File.separator +"kallejero"+File.separator+"images");
        if(dir.exists()){
        	Log.d("dir", "exist");
        }
        else{
        	Log.d("dir", "no exist");
        	dir.mkdirs();
        }
        File file = new File(dir.getPath().toString()+File.separator + id+".png");
        if(file.exists()){
        	 ProgressBar loading=(ProgressBar) findViewById(R.id.progress);
        	Bitmap imagen = BitmapFactory.decodeFile(file.getAbsolutePath());
        	 loading.setVisibility(View.GONE);
        	 imageView.setVisibility(View.VISIBLE);
        	imageView.setImageBitmap(imagen);
        }
        
        else{
        
		       new download().execute(url_imagen);
		        	
       
        }
        titulo.setText(nombre);
        about.setText(descripcion);
        category.setText(categoria);
        
        Button mapa=(Button) findViewById(R.id.mapa_button);
        mapa.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(onClick.this, MapaActivity.class);
				intent.putExtra("lat",lat);
				intent.putExtra("lng", lng);
				startActivity(intent);
			}
        	
        });
        
        Button destino=(Button) findViewById(R.id.dest_button);
        
        destino.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(onClick.this, ProximityService.class);
				intent.putExtra("negocio", negocio);
				startService(intent);
			}
        	
        });
        
        Button link=(Button) findViewById(R.id.link_button);
        
        link.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 Uri uriUrl = Uri.parse("http://www.kallejero.es/negocios/"+id+".html");
			     Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
			     startActivity(launchBrowser);
			     
			}
        	
        });
        // displaying selected product name


    }

 class download extends AsyncTask<String, Void, Bitmap>{
	 @Override
	    protected Bitmap doInBackground(String... params) {
	        // TODO Auto-generated method stub
	        String link = params[0];
	        link = link.replaceAll(" ", "%20");
	        try {
	            URLEncoder.encode(link,"UTF-8");
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        Log.d("link", link);
	        HttpGet httpRequest = null;

	        httpRequest = new HttpGet(link);

	        HttpClient httpclient = new DefaultHttpClient();

	        HttpResponse response = null;
	        try {
	            response = (HttpResponse) httpclient.execute(httpRequest);
	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        HttpEntity entity = response.getEntity();

	        BufferedHttpEntity bufHttpEntity = null;
	        try {
	            bufHttpEntity = new BufferedHttpEntity(entity);
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }

	        InputStream instream = null;
	        try {
	            instream = bufHttpEntity.getContent();
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	       
	        try {
				return decodeFile(instream, 150);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
			return null;
	    }
	 
	 protected void onPostExecute(Bitmap imagen){
		 ImageView imageView=(ImageView) findViewById(R.id.image_onClick);
		 ProgressBar loading=(ProgressBar) findViewById(R.id.progress);
		 loading.setVisibility(View.GONE);
       	 imageView.setVisibility(View.VISIBLE);
       	 imageView.setImageBitmap(imagen);
       	 try {
			save(imagen, id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	 }
	 }



	    public Bitmap decodeFile(InputStream instream, int reqWidth)
	            throws IOException {
	        // Decodificar tamaño de imagen
	        BitmapFactory.Options o = new BitmapFactory.Options();
	        o.inJustDecodeBounds = true;
	        BitmapFactory.decodeStream(instream, null, o);

	        final int width = o.outWidth;
	        @SuppressWarnings("unused")
	        final int height = o.outHeight;
	        // Find the correct scale value. It should be the power of 2.
	        int inSampleSize = 1;

	        if (width > reqWidth) {
	            inSampleSize = Math.round((float) width / (float) reqWidth);
	        }

	        // Decodificar con inSampleSize
	        BitmapFactory.Options o2 = new BitmapFactory.Options();
	        o2.inSampleSize = inSampleSize;

	        instream.reset();
	        
	        
	       
	        return BitmapFactory.decodeStream(instream, null, o2);
	        //return BitmapFactory.decodeStream(instream, null, o2);

	    }
	    
	    
	    public void save(Bitmap image, String id) throws IOException{
	    	boolean sdDisponible = false;
	    	boolean sdAccesoEscritura = false;
	    	 
	    	//Comprobamos el estado de la memoria externa (tarjeta SD)
	    	String estado = Environment.getExternalStorageState();
	    	 
	    	if (estado.equals(Environment.MEDIA_MOUNTED))
	    	{
	    	    sdDisponible = true;
	    	    sdAccesoEscritura = true;
	    	}
	    	else if (estado.equals(Environment.MEDIA_MOUNTED_READ_ONLY))
	    	{
	    	    sdDisponible = true;
	    	    sdAccesoEscritura = false;
	    	}
	    	else
	    	{
	    	    sdDisponible = false;
	    	    sdAccesoEscritura = false;
	    	}
	    	
	    	if(sdDisponible && sdAccesoEscritura){
	    	ByteArrayOutputStream bytes = new ByteArrayOutputStream();
	    	image.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

	    	//you can create a new file name "test.jpg" in sdcard folder.
	    	File f = new File(Environment.getExternalStorageDirectory()+File.separator
	    							+"kallejero"+File.separator+"images"+File.separator
	    									+ id+".png");
	    	f.createNewFile();
	    	//write the bytes in file
	    	FileOutputStream fo = new FileOutputStream(f);
	    	fo.write(bytes.toByteArray());

	    	// remember close de FileOutput
	    	fo.close();
	    	Bitmap imagen = BitmapFactory.decodeFile(f.getAbsolutePath());
	    	}
	    }
 }





