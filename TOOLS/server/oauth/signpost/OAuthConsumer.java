package oauth.signpost;

import java.io.Serializable;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.signature.SigningStrategy;

public interface OAuthConsumer extends Serializable {
  void setMessageSigner(OAuthMessageSigner paramOAuthMessageSigner);
  
  void setAdditionalParameters(HttpParameters paramHttpParameters);
  
  void setSigningStrategy(SigningStrategy paramSigningStrategy);
  
  void setSendEmptyTokens(boolean paramBoolean);
  
  HttpRequest sign(HttpRequest paramHttpRequest) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  HttpRequest sign(Object paramObject) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  String sign(String paramString) throws OAuthMessageSignerException, OAuthExpectationFailedException, OAuthCommunicationException;
  
  void setTokenWithSecret(String paramString1, String paramString2);
  
  String getToken();
  
  String getTokenSecret();
  
  String getConsumerKey();
  
  String getConsumerSecret();
  
  HttpParameters getRequestParameters();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\OAuthConsumer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */