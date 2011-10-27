package mx.gwtutils.async;

import java.util.List;

public interface ListCallback<GResponse> {
	public void onSuccess(List<GResponse> responses);
	public void onFailure(Throwable t);
	
}
