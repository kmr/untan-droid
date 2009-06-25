package net.necomimi.android.utut;

public interface TwitClient {
	public void init(String id, String password, String prefix);
	public void twit(String message) throws TwitException;

}
