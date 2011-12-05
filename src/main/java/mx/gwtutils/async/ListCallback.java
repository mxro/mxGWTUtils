/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils.async;

import java.util.List;

public interface ListCallback<GResponse> {
	public void onSuccess(List<GResponse> responses);
	public void onFailure(Throwable t);
	
}
