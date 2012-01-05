package mx.gwtutils.tests;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import mx.gwtutils.concurrent.SimpleExecutor;
import mx.gwtutils.concurrent.SingleInstanceThread;

import org.junit.Assert;
import org.junit.Test;

public class TestSingleInstanceThread {

	private final class Worker implements Runnable {
		private final SingleInstanceThread thread;
		private final Queue<String> originalData;

		private Worker(final SingleInstanceThread thread,
				final Queue<String> originalData) {
			this.thread = thread;
			this.originalData = originalData;
		}

		@Override
		public void run() {
			for (int j = 1; j <= 1000; j++) {
				originalData.offer("item" + j);
			}
			for (int i = 1; i <= 100; i++) {

				thread.startIfRequired();
				Thread.yield();
				try {
					Thread.sleep(1);
				} catch (final InterruptedException e) {

				}
			}
		}
	}

	@Test
	public void test_single_instance_thread_started_by_two_threads()
			throws InterruptedException, ExecutionException {
		final SimpleExecutor executor = new SimpleExecutor() {

			@Override
			public void execute(final Runnable runnable) {
				new Thread() {
					@Override
					public void run() {
						runnable.run();
					}
				}.start();
			}

		};

		final Queue<String> originalData = new ConcurrentLinkedQueue<String>();
		final Vector<String> testData = new Vector<String>();

		final SingleInstanceThread thread = new SingleInstanceThread(executor) {

			@Override
			public void run(final Notifiyer notifiyer) {
				for (final String text : testData) {
					text.toLowerCase();
				}
				String value;
				while ((value = originalData.poll()) != null) {
					testData.add(value);
				}

				notifiyer.notifiyFinished();
			}
		};

		final Runnable worker = new Worker(thread, originalData);

		final ExecutorService cachedThreadPool = Executors
				.newCachedThreadPool();

		final Future<?> worker1 = cachedThreadPool.submit(worker);
		final Future<?> worker2 = cachedThreadPool.submit(worker);
		final Future<?> worker3 = cachedThreadPool.submit(worker);

		worker1.get();
		worker2.get();
		worker3.get();

		Assert.assertEquals(testData.size(), 3000);
	}
}
