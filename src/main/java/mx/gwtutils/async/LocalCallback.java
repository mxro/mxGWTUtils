package mx.gwtutils.async;


public interface LocalCallback<GOutput> {
	
	public void onSuccess(GOutput response);
	public void onFailure(Throwable t);
	
}
