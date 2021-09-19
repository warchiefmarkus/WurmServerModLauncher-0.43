package oauth.signpost.signature;

import java.io.Serializable;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.http.HttpRequest;

public interface SigningStrategy extends Serializable {
  String writeSignature(String paramString, HttpRequest paramHttpRequest, HttpParameters paramHttpParameters);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\signature\SigningStrategy.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */