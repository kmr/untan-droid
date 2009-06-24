package net.necomimi.android.utut;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;

public interface MediaController {
	public void init(Context ctx, int sound) throws IllegalStateException, IOException;
	public void setCompletionListener(MediaPlayer.OnCompletionListener listener);
	public void play();
	public void stop();
	public boolean isPlaying();
	public void destroy();
}
