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

	private volatile boolean shutdownRequested = false;
	private volatile boolean isShutDown = false;
	private volatile QueueShutdownCallback shutDowncallback;

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

	public interface QueueShutdownCallback {
		public void onShutdown();
	}

	public void requestShutdown(final QueueShutdownCallback callback) {
		shutDowncallback = callback;
		shutdownRequested = true;
		thread.startIfRequired();
	}

	/**
	 * Schedules to process this item.
	 * 
	 * @param item
	 */
	public void offer(final GItem item) {
		synchronized (queue) {
			if (isShutDown) {
				throw new IllegalStateException(
						"Cannot submit tasks for a shutdown worker: [" + item
								+ "]");
			}
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

					if (shutdownRequested) {
						isShutDown = true;
						shutDowncallback.onShutdown();
					}
				}
			}

		};
		this.queue = queue;
	}

}
