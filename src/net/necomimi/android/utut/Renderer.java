package net.necomimi.android.utut;

import android.app.Activity;

public interface Renderer {
	public void init(Activity activity);
	public void changeImage(int res) throws RenderException;
	public void changeCalText(String text) throws RenderException;
	
}
