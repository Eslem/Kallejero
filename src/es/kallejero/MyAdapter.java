package es.kallejero;


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

import es.kallejero.parser.Bitmap_fromURL;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


@SuppressWarnings("rawtypes")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MyAdapter extends ArrayAdapter{
	Activity context;
	ArrayList<HashMap<String, String>> data;
   

  	@SuppressWarnings("unchecked")
	public MyAdapter(Activity context, ArrayList<HashMap<String, String>> data) {
  		super(context, R.layout.negocio, data);
  		this.context = context;
  		this.data = data;
  	}
  	
  	public View getView(int position, View convertView, ViewGroup parent) 
  	{
			View item = convertView;
			ViewHolder holder;
			HashMap<String, String> negocio = data.get(position);
  		
  			LayoutInflater inflater = context.getLayoutInflater();
  			item = inflater.inflate(R.layout.negocio, null);
  			
  			holder = new ViewHolder();
  			holder.Title = (TextView)item.findViewById(R.id.titulo);
  			holder.description=(TextView)item.findViewById(R.id.descripcion);
  			holder.categoria=(TextView)item.findViewById(R.id.categoria);
  			holder.pbar=(ProgressBar) item.findViewById(R.id.progresBar);
  			
  			holder.imagen= (ImageView) item.findViewById(R.id.image);
  			item.setTag(holder);

  			holder = (ViewHolder)item.getTag();
  			
  			holder.Title.setText(negocio.get("nombre"));
  			holder.categoria.setText(negocio.get("categoria"));
  			holder.description.setText(negocio.get("descripcion"));
  			String id=negocio.get("id");
  			
  			File dir=new File(Environment.getExternalStorageDirectory()
  	                + File.separator +"kallejero"+File.separator+"images");
  	        if(!dir.exists()){
  	        	dir.mkdirs();
  	        }
  	        File file = new File(dir.getPath().toString()+File.separator + id+".png");
  	        if(file.exists()){
  	        	 
  	        	Bitmap imagen = BitmapFactory.decodeFile(file.getAbsolutePath());
  	        	holder.imagen.setImageBitmap(imagen);
  	        	holder.imagen.setVisibility(View.VISIBLE);
  	        	holder.pbar.setVisibility(View.GONE);
  	        }
  	        
  	        else{
  	        
  			       new download(id, holder).execute(negocio.get("imagen"));
  			 	       
  	        }
  	        
  	        return item;
			
		
  	
  	}
class ViewHolder{
TextView Title;
TextView description;
TextView categoria;
ImageView imagen;
ProgressBar pbar;
}


class download extends AsyncTask<String, Void, Bitmap>{
	String id;
	ViewHolder holder;
	public download(String id, ViewHolder holder){
		this.id=id;
		this.holder=holder;
	}
	
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
		 holder.imagen.setVisibility(View.VISIBLE);
		 holder.pbar.setVisibility(View.GONE);
		 holder.imagen.setImageBitmap(imagen);
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













	


