package mx.gwtutils.concurrent;

import java.util.ArrayList;
import java.util.List;
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
	protected abstract void processItems(List<GItem> item);

	/**
	 * This will start an asynchronous worker thread if no worker thread is
	 * already running.
	 */
	public void startIfRequired() {
		synchronized (queue) {
			thread.startIfRequired();
		}
	}

	/**
	 * Schedules to process this item.
	 * 
	 * @param item
	 */
	public void offer(final GItem item) {
		synchronized (queue) {
			queue.offer(item);
		}
	}

	public boolean isRunning() {
		return thread.getIsRunning();
	}

	public SingleInstanceThread getThread() {
		return thread;
	}

	public SingleInstanceQueueWorker(final OneExecutor executor,
			final Queue<GItem> queue) {
		this.thread = new SingleInstanceThread(executor) {

			@Override
			public void run(final Notifiyer notifiyer) {

				synchronized (queue) {

					while (queue.size() > 0) {
						final List<GItem> items = new ArrayList<GItem>(
								queue.size());

						GItem next;
						while ((next = queue.poll()) != null) {
							items.add(next);
							// break;
						}

						processItems(items);
					}

					notifiyer.notifiyFinished();
				}
			}

		};
		this.queue = queue;
	}

}
