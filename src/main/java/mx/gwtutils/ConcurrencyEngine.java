/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils;

import mx.gwtutils.ThreadSpace.Step;
import mx.gwtutils.tests.AbstractTimer;

/**
 * A simple abstraction for a number of concurrency operations. These are
 * supported by GWT applications as well as by JSE applications.
 * 
 * @author Max Rohde
 * 
 */
public abstract class ConcurrencyEngine {

	boolean printLog = false;

	/**
	 * All future log entries will be written to the console.
	 */
	public void printLog() {
		printLog = true;
	}

	public void will(final String doWhat) {
		if (printLog) {
			System.out.println("Will '" + doWhat + "'");
		}
	}

	public void has(final String doneWhat) {
		if (printLog) {
			System.out.println("Has '" + doneWhat + "'");
		}
	}

	public abstract void yield();

	public abstract AbstractTimer newTimer(Runnable timer);

	public interface Verifyer {
		public void join();
	}

	public Verifyer runAsync(final Step step) {
		sleep(1, step);
		return new Verifyer() {

			@Override
			public void join() {

			}

		};
	}

	/**
	 * 
	 * @param millisec
	 *            For GWT, this needs to be a positive value greater than 0.
	 * @param after
	 */
	public void sleep(final int millisec, final Step after) {
		assert millisec > 0 : "Millisec should be greater than 0.";

		final AbstractTimer t = newTimer(new Runnable() {

			@Override
			public void run() {
				after.process();
			}

		});
		t.schedule(millisec);
	}

	/**
	 * Enters the thread into a loop, which is executed for the specified
	 * duration. If the time specified in the duration is passed, an exception
	 * will be thrown. To prevent this {@link #finishTest()} should be called
	 * within the specified time frame.<br />
	 * <br />
	 * {@link #delayTestFinish(int)} should sensibly be called as the last
	 * instruction in a test case.
	 * 
	 * @param duration
	 *            Duration in ms for which the test case waits
	 */
	public abstract void delayTestFinish(int duration);

	/**
	 * Finishes a delayed test successfully.
	 * 
	 * @see #delayTestFinish(int)
	 */
	public abstract void finishTest();

	/**
	 * If there is a currently delayed test case, this method will fail this
	 * test.
	 * 
	 * @param t
	 */
	public abstract void failTest(Throwable t);

}
