/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils.async;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import mx.gwtutils.ThreadSpace;
import mx.gwtutils.ThreadSpace.Step;
import mx.gwtutils.internal.async.PendingMessageEntry;
import one.async.joiner.ListCallback;

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

	private final Vector<PendingMessageEntry<GMessage, GResponse>> sentMessages;
	private final ThreadSpace clientThreadSpace;

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
			// System.out.println(entry.messages + " = " + messages);
			if (entry.messages == messages) {
				assert position == -1 : "Messages defined in cache more than once: ["
						+ messages + "]";
				position = i;
			}
			i++;
		}
		// System.out.println("in position: "+position);
		return position;
	}

	protected PendingMessageEntry<GMessage, GResponse> getEntry(
			final int position) {
		return this.sentMessages.get(position);
	}

	/**
	 * Checks if for the message first sent to the server a response is
	 * available. If yes, the response if forwared for the client to process.
	 */
	void attemptToExecuteCallback() {
		if (sentMessages.size() <= 0) {
			return;
		}

		final PendingMessageEntry<GMessage, GResponse> zeroEntry = sentMessages
				.get(0);

		// System.out.println("process: "+zeroEntry.messages);
		if (zeroEntry.responses != null || zeroEntry.t != null) {
			assert zeroEntry.callback != null;
			sentMessages.remove(0);

			clientThreadSpace.add(new Step() {

				@Override
				public void process() {
					if (zeroEntry.isSuccess) {
						// try {
						zeroEntry.callback.onSuccess(zeroEntry.responses);
						// } catch (final Throwable t) {
						// t.printStackTrace(); // TODO remove this printout

						// zeroEntry.callback.onFailure(t);
						// }

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
		// System.out.println("messages sent: "+messages);
		// System.out.println("messages sent: "+messages);
		this.registerMessage(messages);

		return new SortedCallback(messages, callback);
	}

	public class SortedCallback implements ListCallback<GResponse> {

		final List<GMessage> messages;
		final ListCallback<GResponse> callback;

		@Override
		public void onSuccess(final List<GResponse> responses) {
			// System.out.println("messages response received on client: "
			// + messages);
			final int position = getEntryPosition(messages);
			// System.out.println("position "+position);
			assert position >= 0 : "Invalid state in callback lineraizer. Messages ["
					+ messages + "] not registered in cache.";

			final PendingMessageEntry<GMessage, GResponse> e = getEntry(position);

			e.responses = responses;
			e.callback = callback;
			e.isSuccess = true;

			attemptToExecuteCallback();

		}

		@Override
		public void onFailure(final Throwable t) {
			// System.out.println("failed messages response received on client: "
			// + messages);
			//
			// t.printStackTrace();
			final int position = getEntryPosition(messages);

			assert position >= 0 : "Invalid state in callback lineraizer for failure ["
					+ t.getMessage()
					+ "]. Failed messages ["
					+ messages
					+ "] not registered in cache.";

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
