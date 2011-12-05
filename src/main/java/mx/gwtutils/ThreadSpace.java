/*******************************************************************************
 * Copyright 2011 Max Erik Rohde http://www.mxro.de
 * 
 * All rights reserved.
 ******************************************************************************/
package mx.gwtutils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * used to synchronized events originating from multiple threads into one stream
 * of commands.
 * 
 * @author mroh004
 * 
 */
public class ThreadSpace {

	public static interface Step {
		public void process();
	}
	
	protected final List<Step> steps;

	protected volatile boolean processing;

	private void processStepsGuarded() {

		final List<Step> stepsToBeProcessed = new ArrayList<Step>(steps.size());
		stepsToBeProcessed.addAll(steps);

		for (final Step step : stepsToBeProcessed) {
			try {
				step.process();
			} finally {
				steps.remove(step);
			}
		}

	}

	public synchronized void processSteps() {
		// this is not very thread safe but should
		// be good enough for JavaScript
		// for Java Threads, the synchronized flag for the method should work.

		if (processing) {
			return;
		}

		try {
			processing = true;

			while (steps.size() > 0) {
				processStepsGuarded();
			}

		} finally {
			processing = false;
		}

	}

	public synchronized void add(final Step s) {
		this.steps.add(s);
	}

	public ThreadSpace() {
		super();
		this.processing = false;
		this.steps = new LinkedList<Step>();
	}

}
