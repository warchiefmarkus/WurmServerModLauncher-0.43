package oauth.signpost;

import java.io.Serializable;
import java.util.Map;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;

public interface OAuthProvider extends Serializable {
  String retrieveRequestToken(OAuthConsumer paramOAuthConsumer, String paramString, String... paramVarArgs) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  void retrieveAccessToken(OAuthConsumer paramOAuthConsumer, String paramString, String... paramVarArgs) throws OAuthMessageSignerException, OAuthNotAuthorizedException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  HttpParameters getResponseParameters();
  
  void setResponseParameters(HttpParameters paramHttpParameters);
  
  @Deprecated
  void setRequestHeader(String paramString1, String paramString2);
  
  @Deprecated
  Map<String, String> getRequestHeaders();
  
  void setOAuth10a(boolean paramBoolean);
  
  boolean isOAuth10a();
  
  String getRequestTokenEndpointUrl();
  
  String getAccessTokenEndpointUrl();
  
  String getAuthorizationWebsiteUrl();
  
  void setListener(OAuthProviderListener paramOAuthProviderListener);
  
  void removeListener(OAuthProviderListener paramOAuthProviderListener);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\OAuthProvider.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */