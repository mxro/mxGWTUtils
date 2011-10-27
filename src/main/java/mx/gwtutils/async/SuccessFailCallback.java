package mx.gwtutils.async;

public interface SuccessFailCallback<GError> {
	public void onSuccess();
	public void onFail(GError message);
}
