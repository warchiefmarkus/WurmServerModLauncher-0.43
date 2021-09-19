package oauth.signpost;

import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

public interface OAuthProviderListener {
  void prepareRequest(HttpRequest paramHttpRequest) throws Exception;
  
  void prepareSubmission(HttpRequest paramHttpRequest) throws Exception;
  
  boolean onResponseReceived(HttpRequest paramHttpRequest, HttpResponse paramHttpResponse) throws Exception;
}


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\OAuthProviderListener.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */