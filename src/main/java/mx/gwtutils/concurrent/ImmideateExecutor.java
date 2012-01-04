package mx.gwtutils.concurrent;

/**
 * Will always execute the passed runnable immediately.
 * 
 * @author mx
 * 
 */
public class ImmideateExecutor implements SimpleExecutor {

	@Override
	public void execute(final Runnable runnable) {
		runnable.run();
	}

}
