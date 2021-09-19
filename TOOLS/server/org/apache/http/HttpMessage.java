package org.apache.http;

import org.apache.http.params.HttpParams;

public interface HttpMessage {
  ProtocolVersion getProtocolVersion();
  
  boolean containsHeader(String paramString);
  
  Header[] getHeaders(String paramString);
  
  Header getFirstHeader(String paramString);
  
  Header getLastHeader(String paramString);
  
  Header[] getAllHeaders();
  
  void addHeader(Header paramHeader);
  
  void addHeader(String paramString1, String paramString2);
  
  void setHeader(Header paramHeader);
  
  void setHeader(String paramString1, String paramString2);
  
  void setHeaders(Header[] paramArrayOfHeader);
  
  void removeHeader(Header paramHeader);
  
  void removeHeaders(String paramString);
  
  HeaderIterator headerIterator();
  
  HeaderIterator headerIterator(String paramString);
  
  HttpParams getParams();
  
  void setParams(HttpParams paramHttpParams);
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\HttpMessage.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */