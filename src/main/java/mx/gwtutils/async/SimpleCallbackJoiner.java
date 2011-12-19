/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils.async;

/**
 * Allows to wait for a specified number of asynchronous operations.
 * 
 * @see {@link ListCallbackJoiner}
 * @author <a href="http://www.mxro.de/">Max Erik Rohde</a>
 * 
 *         Copyright Max Erik Rohde 2011. All rights reserved.
 */
public abstract class SimpleCallbackJoiner {

	final int expected;
	volatile int received;
	volatile boolean failed;

	/**
	 * This method is called when for all expected callbacks
	 * {@link #registerReceived()} has been called.
	 */
	public abstract void onCompleted();

	public abstract void onFailed(Throwable t);

	/**
	 * Call this method when an expected callback has been received.
	 */
	public void registerReceived() {
		received++;
		if (!failed && received == expected) {
			onCompleted();
		}
	}

	public void registerFail(final Throwable t) {
		if (!failed) {
			failed = true;
			onFailed(t);
		}
		failed = true;

	}

	public SimpleCallbackJoiner(final int expected) {
		super();
		this.expected = expected;
		this.failed = false;
	}

}
