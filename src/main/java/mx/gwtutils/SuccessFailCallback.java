package mx.gwtutils;

public interface SuccessFailCallback<GError> {
	public void onSuccess();
	public void onFail(GError message);
}
