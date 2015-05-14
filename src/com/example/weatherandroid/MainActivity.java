package com.example.weatherandroid;

import java.util.ArrayList;














import com.example.weatherandroid.CityPreference;
import com.example.weatherandroid.R;
import com.example.weatherandroid.WeatherFragment;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class MainActivity extends Activity {
	
	Button AddCity;
	ListView listView;
	ArrayList<String> my_array_list;
	ArrayAdapter<String> array_adapter;
	static String City;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		AddCity = (Button) findViewById(R.string.add_city);
		
		my_array_list = new ArrayList<String>();
		listView = (ListView) findViewById(R.id.listView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				System.out.println("Pressed" + my_array_list.get(position));
				City = my_array_list.get(position);
				Intent intent = new Intent(MainActivity.this,
						WeatherFragment.class);
				startActivity(intent);
				
		

			}
		});
	}
	
	private void showInputDialog(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Add City Name");
		
		final EditText input = new EditText(this);
		
		input.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.LIGHTEN);
		
		input.setInputType(InputType.TYPE_CLASS_TEXT);
		
		builder.setView(input);
		
	    builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				addCity(input.getText().toString());
				
			}
		});
	    
	    builder.show();
	}
	
	@SuppressLint("NewApi")
	public void addCity(String city){
		
		my_array_list.add(city);

		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, my_array_list);
		listView.setAdapter(arrayAdapter);
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.add_city) {
			showInputDialog();
		}
		return false;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
}
