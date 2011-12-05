/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils;

import java.util.LinkedList;
import java.util.List;


public class ConditionalThread {

	protected final List<ConditionalStep> steps;
	protected volatile boolean processing;
	protected static final int MAX_RETRIES = 500;
	protected volatile int retries;
	
	public static interface ConditionalStep {
		/**
		 * Should return true if operation was successful, otherwise false.
		 * 
		 * @return
		 */
		public boolean process();
	}

	public ConditionalThread() {
		steps = new LinkedList<ConditionalStep>();
		retries = MAX_RETRIES;
	}

	protected void processStepsGuarded() {
		if (steps.size() == 0) {
			return;
		}
		final ConditionalStep step = steps.get(0);
		//System.out.println("steps remaining: "+steps.size());
		if (step.process()) {
			steps.remove(0);
			retries = MAX_RETRIES;
					
			processStepsGuarded();
		} else {
			retries--;
			assert retries > 0 : "Thread aborted since maximum retires ("+MAX_RETRIES+") reached.";			
		}

	}

	public synchronized void processSteps() {
		if (processing) {
			return;
		}

		try {
			processing = true;

			if (steps.size() > 0) {
				processStepsGuarded();
			}

		} finally {
			processing = false;
		}
	}

	public synchronized void add(final ConditionalStep s) {
		this.steps.add(s);
	}

}
