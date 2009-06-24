package net.necomimi.android.utut;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

public class MediaControllerImpl implements MediaController {
	private MediaPlayer player;
	
	public MediaControllerImpl() {
	}
	
	public void init(Context ctx, int sound) throws IllegalStateException, IOException {
		this.player = MediaPlayer.create(ctx, sound);
	}
	
	public void play() {
		if (this.player.isPlaying()) {
			this.player.seekTo(0);
		}
		this.player.start();
	}
	
	public void stop() {
		this.player.stop();
	}
	
	public boolean isPlaying() {
		return this.player.isPlaying();
	}

	public void destroy() {
		this.player.release();
	}

	public void setCompletionListener(OnCompletionListener listener) {
		this.player.setOnCompletionListener(listener);
	}
}
