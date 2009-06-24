package net.necomimi.android.utut;

import android.hardware.SensorManager;

public class ShakeControllerImpl implements ShakeController {
    private static float SHAKED_ACCELERATION = SensorManager.GRAVITY_EARTH * 2;
	private static final int SHAKE_INTERVAL = 200;
    private static float CAL_PER_SHAKE = 0.1f;
	
    private float currentOrientationValue = 0.0f;
	private float currentAccelerationValue = 0.0f;
    private boolean shaking = false;
    private long startShakeTime = 0L;
    private float usedCal = 0.0f;

	public ShakeControllerImpl() {
    }
    
    public float getCurrentOrientationValue() {
		return currentOrientationValue;
	}

	public float getCurrentAccelerationValue() {
		return currentAccelerationValue;
	}

	public boolean isShaking() {
		return shaking;
	}    
    
    public float getUsedCal() {
		return usedCal;
	}

	public boolean isShaked(float yAccel) {
		// high cut
        this.currentOrientationValue = yAccel * 0.1f + this.currentOrientationValue * 0.9f;
        // low cut
        this.currentAccelerationValue = yAccel - currentOrientationValue;
//        accelerometerValue.setText(Float.toString(this.currentAccelerationValue));
        if(this.currentAccelerationValue < -SHAKED_ACCELERATION) {
        	if (!this.shaking) {
            	// start shake!
            	this.shaking = true;
            	this.startShakeTime = System.currentTimeMillis();
        	}
        } else {
        	if (this.shaking && this.currentAccelerationValue >= 0 &&
        		System.currentTimeMillis() - this.startShakeTime > SHAKE_INTERVAL) {
        		// accept shaked!
    			this.usedCal += CAL_PER_SHAKE;
//    			this.calValue.setText(MessageFormat.format(CAL_PATTERN, new Object[]{this.used_cal}));
//            	try {
//					toggleUt();
    			this.shaking = false;
            	return true;
//				} catch (TwitException e) {
//					// TODO: log > Ignore
//				}
            }
        }
        return false;
	}

}
