package oauth.signpost.http;

import java.io.IOException;
import java.io.InputStream;

public interface HttpResponse {
  int getStatusCode() throws IOException;
  
  String getReasonPhrase() throws Exception;
  
  InputStream getContent() throws IOException;
  
  Object unwrap();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\http\HttpResponse.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */