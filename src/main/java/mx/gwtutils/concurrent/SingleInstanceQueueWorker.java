package mx.gwtutils.concurrent;

import java.util.Queue;

import one.utils.concurrent.OneExecutor;

/**
 * Allows to build a queue of objects, which are processed sequentially and
 * non-concurrently.
 * 
 * @author mroh004
 * 
 * @param <GItem>
 */
public abstract class SingleInstanceQueueWorker<GItem> {

	private final SingleInstanceThread thread;
	private final Queue<GItem> queue;

	/**
	 * It is guaranteed that this method is only called by one worker thread at
	 * the time and that the items are forwarded FIFO how they were offered.
	 * 
	 * @param item
	 */
	protected abstract void processItem(GItem item);

	/**
	 * This will start an asynchronous worker thread if no worker thread is
	 * already running.
	 */
	public void startIfRequired() {
		thread.startIfRequired();
	}

	/**
	 * Schedules to process this item.
	 * 
	 * @param item
	 */
	public void offer(final GItem item) {
		queue.offer(item);
	}

	public SingleInstanceQueueWorker(final OneExecutor executor,
			final Queue<GItem> queue) {
		this.thread = new SingleInstanceThread(executor) {

			@Override
			public void run(final Notifiyer notifiyer) {
				GItem next;

				while ((next = queue.poll()) != null) {
					processItem(next);
				}

				notifiyer.notifiyFinished();
			}

		};
		this.queue = queue;
	}

}
