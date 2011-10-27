package mx.gwtutils.async;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import mx.gwtutils.ThreadSpace;
import mx.gwtutils.ThreadSpace.Step;
import mx.gwtutils.internal.async.PendingMessageEntry;

/**
 * {@link CallbackSorter} will assure that callbacks for incoming messages are
 * sent in the same order as they are received.
 * 
 * @author Max Rohde
 * 
 * @param <GMessage>
 * @param <GResponse>
 */
public class CallbackSorter<GMessage, GResponse> {

	protected final Vector<PendingMessageEntry<GMessage, GResponse>> sentMessages;
	protected final ThreadSpace clientThreadSpace;

	protected void registerMessage(final List<GMessage> messages) {
		this.sentMessages.add(new PendingMessageEntry<GMessage, GResponse>(
				messages));
	}

	protected int getEntryPosition(final List<GMessage> messages) {
		int i = 0;
		int position = -1;
		final List<PendingMessageEntry<GMessage, GResponse>> localMessages = new ArrayList<PendingMessageEntry<GMessage, GResponse>>(
				sentMessages);

		for (final PendingMessageEntry<GMessage, GResponse> entry : localMessages) {
			if (entry.messages == messages) {
				assert position==-1 : "Messages defined in cache more than once: ["+messages+"]";
				position = i;
			}
			i++;
		}
		return position;
	}

	protected PendingMessageEntry<GMessage, GResponse> getEntry(final int position) {
		return this.sentMessages.get(position);
	}

	protected void attemptToExecuteCallback() {
		if (sentMessages.size() <= 0) {
			return;
		}

		final PendingMessageEntry<GMessage, GResponse> zeroEntry = sentMessages
				.get(0);

		//System.out.println("process: "+zeroEntry.messages);
		if (zeroEntry.responses != null || zeroEntry.t != null) {
			assert zeroEntry.callback != null;
			sentMessages.remove(0);
			
			clientThreadSpace.add(new Step() {

				@Override
				public void process() {
					if (zeroEntry.isSuccess) {
						zeroEntry.callback.onSuccess(zeroEntry.responses);
					} else {
						zeroEntry.callback.onFailure(zeroEntry.t);
					}
				}

			});
			clientThreadSpace.processSteps();
			
			attemptToExecuteCallback();
		} 
	}

	/**
	 * Create a callback, which will only be triggered after callbacks for
	 * previously sent messages have been received.
	 * 
	 * @param messages
	 * @param callback
	 * @return
	 */
	public ListCallback<GResponse> createCallback(
			final List<GMessage> messages,
			final ListCallback<GResponse> callback) {
		this.registerMessage(messages);

		return new SortedCallback(messages, callback);
	}

	public class SortedCallback implements
			ListCallback<GResponse> {

		final List<GMessage> messages;
		final ListCallback<GResponse> callback;

		@Override
		public void onSuccess(final List<GResponse> responses) {
			//System.out.println("received: "+messages);
			final int position = getEntryPosition(messages);

			assert position >= 0 : "Invalid state in callback lineraizer. Messages ["
					+ messages + "] not registered in cache.";

			final PendingMessageEntry<GMessage, GResponse> e = 
					getEntry(position);

			e.responses = responses;
			e.callback = callback;
			e.isSuccess = true;

			attemptToExecuteCallback();

		}

		@Override
		public void onFailure(final Throwable t) {
			//System.out.println("failed: "+messages);
			final int position =getEntryPosition(messages);

			assert position >= 0 : "Invalid state in callback lineraizer. Failed messages ["
					+ messages + "] not registered in cache.";

			final PendingMessageEntry<GMessage, GResponse> e = getEntry(position);

			e.t = t;
			e.isSuccess = false;
			e.callback = callback;

			attemptToExecuteCallback();
		}

		public SortedCallback(final List<GMessage> messages,
				final ListCallback<GResponse> callback) {
			super();
			this.messages = messages;
			this.callback = callback;
			
		}

	}

	public CallbackSorter(final ThreadSpace threadSpace) {
		super();
		this.clientThreadSpace = threadSpace;
		this.sentMessages = new Vector<PendingMessageEntry<GMessage, GResponse>>();
	}

}