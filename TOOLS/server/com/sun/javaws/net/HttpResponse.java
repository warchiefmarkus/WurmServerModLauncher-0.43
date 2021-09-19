package com.sun.javaws.net;

import java.io.BufferedInputStream;
import java.net.URL;

public interface HttpResponse {
  URL getRequest();
  
  int getStatusCode();
  
  int getContentLength();
  
  long getLastModified();
  
  String getContentType();
  
  String getResponseHeader(String paramString);
  
  BufferedInputStream getInputStream();
  
  void disconnect();
  
  String getContentEncoding();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\com\sun\javaws\net\HttpResponse.class
 * Java compiler version: 2 (46.0)
 * JD-Core Version:       1.1.3
 */