package net.necomimi.android.common;

public class ComponentInitializeException extends Exception {

	private static final long serialVersionUID = 1861529990165514184L;

	public ComponentInitializeException() {
		super();
	}

	public ComponentInitializeException(String detailMessage,
			Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ComponentInitializeException(String detailMessage) {
		super(detailMessage);
	}

	public ComponentInitializeException(Throwable throwable) {
		super(throwable);
	}

}
