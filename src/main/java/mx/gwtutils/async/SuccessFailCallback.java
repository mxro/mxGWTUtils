/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils.async;

public interface SuccessFailCallback<GError> {
	public void onSuccess();
	public void onFail(GError message);
}
