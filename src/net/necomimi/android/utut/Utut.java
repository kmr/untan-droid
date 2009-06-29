package net.necomimi.android.utut;

import java.text.DecimalFormat;
import java.util.List;

import net.necomimi.android.common.ComponentInitializeException;
import net.necomimi.android.common.SimpleContainer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

public class Utut extends Activity implements SensorEventListener,
											  MediaPlayer.OnCompletionListener {
	private static final String MOETA = " キロカロリーもえた!";
	private static final String YUNOTTI = " x/ _ /x < ";
	private static final String EMPTY = "";
	private final String MEDIA_CONTROLLER = "media_controller";
	private final String TWIT = "twit";
	private final String SHAKE_CONTROLLER = "shake_controller";
	private final String RENDERER = "renderer";
	private static final int MEDIA_PLAY_INTERVAL = 200;
    private static final int SUB_FORM_NORM = 0;
	private static final DecimalFormat CAL_PATTERN = new DecimalFormat("##0.0");
    
	private SimpleContainer container;
	private TextView accelerometerValue;
	private SensorManager sensorManager;
	private MediaController media;
	private TwitClient twit;
	private ShakeController shakeController;
	private Renderer renderer;
    private int playing = 0;
    private long previousPlayTime = 0L;
    private boolean twitEnabled = false;
    	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // get container
        this.container = SimpleContainer.getContainer(this);
        try {
        	initContainer();
			checkPreference();
		} catch (ComponentInitializeException e) {
			// error abort.
			Log.e(Utut.class.getName() + ".onCreate", "Preference load error", e);
			finish();
		}
        
        // get views        
        this.accelerometerValue = (TextView)findViewById(R.id.accelerometer_value);
        this.accelerometerValue.setText(Float.toString(this.shakeController.getCurrentAccelerationValue()));
        try {
			this.renderer.changeCalText(CAL_PATTERN.format(this.shakeController.getUsedCal()));
			this.renderer.changeImage(R.raw.un);
		} catch (RenderException e) {
			// error abort.
			Log.e(Utut.class.getName() + ".onCreate", "Preference load error", e);
			finish();
		}
    }
    
    private void initContainer() throws ComponentInitializeException {
        // get container
        this.container = SimpleContainer.getContainer(this);
        this.shakeController = (ShakeController) this.container.getConponent(SHAKE_CONTROLLER);
        this.renderer = (Renderer) this.container.getConponent(RENDERER);
        this.renderer.init(this);
        // sensor
        this.sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
        try {
			this.media = (MediaController)this.container.getConponent(MEDIA_CONTROLLER);
			initMedia();
			this.media.setCompletionListener(this);
		} catch (ComponentInitializeException e) {
			Log.e(Utut.class.getName() + ".onStart", "media component load error", e);
			throw new RuntimeException(e);
		} catch (IllegalStateException e) {
			Log.e(Utut.class.getName() + ".onStart", "media is illegal state", e);
			throw new RuntimeException(e);
		}

		List<Sensor> sensors = this.sensorManager.getSensorList(Sensor.TYPE_ACCELEROMETER);
    	if (sensors.size() != 1) {
    		this.finish();
    	}
    	this.sensorManager.registerListener(this,
    										sensors.get(0),
    										SensorManager.SENSOR_DELAY_FASTEST);
    }
    
    @Override
    protected void onStop() {
    	if (null != this.sensorManager) {
    		this.sensorManager.unregisterListener(this);
    	}
    	
    	if (null != this.media) {
    		this.media.destroy();
    	}
    	super.onStop();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {
		switch(event.sensor.getType()) {
		case Sensor.TYPE_ACCELEROMETER:
            boolean shaked = this.shakeController.isShaked(event.values[1]);
            accelerometerValue.setText(Float.toString(
            		this.shakeController.getCurrentAccelerationValue()));
            if (shaked) {
				try {
					this.renderer.changeCalText(CAL_PATTERN.format(this.shakeController.getUsedCal()));
					twitUntn();
					toggleUt();
				} catch (TwitException e) {
					// error continue.
					Log.e(Utut.class.getName() + ".onSensorChanged", "Twitter post error.", e);
				} catch (RenderException e) {
					// error abort.
					Log.e(Utut.class.getName() + ".onSensorChanged", "Display resource error.", e);
					finish();
				}				
            }
			
		default:
			;
		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    switch (event.getAction()) {
	    case MotionEvent.ACTION_UP:
	    	// touched!
	    	try {
					toggleUt();
				} catch (TwitException e) {
					// error ignore.
					Log.e(Utut.class.getName() + ".onTouchEvent", "Media playback error.", e);
				} catch (RenderException e) {
					// error abort.
					Log.e(Utut.class.getName() + ".onTouchEvent", "Display resource error.", e);
					finish();
				}
	        break;
	    }
	    
	    return super.onTouchEvent(event);
	}	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
		case KeyEvent.KEYCODE_MENU:
			Intent configIntent = new Intent(Utut.this, SettingActivity.class);
			startActivityForResult(configIntent, SUB_FORM_NORM);
			
		default:
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data){  
        if (requestCode == SUB_FORM_NORM && resultCode == RESULT_OK) {
        	try {
        		checkPreference();
        	} catch (ComponentInitializeException e) {
    			// error ignore.
    			Log.e(Utut.class.getName() + ".onActivityResult",
    					"Preference update error.", e);
        	}
        }  
    }
	
	private void checkPreference() throws ComponentInitializeException {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		if (pref.getBoolean(SettingActivity.CONFIG_TWIT_ENABLED_KEY, false)) {
			String id = pref.getString(SettingActivity.CONFIG_TWIT_ID_KEY, EMPTY);
			String password = pref.getString(SettingActivity.CONFIG_TWIT_PASSWORD_KEY, EMPTY);
			String prefix = pref.getString(SettingActivity.CONFIG_TWIT_PREFIX_KEY, EMPTY);
			if (!(EMPTY.equals(id) || EMPTY.equals(password))) {
				enableTwit(id, password, prefix);
			}
		} else {
			disableTwit();
		}
		
		initMedia();
	}
	
	private void initMedia() throws ComponentInitializeException {
		if (null == this.media) {
			return;
		}
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
		try {
			this.media.init(this,
					R.raw.class.getDeclaredField(
					pref.getString(SettingActivity.CONFIG_SOUND_KEY,
							SettingActivity.CONFIG_SOUND_DEFAULT)).getInt(null));
		} catch (Exception e) {
			try {
				this.media.init(this, R.raw.castanet);
			} catch (Exception e1) {
				throw new ComponentInitializeException(e1);
			}
		}
	}
	
	/**
	 * Play sound
	 * 
	 * @return true if play sound, false if did not play sound.
	 * @throws TwitException 
	 * @throws RenderException 
	 */
	private boolean toggleUt() throws TwitException, RenderException {
		if (System.currentTimeMillis() - this.previousPlayTime > MEDIA_PLAY_INTERVAL) {
			synchronized(this) {
				this.playing++;
			}
			// play sound and switch image to tan.
			this.previousPlayTime = System.currentTimeMillis();
	        this.renderer.changeImage(R.raw.tan);
			this.media.play();
			return true;
		}
		return false;
	}

	/**
	 * Called when media is completed.
	 */
	public void onCompletion(MediaPlayer mp) {
		if (this.playing >= 1) {
			synchronized(this) {
				this.playing--;
			}
			// Switch image to un.
			try {
				this.renderer.changeImage(R.raw.un);
			} catch (RenderException e) {
				// error abort.
				Log.e(Utut.class.getName() + ".onCompletion", "Display resource error.", e);
				finish();
			}
		}
	}
	
	private void enableTwit(String id, String password, String prefix) throws ComponentInitializeException {
		if (null == this.twit) {
			this.twit = (TwitClient) this.container.getConponent(TWIT);
		}
		this.twit.init(id, password, prefix);
		this.twitEnabled = true;
	}
	
	private void disableTwit() {
		this.twitEnabled = false;
	}
	
	private void twitUntn() throws TwitException {
		if (this.twitEnabled) {
			this.twit.twit(YUNOTTI + CAL_PATTERN.format(this.shakeController.getUsedCal()) + MOETA);
		}
	}
	
}