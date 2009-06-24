package net.necomimi.android.utut;

public interface ShakeController {
	
    public float getCurrentOrientationValue();
  	public float getCurrentAccelerationValue();
	public boolean isShaking();
    public float getUsedCal();
	
	public boolean isShaked(float yAccel);
}
