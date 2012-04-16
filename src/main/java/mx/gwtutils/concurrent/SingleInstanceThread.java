/**
 * 
 */
package mx.gwtutils.concurrent;

import one.utils.concurrent.OneExecutor;

/**
 * A thread of which only one instance runs at any one time.<br/>
 * <b>NOTE: </b>Implementing classes MUST call {@link #notifiyFinished()} in
 * their {@link #run()} implementation.
 * 
 * @author <a href="http://www.mxro.de/">Max Erik Rohde</a>
 * 
 *         Copyright Max Erik Rohde 2011. All rights reserved.
 */
public abstract class SingleInstanceThread {

	private final OneExecutor executor;
	private volatile Boolean isRunning;
	private volatile long lastCall;
	private long maxCalltime;
	private final Notifiyer notifiyer;

	public void startIfRequired() {

		if (maxCalltime > -1 && lastCall > -1
				&& (System.currentTimeMillis() - lastCall) > maxCalltime) {
			isRunning = false;
			new Exception("Worker thread was manually reset.")
					.printStackTrace(System.err);
		}

		synchronized (isRunning) {
			if (isRunning) {
				return;
			}

			isRunning = true;
		}

		executor.execute(new Runnable() {

			@Override
			public void run() {
				assert isRunning;
				lastCall = System.currentTimeMillis();

				SingleInstanceThread.this.run(notifiyer);
			}

		});

	}

	public class Notifiyer {
		/**
		 * This method must be called when all pending operations for this
		 * thread are completed.
		 */
		public void notifiyFinished() {
			lastCall = -1;
			isRunning = false;
		}
	}

	public Boolean getIsRunning() {
		return isRunning;
	}

	public void setMaxCallTime(final long maxCallTimeInMs) {
		this.maxCalltime = maxCallTimeInMs;
	}

	public OneExecutor getExecutor() {
		return executor;
	}

	/**
	 * callWhenFinished.notifiyFinished must be called when finished.
	 * 
	 * @param callWhenFinished
	 */
	public abstract void run(Notifiyer callWhenFinished);

	public SingleInstanceThread(final OneExecutor executor) {
		super();
		this.executor = executor;
		this.isRunning = false;
		this.notifiyer = new Notifiyer();
		this.maxCalltime = -1;
		this.lastCall = -1;
	}

}
