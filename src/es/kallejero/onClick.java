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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
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
import es.kallejero.parser.JsonParser;
import es.kallejero.parser.JsonParserComments;
import es.kallejero.parser.Rest;
import es.kallejero.parser.RestComments;
import es.kallejero.parser.downloadImage;

import  es.kallejero.R;

/**
 * Created by Eslem on 17/07/13.
 */
public class onClick extends Activity {
	public boolean CisVisible=false;
	class DownloadTask extends AsyncTask<String, Void, String> {
		protected String doInBackground(String... params) {
	         try {
	             String jsonString = new RestComments().downloadData(params[0]);
	             return jsonString;
	         } catch (Exception e) {
	             return null;
	         }
	     }
	
	
	     protected void onPostExecute(String jsonString) {
	         if (jsonString == null) {
	             return;
	         }
	
	         data_comments.clear();
	         data_comments.addAll(new JsonParserComments().parserArray(jsonString));
	         if(data_comments.size()==0){
	        	 TextView titulo=(TextView) findViewById(R.id.hComments);
	        	 titulo.setText("No hay ningun comentario aun");
	         }
	         mAdapter.notifyDataSetChanged();
	         Helper.getListViewSize(comentarios);
	     }
	}
  	ArrayList<HashMap<String, String>> data_comments = new ArrayList<HashMap<String, String>>();
 	private SimpleAdapter mAdapter;
	ListView comentarios;
	
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
        String Spuntuacion =negocio.get("puntuacion");
        String[] parts = posicion.split(",");
        final String lat = parts[0];
        final String lng = parts[1];
        new DownloadTask().execute("?id="+id);

        TextView titulo=(TextView) findViewById(R.id.titulo_onClick);
        TextView about=(TextView) findViewById(R.id.descripcion_OnClick);
        TextView category=(TextView) findViewById(R.id.categoria_OnCLick);
        ImageView imageView=(ImageView) findViewById(R.id.image_onClick);
        TextView visitas=(TextView) findViewById(R.id.visitas_OnClick);
        TextView puntuacion=(TextView) findViewById(R.id.puntuacion_OnCLick);

        visitas.setText("Visitas: " +Svisitas);
        puntuacion.setText("Puntuacion: "+Spuntuacion);
        
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
        	ProgressBar loading=(ProgressBar) findViewById(R.id.progress);
		       new downloadImage(id, imageView, loading).execute(url_imagen);
		        	
       
        }
        titulo.setText(nombre);
        about.setText(descripcion);
        category.setText(categoria);
        
        //Lista
        comentarios =(ListView)  findViewById(R.id.list_comments);
        
        String[] from = { "nombre", "fecha", "comentario" };
        int[] to = { R.id.nombre_comentario, R.id.fecha_comentario, R.id.texto_comentario };

        mAdapter = new SimpleAdapter(this, data_comments,
                R.layout.comentarios, from, to);
        comentarios.setAdapter(mAdapter);
        
       Helper.getListViewSize(comentarios);
        
        //Botones
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
        
        

        Button comentB=(Button) findViewById(R.id.comments_button);
        comentB.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LinearLayout comm=(LinearLayout) findViewById(R.id.comments_layout);
				if(CisVisible){
					comm.setVisibility(View.GONE);
					CisVisible=false;
					
				}
				else{
					comm.setVisibility(View.VISIBLE);
					CisVisible=true;
				}
				 
				 
			     
			}
        	
        });

    }

 
 }





