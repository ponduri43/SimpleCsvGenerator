package com.example.simplecsvgenerator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FormActivity extends Activity {

	// File f;
	File fdir;
	FileWriter writer;
	Intent intent;

	File f2;

	EditText sid, column4, column3, column2;

	Button saveBT;

	double[] d;
	int maxid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_generate_csv);

		intilizeXmlVaribles();

		try {
			fdir = new File(Environment.getExternalStorageDirectory()
					.toString());
			fdir.mkdir();
			f2 = new File(fdir.getAbsolutePath() + File.separator
					+ "Report.csv");
			if (f2.createNewFile()) {
				writer = new FileWriter(f2.getAbsolutePath(), true);
				createHeader();
			} else {
				// load max id
				getMaxid();
			}

			writer = new FileWriter(f2.getAbsolutePath(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		findViewById(R.id.saveBT).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				new appendToCsvTask().execute("");
			}
		});

	};

	private void createHeader() {

		try {
			writer.append("id,coulmn2,coulmn3,coulmn4");
			writer.append('\n');
			writer.flush();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void getMaxid() {
		if (f2.exists()) {
			String csvFile_old = f2.getAbsolutePath();
			BufferedReader br_old = null;
			String line_old = "";
			String cvsSplitBy_old = ",";

			try {
				br_old = new BufferedReader(new FileReader(csvFile_old));
				while ((line_old = br_old.readLine()) != null) {
					String[] fields = line_old.split(cvsSplitBy_old);
					try {
						maxid = max(maxid, Integer.parseInt(fields[0]));
					
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
			} finally {
				if (br_old != null) {
					try {
						br_old.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				
				sid = (EditText) findViewById(R.id.sid);
				maxid=maxid+1;
				sid.setText("" + maxid);
				sid.setEnabled(false);
			}

		}
	}

	private int max(int a, int b) {
		return a > b ? a : b;
	}

	private class appendToCsvTask extends AsyncTask<String, Void, String> {

		ProgressDialog pd;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(FormActivity.this,
					ProgressDialog.STYLE_SPINNER);
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... arg0) {
			appendToCsvFile(f2.getAbsolutePath());
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			pd.dismiss();
			Intent intent = new Intent(getApplicationContext(),
					FormActivity.class);
			startActivity(intent);
			finish();
		}

	}

	private void intilizeXmlVaribles() {


		column2 = (EditText) findViewById(R.id.column2);
		column3 = (EditText) findViewById(R.id.column3);
		column4 = (EditText) findViewById(R.id.column4);

	}

	private void appendToCsvFile(String sFileName) {
		try {

			StringBuilder s = new StringBuilder(maxid + "").append(",")
					.append(column2.getText().toString()).append(',')
					.append(column3.getText().toString()).append(',')
					.append(column4.getText().toString()).append('\n');
			
			writer.append(s.toString());
			writer.flush();
			writer.close();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					"Record not saved due to " + e.getLocalizedMessage(), 1000)
					.show();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.generate_csv, menu);
		return true;
	}

}
