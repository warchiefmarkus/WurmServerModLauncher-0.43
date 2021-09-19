package oauth.signpost.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface HttpRequest {
  String getMethod();
  
  String getRequestUrl();
  
  void setRequestUrl(String paramString);
  
  void setHeader(String paramString1, String paramString2);
  
  String getHeader(String paramString);
  
  Map<String, String> getAllHeaders();
  
  InputStream getMessagePayload() throws IOException;
  
  String getContentType();
  
  Object unwrap();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\oauth\signpost\http\HttpRequest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */