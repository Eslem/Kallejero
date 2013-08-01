package es.kallejero;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import es.kallejero.parser.JsonParser;
import es.kallejero.parser.Rest;


public class NegociosActivity extends ListActivity {
	 String opciones="";
	 String categoria;
	 String ciudad;
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

            mAdapter.notifyDataSetChanged();
        }

    }

    ArrayList<HashMap<String, String>> mData = new ArrayList<HashMap<String, String>>();
    private SimpleAdapter mAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_negocios);

        String[] from = { "nombre", "descripcion", "categoria" };
        int[] to = { R.id.titulo, R.id.descripcion, R.id.categoria };

        mAdapter = new SimpleAdapter(this, mData,
                R.layout.negocio, from, to);
        setListAdapter(mAdapter);
       
        Spinner categorias=(Spinner) findViewById(R.id.categorias);
        categorias.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2==0){
					categoria="";
				}
				else{
					categoria=arg0.getItemAtPosition(arg2).toString();
				
				}
				
				if(ciudad==""){
					if(categoria==""){
						opciones="";
					}
					else{
						opciones="?categoria="+categoria;
					}
					
				}
				else{
					if(categoria==""){
						opciones="?ciudad="+ciudad;
					}else{
						opciones="?categoria="+categoria+"&ciudad="+ciudad;
					}
					
				}
				
				
				
				
				new DownloadTask().execute(opciones);
				Toast.makeText(getApplicationContext(), "Selecionada: "+arg0.getItemAtPosition(arg2).toString()+ "   Opcion: "+opciones, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        Spinner ciudades=(Spinner) findViewById(R.id.ciudades);
        ciudades.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if(arg2==0){
					ciudad="";
				}
				else{
					ciudad=arg0.getItemAtPosition(arg2).toString();
				}
				
				if(ciudad==""){
					if(categoria==""){
						opciones="";
					}
					else{
						opciones="?categoria="+categoria;
					}
					
				}
				else{
					if(categoria==""){
						opciones="?ciudad="+ciudad;
					}else{
						opciones="?categoria="+categoria+"&ciudad="+ciudad;
					}
					
				}			
				
				new DownloadTask().execute(opciones);
				Toast.makeText(getApplicationContext(), "Selecionada: "+arg0.getItemAtPosition(arg2).toString()+ "   Opcion: "+opciones, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
               
        
        

    }

    protected void onListItemClick(ListView l, View v, int position, long id) {
        final HashMap<String, String> negocio = mData.get(position);


        final Intent intent = new Intent(this, onClick.class);
       intent.putExtra("negocio", negocio);

        startActivity(intent);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    protected void onPause() { 
    MainActivity.opciones=opciones;
    super.onPause();
   }
    
}
