package mx.gwtutils.tests;

import mx.gwtutils.ThreadSpace.Step;

/**
 * A simple abstraction for a number of concurrency operations. These are
 * supported by GWT applications as well as by JSE applications.
 * 
 * @author mroh004
 * 
 */
public abstract class ConcurrencyEngine {

	public abstract AbstractTimer newTimer(Runnable timer);

	/**
	 * Enters the thread into a loop, which is executed for the specified
	 * duration. If the time specified in the duration is passed, an exception
	 * will be thrown. To prevent this {@link #finishTest()} should be called
	 * within the specified time frame.<br />
	 * <br /> {@link #delayTestFinish(int)} should sensibly be called as the last
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

	public void sleep(final int millisec, final Step after) {
		final AbstractTimer t = newTimer(new Runnable() {

			@Override
			public void run() {
				after.process();
			}

		});
		t.schedule(millisec);
	}
}