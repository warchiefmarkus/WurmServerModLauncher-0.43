package org.apache.http;

public interface StatusLine {
  ProtocolVersion getProtocolVersion();
  
  int getStatusCode();
  
  String getReasonPhrase();
}


/* Location:              C:\Users\leo\Desktop\server.jar!\org\apache\http\StatusLine.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */