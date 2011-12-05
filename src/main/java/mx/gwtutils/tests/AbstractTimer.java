/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils.tests;

public interface AbstractTimer {
	public void schedule(int when);
	public void run();
}
