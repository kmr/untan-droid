package net.necomimi.android.utut;

//import net.unto.twitter.Api;
import android.util.Log;
//import twitter4j.AsyncTwitter;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitClientImpl implements TwitClient {
	private Twitter twitter;
//	private Api twitter;
	
	public TwitClientImpl() {
	}

	public void init(String id, String password) {
//		Api api = Api.builder().username(id).password(password).build();
		this.twitter = new Twitter(id, password);
	}
	
	public void twit(String message) throws TwitException {
		TwitAgent agent = new TwitAgent(message);
		agent.start();
//		try {
//			this.twitter.updateStatus(message);
////			this.twitter.updateStatus(message).build().post();
//		} catch (TwitterException e) {
//			throw new TwitException(e.getMessage());
//		}
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
