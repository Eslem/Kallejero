package es.kallejero;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

class downloadImage extends AsyncTask<String, Void, Bitmap>{
	private String id;
	private ImageView imagen;
	private ProgressBar progressBar;
	
	public downloadImage(String id, ImageView imagen, ProgressBar barra){
		this.id=id;
		this.imagen=imagen;
		this.progressBar=barra;
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
		 ImageView imageView=this.imagen;
		 ProgressBar loading=progressBar;
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
