package mx.gwtutils.tests;

import mx.gwtutils.ThreadSpace.Step;


public abstract class ConcurrencyEngine implements DelayTest {
	public abstract AbstractTimer newTimer(Runnable timer);

	public void sleep(final int millisec, final Step after) {
		final AbstractTimer t = newTimer(new Runnable() {
	
			@Override
			public void run() {
				after.process();
			}
	
		});
		t.schedule(millisec);
	}
}