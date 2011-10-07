package mx.gwtutils;


public interface LocalCallback<GOutput> {
	
	public void onSuccess(GOutput response);
	public void onFailure(Throwable t);
	
}
