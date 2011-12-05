/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils.async;

public abstract class SimpleCallbackJoiner {
	
	final int expected;
	volatile int received;
	volatile boolean failed;
	
	public abstract void onCompleted();
	
	public abstract void onFailed(Throwable t);
	
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
