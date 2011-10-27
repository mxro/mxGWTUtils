package mx.gwtutils.async;

import java.util.List;

import mx.gwtutils.internal.async.PendingMessageEntry;

@Deprecated
public class SortedCallback<GMessage, GResponse> implements
		ListCallback<GResponse> {

	final List<GMessage> messagesCb;
	final ListCallback<GResponse> callback;
	final CallbackSorter<GMessage, GResponse> manager;

	@Override
	public void onSuccess(final List<GResponse> responses) {
		
		final int position = manager.getEntryPosition(messagesCb);

		assert position >= 0 : "Invalid state in callback lineraizer. Messages ["
				+ messagesCb + "] not registered in cache.";

		final PendingMessageEntry<GMessage, GResponse> e = manager
				.getEntry(position);
		
		e.responses = responses;
		e.callback = callback;
		e.isSuccess = true;

		manager.attemptToExecuteCallback();

	}

	@Override
	public void onFailure(final Throwable t) {
		final int position = manager.getEntryPosition(messagesCb);

		assert position >= 0 : "Invalid state in callback lineraizer. Failed messages ["
				+ messagesCb + "] not registered in cache.";

		final PendingMessageEntry<GMessage, GResponse> e = manager
				.getEntry(position);

		e.t = t;
		e.isSuccess = false;
		e.callback = callback;

		manager.attemptToExecuteCallback();
	}

	public SortedCallback(final List<GMessage> messages,
			final ListCallback<GResponse> callback,
			final CallbackSorter<GMessage, GResponse> messageManager) {
		super();
		this.messagesCb = messages;
		this.callback = callback;
		this.manager = messageManager;
	}

}