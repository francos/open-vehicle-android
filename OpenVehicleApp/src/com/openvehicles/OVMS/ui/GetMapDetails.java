package com.openvehicles.OVMS.ui;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import com.google.gson.*;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.stream.JsonReader;
import com.luttu.Main;
import com.openvehicles.OVMS.entities.ChargePoint;
import com.openvehicles.OVMS.ui.utils.Database;


// This is the OCM API fetch background job

public class GetMapDetails extends AsyncTask<Void, Void, Void> {
	Main main;

	String url;
	Database database;
	afterasytask name;
	Gson gson;
	ArrayList<ChargePoint> chargePoints;


	public interface afterasytask {
		public void after(boolean flBoolean);
	}

	public GetMapDetails(Context context, String url, afterasytask name) {
		main = new Main(context);
		this.url = url;
		database = new Database(context);
		this.name = name;
		gson = new Gson();
		chargePoints = new ArrayList<ChargePoint>();
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		// main.Diashow();
	}

	@Override
	protected Void doInBackground(Void... params) {

		// read from OCM:

		Log.d("OCM", "GetMapDetails from url=" + url);

		try {
			getdata();
		} catch (IOException e) {
			e.printStackTrace();
		}

		Log.d("OCM", "GetMapDetails read " + chargePoints.size() + " chargepoints");

		// update database:

		for (int i = 0; i < chargePoints.size(); i++) {
			database.insert_mapdetails(chargePoints.get(i));
		}

		Log.d("OCM", "GetMapDetails saved " + chargePoints.size() + " chargepoints to database");

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		// main.Diacancel();
		System.out.println("post");
		name.after(true);
	}


	private void getdata() throws IOException {

		// open URL for JSON parser:

		URL obj_URL = new URL(url);
		InputStream in = obj_URL.openStream();
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

		// read charge points array:

		reader.beginArray();
		while (reader.hasNext()) {
			ChargePoint chargePoint = gson.fromJson(reader, ChargePoint.class);
			chargePoints.add(chargePoint);
		}
		reader.endArray();
		reader.close();

	}


}
