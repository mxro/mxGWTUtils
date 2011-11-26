package mx.gwtutils.async;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import mx.gwtutils.IdentityArrayList;
import mx.gwtutils.MxroGWTUtils;

/**
 * This utility class supports in writing Java in a more asynchronous style.
 * This class allows to <i>collect</i> the results from various callbacks, which
 * result from calling a method on all elements of a list. The callback provided
 * by this class will only be called if all the callbacks for the individual
 * list elements have been executed.
 * 
 * @author Max Rohde
 * 
 * @param <GInput>
 * @param <GOutput>
 */
public class OrderedCallbackJoiner<GInput, GOutput> {
	final Map<Integer, GOutput> responseMap;
	final List<GInput> messages;
	final int expectedSize;
	final ListCallback<GOutput> callback;

	public LocalCallback<GOutput> createCallback(final GInput message) {
		return new LocalCallback<GOutput>() {

			@Override
			public void onSuccess(final GOutput response) {
				responseMap.put(messages.indexOf(message), response);

				if (MxroGWTUtils.isMapComplete(responseMap, expectedSize)) {
					final List<GOutput> localResponses = MxroGWTUtils
							.toOrderedList(responseMap);

					callback.onSuccess(localResponses);
				}
			}

			@Override
			public void onFailure(final Throwable t) {
				callback.onFailure(t);
			}

		};
	}

	public OrderedCallbackJoiner(final List<GInput> messages,
			final ListCallback<GOutput> callback) {
		super();
		this.messages = new IdentityArrayList<GInput>(messages);
		this.responseMap = new IdentityHashMap<Integer, GOutput>(); // TODO should this map be synchronized?
		expectedSize = messages.size();
		this.callback = callback;
	}

}