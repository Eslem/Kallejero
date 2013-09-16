package es.kallejero;


import es.kallejero.mapa.MapaActivity;
import android.R.color;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TabHost;
import android.widget.Toast;



@SuppressLint("NewApi")
public class MainActivity extends TabActivity {
    public static String opciones="";

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity);

		TabHost tabs=(TabHost)findViewById(android.R.id.tabhost);
		//setTabColor(tabs);
		tabs.setup();
		
		TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
		
		Intent negocios = new Intent(this, NegociosActivity.class);
		spec.setContent(negocios);
		spec.setIndicator("Negocios");
		tabs.addTab(spec);
		
		
		spec=tabs.newTabSpec("Mapa");
		Intent mapa = new Intent(this, MapaActivity.class);
		spec.setContent(mapa);
		spec.setIndicator("Mapa");
		tabs.addTab(spec);
		
		
	   


    }
    
       
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}


