package net.necomimi.android.utut;

public class TwitException extends Exception {

	private static final long serialVersionUID = -5323586692508695137L;

	public TwitException() {
		super();
	}

	public TwitException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public TwitException(String detailMessage) {
		super(detailMessage);
	}

	public TwitException(Throwable throwable) {
		super(throwable);
	}
}
