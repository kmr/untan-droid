package net.necomimi.android.utut;

public interface TwitClient {
	public void init(String id, String password);
	public void twit(String message) throws TwitException;

}
