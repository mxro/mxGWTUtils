/**
 * 
 */
package mx.gwtutils.concurrent;

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

	private final SimpleExecutor executor;
	private volatile Boolean isRunning;
	private final Notifiyer notifiyer;

	public void startIfRequired() {
		synchronized (isRunning) {
			if (isRunning) {
				return;
			}

			isRunning = true;
		}

		executor.execute(new Runnable() {

			@Override
			public void run() {
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
			isRunning = false;
		}
	}

	/**
	 * callWhenFinished.notifiyFinished must be called when finished.
	 * 
	 * @param callWhenFinished
	 */
	public abstract void run(Notifiyer callWhenFinished);

	public SingleInstanceThread(final SimpleExecutor executor) {
		super();
		this.executor = executor;
		this.isRunning = false;
		this.notifiyer = new Notifiyer();
	}

}
