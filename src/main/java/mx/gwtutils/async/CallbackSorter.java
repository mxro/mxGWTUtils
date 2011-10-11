package mx.gwtutils.async;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import mx.gwtutils.ListCallback;
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

	void registerMessage(final List<GMessage> messages) {
		this.sentMessages.add(new PendingMessageEntry<GMessage, GResponse>(
				messages));
	}

	int getEntryPosition(final List<GMessage> messages) {
		int i = 0;
		int position = -1;
		final List<PendingMessageEntry<GMessage, GResponse>> localMessages = new ArrayList<PendingMessageEntry<GMessage, GResponse>>(
				sentMessages);

		for (final PendingMessageEntry<GMessage, GResponse> entry : localMessages) {
			if (entry.messages == messages) {
				position = i;
			}
			i++;
		}
		return position;
	}

	PendingMessageEntry<GMessage, GResponse> getEntry(final int position) {
		return this.sentMessages.get(position);
	}

	void attemptToExecuteCallback() {
		if (sentMessages.size() <= 0)
			return;

		final PendingMessageEntry<GMessage, GResponse> zeroEntry = sentMessages
				.get(0);

		if (zeroEntry.responses != null) {
			assert zeroEntry.callback != null;
			sentMessages.remove(0);

			if (zeroEntry.isSuccess) {
				zeroEntry.callback.onSuccess(zeroEntry.responses);
			} else {
				zeroEntry.callback.onFailure(zeroEntry.t);
			}
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

		return new SortedCallback<GMessage, GResponse>(messages, callback, this);
	}

	public CallbackSorter() {
		super();
		this.sentMessages = new Vector<PendingMessageEntry<GMessage, GResponse>>();
	}

}