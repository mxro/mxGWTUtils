/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils.internal.async;

import java.util.List;

import delight.async.callbacks.ListCallback;


public class PendingMessageEntry<GMessage, GResponse> {
	public final List<GMessage> messages;
	public List<GResponse> responses;
	public ListCallback<GResponse> callback;
	public boolean isSuccess;
	public Throwable t;

	public PendingMessageEntry(final List<GMessage> messages) {
		super();
		this.messages = messages;
	}

}
