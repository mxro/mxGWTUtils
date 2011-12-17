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
public abstract class SingleInstanceThread implements Runnable {

	private final SimpleExecutor executor;
	private volatile Boolean isRunning;

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
				this.run();

			}

		});

	}

	public void notifiyFinished() {
		isRunning = false;
	}

	public SingleInstanceThread(final SimpleExecutor executor) {
		super();
		this.executor = executor;
	}

}
