package net.necomimi.android.utut;

import android.util.Log;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitClientImpl implements TwitClient {
	private static final String BLANK = " ";
	private Twitter twitter;
	private String prefix;
	
	public TwitClientImpl() {
	}

	public void init(String id, String password, String prefix) {
		this.twitter = new Twitter(id, password);
		this.prefix = prefix;
	}
	
	public void twit(String message) throws TwitException {
		TwitAgent agent = new TwitAgent(this.prefix + BLANK + message);
		agent.start();
	}
	
	private class TwitAgent extends Thread {
		private String message;
		
		public TwitAgent(String message) {
			this.message = message;
		}
		
		@Override
		public void run() {
			try {
				TwitClientImpl.this.twitter.updateStatus(this.message);
			} catch (TwitterException e) {
				Log.w(Utut.class.getName() + ".onSensorChanged", "Twitter post error.", e);				
			}
		}
		
	}

}
