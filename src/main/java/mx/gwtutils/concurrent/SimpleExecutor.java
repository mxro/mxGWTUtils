/**
 * 
 */
package mx.gwtutils.concurrent;

/**
 * @author <a href="http://www.mxro.de/">Max Erik Rohde</a>
 * 
 * Copyright Max Erik Rohde 2011. All rights reserved.
 */
public interface SimpleExecutor {
	
	/**
	 * Run this command asynchronously.
	 * 
	 * @param runnable
	 */
	public void execute(Runnable runnable);
	
}
