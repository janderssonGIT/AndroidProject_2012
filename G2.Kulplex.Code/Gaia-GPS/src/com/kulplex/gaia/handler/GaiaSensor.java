/**
 * GaiaSensor
 * 
 * This class handles the compass rotation
 * 
 * 
 * @author Jiayu Hu
 * 
 */


package com.kulplex.gaia.handler;

import java.util.List;

import com.kulplex.gaia.CustomMapView;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class GaiaSensor implements SensorEventListener{

	@SuppressWarnings("unused")
	static private boolean mRegisteredSensor;
	// set SensorManager
	static private SensorManager mSensorManager;
	static private Context mContext;
	
	static private float orientation = 0;
	
	public GaiaSensor(Context context){
		
		mContext = context;
		mRegisteredSensor = false;
		// get SensorManager instance
		mSensorManager = (SensorManager) mContext.getSystemService("sensor");
		
		
		List<Sensor> sensors = mSensorManager
				.getSensorList(Sensor.TYPE_ORIENTATION);

		if (sensors.size() > 0) {

			Sensor sensor = sensors.get(0);

			mRegisteredSensor = mSensorManager.registerListener(this, sensor,
					SensorManager.SENSOR_DELAY_FASTEST);
		}
		
	}
	

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {

			float x = event.values[SensorManager.DATA_X];

			CustomMapView.rotation = (int) x;
			
			Log.d("Orientation", "Rotation: " + CustomMapView.rotation);

		}
	}
	
	
	public float getOrientation()
	{
		return orientation;
	}

}
